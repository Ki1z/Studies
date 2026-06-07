package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import com.sky.properties.ShopLocationProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class LocationUtil {

    private final ShopLocationProperties shopLocationProperties;

    // 高德api
    // 获取地址api
    private static final String GET_COORDINATE_URL = "https://restapi.amap.com/v3/geocode/geo";
    // 计算距离api
    private static final String GET_DISTANCE_URL = "https://restapi.amap.com/v3/distance";

    /**
     * 根据地址获取经纬度
     *
     * @param address
     * @return
     */
    public String getCoordinateByAddress(String address) {
        String key = shopLocationProperties.getKey();
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("key", key);
        urlMap.put("address", address);
        // 发送请求
        String json = HttpClientUtil.doGet(GET_COORDINATE_URL, urlMap);
        // 解析结果
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject.getInteger("status") != 1) {
            // 获取失败
            return null;
        }
        // 获取地址名称
        String formattedAddress = jsonObject.getJSONArray("geocodes").getJSONObject(0).getString("formatted_address");
        log.info("获取到的地址名称：{}", formattedAddress);
        // 获取经纬度
        String coordinate = jsonObject.getJSONArray("geocodes").getJSONObject(0).getString("location");
        log.info("获取到的经纬度：{}", coordinate);
        return coordinate;
    }

    /**
     * 计算两个经纬度之间的距离
     *
     * @param targetCoordinate
     * @return
     */
    public Long getDistance(String targetCoordinate) {
        String key = shopLocationProperties.getKey();
        // 获取店铺经纬度
        String coordinate = shopLocationProperties.getLongitude() + "," + shopLocationProperties.getLatitude();
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("key", key);
        urlMap.put("origins", coordinate);
        urlMap.put("destination", targetCoordinate);
        urlMap.put("type", "0");
        // 发送请求
        String json = HttpClientUtil.doGet(GET_DISTANCE_URL, urlMap);
        // 解析结果
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject.getInteger("status") != 1) {
            // 获取失败
            return null;
        }
        String distance = jsonObject.getJSONArray("results").getJSONObject(0).getString("distance");
        log.info("获取的距离：{}米", distance);
        return Long.valueOf(distance);
    }
}
