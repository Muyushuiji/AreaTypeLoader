package com.wxxx.gis.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: gis-web
 * @description: 区域网格
 * @author: hxl
 * @create: 2022-08-19 16:44
 **/
@Data
@TableName("area_beijing")
public class AreaBeijing {

    private Integer gid;
    private Integer objectid1;
    private Integer objectid;
    private String areaType;
    private BigDecimal shapeLeng;
    private String city;
    private String centerX;
    private String centerY;
    private String province;
    private BigDecimal shapeLe1;
    private BigDecimal shapeArea;
    private String geom;
}
