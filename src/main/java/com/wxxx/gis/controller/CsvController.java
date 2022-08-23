package com.wxxx.gis.controller;

import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.wxxx.gis.entity.GenParam;
import com.wxxx.gis.entity.GridVO;
import com.wxxx.gis.entity.Param;
import com.wxxx.gis.service.GridService;
import com.wxxx.gis.util.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-22 18:23
 **/
@RestController
@RequestMapping(value = "/csv")
@Slf4j
public class CsvController {

    @Resource
    private GridService gridService;

    @Value("${web.path}")
    private String path;

    @GetMapping("/grid")
    public String parseByName(MultipartFile file, String province) throws IOException, ParseException {

        log.info("------开始读取csv数据--");
        List<Param> sourceParams = CsvUtil.parseByName(file);
        log.info("------读取csv数据结束-------");
        //获取经纬度 判断是否在网格内

        List<GenParam> genParams = new ArrayList<>();
        List<GridVO> allByProvince = gridService.findAll();
        log.info("------开始判断点跟网格的所属关系结束-------");
        WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
        for (Param param : sourceParams) {
            String wktPoint = "POINT (" + param.getLongitude() + " " + param.getLatitude() + ")";
            Geometry point = wktReader.read(wktPoint);
            for (GridVO gridVO : allByProvince) {
                JSONObject geom = JSON.parseObject(gridVO.getGeom());
                Reader reader = new StringReader(geom.toString());
                GeometryJSON gjson = new GeometryJSON(15);
                Geometry wktgeom = gjson.read(reader);

                if (wktgeom.contains(point)) {
                    log.info("---点存在----");
                    //点在多边形内
                    GenParam genParam = new GenParam();
                    genParam.setCGI(param.getCGI());
                    genParam.setProvince(param.getProvince());
                    genParam.setCity(param.getCity());
                    genParam.setGridId(gridVO.getGridId());
                    genParams.add(genParam);
                    break;
                }
            }

        }
        log.info("----计算结束，开始生成导出csv文件----");
        String csvPath = "";
        try {
            csvPath = CsvUtil.writeByEntity(genParams, path, province);
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        log.info("----csv文件生成完毕----");

        return csvPath;

    }
}
