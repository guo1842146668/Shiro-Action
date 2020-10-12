package im.zhaojun.system.service;

import com.github.pagehelper.PageHelper;
import im.zhaojun.system.mapper.VersionMapper;
import im.zhaojun.system.model.Version;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VersionService {
    @Resource
    private VersionMapper versionMapper;

    public List<Version> getAll(Integer page,Integer count){
        PageHelper.startPage(page, count);
        return  versionMapper.getAll();
    }

    public Version getOne(Integer version){
        return  versionMapper.getOne(version);
    }
}
