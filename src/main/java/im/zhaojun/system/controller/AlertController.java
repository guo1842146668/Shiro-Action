package im.zhaojun.system.controller;

import com.github.pagehelper.PageInfo;
import im.zhaojun.common.annotation.OperationLog;
import im.zhaojun.common.util.PageResultBean;
import im.zhaojun.common.util.ResultBean;
import im.zhaojun.system.model.User;
import im.zhaojun.system.service.AlertService;
import im.zhaojun.tool.ReadFile;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-09-01
 */
@CrossOrigin
@Controller
@RequestMapping("/alert")
public class AlertController {
    @Resource
    private AlertService alertService;

    @OperationLog("警报信息页面挑转")
    @GetMapping("/index")
    public String index(){
        return  "alert/alert-list";
    }

    @OperationLog("查询设备信息列表")
    @GetMapping("/list")
    @ResponseBody
    public PageResultBean<Map<String, Object>> listAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                       String equipmentNO,  String equipmentAddress, String equipmentType, String userID, String startDate,
                                                       String endDate)
    {
         Map<String,Object> map=new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getType() == null || user.getType() != 1){
            if(user.getDeptId() != 0){
                userID = user.getUserId().toString();
            }else{
                map.put("dept_id",user.getUserId());
            }
        }
        if(equipmentNO != null && !equipmentNO.equals("")){
            equipmentNO = ReadFile.specialStr(equipmentNO.trim());// 排除%等通配符
            equipmentNO = ReadFile.specialStrKeyword(equipmentNO.trim());
            map.put("equipmentNO",equipmentNO.trim());
        }

        if(equipmentAddress != null && !equipmentAddress.equals("")){
            equipmentAddress = ReadFile.specialStr(equipmentAddress.trim());// 排除%等通配符
            equipmentAddress = ReadFile.specialStrKeyword(equipmentAddress.trim());
            map.put("equipmentAddress",equipmentAddress.trim());
        }
        map.put("equipmentType",equipmentType);
        map.put("userID",userID);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        List<Map<String, Object>> maps = alertService.getAlert(map, page, limit);
        PageInfo<Map<String, Object>> userPageInfo = new PageInfo<>(maps);
        return new PageResultBean<>(userPageInfo.getTotal(), userPageInfo.getList());
    }

  /*  @GetMapping("/test")
    @ResponseBody
    public ResultBean test() throws IOException {
        WebSocketServer.sendInfo("true","10");
        return ResultBean.success();
    }*/
}
