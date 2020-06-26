package gy.finolo.asyncdemo.service.impl;

import com.github.jaiimageio.impl.common.SimpleRenderedImage;
import gy.finolo.asyncdemo.service.PDFServcie;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.interactive.pagenavigation.PDThreadBead;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @description:
 * @author: Simon
 * @date: 2020-06-22 17:29
 */
@Service
public class PDFServiceImpl implements PDFServcie {

    @Autowired
    private PDFServiceHelper pdfServiceHelper;

    @Qualifier("getAsyncExecutor")
    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Override
    public void convertSerially(MultipartFile file) {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        long start = System.currentTimeMillis();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            int pageCount = document.getNumberOfPages();
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < pageCount; i++) {
                System.out.println("processing page: " + i);
                BufferedImage image = renderer.renderImage(i);
                ImageIO.write(image, "PNG", new File(ResourceUtils.getURL("classpath:").getPath(), i +
                        ".png"));
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
    public void convertConcurrently(MultipartFile file) throws Exception {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        long start = System.currentTimeMillis();

        List<Future<String>> futures = new ArrayList<>();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            int pageCount = document.getNumberOfPages();

            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                Future<String> future = pdfServiceHelper.asyncConvertOnePage(file.getInputStream(), pageIndex);
                futures.add(future);
            }

            for (Future<String> future : futures) {
                String res = future.get();
                System.out.println(res);
            }

        } catch (IOException e) {
            e.printStackTrace();
            cancelFutures(futures);
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            cancelFutures(futures);
            throw e;
        } catch (ExecutionException e) {
            e.printStackTrace();
            cancelFutures(futures);
            throw e;
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
    public void convertByCallable(MultipartFile file) throws Exception {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        long start = System.currentTimeMillis();
        List<Future<String>> futures = new ArrayList<>();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {

            int pageCount = document.getNumberOfPages();

            for (int i = 0; i < pageCount; i++) {
                ConvertOnePageTask task = new ConvertOnePageTask(file.getInputStream(), i);
                futures.add(executor.submit(task));
            }
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }

        } catch (IOException e) {
            e.printStackTrace();
            cancelFutures(futures);
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            cancelFutures(futures);
            throw e;
        } catch (ExecutionException e) {
            e.printStackTrace();
            cancelFutures(futures);
            throw e;
        }

        System.out.println("convertByCallable cost: " + (System.currentTimeMillis() - start));

    }

    /**
     * 多线程出错
     */
    @AllArgsConstructor
    private static class ConvertOnePageTask implements Callable<String> {

//        private PDDocument document;
        private InputStream inputStream;
        private Integer pageIndex;

        @Override
        public String call() throws IOException {
            long start = System.currentTimeMillis();

            try (PDDocument document = PDDocument.load(inputStream)) {
                PDFRenderer renderer = new PDFRenderer(document);
                BufferedImage image = renderer.renderImage(pageIndex);
                ImageIO.write(image, "PNG", new File(ResourceUtils.getURL("classpath:").getPath(), pageIndex + ".png"));
            }

            return Thread.currentThread().getName() + " convert page: " + pageIndex + " cost " + (System.currentTimeMillis() - start);
        }
    }

}
