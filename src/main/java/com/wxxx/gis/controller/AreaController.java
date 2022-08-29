package com.wxxx.gis.controller;

import com.wxxx.gis.entity.Area;
import com.wxxx.gis.entity.AreaGid;
import com.wxxx.gis.entity.AreaValid;
import com.wxxx.gis.service.AreaGidService;
import com.wxxx.gis.service.AreaService;
import com.wxxx.gis.service.AreaValidService;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-24 13:46
 **/
@RestController
@RequestMapping(value = "/csv/area")
@Slf4j
public class AreaController {

    @Resource
    private AreaService areaService;
    @Resource
    private AreaGidService areaGidService;
    @Resource
    private AreaValidService areaValidService;

    public static final Integer LONGITUDE_STRING_LENGTH = 9;
    public static final Integer LATITUDE_STRING_LENGTH = 8;

    public static final Integer AREA_EXPANSION_FACTOR = 100000;

    /**
     * 去掉网格表中省份（直辖市）的后缀
     *
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
     * 去掉网格表中 city 的后缀
     *
     * @param city
     * @return
     */
    private String removeCiteSuffix(String city) {
        if ("".equals(city) || null == city) {
            return "";
        }
        city = city.trim();

        String keysToRemove[] = {"藏族自治州", "白族自治州", "傣族景颇族自治州", "傈僳族自治州", "壮族苗族自治州", "彝族自治州", "傣族自治州", "藏族羌族自治州", "地区", "蒙古自治州", "土家族苗族自治州", "回族自治州", "新区", "苗族侗族自治州", "布依族苗族自治州"};

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


    private Map<String, List<Area>> initByProvince() {
        Map<String, List<Area>> mapAll = new HashMap<>();

        List<Area> areas = areaService.findAll();
        for (Area area : areas) {
            String province = "";
            if (area.getProvince().endsWith("市") || area.getProvince().endsWith("省")) {
                province = area.getProvince().substring(0, area.getProvince().length() - 1);
            }
            if ("内蒙古自治区".equals(area.getProvince())) {
                province = "内蒙古";
            }
            if ("宁夏回族自治区".equals(area.getProvince())) {
                province = "宁夏";
            }
            if ("广西壮族自治区".equals(area.getProvince())) {
                province = "广西";
            }
            if ("西藏自治区".equals(area.getProvince())) {
                province = "西藏";
            }
            if ("新疆维吾尔自治区".equals(area.getProvince())) {
                province = "新疆";
            }
            if (mapAll.containsKey(province.trim())) {
                mapAll.get(province.trim()).add(area);
            } else {
                List<Area> areaList = new ArrayList<>();
                areaList.add(area);
                mapAll.put(province.trim(), areaList);
            }
        }
        return mapAll;
    }

    @GetMapping("loadAreaType")
    public String loadAreaType() throws ParseException {
        Map<String, List<Area>> map = null;
        try {
            map = initAllArea();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // String pathString = "D:\\code\\2022年8月19日-工参计算\\01.余迪秋2022年8月18日提供的第一份工参5G工参是全的，4G工参不全，没有16进制之类的数据\\merge20220415\\merge20220415\\5Gxin - 副本.csv";
        String pathString = "C:\\Users\\14818\\Desktop\\Site-Plan\\merge20220415\\5Gxin - 副本.csv";
        //String pathOutString = "D:\\code\\2022年8月19日-工参计算\\01.余迪秋2022年8月18日提供的第一份工参5G工参是全的，4G工参不全，没有16进制之类的数据\\merge20220415\\merge20220415\\5GareaType.csv";
        String pathOutString = "C:\\Users\\14818\\Desktop\\Site-Plan\\merge20220415\\5GareaType.csv";
        int skipLines = 0;
        FileWriter fw = null;
        BufferedWriter bw = null;

        // 用于筛选省份
        List<Area> filteredArea = new ArrayList<>();
        WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
        String str = "";
        String titleArray[] = null;
        String activeSite = "";
        String activeAreaType = "";
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
                    if (line < skipLines) {
                        continue;
                    }

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
                    String areaType = "";
                    String searchPro = province.replace("\"", "").trim();
                    String searchCity = city.replace("\"", "").trim();

                    if (activeSite.equalsIgnoreCase(strLongitute.trim() + strLatitude.trim())) {
                        // 相同经纬度，不需要计算
                        areaType = activeAreaType;
                    } else {
                        // 一组新的经纬度，进行记录
                        activeSite = strLongitute.trim() + strLatitude.trim();
                        if (map.containsKey(searchPro + "-" + searchCity)) {
                            filteredArea = map.get(searchPro + "-" + searchCity);
                        } else {
                            // log.info("The cell:{} ({},{}) not in grid", cgi, longitute, latitude);
                        }
                        //构造工参 空间信息
                        String wktPoint = "POINT (" + longitute + " " + latitude + ")";
                        Geometry point = wktReader.read(wktPoint);
                        // 找网格的空间信息，通过省份、城市筛选
                        // 得到网格列表
                        for (Area area : filteredArea) {
                            // 经纬度大于外接矩形，不判断
                            if (longitute > area.getMaxX() || longitute < area.getMinX()
                                    || latitude > area.getMaxY() || latitude < area.getMinY()) {
                                continue;
                            }

//                            JSONObject geom = JSON.parseObject(area.getGeom());
                            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                            WKTReader reader = new WKTReader(geometryFactory);
                            //Point point = (Point) reader.read("POINT (1 1)");
                            //log.info("check cgi:{}({},{}){}-{}:centerX={},centerY={},subGeomNo={}",cgi,strLongitute,strLatitude,area.getProvince(),area.getCity(),area.getCenterX(),area.getCenterY(),area.getSubGeomNo());
                            //area.get
                            String geom = area.getGeom();
                            //Reader reader = new StringReader(geom);
                            //GeometryJSON gjson = new GeometryJSON(15);
//            JSONObject geom = JSON.parseObject(area.getGeom());
//            Reader reader = new StringReader(geom.toString());
//            GeometryJSON gjson = new GeometryJSON(15);
                            Geometry wktgeom = reader.read(geom);
                            try {
                                if (point.within(wktgeom)) {
                                    // log.info("---点存在----");
                                    // 点在多边形内
                                    // 获取经度纬度对应的网格id
                                    areaType = area.getAreaType();
                                    // log.info("The cell:{} ({},{}) 在网格中，网格 id 为{}", cgi, longitute, latitude, gridid);
                                    break;
                                }
//                                if (wktgeom.contains(point)) {
//                                    // log.info("---点存在----");
//                                    // 点在多边形内
//                                    // 获取经度纬度对应的网格id
//                                    areaType = area.getAreaType();
//                                    // log.info("The cell:{} ({},{}) 在网格中，网格 id 为{}", cgi, longitute, latitude, gridid);
//                                    break;
//                                }// end if
                            } catch (Exception e) {
                                log.info("check cgi:{}({},{}){}-{}:centerX={},centerY={},subGeomNo={}", cgi, strLongitute, strLatitude, area.getProvince(), area.getCity(), area.getCenterX(), area.getCenterY(), area.getSubGeomNo());
                                continue;
                            }
                        }// end for
                        activeAreaType = areaType;
                    }// end else
                    //拼接输出
                    String outString = cgi + "," + "\"" + areaType + "\"" + "," + province + "," + city + "\r\n";
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
            } catch (Exception e) {
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

    @GetMapping("loadAreaTypeNew")
    public String loadAreaTypeNew() throws ParseException {
        Map<String, List<AreaGid>> map = null;
        try {
            map = initAllAreaGid();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // String pathString = "D:\\code\\2022年8月19日-工参计算\\01.余迪秋2022年8月18日提供的第一份工参5G工参是全的，4G工参不全，没有16进制之类的数据\\merge20220415\\merge20220415\\5Gxin - 副本.csv";
        String pathString = "C:\\Users\\14818\\Desktop\\Site-Plan\\merge20220415\\5Gxin - 副本.csv";
        //String pathOutString = "D:\\code\\2022年8月19日-工参计算\\01.余迪秋2022年8月18日提供的第一份工参5G工参是全的，4G工参不全，没有16进制之类的数据\\merge20220415\\merge20220415\\5GareaType.csv";
        String pathOutString = "C:\\Users\\14818\\Desktop\\Site-Plan\\merge20220415\\5GareaType.csv";
        int skipLines = 0;
        FileWriter fw = null;
        BufferedWriter bw = null;

        // 用于筛选省份
        List<AreaGid> filteredAreaGid = new ArrayList<>();
        WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
        String str = "";
        // 工参文件标题
        String titleArray[] = null;
        // 在工参中，通常几个小区连续存储，他们的经纬度是一样的，因此只需要计算一次就可以了
        // 用经纬度标识一个active site： activeSite = strLongitute.trim() + strLatitude.trim();
        String activeSite = "";
        String activeAreaType = "";

        String activeProvince = "";
        String activeCity = "";

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
                    // 根据要求跳过若干行
                    if (line < skipLines) {
                        continue;
                    }

                    if (line % 5000 == 0) {
                        log.info("读取{}行，{}", line, str);
                    }

                    // 正则表达式保证数值中不含有分隔符
                    String[] split = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    //取出省份、城市、经度、纬度、cgi
                    String strLongitude = split[12].trim();
                    String strLatitude = split[13].trim();
                    double longitude = Double.parseDouble(strLongitude.replace("\"", ""));
                    double latitude = Double.parseDouble(strLatitude.replace("\"", ""));
                    String province = split[1].trim();
                    String city = split[2].trim();
                    String cgi = split[11].trim();
                    String areaType = "";
                    String searchPro = province.replace("\"", "").trim();
                    String searchCity = city.replace("\"", "").trim();

                    // 用于统计各个地市的耗时。乱序，以省为单位统计
//                    if (!activeProvince.trim().equalsIgnoreCase(province.trim())
//                            || !activeCity.trim().equalsIgnoreCase(city.trim())) {
                    if (!activeProvince.trim().equalsIgnoreCase(province.trim())) {
                        log.info("Line:{},new province{}", line, province, city);
                        activeProvince = province;
                        activeCity = city;
                    }


                    if (activeSite.equalsIgnoreCase(strLongitude.trim() + strLatitude.trim())) {
                        // 相同经纬度，不需要计算
                        areaType = activeAreaType;
                    } else {
                        // 一组新的经纬度，进行记录
                        activeSite = strLongitude.trim() + strLatitude.trim();
                        if (map.containsKey(searchPro + "-" + searchCity)) {
                            filteredAreaGid = map.get(searchPro + "-" + searchCity);
                        } else {
                            log.info("The cell:{} ({},{}) cannot be load, area info missed", cgi, longitude, latitude);
                        }

                        //构造工参 空间信息
                        String wktPoint = "POINT (" + longitude + " " + latitude + ")";
                        Geometry point = wktReader.read(wktPoint);
                        // 找网格的空间信息，通过省份、城市筛选
                        // 得到网格列表
                        for (AreaGid areaGid : filteredAreaGid) {
                            // 经纬度大于外接矩形，不判断
                            if (longitude > areaGid.getMaxX() || longitude < areaGid.getMinX()
                                    || latitude > areaGid.getMaxY() || latitude < areaGid.getMinY()) {
                                continue;
                            }
                            // 已经用经纬度匹配了，不需要用栅格获取，后面优化时，可以用栅格反向查找AreaGid数组
                            //calcRasterKeyByDecimalPlaces(strLongitude, strLatitude, 2);

//                            JSONObject geom = JSON.parseObject(area.getGeom());
                            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                            WKTReader reader = new WKTReader(geometryFactory);
                            //log.info("check cgi:{}({},{}){}-{}:centerX={},centerY={},subGeomNo={}",cgi,strLongitute,strLatitude,area.getProvince(),area.getCity(),area.getCenterX(),area.getCenterY(),area.getSubGeomNo());
                            String geom = areaGid.getGeom();
                            Geometry wktgeom = reader.read(geom);
                            try {
                                if (wktgeom.contains(point)) {
                                    // log.info("---点存在----");
                                    // 点在多边形内
                                    // 获取经度纬度对应的网格id
                                    areaType = areaGid.getAreaType();
                                    // log.info("The cell:{} ({},{}) 在网格中，网格 id 为{}", cgi, longitute, latitude, gridid);
                                    break;
                                }// end if
                            } catch (Exception e) {
                                log.info("check cgi:{}({},{}){}-{}:gid={},,subGeomNo={}", cgi, strLongitude, strLatitude, areaGid.getProvince(), areaGid.getCity(), areaGid.getGid(), areaGid.getSubGeomNo());
                                continue;
                            }
                        }// end for
                        activeAreaType = areaType;
                    }// end else
                    //拼接输出
                    String outString = cgi + "," + "\"" + areaType + "\"" + "," + province + "," + city + "\r\n";
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
            } catch (Exception e) {
                log.error("-----{}异常-----", str);
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            log.error("-----{}异常-----", str);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("-----{}异常-----", str);
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


        log.info("loadAreaTypeNew end");
        return "111";
    }


    /**
     * 输入经纬度，返回对应的位置编码
     * 主要采用除以因子后取整数的方式
     *
     * @param x         经度
     * @param y         纬度
     * @param parameter 因子
     * @return
     */
    private String calcRasterKey(String x, String y, long parameter) {
        String rasterKey = "";
        try {
            while (x.length() < LONGITUDE_STRING_LENGTH) {
                x += "0";
            }

            while (y.length() < LATITUDE_STRING_LENGTH) {
                y += "0";
            }

            Integer longitude = Integer.parseInt(x.replace(".", ""));
            Integer latitude = Integer.parseInt(y.replace(".", ""));

            rasterKey = "" + (longitude / parameter) + "-" + (latitude / parameter);

        } catch (Exception e) {
            rasterKey = "";
        }

        return rasterKey;
    }

    private void calcRasterKeyGroup(String minX, String maxX, String minY, String maxY, long parameter) {
        String key1 = calcRasterKey(minX, minY, parameter);
        String key2 = calcRasterKey(maxX, maxY, parameter);


        int a = 100;
    }

    /**
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private Map<String, List<AreaGid>> initAllAreaGid() throws IOException, ParseException {
        Map<String, List<AreaGid>> map = new HashMap<>();
        // List<AreaGid> areaGids = areaGidService.findByProvinceAndCity("陕西", "渭南");
        List<AreaGid> areaGids = areaGidService.findAll();
        for (AreaGid areaGid : areaGids) {
            //  log.info("Load areaGid table{}-{}", areaGid.getProvince(),areaGid.getCity());
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
            WKTReader reader = new WKTReader(geometryFactory);
            //Point point = (Point) reader.read("POINT (1 1)");
            String geom = areaGid.getGeom();
            Geometry wktgeom = reader.read(geom);

            // 获取所有多边形
            for (int i = 0; i < wktgeom.getNumGeometries(); i++) {
                AreaGid subAreaGid = new AreaGid();
                // 标识类：省份、城市、GID
                subAreaGid.setGid(areaGid.getGid());
                subAreaGid.setProvince(areaGid.getProvince());
                subAreaGid.setCity(areaGid.getCity());
                subAreaGid.setSubGeomNo("" + i);

                // 属性类：形状类型、面积
                // 属性类：中心点坐标、外接矩形坐标
                Geometry geometry = wktgeom.getGeometryN(i);
                subAreaGid.setAreaType(areaGid.getAreaType());
                subAreaGid.setShapeArea(new BigDecimal(geometry.getArea()));
                subAreaGid.setGeom(geometry.toString());

                //检验多边形是否合规
                AreaValid areaValid = new AreaValid();
                areaValid.setProvince(subAreaGid.getProvince());
                areaValid.setCity(subAreaGid.getCity());
                areaValid.setGid(subAreaGid.getGid());
                areaValid.setSubGid(String.valueOf(i));
                // 子多边形的最大、最小x和y
                boolean valid = isValidAreaGid(geometry, subAreaGid, wktgeom, areaValid);
                //添加子多边形校验数据
                areaValidService.save(areaValid);
                if (!valid) {
                    log.error("---校验子多边形合规性失败 gid: {} , city: {} ,province: {} ---", areaGid.getGid(), areaGid.getCity(), areaGid.getProvince());
                    continue;
                }
                LinkedHashSet<String> keySet = null;
                keySet = calcRasterKeyGroupByDecimalPlaces("" + subAreaGid.getMinX(),
                        "" + subAreaGid.getMaxX(),
                        "" + subAreaGid.getMinY(),
                        "" + subAreaGid.getMaxY(), 2);
                subAreaGid.setRasterKeySet(keySet);

                Envelope envelope = geometry.getEnvelopeInternal();
                subAreaGid.setMaxX(envelope.getMaxX());
                subAreaGid.setMinX(envelope.getMinX());
                subAreaGid.setMaxY(envelope.getMaxY());
                subAreaGid.setMinY(envelope.getMinY());


                // 保存到 map
                String province = removeProvinceSuffix(areaGid.getProvince());
                String city = removeCiteSuffix(areaGid.getCity());
                String mapKey = province + "-" + city;
                if (map.containsKey(mapKey.trim())) {
                    map.get(mapKey.trim()).add(subAreaGid);
                } else {
                    List<AreaGid> areaGidList = new ArrayList<>();
                    areaGidList.add(subAreaGid);
                    map.put(mapKey.trim(), areaGidList);
                }
            }
        } // end for (Area area : areas)
        return map;
    }

    /**
     * 根据小数点位数产生 key
     *
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param decimalPlaces
     */
    private LinkedHashSet<String> calcRasterKeyGroupByDecimalPlaces(String minX, String maxX, String minY, String maxY, int decimalPlaces) {

        String formattedMinX = formatPositionDataDecimalPlaces(minX, decimalPlaces);
        String formattedMaxX = formatPositionDataDecimalPlaces(maxX, decimalPlaces);
        String formattedMinY = formatPositionDataDecimalPlaces(minY, decimalPlaces);
        String formattedMaxY = formatPositionDataDecimalPlaces(maxY, decimalPlaces);

        int minXInt = Integer.parseInt(formattedMinX.replace(".", ""));
        int maxXInt = Integer.parseInt(formattedMaxX.replace(".", ""));
        int minYInt = Integer.parseInt(formattedMinY.replace(".", ""));
        int maxYInt = Integer.parseInt(formattedMaxY.replace(".", ""));

        //Map<String, Integer> keyMap = new LinkedHashMap<>();
        LinkedHashSet<String> keySet = new LinkedHashSet<>();
        for (int i = minXInt; i < maxXInt; i++) {
            for (int j = minYInt; j < maxYInt; j++) {
                String tmpKeyString = "" + i + "-" + j;
//                if (keyMap.containsKey(tmpKeyString)) {
//                    keyMap.put(tmpKeyString, keyMap.get(tmpKeyString) + 1);
//                } else {
//                    keyMap.put(tmpKeyString, 1);
//                }
                keySet.add(tmpKeyString);
            }
        }

        return keySet;

    }


    /**
     * 格式化经纬度字符串，保证小数点前3位，小数点后5位，不足的补零，根据小数点位数切割
     *
     * @param xOrY
     * @return
     */
    private String formatPositionDataDecimalPlaces(String xOrY, int decimalPlaces) {
        String formattedString = xOrY;
        while (formattedString.indexOf(".") < 3) {
            formattedString = "0" + formattedString;
        }

        while (formattedString.length() < 9) {
            formattedString = formattedString + "0";
        }

//        if (formattedString.length() >= 10) {
//            formattedString = formattedString.substring(0, 9);
//        }
        formattedString = formattedString.substring(0, 4 + decimalPlaces);

        return formattedString;
    }


    /**
     * 根据子geom中心点鉴定父geom多边形合规性
     *
     * @param geometry   子多边形
     * @param subAreaGid 子网格实体对象
     * @param parentGeom 父多边形
     * @return
     */
    private boolean isValidAreaGid(Geometry geometry, AreaGid subAreaGid, Geometry parentGeom, AreaValid areaValid) {
        // 检查面积是否符合规范
        Envelope envelope = geometry.getEnvelopeInternal();
//        double envelopeAreaCondition = (envelope.getMaxX() - envelope.getMinX()) * (envelope.getMaxY() - envelope.getMinY()) * AREA_EXPANSION_FACTOR - 1;
//        if (envelopeAreaCondition < 0) {
//            return false;
//        }

        // 检查图形是否包含自相交情况
        //设置外接矩形和子多边形的中心点，用中心点校验是否存在外接矩形中

        //多边形质心
        //Point gP = geometry.getCentroid();
        //多边形内心
        Point p = geometry.getInteriorPoint();
        //判断多边形内心是否在父geom内
        boolean contains = isInParentGeom(p, geometry, envelope);
        if (!contains) {
            log.error("---检验子多边形不合规，校验中心点X : {} , Y : {}---", p.getX(), p.getY());
            areaValid.setValidate(false);
            return false;
        }
        areaValid.setValidate(true);

        // 用子多边形的外接矩形中心点作为子多边形的中心点
        subAreaGid.setCenterX("" + (subAreaGid.getMaxX() + subAreaGid.getMinX()) / 2);
        subAreaGid.setCenterY("" + (subAreaGid.getMaxY() + subAreaGid.getMinY()) / 2);
        return true;
    }

    private boolean isInParentGeom(Point p, Geometry geometry, Envelope envelope) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
        WKTReader reader = new WKTReader(geometryFactory);
        boolean x = contains(p.getX(),p.getY(),reader,geometry);
        if (x) {
            return true;
        } else {
            //不包含计算 中心点同纬度 左右两点
            //计算同y轴左边的点
            double leftX = (envelope.getMinX() + p.getX()) / 2;
            double leftY = p.getY();
            boolean leftXPoint = contains(leftX, leftY, reader, geometry);
            if (!leftXPoint) {
                //计算同y轴右边的点
                double rightX = (envelope.getMaxX() + p.getX()) / 2;
                double rightY = p.getY();
                boolean leftYPoint = contains(rightX, rightY, reader, geometry);
                if (!leftYPoint) {
                    //计算同X轴上边的点
                    double topX = p.getX();
                    double topY = (envelope.getMinY() + p.getY()) / 2;
                    boolean topXPoint = contains(topX, topY, reader, geometry);
                    if (!topXPoint) {
                        //计算同X轴下边的点
                        double bottomX = p.getX();
                        double bottomY = (envelope.getMaxY() + p.getY()) / 2;
                        boolean bottomXPoint = contains(bottomX, bottomY, reader, geometry);
                        if (!bottomXPoint) {
                            //计算左上角点
                            double leftTopX = (envelope.getMinX() + p.getX()) / 2;
                            double leftTopY = (envelope.getMinY() + p.getY()) / 2;
                            boolean leftTopPoint = contains(leftTopX, leftTopY, reader, geometry);
                            if (!leftTopPoint) {
                                //计算左下角点
                                double leftBottomX = (envelope.getMinX() + p.getX()) / 2;
                                double leftBottomY = (envelope.getMaxY() + p.getY()) / 2;
                                boolean leftBottomPoint = contains(leftBottomX, leftBottomY, reader, geometry);
                                if (!leftBottomPoint) {
                                    //计算右下角
                                    double rightBottomX = (envelope.getMaxX() + p.getX()) / 2;
                                    double rightBottomY = (envelope.getMaxY() + p.getX()) / 2;
                                    boolean rightBottomPoint = contains(rightBottomX, rightBottomY, reader, geometry);
                                    if (!rightBottomPoint) {
                                        //计算右上角
                                        double rightTopX = (envelope.getMaxX() + p.getX()) / 2;
                                        double rightTopY = (envelope.getMinY() + p.getX()) / 2;
                                        return contains(rightTopX, rightTopY, reader, geometry);
                                    }
                                    return true;
                                }
                                return true;
                            }
                            return true;
                        }
                        return true;
                    }
                    return true;
                }
                //存在返回真
                return leftYPoint;
            }
            return true;
        }
    }

    private boolean contains(double x, double y, WKTReader reader, Geometry parentGeom) {
        String strPoint = "POINT (" + x + " " + y + ")";
        try {
            Geometry point = reader.read(strPoint);
            if (parentGeom.contains(point)) {
                return true;
            }
        } catch (Exception e) {
            log.error("---自相交 x: {}, y:{} ---",x,y);
            e.printStackTrace();
        }
        return false;
    }


    @GetMapping("test")
    public String test() throws ParseException {

        Map<String, List<AreaGid>> map = null;
        try {
            map = initAllAreaGid();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 临时代码，用于生成空间信息
        WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
        double testX = 109.48065;
        double testY = 35.12058;
        String wktPoint = "POINT (" + testX + " " + testY + ")";
        Geometry point = wktReader.read(wktPoint);

        String minX = "109.48065";
        String maxX = "109.50708";
        String minY = "35.12058";
        //String maxY = "35.13277";
        String maxY = "35.132";
        long parameter = 100;
        calcRasterKeyGroup(minX, maxX, minY, maxY, parameter);
        return "end";
    }


    private Map<String, List<Area>> initAllArea() throws IOException, ParseException {
        Map<String, List<Area>> map = new HashMap<>();
        // 查询指定省份城市
        // 这个会报错，因为查询到的空间信息没有经过 st_astext(geom) 进行格式转换
        // 如果希望实现这个功能，需要拼接 sql 才行
//        QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
//        Map<String, Object> param = new HashMap<>();
//        param.put("province", "陕西");
//        param.put("city", "渭南");
//        queryWrapper.allEq(param);
//        List<Area> areas = areaService.list(queryWrapper);

        List<Area> areas = areaService.findByProvinceAndCity("陕西", "渭南");

        for (Area area : areas) {
            //  log.info("Load area table{}-{}", area.getProvince(),area.getCity());
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
            WKTReader reader = new WKTReader(geometryFactory);
            //Point point = (Point) reader.read("POINT (1 1)");
            String geom = area.getGeom();
            Geometry wktgeom = reader.read(geom);

            // 获取所有多边形
            for (int i = 0; i < wktgeom.getNumGeometries(); i++) {
                Area subArea = new Area();
                subArea.setProvince(area.getProvince());
                subArea.setCity(area.getCity());

                subArea.setAreaType(area.getAreaType());
                subArea.setShapeArea(area.getShapeArea());
                subArea.setSubGeomNo("" + i);
//                //if (wktgeom.getGeometryN(i).getGeometryType().equalsIgnoreCase("Polygon")) {
//                    int holesCount = ((Polygon)wktgeom.getGeometryN(i)).getNumInteriorRing();
//                    int numGeometries = ((Polygon)wktgeom.getGeometryN(i)).getNumGeometries();
//                    int a = 100;
//                //}
                Geometry geometry = wktgeom.getGeometryN(i);
                geometry.getCentroid();
                subArea.setCenterX("" + geometry.getCentroid().getX());
                subArea.setCenterY("" + geometry.getCentroid().getY());
                subArea.setGeom(geometry.toString());
                Envelope envelope = geometry.getEnvelopeInternal();
                subArea.setMaxX(envelope.getMaxX());
                subArea.setMinX(envelope.getMinX());
                subArea.setMaxY(envelope.getMaxY());
                subArea.setMinY(envelope.getMinY());
//                Coordinate[] points = geometry.getCoordinates();
//                for (Coordinate tempPoint : points) {
//                    if (tempPoint.x > subArea.getMaxX()) {
//                        subArea.setMaxX(tempPoint.x);
//                    }
//                    if (tempPoint.x < subArea.getMinX()) {
//                        subArea.setMinX(tempPoint.x);
//                    }
//                    if (tempPoint.y > subArea.getMaxY()) {
//                        subArea.setMaxY(tempPoint.y);
//                    }
//                    if (tempPoint.y < subArea.getMinY()) {
//                        subArea.setMinY(tempPoint.y);
//                    }
//                }

                String province = removeProvinceSuffix(area.getProvince());
                String city = removeCiteSuffix(area.getCity());
                String mapKey = province + "-" + city;
                if (map.containsKey(mapKey.trim())) {
                    map.get(mapKey.trim()).add(subArea);
                } else {
                    List<Area> areaList = new ArrayList<>();
                    areaList.add(subArea);
                    map.put(mapKey.trim(), areaList);
                }
            }
            // 获取所有点
//            Coordinate[] points = wktgeom.getCoordinates();
//            for (Coordinate tempPoint : points) {
//                if (tempPoint.x > area.getMaxX()) {
//                    area.setMaxX(tempPoint.x);
//                }
//                if (tempPoint.x < area.getMinX()) {
//                    area.setMinX(tempPoint.x);
//                }
//                if (tempPoint.y > area.getMaxY()) {
//                    area.setMaxY(tempPoint.y);
//                }
//                if (tempPoint.y < area.getMinY()) {
//                    area.setMinY(tempPoint.y);
//                }
//            }

//            String province = removeProvinceSuffix(area.getProvince());
//            String city = removeCiteSuffix(area.getCity());
//            String mapKey = province + "-" + city;

//            if (map.containsKey(mapKey.trim())) {
//                map.get(mapKey.trim()).add(area);
//            } else {
//                List<Area> areaList = new ArrayList<>();
//                areaList.add(area);
//                map.put(mapKey.trim(), areaList);
//            }
        } // end for (Area area : areas)
        return map;
    }
}
