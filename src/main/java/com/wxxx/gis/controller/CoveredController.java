package com.wxxx.gis.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxxx.gis.common.Result;
import com.wxxx.gis.entity.Covered;
import com.wxxx.gis.listener.CoveredExcelListener;
import com.wxxx.gis.mapper.CoveredMapper;
import com.wxxx.gis.service.CoveredService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Consumer;


/**
 * 室外覆盖结果表
 *
 * @author xmmxjy
 * @date 2022-03-11 16:00:26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/covered")
@Api(value = "covered", tags = "室外覆盖结果表管理")
@Slf4j
public class CoveredController {

    @Resource
    private CoveredMapper coveredMapper;

    @Resource
    private CoveredService coveredService;

    @Resource
    private CoveredExcelListener coveredExcelListener;

    @ApiOperation(value = "列表查询", notes = "列表查询")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "province", value = "省份", required = false, dataType = "String", example = "浙江"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, dataType = "String", example = "绍兴"),
            @ApiImplicitParam(name = "region", value = "城市", required = false, dataType = "String", example = "一类区域"),
            @ApiImplicitParam(name = "district", value = "城市", required = false, dataType = "String", example = "越城"),
            @ApiImplicitParam(name = "scene", value = "场景", required = false, dataType = "String", example = "密集城区"),
    }
    )
    public Result list(String province, String city, String region, String district, String scene) {
        LambdaQueryWrapper<Covered> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(province)) {
            queryWrapper.likeRight(Covered::getProvince, province);
        }
        if (StrUtil.isNotBlank(city)) {
            queryWrapper.likeRight(Covered::getCity, city);
        }
        if (StrUtil.isNotBlank(region)) {
            queryWrapper.likeRight(Covered::getRegion, region);
        }
        if (StrUtil.isNotBlank(district)) {
            queryWrapper.likeRight(Covered::getDistrict, district);
        }
        if (StrUtil.isNotBlank(scene)) {
            queryWrapper.eq(Covered::getScene, scene);
        }

        return new Result(1, "success", coveredService.list(queryWrapper));
    }

    @ApiOperation(value = "文件上传", notes = "文件上传")
    @PostMapping("/upload")
    @ResponseBody
    public Result upload(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), coveredExcelListener).head(Covered.class).sheet().doReadSync();
//            AnalysisEventListener<Covered> coveredAnalysisEventListener = ExcelListenerUtils.getListener(this.batchInsert(),10);
//            EasyExcel.read(file.getInputStream(),Covered.class,coveredAnalysisEventListener).sheet().doReadSync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Result(0, "上传失败", null);
        }
        return new Result(1, "上传成功", null);
    }

    /**
     *
     */
    private Consumer<List<Covered>> batchInsert() {
        return covereds -> coveredService.insertBatchSomeColumn(covereds);
    }
}
