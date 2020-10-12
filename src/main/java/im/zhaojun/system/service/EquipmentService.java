package im.zhaojun.system.service;

import com.github.pagehelper.PageHelper;
import im.zhaojun.system.mapper.EquipmentMapper;
import im.zhaojun.system.model.Equipment;
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
 * @since 2020-09-01
 */
@Service
public class EquipmentService {
    @Resource
    private EquipmentMapper equipmentMapper;

    public List<Map<String, Object>> listAll(Equipment equipment, Integer page, Integer count) {
        PageHelper.startPage(page, count);
        /*PageHelper.startPage(page,count);
        List<Map<String, Object>> alert = equipmentMapper.listAll(equipment);
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(alert);*/
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
        return equipmentMapper.updateByEquipmentNo(equipment);
    }

    public List<Map<String, Object>> uploadExcel(Equipment equipment) {
        return equipmentMapper.listAll(equipment);
    }

    public List<Map<String,Object>> selectScheduled(String equipmentNO){
        return  equipmentMapper.selectScheduled(equipmentNO);
    }
}
