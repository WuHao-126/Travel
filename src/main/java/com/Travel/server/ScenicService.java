package com.Travel.server;

import com.Travel.dao.pojo.Scenic;
import com.Travel.vo.Result;
import com.Travel.vo.param.common.PageParam;
import com.Travel.vo.param.scenic.ScenicParam;
import com.Travel.vo.param.scenic.ScenicQuery;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ScenicService extends IService<Scenic> {
    /**
     * 添加景区
     * @param scenic
     * @return
     */
    Result addScennic(Scenic scenic);

    /**
     * 根据分页参数查询景区
     * @param pageParam
     * @return
     */
    Result selectAllScenic(PageParam pageParam);

    /**
     * 根据id查询景区
     * @param id
     * @return
     */
    Scenic selectById(Integer id);

    /**
     * 根据id删除景区
     * @param id
     * @return
     */
    Result deleteScenic(Integer id);

    /**
     * 修改景区信息
     * @param scenic
     * @return
     */
    Result updateScenic(Scenic scenic);

    /**
     * 搜索热门景区
     * @return
     */
    Result selectHotScenic();

    /**
     * 根据景区名称搜索景区
     * @param sname
     * @return
     */
    Result selectSName(String sname);

    /**
     * 根据景区区域搜索景区
     * @param location
     * @param pageParam
     * @return
     */
    Result selectRegio(String location, PageParam pageParam);

    /**
     * 根据景区详细地址搜索景区
     * @param scenicParam
     * @return
     */
    Result selectScenicName(ScenicParam scenicParam);

    Result searchScenic(ScenicQuery scenicQuery);
}
