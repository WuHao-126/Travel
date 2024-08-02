package com.Travel.server;

import com.Travel.dao.pojo.Information;
import com.Travel.vo.Result;
import com.Travel.vo.param.common.PageParam;
import com.Travel.vo.param.information.InformationQuery;
import com.baomidou.mybatisplus.extension.service.IService;

public interface InformationService extends IService<Information> {
    /**
     * 添加资讯
     * @param information
     * @return
     */
    Result addInformation(Information information);

    /**
     * 条件查询资讯列表
     * @param informationQuery
     * @return
     */
    Result searchInformation(InformationQuery informationQuery);

    /**
     * 根据id删除资讯
     * @param id
     * @return
     */
    Result deleteInformation(Integer id);

    /**
     * 修改资讯信息
     * @param information
     * @return
     */
    Result updateInformation(Information information);


    /**
     * 搜索热门资讯
     * @return
     */
    Result selectHotInformation();

    Result addView(Integer id);
}
