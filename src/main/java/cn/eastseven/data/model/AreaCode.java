package cn.eastseven.data.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author eastseven
 */
@Data
public class AreaCode {

    private String code;

    private String name;

    private String province;

    private String city;

    private String county;

    public AreaCode(String code, String name) {
        this.code = code;
        this.name = name;

        this.province = StringUtils.substring(code, 0, 2);
        this.city = StringUtils.substring(code, 2, 4);
        this.county = StringUtils.substring(code, 4, 6);
    }

    public boolean isProvince() {
        return "00".equals(city) && "00".equals(county);
    }

    public boolean isCity() {
        return !"00".equals(province) && !"00".equals(city) && "00".equals(county);
    }

    public boolean isCounty() {
        return !"00".equals(province) && !"00".equals(city) && !"00".equals(county);
    }
}
