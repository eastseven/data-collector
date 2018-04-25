package cn.eastseven.data.jd;

import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * @author eastseven
 */
@TargetUrl("https://item.jd.com/\\d+.html")
@HelpUrl("https://www.jd.com")
public class JdModel {

    @ExtractBy(value = "#name > div.sku-name", type = ExtractBy.Type.Css, notNull = true)
    private String name;
}
