package com.hrbustsecond.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passwordcracking_Task {
    private  int id;//主键
    private String PC_MD5;//MD5值
    private String PC_Password;//破译结果
    private  int PC_Userid;//创建任务的用户id
    private  int PC_State;//破解中0,成功1,失败2
    private  int PC_Partuserid;//参与任务的用户id
    private  int PC_Maintaskid;//主任务id
    private String PC_File;//对应字典文件位置
    private String PC_Filedir;//字典内容，传值用
    private int PC_Process;//任务进度，完成一本字典加33
}
