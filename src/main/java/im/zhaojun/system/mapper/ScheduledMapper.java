package im.zhaojun.system.mapper;

import im.zhaojun.system.model.Scheduled;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-09-02
 */
@Mapper
public interface ScheduledMapper {

    List<Map<String,Object>> listAll(Integer userID);

    Map<String,Object> selectOne(Integer cron_id);

    int updateScheduled(Scheduled scheduled);

    List<Map<String,Object>> listByEquipmentNO(String equipmentNO);

    Scheduled getScheduledByID(Integer userID);

    List<Scheduled> getByCronId(String equipmentNO);

    int saveScheduled(Scheduled scheduled);

}
