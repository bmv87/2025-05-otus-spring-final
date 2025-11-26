package ru.otus.fin.library.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.otus.fin.library.dto.ErrorDto;
import ru.otus.fin.library.dto.FieldErrorDto;
import ru.otus.fin.library.services.LocalizedMessagesService;

import java.util.List;


@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final LocalizedMessagesService localizedMessagesService;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handeNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        return new ErrorDto("404", ex.getMessage(), null);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handeException(BindException ex) {
        log.error(ex.getMessage());
        List<FieldErrorDto> errors = null;
        if (ex.getBindingResult().hasFieldErrors()) {
            errors = ex.getBindingResult().getFieldErrors()
                    .stream()
                    .map(e -> new FieldErrorDto(e.getField(), e.getDefaultMessage()))
                    .toList();
        }
        return new ErrorDto("400", localizedMessagesService.getMessage("errors.validation"), errors);
    }

    @ExceptionHandler(BusinessValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorDto handeException(BusinessValidationException ex) {
        log.error(ex.getMessage());
        return new ErrorDto("422", ex.getMessage(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handeException(AccessDeniedException ex) {
        log.error(ex.getMessage());
        return new ErrorDto("403", localizedMessagesService.getMessage("errors.access_denied"), null);
    }

    @ExceptionHandler(value = {FileSizeLimitExceededException.class, RequestEntityTooLargeException.class, MaxUploadSizeExceededException.class})
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ErrorDto handeException(FileSizeLimitExceededException ex) {
        log.error(ex.getMessage());
        return new ErrorDto("413", localizedMessagesService.getMessage("errors.max_file_size"), null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handeException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ErrorDto("500", localizedMessagesService.getMessage("errors.unexpected"), null);
    }
}
