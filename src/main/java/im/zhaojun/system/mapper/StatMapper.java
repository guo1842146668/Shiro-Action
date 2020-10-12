package im.zhaojun.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatMapper {
    /**
     *  根据设备类型统计
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<Map<String,Object>> getDate(String startDate, String endDate);

    /**
     *  统计设备离线率
     * @return 查询结果
     */
    List<Map<String,Object>> getOff_line(Map<String,Object> map);

    /**
     * 根据月份统计
     * @return 查询结果
     */
    List<Map<String,Object>> getMonth();
}
