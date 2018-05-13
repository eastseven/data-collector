package cn.eastseven.data;

import cn.eastseven.data.model.AreaCode;
import cn.eastseven.data.service.AreaCodeSpider;
import com.google.common.collect.Maps;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class AreaCodeTests extends DataCollectorApplicationTests {

    @Autowired
    private AreaCodeSpider spider;

    @Test
    public void test() throws IOException {
        spider.start();
    }

    @Test
    public void export() throws IOException, TemplateException {
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
}
