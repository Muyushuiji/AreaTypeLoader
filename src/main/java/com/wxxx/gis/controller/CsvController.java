package com.wxxx.gis.controller;

import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
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

    @GetMapping("/mock")
    public List<Param> parseByName(MultipartFile file) throws IOException, ParseException {

        List<Param> Sourceparams = CsvUtil.parseByName(file);
        //获取经纬度 判断是否在网格内

        List<GridVO> gridVOS = gridService.findAll();


        WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
        for (Param param : Sourceparams) {
            String wktPoint = "POINT (" + param.getLongitude() + " " + param.getLatitude() + ")";
            Geometry point = wktReader.read(wktPoint);
            for (GridVO gridVO : gridVOS) {
                JSONObject geom = JSON.parseObject(gridVO.getGeom());
//                JSONObject geom = geometriesArray.getJSONObject("geometry");
//                Reader reader = new StringReader(gridVO.getGeom());
                Reader reader = new StringReader(geom.toString());
                GeometryJSON gjson = new GeometryJSON(15);
                Geometry wktgeom = gjson.read(reader);

                if (wktgeom.contains(point)) {
                    log.info("---存在----");
                    //点在多边形内
//                   param.setGridId(gridVO.getGridId());
                    param.setCalgridId(gridVO.getGridId());
                    break;
                }

            }

        }

        return Sourceparams;

    }
}
