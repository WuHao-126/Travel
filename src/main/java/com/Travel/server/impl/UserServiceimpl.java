package com.Travel.server.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.Travel.dao.mapper.UserMapper;
import com.Travel.dao.pojo.User;
import com.Travel.exception.BusinessException;
import com.Travel.server.UserService;
import com.Travel.util.AlgorithmUtils;
import com.Travel.vo.*;
import com.Travel.vo.param.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.apache.commons.math3.util.Pair;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceimpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String PUBLIC_KEY="wuhao";
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
        password= DigestUtils.md5DigestAsHex((PUBLIC_KEY + password).getBytes());
        User user = query()
                .eq("username", username)
                .eq("password", password)
                .one();
        if (user == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        servletRequest.getSession().setAttribute("userLogin",user);
        Integer userId = user.getId();
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
            password= DigestUtils.md5DigestAsHex((PUBLIC_KEY + password).getBytes());
            user.setPassword(password);
            user.setPower(0);
            String nickName = SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(10);
            user.setNickName(nickName);
            user.setUserPhoto(SystemConstants.IMG_DEFAULT_NAME);
            user.setGrade(1);
            user.setFans(0);
            user.setExperience(0);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
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
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
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

    @Override
    public Result updateTags(List<String> tags, HttpServletRequest servletRequest) {
        UserVo userVo = selectCurrentLoginUser(servletRequest);
        if(userVo==null){
            throw new RuntimeException("未登录/登录已过期");
        }
        Integer id = userVo.getId();
        String tagsJson = JSONUtil.parse(tags).toString();
        boolean flag = userMapper.updateTags(tagsJson, id);
        if(flag){
            return Result.success(tagsJson);
        }
        return Result.fail(ErrorCode.UPDATE_ERROR.getCode(),ErrorCode.UPDATE_ERROR.getMsg());
    }

    @Override
    public Result matchUsers(long num, UserVo loginUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tags");
        queryWrapper.isNotNull("tags");
        List<User> userList = this.list(queryWrapper);
        String tags = loginUser.getTags();
        Gson gson = new Gson();
        // 获取当前登录用户的标签
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
        // 用户列表的下标 => 相似度
        List<Pair<User, Long>> list = new ArrayList<>();
        // 依次计算所有用户和当前用户的相似度
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            String userTags = user.getTags();
            // 无标签或者为当前用户自己
            if (StringUtils.isBlank(userTags) || user.getId() == loginUser.getId()) {
                continue;
            }
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {
            }.getType());
            // 计算分数
            long distance = AlgorithmUtils.minDistance(tagList, userTagList);
            list.add(new Pair<>(user, distance));
        }
        // 按编辑距离由小到大排序
        List<Pair<User, Long>> topUserPairList = list.stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .limit(num)
                .collect(Collectors.toList());
        // 原本顺序的 userId 列表
        List<Integer> userIdList = topUserPairList.stream().map(pair -> pair.getKey().getId()).collect(Collectors.toList());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIdList);
        // 1, 3, 2
        // User1、User2、User3
        // 1 => User1, 2 => User2, 3 => User3
        Map<Integer, List<UserVo>> userIdUserListMap = this.list(userQueryWrapper)
                .stream()
                .map(user -> copy(user))
                .collect(Collectors.groupingBy(UserVo::getId));
        List<UserVo> finalUserList = new ArrayList<>();
        for (Integer userId : userIdList) {
            finalUserList.add(userIdUserListMap.get(userId).get(0));
        }
        return Result.success(finalUserList);
    }

    @Override
    public Result searchUsersByTags(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new RuntimeException("标签搜索用户参数为空");
        }
        // 1. 先查询所有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        List<UserVo> voList = copyList(userList);
        Gson gson = new Gson();
        // 2. 在内存中判断是否包含要求的标签
        List<UserVo> collect = voList.stream().filter(user -> {
            String tagsStr = user.getTags();
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, new com.google.gson.reflect.TypeToken<Set<String>>() {
            }.getType());
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
        return  Result.success(collect);
    }

    @Override
    @Transactional
    public Result addConcernUser(Integer concernUserId, Integer loginUserId) {
        if(concernUserId <= 0 || loginUserId <= 0){
            throw new RuntimeException("关注用户参数错误，id:"+concernUserId);
        }
        //判断关注用户是否存在
        User user = query().eq("id", concernUserId).one();
        if(user==null){
            throw new RuntimeException("该用户已不存在：id"+concernUserId);
        }
        //判断是否关注过
        Boolean aBoolean = isnoConcern(concernUserId, loginUserId);
        if(aBoolean){
            boolean flag=userMapper.cancelConcern(loginUserId, concernUserId);
            if (flag){
                user.setFans(user.getFans()-1);
                boolean b = updateById(user);
                if(b){
                    return Result.success("取消关注");
                }
            }
            return Result.success("取消关注");
        }
        boolean flag=userMapper.addConcernUser(concernUserId,loginUserId);
        if(flag){
            user.setFans(user.getFans()+1);
            boolean b = updateById(user);
            if(b){
                return Result.success("关注成功");
            }
        }
        return Result.fail(ErrorCode.INSERT_ERROR.getCode(),ErrorCode.INSERT_ERROR.getMsg());
    }

    @Override
    public Boolean isnoConcern(Integer concernUserId, Integer loginUserId) {
        if(concernUserId <= 0 || loginUserId <= 0){
            throw new RuntimeException("关注用户参数错误，id:"+concernUserId);
        }
        User user = query().eq("id", concernUserId).one();
        if(user==null){
            throw new RuntimeException("该用户已不存在：id"+concernUserId);
        }
        int num=userMapper.isNoConcern(loginUserId,concernUserId);
        if(num==0){
            return false;
        }
        return true;
    }

    @Override
    public Result getUserFansList(IdParam idParam) {
        Integer id = idParam.getId();
        Integer currentPage = idParam.getCurrentPage();
        Integer pageSize = idParam.getPageSize();
        if(id <= 0){
            throw new RuntimeException("粉丝列表查询参数异常 id:"+id);
        }
        List<UserVo> userList=userMapper.getUserFans(id,currentPage,pageSize);
        return Result.success(userList);
    }

    @Override
    public Result getUserConcernList(IdParam idParam) {
        Integer id = idParam.getId();
        Integer currentPage = idParam.getCurrentPage();
        Integer pageSize = idParam.getPageSize();
        if(id <= 0){
            throw new RuntimeException("关注列表查询参数异常 id:"+id);
        }
        List<UserVo> userList=userMapper.getUserConcern(id,currentPage,pageSize);
        return Result.success(userList);
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
