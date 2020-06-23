package gy.finolo.asyncdemo.controller;

import gy.finolo.asyncdemo.service.PDFServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: Simon
 * @date: 2020-06-22 17:23
 */
@RestController
@RequestMapping("/convert")
public class PDFController {

    @Autowired
    private PDFServcie pdfServcie;

    /**
     * 顺序执行
     */
    @PostMapping("/serially")
    public void convertSerially(MultipartFile file) {
        pdfServcie.convertSerially(file);
    }

    @PostMapping("/concurrently")
    public void convertConcurrently(MultipartFile file) {
        pdfServcie.convertConcurrently(file);
    }

    @PostMapping("/concurrently2")
    public void convertConcurrently2(MultipartFile file) {
        pdfServcie.convertConcurrently2(file);
    }
}
