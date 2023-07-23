package com.Travel.server.impl;

import cn.hutool.core.util.RandomUtil;
import com.Travel.dao.mapper.UserMapper;
import com.Travel.dao.pojo.User;
import com.Travel.server.UserService;
import com.Travel.util.JWTUtils;
import com.Travel.util.UserThreadLocal;
import com.Travel.vo.*;
import com.Travel.vo.param.PageParam;
import com.Travel.vo.param.UpdateUserParam;
import com.Travel.vo.param.UserLoginRequest;
import com.Travel.vo.param.UserParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceimpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Result selectUser(UserLoginRequest userLoginRequest, HttpServletRequest servletRequest) {
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        String code=userLoginRequest.getCode();
        if (StringUtils.isAllEmpty(username,password,code)) {
            return Result.fail(ErrorCode.PARAMS_EMPTY.getCode(), ErrorCode.PARAMS_EMPTY.getMsg());
        }
        String code1 = stringRedisTemplate.opsForValue().get("code");
        if(!code.equals(code1)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),"验证码错误");
        }
        // 账户不能包含特殊字符 正则表达式
        if(username.length()<4 || username.length()>11){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),"账户长度小于4或者大于11");
        }
        if(password.length()<4 || password.length()>11){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),"密码长度小于8或者大于11");
        }
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),"含有特殊字符");
        }
        User user = query()
                .eq("username", userLoginRequest.getUsername())
                .eq("password", userLoginRequest.getPassword())
                .one();
        if (user == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        servletRequest.getSession().setAttribute("userLogin",user);
        Integer userId = user.getId();
//        String token = JWTUtils.createToken(userId);
//        stringRedisTemplate.opsForValue().set("token", token, RediConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        //用户信息脱敏
        UserVo userVo=new UserVo();
        BeanUtils.copyProperties(user,userVo);
        servletRequest.getSession().setAttribute(SystemConstants.CURRENT_LOGIN_USER,userVo);
        if (user != null) {
            userMapper.addExperience(userId);
            if(user.getExperience()%1000==0){
                userMapper.addGrade(userId);
            }
            return Result.success(null);
        } else {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
    }
    @Override
    public Result selectAllSimpleUser(PageParam pageParam) {
        Page<User> page = query().eq("power", 0).page(new Page<User>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<User> list = page.getRecords();
        if (!list.isEmpty()) {
            return Result.success(list);
        } else {
            return Result.fail(ErrorCode.NO_DATE.getCode(), ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public Result selectAllAdministrators(PageParam pageParam) {
        Page<User> page = query().eq("power", 1).page(new Page<User>(pageParam.getCurrentPage(), pageParam.getPageSize()));
        List<User> list = page.getRecords();
        if (!list.isEmpty()) {
            return Result.success(list);
        } else {
            return Result.fail(ErrorCode.NO_DATE.getCode(), ErrorCode.NO_DATE.getMsg());
        }
    }

    @Override
    public User selectByIdUser(Integer id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public Result updateUser(User user) {
        int id = user.getId();
        if (id == 0 || user == null) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        boolean flag = updateById(user);
        if (flag) {
            return Result.success(null);
        } else {
            return Result.fail(ErrorCode.UPDATE_ERROR.getCode(), ErrorCode.UPDATE_ERROR.getMsg());
        }
    }

    @Override
    public Result deleteUser(Integer id) {
        if (id <= 0) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        boolean flag = removeById(id);
        if (flag) {
            return Result.success(null);
        } else {
            return Result.fail(ErrorCode.DELETE_ERROR.getCode(), ErrorCode.DELETE_ERROR.getMsg());
        }
    }


    @Override
    public Result selectByUserName(Integer username) {
        User user = query().eq("username", username).one();
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.fail(ErrorCode.NO_DATE.getCode(), ErrorCode.NO_DATE.getMsg());
        }
    }


    @Override
    public Result registerUser(UserParam userParam) {
        String username = userParam.getUsername();
        String password = userParam.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_EMPTY.getCode(), ErrorCode.PARAMS_EMPTY.getMsg());
        }
        // 账户不能包含特殊字符 正则表达式
        if(username.length()<4 || username.length()>11){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),"账户长度小于4或者大于11");
        }
        if(password.length()<8 || password.length()>11){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),"密码长度小于8或者大于11");
        }
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),"含有特殊字符");
        }
        User userFlag = query().eq("username", username).one();
        if (userFlag == null) {
            //注册初始化
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setPower(0);
            String nickName = SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(10);
            user.setNickName(nickName);
            user.setUserPhoto(SystemConstants.IMG_DEFAULT_NAME);
            user.setGrade(1);
            user.setFans(0);
            user.setExperience(0);
            //添加
            boolean save = save(user);
            if (save) {
                return Result.success(null);
            } else {
                return Result.fail(ErrorCode.INSERT_ERROR.getCode(), ErrorCode.INSERT_ERROR.getMsg());
            }
        } else {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
    }

    @Override
    public Result registerUser(User user) {
        if (user == null) {
            return Result.fail(ErrorCode.PARAMS_EMPTY.getCode(), ErrorCode.PARAMS_EMPTY.getMsg());
        }
        User username = query().eq("username", user.getUsername()).one();
        if (username != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        user.setGrade(1);
        user.setFans(0);
        user.setPower(0);
        user.setUserPhoto("default.jpg");
        user.setExperience(10);
        user.setNickName(RandomUtil.randomString(6) + "_user");
        boolean flag = save(user);
        if (flag) {
            return Result.success(null);
        } else {
            return Result.fail(ErrorCode.INSERT_ERROR.getCode(), ErrorCode.INSERT_ERROR.getMsg());
        }
    }

    @Override
    public UserVo selectByIdUserVo(Integer id) {
        User user = userMapper.selectById(id);
        UserVo userVo = new UserVo();
        if (user != null) {
            BeanUtils.copyProperties(user, userVo);
            return userVo;
        }
        return null;
    }

    //TODO
    @Override
    public UserVo selectCurrentLoginUser(HttpServletRequest servletRequest) {
        UserVo userVo = (UserVo)servletRequest.getSession().getAttribute(SystemConstants.CURRENT_LOGIN_USER);
        if(userVo==null){
            return null;
        }
        return userVo;
    }
    //TODO
    @Override
    public Boolean outLogin(HttpServletRequest servletRequest) {
       servletRequest.getSession().removeAttribute(SystemConstants.CURRENT_LOGIN_USER);
        return true;
    }

    @Override
    public List<UserVo> userGardeArrange(){
        Page<User> page = query().orderByDesc("grade").page(new Page<>(1, 10));
        List<User> records = page.getRecords();
        return copyList(records);
    }

    @Override
    public Result updateUsers(UpdateUserParam updateUserParam) {
        userMapper.updateUserInformation(updateUserParam);
        return Result.success(null);
    }

    public List<UserVo> copyList(List<User> list){
        List<UserVo> voList=new ArrayList<>();
        for (User user : list) {
            voList.add(copy(user));
        }
        return voList;
    }
    public UserVo copy(User user){
        UserVo userVo=new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }
}
