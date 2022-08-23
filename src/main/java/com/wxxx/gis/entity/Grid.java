package com.wxxx.gis.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
public class Grid {

    @TableId
    private String id;
}
