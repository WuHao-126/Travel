package com.Travel.dao.mapper;

import com.Travel.dao.pojo.User;
import com.Travel.vo.param.UpdateUserParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    void addExperience(Integer userId);
    void addGrade(Integer id);
    void updateUserInformation(UpdateUserParam updateUserParam);
}
