package com.Travel.vo.param.information;

import com.Travel.vo.param.common.PageParam;
import lombok.Data;

import java.util.Date;

@Data
public class InformationQuery extends PageParam {
    private Integer id;
    private String title;
    private String type;
    private Date startTime;
    private Date endTime;
}
