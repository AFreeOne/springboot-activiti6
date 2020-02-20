package org.freeatalk.freeone.springboot.activiti6.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.freeatalk.freeone.springboot.activiti6.entity.TUser;

public interface TUserMapper {
    boolean deleteByPrimaryKey(String id);

    boolean insert(TUser user);

    boolean insertSelective(TUser user);

    TUser selectByPrimaryKey(String id);

    boolean updateByPrimaryKeySelective(TUser user);

    boolean updateByPrimaryKey(TUser record);
    
    TUser getUserByUsernamePassword(@Param("username") String username,@Param("password") String password);
}