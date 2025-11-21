package ru.otus.fin.library.services;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.fin.library.config.MinioProperties;
import ru.otus.fin.library.exceptions.RequestEntityTooLargeException;

import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MinioBlobService implements BlobService {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    private final LocalizedMessagesService localizedMessagesService;

    @SneakyThrows
    public String upload(MultipartFile multipartFile) {
        String extension = FileNameUtils.getExtension(multipartFile.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + extension;
        var size = multipartFile.getSize();
        var sizeMb = size / (1024 * 2);
        if (size > minioProperties.getFileSize()) {
            throw new RequestEntityTooLargeException(
                    localizedMessagesService.getMessage("errors.max_file_size", sizeMb));
        }
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(fileName)
                        .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                        .contentType(multipartFile.getContentType())
                        .build()
        );
        return fileName;
    }

    @SneakyThrows
    public Resource download(String filename) {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(filename)
                        .build()
        );
        return new InputStreamResource(stream);
    }
}
