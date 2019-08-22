package gh.shin.web;

import gh.shin.util.NoDataFoundException;
import gh.shin.web.value.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestExceptionHandler extends ExceptionHandlerExceptionResolver {
    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(NoDataFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public WebResponse handleNotFound(HttpServletRequest req, NoDataFoundException ex) {
        return _makeError(HttpStatus.NOT_FOUND, req.getRequestURL(), ex);
    }

    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public WebResponse handleNotFound(HttpServletRequest req, NoHandlerFoundException ex) {
        return _makeError(HttpStatus.NOT_FOUND, req.getRequestURL(), ex);
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public WebResponse handleInternalServerError(HttpServletRequest req, RuntimeException ex) {
        return _makeError(HttpStatus.INTERNAL_SERVER_ERROR, req.getRequestURL(), ex);
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public WebResponse handleUnsupportedMediaType(HttpServletRequest req, HttpMediaTypeNotSupportedException ex) {
        return _makeError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, req.getRequestURL(), ex);
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public WebResponse handleBadReques(HttpServletRequest req, MissingServletRequestParameterException ex) {
        return _makeError(HttpStatus.BAD_REQUEST, req.getRequestURL(), ex);
    }

    private WebResponse _makeError(HttpStatus status, StringBuffer url, Exception ex){
        log.error("Error {} from {}", status, url, ex);
        return WebResponse.error(ex.getMessage());
    }

}
