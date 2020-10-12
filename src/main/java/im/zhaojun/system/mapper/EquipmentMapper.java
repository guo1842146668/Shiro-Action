package im.zhaojun.system.mapper;

import im.zhaojun.system.model.Equipment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-09-01
 */
@Mapper
public interface EquipmentMapper {
    /**
     *  查询所有设备信息
     * @param equipment 设备实体类
     * @return admin返回所有设备，其他返回自己名下设备
     */
    List<Map<String, Object>> listAll(Equipment equipment);

    /**
     * 根据设备ID查询设备信息
     * @param equipmentID 设备ID
     * @return 返回设备信息
     */
    Map<String, Object> selectOne(Integer equipmentID);

    /**
     * 添加设备
     * @param equipment 设备实体
     * @return 返回添加结果
     */
    int saveEquipment(Equipment equipment);

    /**
     * 修改设备
     * @param equipment 设备实体
     * @return 返回修改结果
     */
    int updateEquipment(Equipment equipment);

    /**
     * 删除设备
     * @param equipmentID 设备ID
     * @return 返回删除结果
     */
    int deleteEquipment(Integer equipmentID);

    /**
     * 根据设备唯一NO查询
     * @param equipmentNO 设备NO
     * @return 返回设备信息
     */
    Map<String, Object> selectByEquipmentNO(String equipmentNO);

    /**
     * 修改设备
     * @param equipment 设备实体
     * @return 返回修改结果
     */
    int updateByEquipmentNo(Equipment equipment);

    /**
     * 修改设备
     * @param equipmentNO 设备ID
     * @return 返回修改结果
     */
    List<Map<String,Object>> selectScheduled(String equipmentNO);
}
