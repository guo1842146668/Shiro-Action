package im.zhaojun.system.mapper;

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
public interface DictMapper {

    List<Map<String,Object>> getGuide(String type);
}
