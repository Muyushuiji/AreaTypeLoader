package com.wxxx.gis.controller;

import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.wxxx.gis.entity.CityGridVO;
import com.wxxx.gis.entity.GenParam;
import com.wxxx.gis.entity.GridVO;
import com.wxxx.gis.entity.Param;
import com.wxxx.gis.service.GridService;
import com.wxxx.gis.util.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
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
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-22 18:23
 **/
@RestController
@RequestMapping(value = "/csv")
@Slf4j
public class GridController {

    @Resource
    private GridService gridService;

//    @Value("${web.path}")
//    private String path;

//    @GetMapping("/grid")
//    public String parseByName(MultipartFile file, String province) throws IOException, ParseException {
//
//        log.info("------开始读取csv数据--");
//        List<Param> sourceParams = CsvUtil.parseByName(file);
//        log.info("------读取csv数据结束-------");
//        //获取经纬度 判断是否在网格内
//
//        List<GenParam> genParams = new ArrayList<>();
//        List<GridVO> allByProvince = gridService.findAll();
//        log.info("------开始判断点跟网格的所属关系结束-------");
//        WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
//        for (Param param : sourceParams) {
//            String wktPoint = "POINT (" + param.getLongitude() + " " + param.getLatitude() + ")";
//            Geometry point = wktReader.read(wktPoint);
//            for (GridVO gridVO : allByProvince) {
//                JSONObject geom = JSON.parseObject(gridVO.getGeom());
//                Reader reader = new StringReader(geom.toString());
//                GeometryJSON gjson = new GeometryJSON(15);
//                Geometry wktgeom = gjson.read(reader);
//
//                if (wktgeom.contains(point)) {
//                    log.info("---点存在----");
//                    //点在多边形内
//                    GenParam genParam = new GenParam();
//                    genParam.setCGI(param.getCGI());
//                    genParam.setProvince(param.getProvince());
//                    genParam.setCity(param.getCity());
//                    genParam.setGridId(gridVO.getGridId());
//                    genParams.add(genParam);
//                    break;
//                }
//            }
//
//        }
//        log.info("----计算结束，开始生成导出csv文件----");
//        String csvPath = "";
//        try {
//            csvPath = CsvUtil.writeByEntity(genParams, path, province);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "false";
//        }
//        log.info("----csv文件生成完毕----");
//
//        return csvPath;
//
//    }

    @GetMapping("loadWorkingParameter")
    public String loadWorkingParameter() {

        Map<String, List<CityGridVO>> map = initByProvince();

        String pathString = "C:\\Users\\14818\\Desktop\\Site-Plan\\merge20220415\\5Gxin - 副本.csv";
        String pathOutString = "C:\\Users\\14818\\Desktop\\Site-Plan\\merge20220415\\5Ggrid-142115.csv";

        FileWriter fw = null;
        BufferedWriter bw = null;
        List<CityGridVO> allByProvince = new ArrayList<>();
        WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
        String str = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(pathString));
            fw = new FileWriter(pathOutString);
            bw = new BufferedWriter(fw);


            int line = 0;
            try {
                while ((str = in.readLine()) != null) {
                    line++;
                    if (line < 142115) {
                        continue;
                    }

                    String[] split = str.split(",");
                    //取出省份、城市、经度、纬度、cgi
                    String strLongitute = split[12].trim();
                    String strLatitude = split[13].trim();
                    double longitute = Double.parseDouble(strLongitute.replace("\"", ""));
                    double latitude = Double.parseDouble(strLatitude.replace("\"", ""));
                    String province = split[1].trim();
                    String city = split[2].trim();
                    String cgi = split[11].trim();
                    String gridid = "";
                    String searchPro = province.replace("\"", "").trim();

                    if (map.containsKey(searchPro)) {
                        allByProvince = map.get(searchPro);
                    }
                    //构造工参 空间信息
                    String wktPoint = "POINT (" + longitute + " " + latitude + ")";
                    Geometry point = wktReader.read(wktPoint);
                    // 找网格的空间信息，通过省份、城市筛选
                    // 得到网格列表
                    for (CityGridVO gridVO : allByProvince) {
                        JSONObject geom = JSON.parseObject(gridVO.getGeom());
                        Reader reader = new StringReader(geom.toString());
                        GeometryJSON gjson = new GeometryJSON(15);
                        Geometry wktgeom = gjson.read(reader);

                        if (wktgeom.contains(point)) {
                            log.info("---点存在----");
                            //点在多边形内
                            //获取经度纬度对应的网格id
                            gridid = gridVO.getGridId();
                            break;
                        }
                    }
                    //拼接输出
                    String outString = cgi + "," + "\"" + gridid + "\"" + "," + province + "," + city + "\r\n";
                    bw.write(outString);
                    bw.flush();
                }
            } catch (IOException e) {
                log.error("-----{}异常-----", str);
                e.printStackTrace();
            } catch (NumberFormatException e) {
                log.error("-----{}异常-----", str);
                e.printStackTrace();
            } catch (ParseException e) {
                log.error("-----{}异常-----", str);
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("333333333333333 end");
        return "111";
    }

    private Map<String, List<CityGridVO>> initByProvince() {
        Map<String, List<CityGridVO>> mapAll = new HashMap<>();

        List<CityGridVO> cityGridVOS = gridService.findAll();
        for (CityGridVO cityGridVO : cityGridVOS) {
            String province = "";
            if (cityGridVO.getProvince().endsWith("市") || cityGridVO.getProvince().endsWith("省")) {
                province = cityGridVO.getProvince().substring(0, cityGridVO.getProvince().length() - 1);
            }
            if ("内蒙古自治区".equals(cityGridVO.getProvince())) {
                province = "内蒙古";
            }
            if ("宁夏回族自治区".equals(cityGridVO.getProvince())) {
                province = "宁夏";
            }
            if ("广西壮族自治区".equals(cityGridVO.getProvince())) {
                province = "广西";
            }
            if ("西藏自治区".equals(cityGridVO.getProvince())) {
                province = "西藏";
            }
            if ("新疆维吾尔自治区".equals(cityGridVO.getProvince())) {
                province = "新疆";
            }
            if (mapAll.containsKey(province.trim())) {
                mapAll.get(province.trim()).add(cityGridVO);
            } else {
                List<CityGridVO> cityGridList = new ArrayList<>();
                cityGridList.add(cityGridVO);
                mapAll.put(province.trim(), cityGridList);
            }
        }
        return mapAll;
    }

    private Map<String, List<CityGridVO>> init() {
        Map<String, List<CityGridVO>> mapAll = new HashMap<>();

        List<CityGridVO> cityGridVOS = gridService.findAll();
        for (CityGridVO cityGridVO : cityGridVOS) {
            if (mapAll.containsKey(cityGridVO.getProvince().trim() + "-" + cityGridVO.getCity().trim())) {
                mapAll.get(cityGridVO.getProvince().trim() + "-" + cityGridVO.getCity().trim()).add(cityGridVO);
            } else {
                List<CityGridVO> cityGridList = new ArrayList<>();
                cityGridList.add(cityGridVO);
                mapAll.put(cityGridVO.getProvince().trim() + "-" + cityGridVO.getCity().trim(), cityGridList);
            }
        }
        return mapAll;
    }


    /**
     * 去掉网格表中省份（直辖市）的后缀
     * @param province
     * @return
     */
    private String removeProvinceSuffix(String province) {
        if (province.endsWith("市") || province.endsWith("省")) {
            province = province.substring(0, province.length() - 1);
        }
        if ("内蒙古自治区".equals(province)) {
            province = "内蒙古";
        }
        if ("宁夏回族自治区".equals(province)) {
            province = "宁夏";
        }
        if ("广西壮族自治区".equals(province)) {
            province = "广西";
        }
        if ("西藏自治区".equals(province)) {
            province = "西藏";
        }
        if ("新疆维吾尔自治区".equals(province)) {
            province = "新疆";
        }

        return province;
    }

    /**
     *去掉网格表中 city 的后缀
     * @param city
     * @return
     */
    private String removeCiteSuffix(String city) {
        city = city.trim();

        String keysToRemove[] = {"藏族自治州","白族自治州","傣族景颇族自治州","傈僳族自治州","壮族苗族自治州","彝族自治州","傣族自治州","藏族羌族自治州","地区","蒙古自治州","土家族苗族自治州","回族自治州","新区","苗族侗族自治州","布依族苗族自治州"};

        if (city.endsWith("市") || city.endsWith("盟")) {
            city = city.substring(0, city.length() - 1);
        } else {
            for (String keyToRemove : keysToRemove) {
                if (-1 != city.indexOf(keyToRemove)) {
                    city = city.replace(keyToRemove, "");
                }
            }
        }

        return city;
    }


    /**
     * 将所有网格加载到内存中
     * 返回MAP，key 为“省份缩写-地市缩写” value 为 CityGridVO 对象
     * @return
     */
    private Map<String, List<CityGridVO>> initAllGrid() throws IOException {
        Map<String, List<CityGridVO>> allGrids = new HashMap<>();

        List<CityGridVO> cityGridVOS = gridService.findAll();
        for (CityGridVO cityGridVO : cityGridVOS) {
            JSONObject geom = JSON.parseObject(cityGridVO.getGeom());
            Reader reader = new StringReader(geom.toString());
            GeometryJSON gjson = new GeometryJSON(15);
            Geometry wktgeom = gjson.read(reader);
            // 获取所有点
            Coordinate[] points = wktgeom.getCoordinates();
            for (Coordinate tempPoint : points) {
                if (tempPoint.x > cityGridVO.getMaxX()) {
                    cityGridVO.setMaxX(tempPoint.x);
                }
                if (tempPoint.x < cityGridVO.getMinX()) {
                    cityGridVO.setMinX(tempPoint.x);
                }
                if (tempPoint.y > cityGridVO.getMaxY()) {
                    cityGridVO.setMaxY(tempPoint.y);
                }
                if (tempPoint.y < cityGridVO.getMinY()) {
                    cityGridVO.setMinY(tempPoint.y);
                }
            }

            String province = removeProvinceSuffix(cityGridVO.getProvince());
            String city = removeCiteSuffix(cityGridVO.getCity());
            String mapKey = province + "-" + city;

            if (allGrids.containsKey(mapKey.trim())) {
                allGrids.get(mapKey.trim()).add(cityGridVO);
            } else {
                List<CityGridVO> cityGridList = new ArrayList<>();
                cityGridList.add(cityGridVO);
                allGrids.put(mapKey.trim(), cityGridList);
            }
        }
        return allGrids;
    }


    @GetMapping("loadWorkingParameter5GYY")
    public String loadWorkingParameter5GYY() {
        Map<String, List<CityGridVO>> map = null;
        try {
            map = initAllGrid();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String pathString = "D:\\code\\2022年8月19日-工参计算\\01.余迪秋2022年8月18日提供的第一份工参5G工参是全的，4G工参不全，没有16进制之类的数据\\merge20220415\\merge20220415\\5Gxin - 副本.csv";
        String pathOutString = "D:\\code\\2022年8月19日-工参计算\\01.余迪秋2022年8月18日提供的第一份工参5G工参是全的，4G工参不全，没有16进制之类的数据\\merge20220415\\merge20220415\\5Ggrid-test.csv";

        FileWriter fw = null;
        BufferedWriter bw = null;

        // 用于筛选省份
        List<CityGridVO> filteredGrid = new ArrayList<>();
        WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
        String str = "";
        String titleArray[] = null;
        String activeSite = "";
        String activeGridId = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(pathString));
            fw = new FileWriter(pathOutString);
            bw = new BufferedWriter(fw);

            int line = 0;
            try {
                while ((str = in.readLine()) != null) {
                    if (0 == line) {
                        titleArray = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                        line++;
                        continue;
                    }
                    line++;

                    if (line % 100000 == 0) {
                        log.info("读取{}行，{}", line, str);
                    }

                    // 正则表达式保证数值中不含有分隔符
                    String[] split = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    //取出省份、城市、经度、纬度、cgi
                    String strLongitute = split[12].trim();
                    String strLatitude = split[13].trim();
                    double longitute = Double.parseDouble(strLongitute.replace("\"", ""));
                    double latitude = Double.parseDouble(strLatitude.replace("\"", ""));
                    String province = split[1].trim();
                    String city = split[2].trim();
                    String cgi = split[11].trim();
                    String gridid = "";
                    String searchPro = province.replace("\"", "").trim();
                    String searchCity = city.replace("\"", "").trim();

                    if (activeSite.equalsIgnoreCase(strLongitute.trim() + strLatitude.trim())) {
                        // 相同经纬度，不需要计算
                        gridid = activeGridId;
                    } else {
                        // 一组新的经纬度，进行记录
                        activeSite = strLongitute.trim() + strLatitude.trim();
                        if (map.containsKey(searchPro + "-" + searchCity)) {
                            filteredGrid = map.get(searchPro + "-" + searchCity);
                        } else {
                            // log.info("The cell:{} ({},{}) not in grid", cgi, longitute, latitude);
                        }
                        //构造工参 空间信息
                        String wktPoint = "POINT (" + longitute + " " + latitude + ")";
                        Geometry point = wktReader.read(wktPoint);
                        // 找网格的空间信息，通过省份、城市筛选
                        // 得到网格列表
                        for (CityGridVO gridVO : filteredGrid) {
                            // 经纬度大于外接矩形，不判断
                            if (longitute > gridVO.getMaxX() || longitute < gridVO.getMinX()
                                    || latitude > gridVO.getMaxY() || latitude < gridVO.getMinY()) {
                                continue;
                            }

                            JSONObject geom = JSON.parseObject(gridVO.getGeom());
                            Reader reader = new StringReader(geom.toString());
                            GeometryJSON gjson = new GeometryJSON(15);
                            Geometry wktgeom = gjson.read(reader);


                            if (wktgeom.contains(point)) {
                                // log.info("---点存在----");
                                // 点在多边形内
                                // 获取经度纬度对应的网格id
                                gridid = gridVO.getGridId();
                                // log.info("The cell:{} ({},{}) 在网格中，网格 id 为{}", cgi, longitute, latitude, gridid);
                                break;
                            }// end if
                        }// end for
                        activeGridId = gridid;
                    }// end else
                    //拼接输出
                    String outString = cgi + "," + "\"" + gridid + "\"" + "," + province + "," + city + "\r\n";
                    bw.write(outString);
                    bw.flush();
                }
            } catch (IOException e) {
                log.error("-----{}异常-----", str);
                e.printStackTrace();
            } catch (NumberFormatException e) {
                log.error("-----{}异常-----", str);
                e.printStackTrace();
            } catch (ParseException e) {
                log.error("-----{}异常-----", str);
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            log.error("-----{}异常-----", str);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("333333333333333 end");
        return "111";
    }

    @GetMapping("loadWorkingParameter4G")
    public String loadWorkingParameter4G() throws ParseException {
        Map<String, List<CityGridVO>> map = null;
        try {
            map = initAllGrid();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String pathString = "C:\\Users\\14818\\Desktop\\Site-Plan\\merge20220415\\4Gxin.csv";
        String pathOutString = "C:\\Users\\14818\\Desktop\\Site-Plan\\merge20220415\\4Ggrid.csv";

        FileWriter fw = null;
        BufferedWriter bw = null;

        // 用于筛选省份
        List<CityGridVO> filteredGrid = new ArrayList<>();
        WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
        String str = "";
        String titleArray[] = null;
        String activeSite = "";
        String activeGridId = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(pathString));
            fw = new FileWriter(pathOutString);
            bw = new BufferedWriter(fw);

            int line = 0;
            try {
                while ((str = in.readLine()) != null) {
                    if (0 == line) {
                        titleArray = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                        line++;
                        continue;
                    }
                    line++;

                    if (line % 100000 == 0) {
                        log.info("读取{}行，{}", line, str);
                    }

                    // 正则表达式保证数值中不含有分隔符
                    String[] split = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    //取出省份、城市、经度、纬度、cgi
                    String strLongitute = split[13].trim();
                    String strLatitude = split[12].trim();
                    double longitute = Double.parseDouble(strLongitute.replace("\"", ""));
                    double latitude = Double.parseDouble(strLatitude.replace("\"", ""));
                    String province = split[1].trim();
                    String city = split[2].trim();
                    String cgi = split[3].trim();
                    String gridid = "";
                    String searchPro = province.replace("\"", "").trim();
                    String searchCity = city.replace("\"", "").trim();

                    if (activeSite.equalsIgnoreCase(strLongitute.trim() + strLatitude.trim())) {
                        // 相同经纬度，不需要计算
                        gridid = activeGridId;
                    } else {
                        // 一组新的经纬度，进行记录
                        activeSite = strLongitute.trim() + strLatitude.trim();
                        if (map.containsKey(searchPro + "-" + searchCity)) {
                            filteredGrid = map.get(searchPro + "-" + searchCity);
                        } else {
                            // log.info("The cell:{} ({},{}) not in grid", cgi, longitute, latitude);
                        }
                        //构造工参 空间信息
                        String wktPoint = "POINT (" + longitute + " " + latitude + ")";
                        Geometry point = wktReader.read(wktPoint);
                        // 找网格的空间信息，通过省份、城市筛选
                        // 得到网格列表
                        for (CityGridVO gridVO : filteredGrid) {
                            // 经纬度大于外接矩形，不判断
                            if (longitute > gridVO.getMaxX() || longitute < gridVO.getMinX()
                                    || latitude > gridVO.getMaxY() || latitude < gridVO.getMinY()) {
                                continue;
                            }

                            JSONObject geom = JSON.parseObject(gridVO.getGeom());
                            Reader reader = new StringReader(geom.toString());
                            GeometryJSON gjson = new GeometryJSON(15);
                            Geometry wktgeom = gjson.read(reader);


                            if (wktgeom.contains(point)) {
                                // log.info("---点存在----");
                                // 点在多边形内
                                // 获取经度纬度对应的网格id
                                gridid = gridVO.getGridId();
                                // log.info("The cell:{} ({},{}) 在网格中，网格 id 为{}", cgi, longitute, latitude, gridid);
                                break;
                            }// end if
                        }// end for
                        activeGridId = gridid;
                    }// end else
                    //拼接输出
                    String outString = cgi + "," + "\"" + gridid + "\"" + "," + province + "," + city + "\r\n";
                    bw.write(outString);
                    bw.flush();
                }
            } catch (IOException e) {
                log.error("-----{}异常-----", str);
                e.printStackTrace();
            } catch (NumberFormatException e) {
                log.error("-----{}异常-----", str);
                e.printStackTrace();
            } catch (ParseException e) {
                log.error("-----{}异常-----", str);
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            log.error("-----{}异常-----", str);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("333333333333333 end");
        return "111";
    }
}
