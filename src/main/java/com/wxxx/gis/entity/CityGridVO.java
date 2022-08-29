package com.wxxx.gis.entity;

import lombok.Data;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-23 19:00
 **/
@Data
public class CityGridVO {
    private String province;
    private String city;
    private String gridId;
    private String geom;
    private double maxX;
    private double maxY;
    private double minX;
    private double minY;
}
