package com.Travel.dao.mapper;

import com.Travel.dao.pojo.User;
import com.Travel.vo.UserVo;
import com.Travel.vo.param.user.UpdateUserParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    void addExperience(Integer userId);
    void addGrade(Integer id);
    void updateUserInformation(UpdateUserParam updateUserParam);
    boolean updateTags(@Param("tagsJson") String tagsJson, @Param("id") Integer id);
    boolean addConcernUser(@Param("concernUserId") Integer concernUserId, @Param("loginUserId") Integer loginUserId);

    int isNoConcern(@Param("loginUserId") Integer loginUserId, @Param("concernUserId")Integer concernUserId);

    boolean cancelConcern(@Param("loginUserId") Integer loginUserId, @Param("concernUserId")Integer concernUserId);

    List<UserVo> getUserFans(@Param("id") Integer id,@Param("currentPage") Integer currentPage,@Param("pageSize") Integer pageSize);

    List<UserVo> getUserConcern(@Param("id") Integer id,@Param("currentPage") Integer currentPage,@Param("pageSize") Integer pageSize);

    List<Integer> getUserFansId(Integer id);
}
