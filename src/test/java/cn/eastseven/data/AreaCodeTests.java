package cn.eastseven.data;

import cn.eastseven.data.model.AreaCode;
import cn.eastseven.data.repository.AreaCodeRepository;
import cn.eastseven.data.service.AreaCodeSpider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class AreaCodeTests extends DataCollectorApplicationTests {

    @Autowired
    private AreaCodeSpider spider;

    @Autowired
    private AreaCodeRepository repository;

    @Test
    public void test() throws IOException {
        spider.start();
    }

    @Test
    public void export2JavaScript() throws IOException, TemplateException {
        List<AreaCode> allCity = spider.getAllCity();
        Assert.assertNotNull(allCity);
        Assert.assertFalse(allCity.isEmpty());

        /**
         *
         * { label: '', options: [{value:'', label:''}] }
         *
         * */
        List<AreaCode> provinceList = allCity.stream().filter(AreaCode::isProvince).collect(Collectors.toList());
        List<Map> data = provinceList.stream().map(province -> {
            final String code = province.getProvince();
            final String name = province.getName();
            Map group = Maps.newHashMap();
            group.put("label", province.getName());
            List<Map> cityData = allCity.stream().filter(areaCode -> areaCode.getProvince().equals(code))
                    .map(areaCode -> {
                        Map map = Maps.newHashMap();
                        map.put("label", areaCode.getName());
                        map.put("value", areaCode.getCode());
                        return map;
                    }).collect(Collectors.toList());

            if (cityData.size() != 1) {
                cityData = cityData.stream().filter(map -> !map.get("label").equals(name)).collect(Collectors.toList());
            }

            group.put("options", cityData);
            return group;
        }).collect(Collectors.toList());

        Configuration config = new Configuration(Configuration.getVersion());
        config.setDirectoryForTemplateLoading(Paths.get("src/test", "resources").toFile());

        config.setObjectWrapper(new DefaultObjectWrapper(Configuration.getVersion()));
        Template template = config.getTemplate("city.ftl");
        Path path = Paths.get("target", "city.js");
        Files.deleteIfExists(path);
        File file = Files.createFile(path).toFile();

        Map root = Maps.newHashMap();
        root.put("provinces", data);
        template.process(root, new FileWriter(file));

    }

    @Test
    public void export() throws Exception {
        List<AreaCode> all = repository.findAll();
        Assert.assertNotNull(all);
        Assert.assertTrue(CollectionUtils.isNotEmpty(all));

        ObjectMapper m = new ObjectMapper();
        String data = m.writeValueAsString(all);

        Path path = Paths.get("target", AreaCode.class.getSimpleName() + ".json");
        Files.deleteIfExists(path);
        Files.createFile(path);
        Files.write(path, data.getBytes("utf-8"), StandardOpenOption.APPEND);
        log.debug(">>> export done");
    }

    @Test
    public void export2SingleJsonFile() throws Exception {
        List<AreaCode> all = repository.findAll();
        Assert.assertTrue(CollectionUtils.isNotEmpty(all));

        List<Map<String, Object>> provinceDataList = Lists.newArrayList();

        List<AreaCode> provinceList = all.stream().filter(AreaCode::isProvince).collect(Collectors.toList());
        for (AreaCode provinceAreaCode : provinceList) {
            // 省
            List<Map<String, Object>> cityDataList = Lists.newArrayList();
            Map<String, Object> provinceData = newMap("name", provinceAreaCode.getName());
            provinceData.put("code", provinceAreaCode.getCode());
            provinceData.put("children", cityDataList);
            provinceDataList.add(provinceData);

            log.info(">>>\tprovince {}, {}", provinceAreaCode.getCode(), provinceAreaCode.getName());

            List<AreaCode> cityList = all.stream().filter(AreaCode::isCity)
                    .filter(areaCode -> areaCode.getProvince().equals(provinceAreaCode.getProvince()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(cityList)) {
                log.info(">>> 直辖市 {} {}", provinceAreaCode.getCode(), provinceAreaCode.getCode());

                List<AreaCode> list = all.stream().filter(AreaCode::isCounty)
                        .filter(areaCode -> areaCode.getProvince().equals(provinceAreaCode.getProvince()))
                        .collect(Collectors.toList());
                list.forEach(areaCode -> log.info(">>>\t\t直辖市 {} {}", areaCode.getCode(), areaCode.getName()));

                List<Map<String, Object>> countyDataList = Lists.newArrayList();
                Map<String, Object> cityData = newMap("name", provinceAreaCode.getName());
                cityData.put("code", provinceAreaCode.getCode());
                cityData.put("children", countyDataList);
                cityDataList.add(cityData);

                // 县
                countyDataList.addAll(list.stream().map(areaCode -> {
                    Map<String, Object> map = newMap("name", areaCode.getName());
                    map.put("code", areaCode.getCode());
                    return map;
                }).collect(Collectors.toList()));
            } else {
                // 市
                for (AreaCode cityAreaCode : cityList) {
                    List<Map<String, Object>> countyDataList = Lists.newArrayList();
                    Map<String, Object> cityData = newMap("name", cityAreaCode.getName());
                    cityData.put("code", cityAreaCode.getCode());
                    cityData.put("children", countyDataList);
                    cityDataList.add(cityData);

                    log.info(">>>\t\t city {} {}", cityAreaCode.getCode(), cityAreaCode.getName());
                    String cityPrefix = StringUtils.substring(cityAreaCode.getCode(), 0, 4);
                    List<AreaCode> countyList = all.stream().filter(AreaCode::isCounty)
                            .filter(areaCode -> areaCode.getCode().startsWith(cityPrefix))
                            .collect(Collectors.toList());

                    countyList.forEach(areaCode -> log.info(">>>\t\t\tcounty {} {}", areaCode.getCode(), areaCode.getName()));

                    // 县
                    List<Map<String, Object>> list = countyList.stream().map(areaCode -> {
                        Map<String, Object> map = newMap("name", areaCode.getName());
                        map.put("code", areaCode.getCode());
                        return map;
                    }).collect(Collectors.toList());
                    countyDataList.addAll(list);

                }
            }
        }

        Assert.assertTrue(CollectionUtils.isNotEmpty(provinceDataList));
        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(provinceDataList);
        log.info(">>> {}\n{}", provinceDataList.size(), json);

        Path path = Paths.get("target", "area_code.json");
        Files.deleteIfExists(path);
        Files.createFile(path);
        Files.write(path, json.getBytes("utf-8"), StandardOpenOption.APPEND);
        log.debug(">>> export done");
    }

    public Map<String, Object> newMap(String key, Object value) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(key, value);
        return map;
    }
}
