package cn.eastseven.data.model;

import lombok.Data;

/**
 * @author eastseven
 * 高校
 */
@Data
public class University {

    /**
     * ID，学校识别码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 所在地
     */
    private String location;

    /**
     * 主管部门
     */
    private String competentDepartment;

    /**
     * 办学层次：专科，本科
     */
    private String level;

    /**
     * 备注
     */
    private String remark;
}
