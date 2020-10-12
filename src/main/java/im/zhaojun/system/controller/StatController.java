package im.zhaojun.system.controller;

import im.zhaojun.common.util.ResultBean;
import im.zhaojun.system.model.User;
import im.zhaojun.system.service.StatService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/stat")
public class StatController {
    @Resource
    private StatService statService;

    @GetMapping("index")
    public String index(){
        return  "stat/stat-list";
    }

    @GetMapping("/getDate")
    @ResponseBody
    public ResultBean getDate(String startDate, String endDate){
        return  ResultBean.success(statService.getDate(startDate, endDate));
    }

    @GetMapping("/getOff_line")
    @ResponseBody
    public ResultBean getOff_line(){
        Map<String,Object> map=new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getType() == null || user.getType() != 1){
            if(user.getDeptId() != 0){
                map.put("userID",user.getUserId());
            }else{
                map.put("dept_id",user.getUserId());
            }
        }
        return  ResultBean.success(statService.getOff_line(map));
    }

    @GetMapping("/getMonth")
    @ResponseBody
    public ResultBean getMonth(){
        return  ResultBean.success(statService.getMonth());
    }
}
