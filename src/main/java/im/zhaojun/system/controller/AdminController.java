package im.zhaojun.system.controller;

import im.zhaojun.common.annotation.OperationLog;
import im.zhaojun.common.util.ResultBean;
import im.zhaojun.system.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private UserService userService;
    @GetMapping("/index")
    public String index(){
        return  "administrators/administrators-list";
    }

    @GetMapping("/select")
    public String selectOne(Integer userId, Model model) {
        model.addAttribute("user", userService.selectOne(userId));
        return "administrators/administrators-update";
    }

    @OperationLog("重置密码")
    @PostMapping("/reset")
    @ResponseBody
    public ResultBean resetPassword(Integer userId, String password) {
        userService.updatePasswordByUserId(userId, password);
        return ResultBean.success();
    }
}
