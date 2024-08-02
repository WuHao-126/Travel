package com.Travel.controller;

import cn.hutool.core.util.RandomUtil;
import com.Travel.annotation.AuthCheck;
import com.Travel.dao.pojo.User;
import com.Travel.server.UserService;
import com.Travel.vo.Result;
import com.Travel.vo.UserVo;
import com.Travel.vo.param.common.IdParam;
import com.Travel.vo.param.common.PageParam;
import com.Travel.vo.param.scenic.ScenicQuery;
import com.Travel.vo.param.user.UpdateUserParam;
import com.Travel.vo.param.user.UserLoginRequest;
import com.Travel.vo.param.user.UserParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "用户管理接口")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询所有普通用户
     * @param pageParam
     * @return
     */
    @ApiOperation("查询所有普通用户接口")
    @PostMapping("/simple")
    public Result selectAllSimpleUser(@RequestBody PageParam pageParam){
        return userService.selectAllSimpleUser(pageParam);
    }
    /**
     * 查询所有管理员用户
     * @param pageParam
     * @return
     */
    @ApiOperation("查询所有管理员用户")
    @PostMapping("/administrators")
    public Result selectAllAdministrators(@RequestBody PageParam pageParam){
        return userService.selectAllAdministrators(pageParam);
    }

    /**
     * 注册用户
     * @param userParam
     * @return
     */
    @ApiOperation("注册用户接口")
    @PostMapping("/register")
    public Result registerUser(@RequestBody UserParam userParam){
        return userService.registerUser(userParam);
    }

    /**
     *  登录
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/select")
    public Result selectUser(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest servletRequest){
        return userService.UserLogin(userLoginRequest,servletRequest);
    }
    /**
     * 根据ID查询用户
     * @param id
     * @return
     */
    @GetMapping("/detailed/{id}")
    public User selectByIdDetailedUser(@PathVariable("id") Integer id){
        return userService.selectByIdUser(id);
    }

    /**
     * 根据id搜索用户
     * @param id
     * @return
     */
    @GetMapping("/id/{id}")
    public UserVo selectByIdUserVo(@PathVariable("id") Integer id){
        return userService.selectByIdUserVo(id);
    }
    /**
     * 根据账号查询用户
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public Result selectByUserName(@PathVariable("username") Integer username){
        return userService.selectByUserName(username);
    }

    /**
     * 获取验证码
     * @return
     */
    @GetMapping("/code")
    public Result getCode(){
        String code= RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set("code",code);
        stringRedisTemplate.expire("code",60*60, TimeUnit.SECONDS);
        return Result.success(code);
    }

    /**.
     * 获取当前用户
     * @param servletRequest
     * @return
     */
    @GetMapping("/currentuser")
    public UserVo selectCurrentLoginUser(HttpServletRequest servletRequest){
        return userService.selectCurrentLoginUser(servletRequest);
    }

    /**
     * 退出登录
     * @param servletRequest
     * @return
     */
    @GetMapping("/logout")
    public Boolean outLogin(HttpServletRequest servletRequest){
        return userService.outLogin(servletRequest);
    }
    /**
     * 修改用户信息(后台)
     * @param user
     * @return
     */
    @PutMapping()
    public Result updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    /**
     * 修改用户信息(前台)
     * @param updateUserParam
     * @return
     */
    @PutMapping("/information")
    public Result updateUser(@RequestBody UpdateUserParam updateUserParam){
        return userService.updateUsers(updateUserParam);
    }

    /**
     * 根据TD删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") Integer id){
        return userService.deleteUser(id);
    }

    /**
     * 修改标签
     * @param tags
     * @param servletRequest
     * @return
     */
    @PostMapping("/update/tags")
    public Result updateTags(@RequestBody(required = false) List<String> tags,HttpServletRequest servletRequest){
        return userService.updateTags(tags,servletRequest);
    }

    /**
     * 根据标签搜索用户
     * @param tagNameList
     * @return
     */
    @PostMapping("/search/tags")
    public Result searchUsersByTags(@RequestBody(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new RuntimeException("标签搜索用户参数为空");
        }
        return userService.searchUsersByTags(tagNameList);
    }
    /**
     * 获取最匹配的用户
     * @param num
     * @param request
     * @return
     */
    @GetMapping("/match/{num}")
    public Result matchUsers(@PathVariable("num") long num, HttpServletRequest request) {
        if (num <= 0 || num > 20) {
            throw new RuntimeException("查询匹配用户参数错误");
        }
        UserVo loginUser = userService.selectCurrentLoginUser(request);
        return userService.matchUsers(num, loginUser);
    }

    /**
     *  关注用户
     * @param idParam
     * @param request
     * @return
     */
    @PostMapping("/concern")
    public Result AddConcernUser(@RequestBody IdParam idParam, HttpServletRequest request){
        UserVo userVo = userService.selectCurrentLoginUser(request);
        if(userVo==null){
            throw new RuntimeException("未登录/登陆失效");
        }
        Integer loginUserId = userVo.getId();
        Integer concernUserId = idParam.getId();
        if(concernUserId<=0 || loginUserId<=0){
            throw new RuntimeException("关注用户参数错误，id:"+concernUserId);
        }
        if(concernUserId.intValue()==loginUserId.intValue()){
            throw new RuntimeException("不能关注自己");
        }
        return userService.addConcernUser(concernUserId,loginUserId);
    }

    /**
     * 是否关注用户
     * @param idParam
     * @param request
     * @return
     */
    @PostMapping("/concern/isno")
    public Boolean isnoConcern(@RequestBody IdParam idParam, HttpServletRequest request){
        UserVo userVo = userService.selectCurrentLoginUser(request);
        if(userVo==null){
            throw new RuntimeException("未登录/登陆失效");
        }
        Integer loginUserId = userVo.getId();
        Integer concernUserId = idParam.getId();
        if(concernUserId<=0 || loginUserId<=0){
            throw new RuntimeException("关注用户参数错误，id:"+concernUserId);
        }
        return userService.isnoConcern(concernUserId,loginUserId);
    }

    /**
     * 获取粉丝列表
     * @param idParam
     * @param servletRequest
     * @return
     */
    @PostMapping("/fanslist")
    public Result getUserFansList(@RequestBody IdParam idParam,HttpServletRequest servletRequest){
        return userService.getUserFansList(idParam);
    }

    /**
     * 获取关注列表
     * @param idParam
     * @param servletRequest
     * @return
     */
    @PostMapping("/concernlist")
    public Result getUserConcernList(@RequestBody IdParam idParam,HttpServletRequest servletRequest){
        return userService.getUserConcernList(idParam);
    }

    @PostMapping("/common/follow")
    public Result getCommonConcern(@RequestBody IdParam idParam,HttpServletRequest servletRequest){
        UserVo userVo = userService.selectCurrentLoginUser(servletRequest);
        if(userVo==null){
            throw new RuntimeException("未登录/登陆失效");
        }
        Integer currentUserId = userVo.getId();
        return userService.getCommonConcern(idParam,currentUserId);
    }
}
