package com.wxxx.gis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: gis-web
 * @description: 网格
 * @author: hxl
 * @create: 2022-08-22 18:37
 **/
@Data
@TableName("citygrid")
public class GridVO {

    private String gridId;
    private String geom;
}
