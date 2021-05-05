package com.hrbustsecond.service;



import com.hrbustsecond.entity.Passwordcracking_Node;
import java.util.List;

public interface Passwordcracking_NodeService {
    List<Passwordcracking_Node> query(Passwordcracking_Node node);
    int update(Passwordcracking_Node node);
    int insert(Passwordcracking_Node node);
}
