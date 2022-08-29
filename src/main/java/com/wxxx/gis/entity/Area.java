package com.wxxx.gis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-24 13:45
 **/
@Data
@TableName("area")
public class Area {
    private String areaType;
    private String city;
    private String centerX;
    private String centerY;
    private String province;
    private BigDecimal shapeArea;
    private String geom;
    @TableField(exist = false)
    private String subGeomNo;
    @TableField(exist = false)
    private double maxX;
    @TableField(exist = false)
    private double maxY;
    @TableField(exist = false)
    private double minX;
    @TableField(exist = false)
    private double minY;
    @TableField(exist = false)
    private List<String> rasterKeyList;

}
