package im.zhaojun.system.controller;

import com.github.pagehelper.PageInfo;
import im.zhaojun.common.annotation.OperationLog;
import im.zhaojun.common.util.PageResultBean;
import im.zhaojun.common.util.ResultBean;
import im.zhaojun.socket.ServerThread9000;
import im.zhaojun.system.model.Scheduled;
import im.zhaojun.system.model.User;
import im.zhaojun.system.service.ScheduledService;
import im.zhaojun.tool.ReadFile;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-09-02
 */
@CrossOrigin
@Controller
@RequestMapping("/scheduled")
public class ScheduledController {
    @Resource
    private ScheduledService scheduledService;

    @GetMapping("/index")
    public String index(){
        return  "scheduled/scheduled-list";
    }

    @GetMapping("list")
    @ResponseBody
    public PageResultBean<Map<String,Object>> listAll(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                      @RequestParam(value = "limit",defaultValue = "10") Integer limit){
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        Integer userID=null;
        if(user.getType() == null || user.getType() != 1){
            userID=user.getUserId();
        }

        List<Map<String, Object>> maps = scheduledService.listAll(userID, page, limit);
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(maps);
        return  new PageResultBean<>(pageInfo.getTotal(),pageInfo.getList());
    }


    @OperationLog("修改定时跳转")
    @GetMapping("/{cron_id}")
    public String update(@PathVariable("cron_id") Integer cron_id, Model model) {
        Map<String, Object> map = scheduledService.selectOne(cron_id);
        model.addAttribute("Scheduled", map);
        return "scheduled/scheduled-add";
    }

    @OperationLog("修改定时")
    @PutMapping("/update")
    @ResponseBody
    public ResultBean update(Scheduled scheduled){
        scheduledService.updateScheduled(scheduled);
        return  ResultBean.success();
    }

    @GetMapping("/openingTiming")
    @ResponseBody
    public String openingTiming(String iccid,String hour1,String minute1,String hour2,String minute2,Integer ID) {
            List<String> list = new ArrayList<>();
            List<Map<String, Object>> maps = scheduledService.listByEquipmentNO(iccid);
            for (int i = 0; i < maps.size(); i++) {
                if (maps.get(i).get("cronStartTime").equals("00:00") && maps.get(i).get("cronEndTime").equals("00:00")) {

                } else {
                    list.add(maps.get(i).get("cronStartTime") + "-" + maps.get(i).get("cronEndTime"));
                }
            }
            if (ReadFile.checkOverlap(list)) {
                return "存在重复时间";
            }
            String num = null;
            if(ID%5==0){
                num = "5";
            }else{
                num = ""+ID%5+"";
            }
            ServerThread9000.setTheStartAndStopTime(ServerThread9000.Division(iccid), hour1, minute1, hour2, minute2,num);
            return "success";
    }


    @GetMapping("/opening")
    @ResponseBody
    public String opening(Integer cronId,String startTime,String endTime){
        Scheduled scheduledByID = scheduledService.getScheduledByID(cronId);
        List<Scheduled> byUserID = scheduledService.getByCronId(scheduledByID.getEquipmentNO());
        List<String> list = new ArrayList<>();
        for (int i = 0; i < byUserID.size(); i++) {
            if(byUserID.get(i).getCronStartTime().equals("00:00") && byUserID.get(i).getCronEndTime().equals("00:00")){

            }else{
                list.add(byUserID.get(i).getCronStartTime()+"-"+byUserID.get(i).getCronEndTime());
            }
        }
        list.add(startTime+"-"+endTime);
        String[] split = startTime.split(":");
        String[] split1 = endTime.split(":");
        if(ReadFile.checkOverlap(list)){
            return "时间设置存在交叉";
        }
        String s = openingTiming(scheduledByID.getEquipmentNO(), split[0], split[1], split1[0], split1[1], cronId);
        if(!s.equals("success")){
            return "设置定时失败";
        }
        scheduledByID.setCronStartTime(startTime);
        scheduledByID.setCronEndTime(endTime);
        if(startTime.equals("00:00")&&endTime.equals("00:00")){
            scheduledByID.setCronStatus(-1);
        }else{
            scheduledByID.setCronStatus(1);
        }
        scheduledService.updateScheduled(scheduledByID);
        return "success";
    }
}
