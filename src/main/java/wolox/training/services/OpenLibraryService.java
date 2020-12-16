package wolox.training.services;

import wolox.training.dtos.BookDTO;
import wolox.training.models.Book;

public interface OpenLibraryService {

    Book getBookByISBN(String isbn);
}
