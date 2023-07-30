package com.Travel.server;

import com.Travel.dao.pojo.User;
import com.Travel.vo.Result;
import com.Travel.vo.UserVo;
import com.Travel.vo.param.*;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 根据分页参数查询普通用户
     * @param pageParam
     * @return
     */
    Result selectAllSimpleUser(PageParam pageParam);

    /**
     * 更具分页参数查询管理员
     * @param pageParam
     * @return
     */
    Result selectAllAdministrators(PageParam pageParam);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User selectByIdUser(Integer id);

    /**
     * 修改用户
     * @param user
     * @return
     */
    Result updateUser(User user);

    /**
     * 删除用户
     * @param id
     * @return
     */
    Result deleteUser(Integer id);

    /**
     * 注册用户
     * @param userParam
     * @return
     */
    Result registerUser(UserParam userParam);

    /**
     * 更具名称搜索用户
     * @param username
     * @return
     */
    Result selectByUserName(Integer username);

    /**
     * 用户登录
     * @param userLoginRequest
     * @param servletRequest
     * @return
     */
    Result selectUser(UserLoginRequest userLoginRequest, HttpServletRequest servletRequest);

    Result registerUser(User user);

    UserVo selectByIdUserVo(Integer id);

    /**
     * 搜索当前用户
     * @param servletRequest
     * @return
     */
    UserVo selectCurrentLoginUser(HttpServletRequest servletRequest);

    /**
     *退出登录
     * @param servletRequest
     * @return
     */
    Boolean outLogin(HttpServletRequest servletRequest);

    /**
     * 根据用户等级进行排名
     * @return
     */
    List<UserVo> userGardeArrange();

    /**
     * 修改用户信息
     * @param updateUserParam
     * @return
     */
    Result updateUsers(UpdateUserParam updateUserParam);

    Result updateTags(List<String> tags, HttpServletRequest servletRequest);

    Result matchUsers(long num, UserVo loginUser);

    Result searchUsersByTags(List<String> tagNameList);

    Result addConcernUser(Integer concernUserId, Integer loginUserId);

    Boolean isnoConcern(Integer concernUserId, Integer loginUserId);

    Result getUserFansList(IdParam idParam);

    Result getUserConcernList(IdParam idParam);
}
