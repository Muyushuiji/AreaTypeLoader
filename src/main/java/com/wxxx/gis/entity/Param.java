package com.wxxx.gis.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * @program: gis-web
 * @description: 原始工参数据
 * @author: hxl
 * @create: 2022-08-22 17:08
 **/
@Data
public class Param {
//    "id",
//            "province",
//            "city",
//            "coverage_scence",
//            "coverage_type",
//            "base_station_type",
//            "base_station_name",
//            "cell_name",
//            "equipment_manufacturer",
//            "equipment_version",
//            "ECI",
//            "CGI",
//            "longitude",
//            "latitude",
//            "grid",
//            "antenna_height",
//            "azimuth",
//            "TAC",
//            "PCI",
//            "network_type",
//            "central_frequency",
//            "frequency_band",
//            "band",
//            "frequency_site",
//            "istenGE",
//            "equipment",
//            "mimo",
//            "type",
//            "parameter_optimization",
//            "beidou",
//            "one_five_eight",
//            "show_site",
//            "adopted_downlink_rate",
//            "adopted_edge_downlink_rate",
//            "adopted_uplink_rate",
//            "adopted_edge_uplink_rate",
//            "adopted_retest_date",
//            "adopted_test_result",
//            "single_check","remark",
//            "same_station_4g",
//            "station_name_4g",
//            "station_number_4g",
//            "project_time",
//            "c_ran","batch",
//            "anchor_cgi",
//            "site_type",
//            "instal_time",
//            "remoteRFdevices",
//            "gridid","id50m",
//            "cell_id",
//            "gNB_id",
//            "form_error",
//            "cell_id_error",
//            "gNB_id_16jz",
//            "gNB_id_16jz_2",
//            "cell_id_16jz",
//            "tac_16jz",
//            "tac_16jz_2",
//            "cgi_format",
//            "cgi_format_one",
//            "cell_name_format",
//            "cover_number",
//            "province_id",
//            "city_id",
//            "cgi_info",
//            "achievement",
//            "area_type"

    @CsvBindByName(column = "CGI")
    private String CGI;
    @CsvBindByName(column = "longitude")
    private Double longitude;
    @CsvBindByName(column = "latitude")
    private Double latitude;
    @CsvBindByName(column = "area_type")
    private String areaType;
    @CsvBindByName(column = "province")
    private String province;
    @CsvBindByName(column = "city")
    private String city;

}
