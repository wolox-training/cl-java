package wolox.training.services.implement;

import static wolox.training.contants.ConstantsMain.BOOK_NOT_FOUND_MSG;
import static wolox.training.contants.ConstantsMain.JSON_FORMAT_STRING;
import static wolox.training.contants.ConstantsMain.PARAM_STRING;
import static wolox.training.contants.ConstantsMain.URL_EXTERNAL_API;

import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.dtos.BookDTO;
import wolox.training.exceptions.responses.BookNotFoundException;
import wolox.training.services.OpenLibraryService;

@Service
public class OpenLibraryServiceImp implements OpenLibraryService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public BookDTO bookInfo(String isbn) {
        String url = String.format(PARAM_STRING,URL_EXTERNAL_API,isbn,JSON_FORMAT_STRING);
        ResponseEntity<Object> response = restTemplate.getForEntity(url , Object.class);
        LinkedHashMap body = (LinkedHashMap) response.getBody();
        if (body.size() == 0){
            throw new BookNotFoundException(BOOK_NOT_FOUND_MSG);
        }
        BookDTO bookInfo = new BookDTO((LinkedHashMap) body.get("ISBN:" + isbn), isbn);
        return bookInfo;
    }

}
