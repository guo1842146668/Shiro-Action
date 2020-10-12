package im.zhaojun.system.service;

import im.zhaojun.system.mapper.StatMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class StatService {
    @Resource
    private StatMapper statMapper;

    public List<Map<String,Object>> getDate(String startDate, String endDate){
        return  statMapper.getDate(startDate,endDate);
    }

    public List<Map<String,Object>> getMonth(){
        return  statMapper.getMonth();
    }

    public List<Map<String,Object>> getOff_line(Map<String,Object> map){
        return  statMapper.getOff_line(map);
    }
}
