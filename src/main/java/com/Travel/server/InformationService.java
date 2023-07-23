package com.Travel.server;

import com.Travel.dao.pojo.Information;
import com.Travel.dao.pojo.Scenic;
import com.Travel.vo.Result;
import com.Travel.vo.param.PageParam;
import com.baomidou.mybatisplus.extension.service.IService;

public interface InformationService extends IService<Information> {
    /**
     * 添加资讯
     * @param information
     * @return
     */
    Result addInformation(Information information);

    /**
     * 根据分页参数搜索资讯
     * @param pageParam
     * @return
     */
    Result selectAllInformation(PageParam pageParam);

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
     * 根据id搜索资讯
     * @param id
     * @return
     */
    Result selectByIdInformation(Integer id);

    /**
     * 搜索热门资讯
     * @return
     */
    Result selectHotInformation();

    /**
     * 根据分页参数搜索站内资讯
     * @param pageParam
     * @return
     */
    Result selectWithinInformation(PageParam pageParam);

    /**
     * 根据分页参数搜索站外资讯
     * @param pageParam
     * @return
     */
    Result selectOutsideInformation(PageParam pageParam);

    /**
     * 根据资讯标题搜索资讯
     * @param title
     * @return
     */
    Result selectTitle(String title);
}
