package ru.otus.fin.library.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;

@RequiredArgsConstructor
@Service
@Slf4j
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final MessageSource messageSource;

    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    private String tryGetMessage(String code, Object... args) {
        try {
            return messageSource.getMessage(code, args, getLocale());
        } catch (NoSuchMessageException ex) {
            log.warn(ex.getMessage());
            return code;
        }
    }

    @Override
    public String getMessage(String code, Object... args) {

        if (args.length > 0) {
            args = Arrays.stream(args)
                    .map(value -> value instanceof String strVal ? tryGetMessage(strVal) : value)
                    .toArray();
        }
        return tryGetMessage(code, args);
    }
}
