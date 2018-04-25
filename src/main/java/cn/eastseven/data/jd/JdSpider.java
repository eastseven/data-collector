package cn.eastseven.data.jd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

/**
 * @author eastseven
 */
@Slf4j
@Service
public class JdSpider {

    public void start() {
        OOSpider.create(Site.me(), JdModel.class).setIsExtractLinks(Boolean.TRUE).addUrl("https://shouji.jd.com").run();
    }
}
