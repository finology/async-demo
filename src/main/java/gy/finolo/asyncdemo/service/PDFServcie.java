package gy.finolo.asyncdemo.service;

import org.springframework.web.multipart.MultipartFile;

public interface PDFServcie {

    void convertSerially(MultipartFile file);

    void convertConcurrently(MultipartFile file);

    void convertConcurrently2(MultipartFile file);
}
