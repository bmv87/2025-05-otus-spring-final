package ru.otus.fin.library.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Autowired
    private DefaultProps props;

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setSupportedLocales(Collections.singletonList(props.getLocale()));
        localeResolver.setDefaultLocale(props.getLocale());
        return localeResolver;
    }
}
