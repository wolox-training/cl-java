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
import wolox.training.exceptions.responses.BookAttributeConflict;
import wolox.training.exceptions.responses.BookIdMismatchException;
import wolox.training.exceptions.responses.BookNotFoundException;
import wolox.training.exceptions.responses.UserIdMismatchException;
import wolox.training.exceptions.responses.UserNotFoundException;
import wolox.training.exceptions.responses.UsernameAlreadyTakenException;

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
    @ExceptionHandler({ BookNotFoundException.class,
                UserNotFoundException.class})
    protected ResponseEntity<Object> handleBookNotFound ( Exception ex,
            WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    /**
     * This method id used to handler exception of {@link BookIdMismatchException}
     * and {@link UserIdMismatchException}
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
            UserIdMismatchException.class,
            ConstraintViolationException.class,
            DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleBookIdMismatch(Exception ex,
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
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * This method id used to handler exception of {@link UsernameAlreadyTakenException,
     * @link BookAttributeConflict}
     * @param ex: Exception reported (Exception)
     * @param request: Request given by the Web (WebRequest)
     * @return handExceptionInternal with the following attributes:
     *  ex (Exception,
     *  msg (String),
     *  headers (HttpHeaders),
     *  response status (HttpStatus.Forbidden),
     *  request (WebRequest).
     */
    @ExceptionHandler({UsernameAlreadyTakenException.class,
            BookAttributeConflict.class})
    public ResponseEntity<Object> handleUnique (Exception ex,
            WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

}
