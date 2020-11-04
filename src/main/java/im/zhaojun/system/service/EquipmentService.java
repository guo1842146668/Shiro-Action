package im.zhaojun.system.service;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import im.zhaojun.system.controller.WebSocketServer;
import im.zhaojun.system.mapper.EquipmentMapper;
import im.zhaojun.system.model.Equipment;
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
public class EquipmentService {
    @Resource
    private EquipmentMapper equipmentMapper;

    public List<Map<String, Object>> listAll(Equipment equipment, Integer page, Integer count) {
        PageHelper.startPage(page, count);
        return equipmentMapper.listAll(equipment);
    }

    public Map<String, Object> selectOne(Integer equipmentID){
        return equipmentMapper.selectOne(equipmentID);
    }

    public int saveEquipment(Equipment equipment){
        return  equipmentMapper.saveEquipment(equipment);
    }

    public int updateEquipment(Equipment equipment){
        return equipmentMapper.updateEquipment(equipment);
    }

    public int deleteEquipment(Integer equipmentID){
        return equipmentMapper.deleteEquipment(equipmentID);
    }

    public Map<String, Object> selectByEquipmentNO(String equipmentNO){
        return equipmentMapper.selectByEquipmentNO(equipmentNO);
    }

    public int updateByEquipmentNo(Equipment equipment){

        try {
            Map<String, Object> map = equipmentMapper.selectByEquipmentNO(equipment.getEquipmentNO());
            if(!map.get("electricStatus").equals(equipment.getElectricStatus()) || !map.get("runningState").equals(equipment.getRunningState()) || !map.get("OperationMode").equals(equipment.getOperationMode()) || !map.get("FaultStatus").equals(equipment.getFaultStatus())){
                WebSocketServer.sendInfo("true","20");
                WebSocketServer.sendInfo("true","100");
            }
            if(!map.get("electricCurrentIA").equals(equipment.getElectricCurrentIA()) || !map.get("electricCurrentIB").equals(equipment.getElectricCurrentIB()) || !map.get("electricCurrentIC").equals(equipment.getElectricCurrentIC())){
                WebSocketServer.sendInfo(JSONUtil.toJsonStr(JSONUtil.parseObj(equipment)),"30");
            }
        }catch (Exception e){
            return  0;
        }
        return equipmentMapper.updateByEquipmentNo(equipment);
    }

    public List<Map<String, Object>> uploadExcel(Equipment equipment) {
        return equipmentMapper.listAll(equipment);
    }

    public List<Map<String,Object>> selectScheduled(String equipmentNO){
        return  equipmentMapper.selectScheduled(equipmentNO);
    }
}
