package com.Travel.dao.mapper;

import com.Travel.dao.pojo.MyCollection;
import com.Travel.dao.pojo.Route;
import com.Travel.dao.pojo.Scenic;
import com.Travel.vo.param.common.CollectionParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface MyCollectionMapper extends BaseMapper<MyCollection> {
    List<Scenic> getUserCollectionScenic(@RequestParam("collectionParam") CollectionParam collectionParam);
    List<Route> getUserCollectionRouter(@RequestParam("collectionParam") CollectionParam collectionParam);
}
