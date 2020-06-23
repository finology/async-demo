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

import java.io.File;
import java.io.FileInputStream;
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

    @Test
    void convertSerially() throws Exception {
        String small = "D:\\development\\java\\async-demo\\pdf\\testpdf.pdf";
        String big = "D:\\development\\java\\async-demo\\pdf\\Java8实战.pdf";
        File file = new File(big);
        InputStream inputStream = new FileInputStream(file);
        MockMultipartFile mockFile = new MockMultipartFile("file", inputStream);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/convert/serially").file(mockFile))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    void convertConcurrently() throws Exception {
        String small = "D:\\development\\java\\async-demo\\pdf\\testpdf.pdf";
        String big = "D:\\development\\java\\async-demo\\pdf\\Java8实战.pdf";
        File file = new File(small);
        InputStream inputStream = new FileInputStream(file);
        MockMultipartFile mockFile = new MockMultipartFile("file", inputStream);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/convert/concurrently").file(mockFile))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    void convertConcurrently2() throws Exception {
        String small = "D:\\development\\java\\async-demo\\pdf\\testpdf.pdf";
        String big = "D:\\development\\java\\async-demo\\pdf\\Java8实战.pdf";
        File file = new File(big);
        InputStream inputStream = new FileInputStream(file);
        MockMultipartFile mockFile = new MockMultipartFile("file", inputStream);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/convert/concurrently2").file(mockFile))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }
}
