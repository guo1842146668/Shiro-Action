package im.zhaojun.system.controller;

import com.github.pagehelper.PageInfo;
import im.zhaojun.common.annotation.OperationLog;
import im.zhaojun.common.util.PageResultBean;
import im.zhaojun.common.util.ResultBean;
import im.zhaojun.common.validate.groups.Create;
import im.zhaojun.system.model.User;
import im.zhaojun.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @GetMapping("/index")
    public String index() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getType() == 1){
            return "user/admin-list";
        }else{
            if(user.getDeptId() == 0){
                return "user/one-list";
            }else{
                return "user/second-list";
            }
        }

    }

    @OperationLog("获取用户列表")
    @GetMapping("/list")
    @ResponseBody
    public PageResultBean<User> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "limit", defaultValue = "10") int limit,
                                        User userQuery) {
        List<User> users = userService.selectAllWithDept(page, limit, userQuery);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return new PageResultBean<>(userPageInfo.getTotal(), userPageInfo.getList());
    }

    @OperationLog("获取用户列表")
    @GetMapping("/oneList")
    @ResponseBody
    public PageResultBean<Map<String,Object>> getOneList(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "limit", defaultValue = "10") int limit,
                                        User userQuery) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        userQuery.setUserId(user.getUserId());
        List<Map<String,Object>> users = userService.selectOneWithDept(page, limit, userQuery);
        PageInfo<Map<String,Object>> userPageInfo = new PageInfo<>(users);
        return new PageResultBean<>(userPageInfo.getTotal(), userPageInfo.getList());
    }

    @OperationLog("获取用户列表")
    @GetMapping("/secondList")
    @ResponseBody
    public PageResultBean<User> getSecondList(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "limit", defaultValue = "10") int limit,
                                           User userQuery) {
        List<User> users = userService.selectAllWithDept(page, limit, userQuery);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return new PageResultBean<>(userPageInfo.getTotal(), userPageInfo.getList());
    }

    @GetMapping("/getOneUser")
    @ResponseBody
    public ResultBean getOneUser(){
        return  ResultBean.success(userService.getOneUser());
    }

    @GetMapping("/add")
    public String add() {
        return "user/user-add";
    }

    @GetMapping("/select")
    public String selectOne(Integer userId, Model model) {
        model.addAttribute("user", userService.selectOne(userId));
        return "user/user-view";
    }

    @OperationLog("编辑角色")
    @PutMapping
    @ResponseBody
    public ResultBean update(@Valid User user, @RequestParam(value = "role[]", required = false) Integer[] roleIds) {
        userService.update(user, roleIds);
        return ResultBean.success();
    }

    @OperationLog("新增用户")
    @PostMapping
    @ResponseBody
    public ResultBean add(@Validated(Create.class) User user, @RequestParam(value = "role[]", required = false) Integer[] roleIds) {
        return ResultBean.success(userService.add(user, roleIds));
    }

    @OperationLog("禁用账号")
    @PostMapping("/{userId:\\d+}/disable")
    @ResponseBody
    public ResultBean disable(@PathVariable("userId") Integer userId) {
        return ResultBean.success(userService.disableUserByID(userId));
    }

    @OperationLog("激活账号")
    @PostMapping("/{userId}/enable")
    @ResponseBody
    public ResultBean enable(@PathVariable("userId") Integer userId) {
        return ResultBean.success(userService.enableUserByID(userId));
    }

    @OperationLog("删除账号")
    @DeleteMapping("/{userId}")
    @ResponseBody
    public ResultBean delete(@PathVariable("userId") Integer userId) {
        userService.delete(userId);
        return ResultBean.success();
    }

    @GetMapping("/{userId}/reset")
    public String resetPassword(@PathVariable("userId") Integer userId, Model model) {
        model.addAttribute("userId", userId);
        return "user/user-reset-pwd";
    }

    @OperationLog("重置密码")
    @PostMapping("/{userId}/reset")
    @ResponseBody
    public ResultBean resetPassword(@PathVariable("userId") Integer userId, String password) {
        userService.updatePasswordByUserId(userId, password);
        return ResultBean.success();
    }


}