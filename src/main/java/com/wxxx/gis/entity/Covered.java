package com.wxxx.gis.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.wxxx.gis.util.CustomDisConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 室外覆盖结果表
 *
 * @author xmmxjy
 * @date 2022-03-11 16:00:26
 */
@Data
@TableName("tb_covered")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "室外覆盖结果表")
public class Covered extends Model<Covered> {
private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Integer id;
    /**
     * 序号
     */
    @ApiModelProperty(value="序号")
    @ExcelProperty("序号")
    private Integer num;
    /**
     * 省份
     */
    @ApiModelProperty(value="省份")
    @ExcelProperty("省份")
    private String province;
    /**
     * 地市
     */
    @ApiModelProperty(value="城市")
    @ExcelProperty("城市")
    private String city;
    /**
     * 区域
     */
    @ApiModelProperty(value="区域")
    @ExcelProperty("区域")
    private String region;
    /**
     * 行政区
     */
    @ApiModelProperty(value="行政区")
    @ExcelProperty("行政区")
    private String district;
    /**
     * 场景
     */
    @ApiModelProperty(value="场景")
    @ExcelProperty("场景")
    private String scene;
    /**
     * 问题说明
     */
    @ApiModelProperty(value="问题说明")
    @ExcelProperty("问题说明")
    private String question;
    /**
     * 距离
     */
    @ApiModelProperty(value="弱覆盖里程（km)")
    @ExcelProperty(value = "弱覆盖里程（km)", converter = CustomDisConverter.class)
    private Double distance = 0d;
    /**
     * 电平
     */
    @ApiModelProperty(value="仿真电平")
    @ExcelProperty("仿真电平")
    private String level;
    /**
     * 原因
     */
    @ApiModelProperty(value="原因")
    @ExcelProperty("原因")
    private String reason;
    /**
     * 建议
     */
    @ApiModelProperty(value="建议")
    @ExcelProperty("建议")
    private String suggest;
    /**
     * 经度
     */
    @ApiModelProperty(value="经度")
    @ExcelProperty("中心经度")
    private Double lng;
    /**
     * 纬度
     */
    @ApiModelProperty(value="纬度")
    @ExcelProperty("中心纬度")
    private Double lat;
    }
