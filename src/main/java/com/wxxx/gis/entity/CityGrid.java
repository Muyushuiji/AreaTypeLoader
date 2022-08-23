package com.wxxx.gis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: gis-web
 * @description: 城区网格
 * @author: hxl
 * @create: 2022-08-19 16:44
 **/
@Data
@TableName("citygrid")
public class CityGrid {
    private Integer gid;
    private Integer objectid1;
    private Integer objectid;
    private Integer userid;
    private Integer gridid;
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
