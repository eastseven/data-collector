package cn.eastseven.data.jd;

import cn.eastseven.data.DataCollectorApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@Slf4j
public class JdSpiderTest extends DataCollectorApplicationTests {

    @Autowired private JdSpider spider;

    @Test
    public void start() {
        spider.start();
    }
}