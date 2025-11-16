package ru.otus.fin.library.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface BlobService {

    String upload(MultipartFile multipartFile);

    Resource download(String filename);
}
