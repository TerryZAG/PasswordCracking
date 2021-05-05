package com.hrbustsecond.dao;





import com.hrbustsecond.entity.Passwordcracking_Node;
import java.util.List;

public interface Passwordcracking_NodeMapper {
    //查询节点
    List<Passwordcracking_Node> query(Passwordcracking_Node node);
    //增加节点
    int insert(Passwordcracking_Node node);
    //修改节点
    int update(Passwordcracking_Node node);
    //删除节点
    //int delete(Passwordcracking_Node node);
}
