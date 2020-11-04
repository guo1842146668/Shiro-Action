package im.zhaojun.system.mapper;

import im.zhaojun.system.model.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(Role role);

    Role selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKey(Role role);

    List<Role> selectAll();

    List<Role> selectAllByQuery(Role roleQuery);

    int count();

    Map<String,Object> selectByUserID(Integer userID);
}