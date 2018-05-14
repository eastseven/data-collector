package cn.eastseven.data.repository;

import cn.eastseven.data.model.AreaCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author d7
 */
public interface AreaCodeRepository extends MongoRepository<AreaCode, String> {

    List<AreaCode> findByCountyIs(String code);

    List<AreaCode> findByProvinceIs(String provinceCode);

    /**
     * 获取 区县 列表数据
     * @param province
     * @param city
     * @return
     */
    List<AreaCode> findByProvinceIsAndCityIs(String province, String city);

    /**
     * 获取 市州 列表数据
     * @param province
     * @param county
     * @return
     */
    List<AreaCode> findByProvinceIsAndCountyIs(String province, String county);

    /**
     * 获取 省份 列表数据
     * @param city
     * @param county
     * @return
     */
    List<AreaCode> findByCityIsAndCountyIs(String city, String county);


}
