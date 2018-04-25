package cn.eastseven.data.service;

import cn.eastseven.data.DataCollectorApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.*;

@Slf4j
public class BankSpiderTest extends DataCollectorApplicationTests {

    @Autowired private BankSpider bankSpider;

    @Test
    public void start() throws IOException {
        bankSpider.start();
    }
}