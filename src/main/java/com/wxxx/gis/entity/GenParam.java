package com.wxxx.gis.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * @program: gis-web
 * @description: 生成导出工参
 * @author: hxl
 * @create: 2022-08-23 14:09
 **/
@Data
public class GenParam {
    @CsvBindByName(column = "CGI")
    private String CGI;
    @CsvBindByName(column = "province")
    private String province;
    @CsvBindByName(column = "city")
    private String city;
    @CsvBindByName(column = "grid_id")
    private String gridId;
}
