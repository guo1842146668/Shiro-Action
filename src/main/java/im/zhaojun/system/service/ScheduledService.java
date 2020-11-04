package im.zhaojun.system.service;


import com.github.pagehelper.PageHelper;
import im.zhaojun.system.mapper.ScheduledMapper;
import im.zhaojun.system.model.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-09-02
 */
@Service
public class ScheduledService {
    @Resource
    private ScheduledMapper scheduledMapper;

    public List<Map<String,Object>> listAll(Integer userID,Integer page,Integer count){
        PageHelper.startPage(page,count);
        return  scheduledMapper.listAll(userID);
    }

    public Map<String,Object> selectOne(Integer cron_id){
        return  scheduledMapper.selectOne(cron_id);
    }

    public int updateScheduled(Scheduled scheduled){
        return  scheduledMapper.updateScheduled(scheduled);
    }

    public List<Map<String,Object>> listAllS(Integer userID){
        return  scheduledMapper.listAll(userID);
    }
    public List<Map<String,Object>> listByEquipmentNO(String equipmentNO){
        return scheduledMapper.listByEquipmentNO(equipmentNO);
    }

    public Scheduled getScheduledByID(Integer userID){
        return scheduledMapper.getScheduledByID(userID);
    }


    public List<Scheduled> getByCronId(String equipmentNO){
        return scheduledMapper.getByCronId(equipmentNO);
    }

    public int saveScheduled(Scheduled scheduled){
        return scheduledMapper.saveScheduled(scheduled);
    }
}
