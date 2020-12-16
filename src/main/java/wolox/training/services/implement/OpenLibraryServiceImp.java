package wolox.training.services.implement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.dtos.BookDTO;
import wolox.training.exceptions.responses.BookAttributeConflict;
import wolox.training.exceptions.responses.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.OpenLibraryService;

@Service
public class OpenLibraryServiceImp implements OpenLibraryService {

    private static final String URL = "https://openlibrary.org/api/books";
    private static final String PARAM_STRING = "%1$s?bibkeys=ISBN:%2$s%3$s";
    private static final String JSON_FORMAT_STRING = "&format=json&jscmd=data";
    private static final String BOOK_NOT_FOUND_MSG = "Book not found";
    private static final String IMAGE_MSG = "Has no image";
    private static final String ATTRIBUTE_CONFLICT_MSG = "An attribute has a conflict";


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modMapper;


    private BookDTO bookInfo(String isbn) {
        String url = String.format(PARAM_STRING,URL,isbn,JSON_FORMAT_STRING);
        ResponseEntity<Object> response = restTemplate.getForEntity(url , Object.class);
        LinkedHashMap body = (LinkedHashMap) response.getBody();
        if (body.size() == 0){
            throw new BookNotFoundException(BOOK_NOT_FOUND_MSG);
        }
        BookDTO bookInfo = mapBookInfo((LinkedHashMap) body.get("ISBN:" + isbn), isbn);
        return bookInfo;


    }

    private BookDTO mapBookInfo(LinkedHashMap data, String isbn) {
        try {
            BookDTO bookInfo = new BookDTO();
            List<String> authorNames = new ArrayList<>();
            List<String> publisherNames = new ArrayList<>();
            bookInfo.setIsbn(isbn);
            bookInfo.setTitle(data.get("title").toString());
            bookInfo.setSubtitle(data.get("subtitle").toString());
            bookInfo.setYear(data.get("publish_date").toString());
            bookInfo.setPages(
                    (Integer) data.get("number_of_pages"));
            List<LinkedHashMap> publishers = new ArrayList<>(
                    (Collection<? extends LinkedHashMap>) data.get("publishers")
            );

            for (LinkedHashMap p : publishers) {
                publisherNames.add(p.get("name").toString());
            }
            bookInfo.setPublishers(publisherNames);

            List<LinkedHashMap> authors = new ArrayList<>(
                    (Collection<? extends LinkedHashMap>) data.get("authors")
            );

            for (LinkedHashMap a : authors) {
                authorNames.add(a.get("name").toString());
            }
            bookInfo.setAuthors(authorNames);
            return bookInfo;
        } catch (RuntimeException ex) {
            throw new BookAttributeConflict(ATTRIBUTE_CONFLICT_MSG);
        }
    }

    @Override
    public Book getBookByISBN(String isbn) {
        try {
            BookDTO isBookApi = null;
            isBookApi = bookInfo(isbn);
            Book bookToSave = modMapper.map(isBookApi, Book.class);
            bookToSave.setAuthor(isBookApi.getAuthors().get(0));
            bookToSave.setPublisher(isBookApi.getPublishers().get(0));
            bookToSave.setImage(IMAGE_MSG);
            bookRepository.save(bookToSave);
            return bookToSave;
        } catch (RuntimeException ex) {
            throw new BookAttributeConflict(ATTRIBUTE_CONFLICT_MSG);
        }
    }
}
