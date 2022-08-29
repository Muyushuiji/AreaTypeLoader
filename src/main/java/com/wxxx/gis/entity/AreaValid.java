package com.wxxx.gis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: gis-web
 * @description: 区域网格合法性
 * @author: hxl
 * @create: 2022-08-29 11:49
 **/
@Data
@TableName("area_valid")
public class AreaValid {
    @TableId(type =  IdType.AUTO)
    private String id;
    private String province;
    private String city;
    private String gid;
    private String subGid;
    private boolean validate;
}
