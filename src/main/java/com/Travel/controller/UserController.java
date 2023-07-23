package com.Travel.controller;

import cn.hutool.core.util.RandomUtil;
import com.Travel.dao.pojo.User;
import com.Travel.server.UserService;
import com.Travel.vo.Result;
import com.Travel.vo.UserVo;
import com.Travel.vo.param.PageParam;
import com.Travel.vo.param.UpdateUserParam;
import com.Travel.vo.param.UserLoginRequest;
import com.Travel.vo.param.UserParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
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
     * TODO 查询所有普通用户
     * @param pageParam
     * @return
     */
    @ApiOperation("查询所有普通用户接口")
    @PostMapping("/simple")
    public Result selectAllSimpleUser(@RequestBody PageParam pageParam){
        return userService.selectAllSimpleUser(pageParam);
    }
    /**
     * TODO 查询所有管理员用户
     * @param pageParam
     * @return
     */
    @ApiOperation("查询所有管理员用户")
    @PostMapping("/administrators")
    public Result selectAllAdministrators(@RequestBody PageParam pageParam){
        return userService.selectAllAdministrators(pageParam);
    }

    /**
     *  TODO 注册用户
     * @param userParam
     * @return
     */
    @ApiOperation("注册用户接口")
    @PostMapping("/register")
    public Result registerUser(@RequestBody UserParam userParam){
        return userService.registerUser(userParam);
    }

    /**
     * TODO 登录检查账号密码
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/select")
    public Result selectUser(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest servletRequest){
        return userService.selectUser(userLoginRequest,servletRequest);
    }


    @PostMapping("/full")
    public Result registerUser(@RequestBody User user){
        return userService.registerUser(user);
    }
    /**
     * TODO 根据ID查询用户
     * @param id
     * @return
     */
    @GetMapping("/detailed/{id}")
    public User selectByIdDetailedUser(@PathVariable("id") Integer id){
        return userService.selectByIdUser(id);
    }


    @GetMapping("/id/{id}")
    public UserVo selectByIdUserVo(@PathVariable("id") Integer id){
        return userService.selectByIdUserVo(id);
    }
    /**
     * TODO 根据账号查询用户
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public Result selectByUserName(@PathVariable("username") Integer username){
        return userService.selectByUserName(username);
    }
    @GetMapping("/code")
    public Result getCode(){
        String code= RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set("code",code);
        stringRedisTemplate.expire("code",60*60, TimeUnit.SECONDS);
        return Result.success(code);
    }
    @GetMapping("/currentuser")
    public UserVo selectCurrentLoginUser(HttpServletRequest servletRequest){
        return userService.selectCurrentLoginUser(servletRequest);
    }
    @GetMapping("/logout")
    public Boolean outLogin(HttpServletRequest servletRequest){
        return userService.outLogin(servletRequest);
    }
    /**
     * TODO 修改用户信息(后台)
     * @param user
     * @return
     */
    @PutMapping()
    public Result updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    /**
     * TODO 修改用户信息(前台)
     * @param updateUserParam
     * @return
     */
    @PutMapping("/information")
    public Result updateUser(@RequestBody UpdateUserParam updateUserParam){
        return userService.updateUsers(updateUserParam);
    }

    /**
     * TODO 根据TD删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") Integer id){
        return userService.deleteUser(id);
    }
}
