package im.zhaojun.system.service;

import im.zhaojun.system.mapper.DictMapper;
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
@Service("dicts")
public class DictService {
    @Resource
    private DictMapper dictMapper;

    public List<Map<String,Object>> getGuide(String type) {
        return dictMapper.getGuide(type);
    }
}
