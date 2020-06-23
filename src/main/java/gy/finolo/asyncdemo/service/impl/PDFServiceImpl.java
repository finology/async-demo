package gy.finolo.asyncdemo.service.impl;

import gy.finolo.asyncdemo.service.PDFServcie;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: Simon
 * @date: 2020-06-22 17:29
 */
@Service
public class PDFServiceImpl implements PDFServcie {

    @Autowired
    private PDFServiceHelper pdfServiceHelper;

    @Override
    public void convertSerially(MultipartFile file) {
        long start = System.currentTimeMillis();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            int pageCount = document.getNumberOfPages();
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < pageCount; i++) {
                System.out.println("processing page: " + i);
                BufferedImage image = renderer.renderImage(i);
                ImageIO.write(image, "PNG", new File("D:\\development\\java\\async-demo\\images", i + ".png"));
            }
        } catch (IOException e) {
            System.out.println("convert serially IOException =============== ");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("convert serially Exception =============== ");
            e.printStackTrace();
        } catch (Error error) {
            System.out.println("convert serially Error =============== ");
            error.printStackTrace();
        }
        System.out.println("convertSerially cost: " + (System.currentTimeMillis() - start));
    }

    @Override
    public void convertConcurrently(MultipartFile file) {
        long start = System.currentTimeMillis();

        List<Future<String>> futures = new ArrayList<>();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            int pageCount = document.getNumberOfPages();

            Splitter splitter = new Splitter();
            List<PDDocument> pages = splitter.split(document);

            int pageIndex = 0;
            for (PDDocument page : pages) {
                Future<String> future = pdfServiceHelper.asyncConvertOnePage(page, pageIndex++);
                futures.add(future);
            }

            for (Future<String> future : futures) {
                String res = future.get();
                System.out.println(res);
            }

        } catch (IOException e) {
            e.printStackTrace();
            cancelFutures(futures);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cancelFutures(futures);
        } catch (ExecutionException e) {
            e.printStackTrace();
            cancelFutures(futures);
        }

        System.out.println("convertConcurrently cost: " + (System.currentTimeMillis() - start));
    }

    private void cancelFutures(List<Future<String>> futures) {
        for (Future<String> future : futures) {
            if (!future.isDone()) {
                future.cancel(true);
                System.out.println("cancel future: " + future);
            }
        }
    }

    @Override
    public void convertConcurrently2(MultipartFile file) {
        long start = System.currentTimeMillis();

        List<Future<String>> futures = new ArrayList<>();

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            int pageCount = document.getNumberOfPages();

            PDFRenderer renderer = new PDFRenderer(document);

            int pageIndex = 0;
            for (int i = 0; i < pageCount; i++) {

                Future<String> future = pdfServiceHelper.asyncConvertOnePage2(renderer, pageIndex++);
                futures.add(future);
            }

            for (Future<String> future : futures) {
                String res = future.get();
                System.out.println(res);
            }

        } catch (IOException e) {
            e.printStackTrace();
            cancelFutures(futures);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cancelFutures(futures);
        } catch (ExecutionException e) {
            e.printStackTrace();
            cancelFutures(futures);
        }

        System.out.println("convertConcurrently cost: " + (System.currentTimeMillis() - start));
    }
}
