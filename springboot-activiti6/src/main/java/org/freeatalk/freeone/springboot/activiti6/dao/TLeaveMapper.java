package org.freeatalk.freeone.springboot.activiti6.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.freeatalk.freeone.springboot.activiti6.entity.TLeave;

public interface TLeaveMapper {
    boolean deleteByPrimaryKey(String id);

    boolean insert(TLeave record);

    boolean insertSelective(TLeave record);

    TLeave selectByPrimaryKey(String id);

    boolean updateByPrimaryKeySelective(TLeave record);

    boolean updateByPrimaryKey(TLeave record);
    
    List<TLeave> listUserLeaves(@Param("userid") String userid,@Param("variables") Map<String, Object> variables);
}