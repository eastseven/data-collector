package cn.eastseven.data.service;

import cn.eastseven.data.model.University;
import com.github.crab2died.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author eastseven
 */
@Slf4j
@Service
public class UniversityExtractor {

    public void start() throws Exception {
        Path path = Paths.get("data", "全国普通高等学校名单.xls");
        List<List<String>> data = ExcelUtils.getInstance().readExcel2List(path.toFile().getPath());

        for (List<String> line : data) {
            University university = new University();
            for (int index = 0; index < line.size(); index++) {
                String value = line.get(index);
                switch (index) {
                    case 1:
                        university.setName(value);
                        break;
                    case 2:
                        university.setCode(value);
                        break;
                    case 3:
                        university.setCompetentDepartment(value);
                        break;
                    case 4:
                        university.setLocation(value);
                        break;
                    case 5:
                        university.setLevel(value);
                        break;
                    case 6:
                        university.setRemark(value);
                        break;
                    default:
                }
            }

            log.debug(">>> {}", university);
        }
    }
}
