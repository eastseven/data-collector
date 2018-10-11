package cn.eastseven.data.service;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author d7
 * <p>
 * [中国外贸企业名录](https://waimao.mingluji.com/)
 * <p>
 * 该网站有反爬机制，需要代理IP
 * <p>
 * 必须用爬虫框架来处理，后面来改
 */
@Slf4j
@Service
public class ForeignTradeEnterpriseService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ForeignTradeEnterpriseService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 入口
     *
     * @throws IOException
     */
    public void start() throws IOException {
        Element body = Jsoup.connect("https://waimao.mingluji.com/node/").get().body();
        Element lastPage = body.select("#block-system-main > div > div.item-list > ul > li.pager-last.last a").first();
        String href = lastPage.attr("href");
        int total = Integer.parseInt(StringUtils.substringAfter(href, "="));
        log.debug(">>> href {}, total {}", href, total);

        for (int page = 0; page <= total; page++) {
            List<String> urls = page("https://waimao.mingluji.com/node?page=" + page);
            urls.forEach(this::detail);
        }
    }

    /**
     * 列表页
     *
     * @param url URL
     * @return url 集合
     * @throws IOException
     */
    public List<String> page(String url) throws IOException {
        final String css1 = "#block-system-main div.content";
        final String css2 = "div.link-wrapper ul.links li.node-readmore a";
        List<String> list = Jsoup.connect(url).get().body().select(css1).select(css2).eachAttr("href");

        return list.stream().map("https://waimao.mingluji.com"::concat).collect(Collectors.toList());
    }

    /**
     * 页面明细数据
     *
     * @param url URL
     * @return 明细数据
     */
    public Map<String, String> detail(String url) {
        Map<String, String> data = Maps.newHashMap();
        try {

            Element body = Jsoup.parse(new URL(url), 100000).body();
            Elements elements = body.select("#block-system-main fieldset").select("ul > li");

            for (Element element : elements) {
                String label = element.select("span.field-label").text();
                String value = element.select("span.field-item").text();

                if (StringUtils.containsAny(label, "购买数据")) {
                    continue;
                }

                data.put(label, value);
            }
        } catch (Exception e) {
            log.error("{}", url);
            log.error("", e);
        }

        if (!data.isEmpty()) {
            mongoTemplate.insert(data, "t_foreign_trade_enterprise");
        }
        return data;
    }
}
