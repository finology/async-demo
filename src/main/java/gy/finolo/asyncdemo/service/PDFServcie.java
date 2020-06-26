package gy.finolo.asyncdemo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PDFServcie {

    void convertSerially(MultipartFile file) throws Exception;

    void convertConcurrently(MultipartFile file) throws Exception;

    void convertByCallable(MultipartFile file) throws Exception;
}
