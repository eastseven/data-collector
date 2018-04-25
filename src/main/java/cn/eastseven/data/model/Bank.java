package cn.eastseven.data.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author eastseven
 * 银行
 */
@Data
public class Bank {

    private String name;

    private String type;

    private String url;

    public Bank(String name, String type, String url) {
        this.name = name;
        this.type = type;
        if (StringUtils.isNoneBlank(url) && !StringUtils.equals(url, "http://")) {
            this.url = url;
        }
    }
}
