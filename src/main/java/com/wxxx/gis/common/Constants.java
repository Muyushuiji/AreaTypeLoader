package com.wxxx.gis.common;

import java.io.File;

/**
 * Constants
 */
public final class Constants {
    // ---------------------------------------
    // 运行模式，web模式和local模式
    public static final String RUN_MODE_WEB = "web";
    public static final String RUN_MODE_LOCAL = "local";
    // 线程模式，单线程模式和多线程模式
    public static final String THREAD_MODE_MULTI_THREAD = "multi-thread";
    public static final String THREAD_MODE_SINGLE_THREAD = "single thread";

    // ---------------------------------------
    // 结果码，后面有时间改枚举
    public static final String RETURN_SUCCESS = "success";
    public static final String RETURN_FAILED = "failed";
    // 任务状态
    public static final String OPENING = "开站中";
    public static final String OPEN_SUPPORT = "可以开站";
    public static final String OPEN_SUCCEED = "开站成功";
    public static final String OPEN_FAILED = "开站失败";
    public static final String OPEN_MANAGE_RELOAD = "综资数据录入成功";
    public static final String OPEN_MANAGE_SYN = "需同步综资数据";
    public static final String OPEN_MANAGE_SYNING = "综资同步中";
    public static final String OPEN_MANAGE_SYN_FILED = "综资同步失败";
    public static final String OPEN_MANAGE_SYN_SUCCESS = "综资同步成功";

    public static final String STACK_CHECK_SUCCESS = "自检成功";
    public static final String STACK_CHECKING = "基站自检中";
    public static final String STACK_CHECK_OVER= "自检完成";
    //扩容任务
    public static final String ISSUE_SUCCESS = "扩容回滚请求下发成功";
    public static final String GET_SOURCE_SUCCESS = "原始站点数据获取成功";
    public static final String EXPANSION_RETURN_SUCCESS = "扩容回调完成";
    public static final String EXPANSION_OPEN_SUCCESS = "扩容成功";
    public static final String EXPANSION_OPEN_FAILED = "扩容失败";
    // request code
    public static final int REQUEST_SUCCESS = 200;
    public static final int REQUEST_FAILED = 500;
    public static final int REQUEST_MESSAGE_MAX = 128;

    //北京综资对接返回码
    public static final int BJ_REQUEST_SUCCESS = 0;
    public static final int BJ_REQUEST_FAILED = 1;
    // aos vendor
    public static final String VENDOR_HW = "华为";
    public static final String VENDOR_ZX = "中兴";
    public static final String VENDOR_DT = "大唐";
    public static final String VENDOR_ALX = "爱立信";
    public static final String VENDOR_NJY = "诺基亚";

    // 模板文件适用场景
    public static final String TEMP_FILE_SCENE_EXPANSION = "扩容";
    public static final String TEMP_FILE_SCENE_NEW5G = "新开5G基站";
    public static final String TEMP_FILE_SCENE_NEW45G = "新开4+5G基站";


    //北京综资对接参数测试平台
    public final static String DEVELOP_PLAN_URL = "https://10.224.8.49:11088/ServiceOpenFrame/gate";

    public final static String DEVELOP_APP_KEY = "94e5ad8b-3f19-4e85-ac1e-550964ef658d";

    public final static String DEVELOP_APP_SECRET = "jKXtYyGSaqnGbbBGLpli";


    public final static String PRODUCT_PLAN_URL = "https://10.224.10.92:11088/ServiceOpenFrame/gate";
    //交割环境
    public final static String PRODUCT_APP_KEY = "1fe20023-538e-437e-bfed-d017cc0cd02f";

    public final static String PRODUCT_APP_SECRET = "4zpuR4jijjttpMZO62Y7";

    public final static String PRODUCT_METHOD = "inspur.auto_open_return";

    public final static String PRODUCT_V = "1";
    //北京图像识别EID的路径
    public final static String SEND_EID_IMAGE_URL = "http://172.30.228.60:8089/tag";
    //北京图像识别板卡信息接口地址
    public final static String SEND_BAND_IMAGE_URL = "http://172.30.228.60:8089/maskrcnn";

    public final static String DEFAULT_CONFIG_CACHE = "defaultConfig";

}
