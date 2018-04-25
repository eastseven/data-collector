package cn.eastseven.data.service;

import cn.eastseven.data.model.Bank;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author eastseven
 */
@Slf4j
@Service
public class BankSpider {

    private static final String url = "http://www.cbrc.gov.cn/chinese/jrjg/index.html";

    public void start() throws IOException {
        Elements elements = Jsoup.connect(url).get().body().select("div.wai");
        elements.forEach(element -> {
            String type = element.select("div.biao div.zi").text();

            Elements lines = element.select("ul > li > ul > li");
            for (Element el : lines) {
                if (!el.hasText()) continue;

                boolean bln =  el.select("a").isEmpty();
                String url = null, name = null;
                if (!bln) {
                    url = el.select("a").attr("href");
                    name = el.select("a").text();
                } else {
                    name = el.text();
                }
                Bank bank = new Bank(name, type, url);
                log.debug(">>> {}, {}", bank, el.html());
            }
        });
    }
}
