package wolox.training.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wolox.training.exceptions.responses.BookAlreadyOwnException;
import wolox.training.exceptions.responses.BookIdMismatchException;
import wolox.training.exceptions.responses.BookNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * This method id used to handler exception of {@link BookNotFoundException}
     * @param ex: Exception reported (Exception)
     * @param request: Request given by the Web (WebRequest)
     * @return handExceptionInternal with the following attributes:
     *  ex (Exception,
     *  msg (String),
     *  headers (HttpHeaders),
     *  response status (HttpStatus.Not_Found),
     *  request (WebRequest).
     */
    @ExceptionHandler({ BookNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound ( Exception ex,
            WebRequest request) {
        return handleExceptionInternal(ex, "Book not found",
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    /**
     * This method id used to handler exception of {@link BookIdMismatchException}
     * @param ex: Exception reported (Exception)
     * @param request: Request given by the Web (WebRequest)
     * @return handExceptionInternal with the following attributes:
     *  ex (Exception,
     *  msg (String),
     *  headers (HttpHeaders),
     *  response status (HttpStatus.Bad_Request),
     *  request (WebRequest).
     */
    @ExceptionHandler({ BookIdMismatchException.class,
            ConstraintViolationException.class,
            DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleBadRequest(Exception ex,
            WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * This method id used to handler exception of {@link BookAlreadyOwnException}
     * @param ex: Exception reported (Exception)
     * @param request: Request given by the Web (WebRequest)
     * @return handExceptionInternal with the following attributes:
     *  ex (Exception,
     *  msg (String),
     *  headers (HttpHeaders),
     *  response status (HttpStatus.Forbidden),
     *  request (WebRequest).
     */
    @ExceptionHandler({BookAlreadyOwnException.class})
    public ResponseEntity<Object> handleOwnedBook (Exception ex,
            WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(),
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}
