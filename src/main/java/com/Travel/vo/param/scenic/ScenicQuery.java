package com.Travel.vo.param.scenic;

import com.Travel.vo.param.common.PageParam;
import lombok.Data;

@Data
public class ScenicQuery extends PageParam {
    private Integer id;
    private String sname;
    private String type;
    private Integer smallPrice;
    private Integer bigPrice;
    private String area;
    private String areaDetailed;
    private Integer level;

}
