package cn.eastseven.data.service;

import cn.eastseven.data.DataCollectorApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.junit.Assert.*;

@Slf4j
public class ForeignTradeEnterpriseServiceTest extends DataCollectorApplicationTests {

    @Autowired
    private ForeignTradeEnterpriseService service;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void start() throws IOException {
        assertNotNull(service);
        service.start();
    }

    @Test
    public void detail() {
        Map<String, String> data = service.detail("https://waimao.mingluji.com/node/115781");
        assertFalse(data.isEmpty());
        data.forEach((k, v) -> log.debug("{} {}", k, v));
    }

    @Test
    public void page() throws IOException {
        List<String> urls = service.page("https://waimao.mingluji.com/node/?page=0");
        assertTrue(isNotEmpty(urls));

        urls.forEach(url -> log.debug("{}", url));
    }
}