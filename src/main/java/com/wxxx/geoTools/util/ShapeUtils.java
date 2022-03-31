package com.wxxx.geoTools.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;

/**
 * Shape文件操作公共类。
 * @author elon
 * @version 2018年6月24日
 */
public class ShapeUtils {
    
    /**
     * 获取Shape文件的坐标系信息。
     * 
     * @param shpFilePath shp文件路径
     * @return 坐标系的WKT形式
     */
    public static String getCoordinateSystemWKT(String shpFilePath) {

        ShapefileDataStore dataStore = buildDataStore(shpFilePath);

        try {
            return dataStore.getSchema().getCoordinateReferenceSystem().toWKT();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dataStore.dispose();
        }
        return "";
    }
    
    /**
     * 构建ShapeDataStore对象。
     * @param shpFilePath shape文件路径。
     * @return
     */
    public static ShapefileDataStore buildDataStore(String shpFilePath) {
        ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
        try {
            ShapefileDataStore dataStore = (ShapefileDataStore) factory
                    .createDataStore(new File(shpFilePath).toURI().toURL());
            if (dataStore != null) {
                dataStore.setCharset(Charset.forName("UTF-8"));
            }
            return dataStore;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

