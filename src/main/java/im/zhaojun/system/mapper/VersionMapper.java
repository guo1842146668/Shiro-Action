package im.zhaojun.system.mapper;

import im.zhaojun.system.model.Version;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface VersionMapper {

    List<Version> getAll();

    Version getOne(Integer version);
}
