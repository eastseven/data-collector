package cn.eastseven.data.service;

import cn.eastseven.data.DataCollectorApplicationTests;
import cn.eastseven.data.model.University;
import cn.eastseven.data.repository.UniversityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.Assert.*;

@Slf4j
public class UniversityExtractorTest extends DataCollectorApplicationTests {

    @Autowired private UniversityExtractor extractor;

    @Autowired private UniversityRepository repository;

    @Test
    public void start() throws Exception {
        extractor.start();
    }

    @Test
    public void test() {
        Page<University> page = repository.findAll(PageRequest.of(0, 50));
        Assert.assertNotNull(page);
        Assert.assertTrue(page.hasContent());

        page.forEach(university -> System.out.println(university.getName()));
    }
}