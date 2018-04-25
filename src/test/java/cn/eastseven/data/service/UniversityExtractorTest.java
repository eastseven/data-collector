package cn.eastseven.data.service;

import cn.eastseven.data.DataCollectorApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@Slf4j
public class UniversityExtractorTest extends DataCollectorApplicationTests {

    @Autowired private UniversityExtractor extractor;

    @Test
    public void start() throws Exception {
        extractor.start();
    }
}