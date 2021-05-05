package com.hrbustsecond.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passwordcracking_Maintask {
    private  int id;//主键
    private String PC_MD5;//MD5值
    private String PC_Password;//破译结果
    private Date PC_CreateTime;//
    private Date PC_EndTime;//
}
