package cn.eastseven.data.repository;

import cn.eastseven.data.model.AreaCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author d7
 */
public interface AreaCodeRepository extends MongoRepository<AreaCode, String> {

    List<AreaCode> findByProvinceIs(String provinceCode);

    List<AreaCode> findByProvinceIsAndCityIs(String provinceCode, String cityCode);

    List<AreaCode> findByCountyIs(String code);
}
