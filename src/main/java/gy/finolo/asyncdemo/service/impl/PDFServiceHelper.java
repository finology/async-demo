package gy.finolo.asyncdemo.service.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: Simon
 * @date: 2020-06-22 18:48
 */
@Service
public class PDFServiceHelper {

    @Async
    public Future<String> asyncConvertOnePage(InputStream inputStream, int pageIndex) throws IOException {

        long start = System.currentTimeMillis();

        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImage(pageIndex);
            ImageIO.write(image, "PNG", new File(ResourceUtils.getURL("classpath:").getPath(), pageIndex + ".png"));
        }

        String res =
                Thread.currentThread().getName() + " convert page: " + pageIndex + " cost " + (System.currentTimeMillis() - start);

        return new AsyncResult<>(res);
    }

}
