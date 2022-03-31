package com.wxxx.geoTools.util;
 
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
 
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
 
/**
 * @ClassName GetShpMsg
 * @Author
 * @Date 2020/4/23
 * @Description TODO
 * @Version 1.0
 */
public class GetShpMsg {
 
    public static void main(String[] args) throws IOException {
        String pathName = "C:\\Users\\admin\\Desktop\\NB项目\\城区(345)网格图层_20220114\\城市网格20220107_region.shp";
        File file = new File(pathName);
 
        // 读取到数据存储中
        FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
        // 获取特征资源
        SimpleFeatureSource simpleFeatureSource = dataStore.getFeatureSource();
        // 要素集合
        SimpleFeatureCollection  simpleFeatureCollection = simpleFeatureSource.getFeatures();
 
        // 要素数量
        int featureSize = simpleFeatureCollection.size();
        // 获取要素迭代器
        SimpleFeatureIterator featureIterator = simpleFeatureCollection.features();
        if(featureIterator.hasNext()){
            // 要素对象
            SimpleFeature feature = featureIterator.next();
            // 要素属性信息，名称，值，类型
            List<Property> propertyList = (List<Property>) feature.getValue();
            for(Property property : propertyList){
              System.out.println("属性名称：" + property.getName());
              System.out.println("属性值：" + property.getValue());
              System.out.println("属性类型：" + property.getType());
              System.out.println();
            }
 
            // 要素属性信息
            List<Object> featureAttributes = feature.getAttributes();
 
            // 要素geometry的类型和坐标，如点，线，面及其组成的坐标
            Object geometryText = feature.getDefaultGeometry();
 
            // geometry属性
            GeometryAttribute geometryAttribute = feature.getDefaultGeometryProperty();
            // 获取坐标参考系信息
            CoordinateReferenceSystem coordinateReferenceSystem = geometryAttribute.getDescriptor().getCoordinateReferenceSystem();
 
            // geometry类型
            GeometryType geometryType = geometryAttribute.getType();
            // geometry类型名称
            Name name = geometryType.getName();
 
            System.out.println("要素数量："+ featureSize);
            System.out.println("要素属性：" + featureAttributes);
            System.out.println("要素geometry位置信息：" + geometryText);
            System.out.println("要素geometry类型名称：" + name);
            System.out.println("shp文件使用的坐标参考系：\n" + coordinateReferenceSystem);
 
        }
 
    }
}