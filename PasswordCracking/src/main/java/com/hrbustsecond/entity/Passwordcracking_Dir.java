package com.hrbustsecond.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passwordcracking_Dir {
    private List<Object> dirspace;
    private String PC_MD5;
    private String PC_Password;
    private int PC_Nodenum;//节点数
}
