package im.zhaojun.system.mapper;

import im.zhaojun.system.model.Alert;
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
public interface AlertMapper {

    List<Map<String,Object>> getAlert(Map<String,Object> map);

    int saveAlert(Alert alert);
}
