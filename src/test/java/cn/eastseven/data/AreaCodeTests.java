package cn.eastseven.data;

import cn.eastseven.data.service.AreaCodeSpider;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class AreaCodeTests extends DataCollectorApplicationTests {

    @Autowired private AreaCodeSpider spider;

    @Test
    public void test() throws IOException {
        spider.start();
    }
}
