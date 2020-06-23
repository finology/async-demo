package gy.finolo.asyncdemo.service.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @description:
 * @author: Simon
 * @date: 2020-06-22 18:48
 */
@Service
public class PDFServiceHelper {

    @Async
    public Future<String> asyncConvertOnePage(PDDocument document, int pageIndex) {

        long start = System.currentTimeMillis();

        PDFRenderer renderer = new PDFRenderer(document);
        try {
            BufferedImage image = renderer.renderImage(0);
            ImageIO.write(image, "PNG", new File("D:\\development\\java\\async-demo\\images", pageIndex + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        document.close();

        String res = "convert page: " + pageIndex + " cost " + (System.currentTimeMillis() - start);
//        System.out.println(res);

        return new AsyncResult<>(res);
//        return new FutureTask<>();
    }

    @Async
    public Future<String> asyncConvertOnePage2(PDFRenderer renderer, int pageIndex) {

        long start = System.currentTimeMillis();

        try {
            BufferedImage image = renderer.renderImage(pageIndex);
            ImageIO.write(image, "PNG", new File("D:\\development\\java\\async-demo\\images", pageIndex + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        document.close();

        String res = "convert page: " + pageIndex + " cost " + (System.currentTimeMillis() - start);
//        System.out.println(res);

        return new AsyncResult<>(res);
//        return new FutureTask<>();
    }
}
