package cn.eastseven.data.model;

import com.hankcs.hanlp.HanLP;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author eastseven
 */
@Data
@Document(collection = "t_area_code")
public class AreaCode {

    @Id
    private String code;

    private String name;

    private String aleph;

    private String province;

    private String provinceName;

    private String city;

    private String cityName;

    private String county;

    private String countyName;

    public AreaCode() {}

    public AreaCode(String code, String name) {
        this.code = code;
        this.name = name;

        this.province = StringUtils.substring(code, 0, 2);
        this.city = StringUtils.substring(code, 2, 4);
        this.county = StringUtils.substring(code, 4, 6);

        this.aleph = StringUtils.substring(HanLP.convertToPinyinFirstCharString(this.name, "", false), 0, 1).toUpperCase();

        if (isProvince()) this.provinceName = this.name;
        if (isCity()) this.cityName = this.name;
        if (isCounty()) this.countyName = this.name;
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
