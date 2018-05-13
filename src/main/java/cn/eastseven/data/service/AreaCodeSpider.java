package cn.eastseven.data.service;

import cn.eastseven.data.model.AreaCode;
import cn.eastseven.data.repository.AreaCodeRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author eastseven
 * <p>
 * 行政区划代码 获取器
 */
@Slf4j
@Service
public class AreaCodeSpider {

    private static final String url = "http://www.mca.gov.cn/article/sj/tjbz/a/2018/201803131439.html";

    private static final Pattern p = Pattern.compile("\\d{6}");

    @Autowired
    private AreaCodeRepository repository;

    public void start() throws IOException {
        List<String> lines = Jsoup.connect(url).get().body().select("table tr").eachText();
        lines = lines.parallelStream().filter(line -> p.matcher(line).find()).collect(Collectors.toList());

        List<AreaCode> data = Lists.newArrayList();
        for (String line : lines) {
            String code = StringUtils.substring(line, 0, 6);
            String name = StringUtils.substringAfterLast(line, code).trim();
            AreaCode area = new AreaCode(code, name);
            log.debug(">>> {}, 省={}, 市={}, 区县={}", area.getName(), area.isProvince(), area.isCity(), area.isCounty());
            data.add(area);
        }

        repository.deleteAll();
        repository.saveAll(data);
        log.info(">>> save area code {}", data.size());

        List<AreaCode> provinceList = data.parallelStream().filter(AreaCode::isProvince).collect(Collectors.toList());
        List<AreaCode> cityList = data.parallelStream().filter(AreaCode::isCity).collect(Collectors.toList());

        provinceList.parallelStream().forEach(province -> {
            String provinceCode = province.getProvince();
            String provinceName = province.getName();
            List<AreaCode> list = repository.findByProvinceIs(provinceCode);
            list.forEach(areaCode -> areaCode.setProvinceName(provinceName));
            repository.saveAll(list);
        });

        cityList.parallelStream().forEach(city -> {
            String provinceCode = city.getProvince();
            String cityCode = city.getCity();
            List<AreaCode> list = repository.findByProvinceIsAndCityIs(provinceCode, cityCode);
            list.forEach(areaCode -> areaCode.setCityName(city.getName()));
            repository.saveAll(list);
        });

    }

    public List<AreaCode> getAllCity() {
        return repository.findByCountyIs("00");
    }
}
