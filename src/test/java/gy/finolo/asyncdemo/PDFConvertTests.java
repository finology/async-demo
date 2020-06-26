package gy.finolo.asyncdemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @description:
 * @author: Simon
 * @date: 2020-06-22 17:50
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PDFConvertTests {

    @Autowired
    private MockMvc mockMvc;

    private String small = "D:\\development\\java\\async-demo\\pdf\\testpdf.pdf";
    private String big = "D:\\development\\java\\async-demo\\pdf\\Java8.pdf";

    // serially, 46s
    private String big_mac = "/Users/simon/Development/workspace/java/async-demo/pdf/Java8.pdf";
    private String media_mac = "/Users/simon/Development/workspace/java/async-demo/pdf/青云蓝海.pdf";

    @Test
    void convertSerially() throws Exception {

        File file = new File(media_mac);
        InputStream inputStream = new FileInputStream(file);
        MockMultipartFile mockFile = new MockMultipartFile("file", inputStream);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/convert/serially").file(mockFile))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    void convertConcurrently() throws Exception {

        File file = new File(media_mac);
        InputStream inputStream = new FileInputStream(file);
        MockMultipartFile mockFile = new MockMultipartFile("file", inputStream);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/convert/concurrently").file(mockFile))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    void convertByCallable() throws Exception {
        File file = new File(media_mac);
        InputStream inputStream = new FileInputStream(file);
        MockMultipartFile mockFile = new MockMultipartFile("file", inputStream);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/convert/convertByCallable").file(mockFile))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    void getPath() throws FileNotFoundException {
        System.out.println(ResourceUtils.getURL("classpath:"));
        System.out.println(PDFConvertTests.class.getResource("/"));
    }

    @Test
    void get18Timestamp() {


    }
}
