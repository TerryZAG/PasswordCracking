package com.hrbustsecond.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passwordcracking_Node {
    private  int id;//主键
    private String PC_Host;//host
    private String PC_Port;//端口
    private int PC_State;//状态：1可用，2占用
}
