package ru.otus.fin.library.services;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}
