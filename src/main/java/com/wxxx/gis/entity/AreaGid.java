package com.wxxx.gis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * ClassName:AreaGid
 * Package:com.wxxx.gis.entity
 * Description:
 *
 * @Date:2022/8/2821:37
 * @Author:yangyang
 */
@Data
@TableName("area_gid")
public class AreaGid {
    private String areaType;
    private String city;
    private String centerX;
    private String centerY;
    private String province;
    private BigDecimal shapeArea;
    private String geom;
    private String gid;
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
    private LinkedHashSet<String> rasterKeySet;
}
