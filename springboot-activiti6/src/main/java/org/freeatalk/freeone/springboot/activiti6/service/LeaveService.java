package org.freeatalk.freeone.springboot.activiti6.service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.freeatalk.freeone.springboot.activiti6.entity.TLeave;

public interface LeaveService {

	boolean addLeave(String id, String type, Date startTime, Date endTime, String remark, String userid);

	List<TLeave> listMyLeaves(String userid, Map<String, Object> variables);

	InputStream getDiagram(String processInstanceId);

    /**  <p></p>   
     * @param userid
     * @param firstrow
     * @param rowcount
     * @return
     * @version: v1.0.0
     * @author: lqq
     * @date: 2020年3月18日 下午1:30:44 
     */
    List<TLeave> listLeavesWithCourseTeacher();

    /**  <p></p>   
     * @return
     * @version: v1.0.0
     * @author: lqq
     * @date: 2020年3月18日 下午5:05:30 
     */
    List<TLeave> pageListLeavesWithMainTeacher();


}
