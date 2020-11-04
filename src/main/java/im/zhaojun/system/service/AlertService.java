package im.zhaojun.system.service;


import com.github.pagehelper.PageHelper;
import im.zhaojun.system.controller.WebSocketServer;
import im.zhaojun.system.mapper.AlertMapper;
import im.zhaojun.system.model.Alert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-09-01
 */
@Service
public class AlertService{
    @Resource
    private AlertMapper alertMapper;

    public List<Map<String,Object>> getAlert(Map<String,Object> map,Integer page,Integer count){
        PageHelper.startPage(page,count);
        return  alertMapper.getAlert(map);
    }

    public int saveAlert(Alert alert){
        try {
            WebSocketServer.sendInfo("true","10");
            System.out.println("时间：" + new Date() + " 添加发送socket");
        }catch (Exception e){
            return  0;
        }
        return alertMapper.saveAlert(alert);
    }
}
