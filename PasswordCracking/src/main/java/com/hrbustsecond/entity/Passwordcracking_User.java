package com.hrbustsecond.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passwordcracking_User {
    private  int id;
    private String name;
    private int onlinestate;//在线状态
}
