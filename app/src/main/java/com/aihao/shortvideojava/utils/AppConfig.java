package com.aihao.shortvideojava.utils;

import android.content.res.AssetManager;

import com.aihao.libcommon.global.AppGlobals;
import com.aihao.shortvideojava.model.BottomBar;
import com.aihao.shortvideojava.model.Destination;
import com.aihao.shortvideojava.model.SofaTab;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class AppConfig {
    private static  HashMap<String, Destination> sDestConfig;

    private  static BottomBar sBottomBar;
    private static SofaTab sSofaTab, sFindTabConfig;

    public static HashMap<String, Destination> getDestConfig() {
        if (sDestConfig == null){
            String content = parseFile("destination.json");
            sDestConfig = JSON.parseObject(content, new TypeReference<HashMap<String,Destination>>(){}.getType());
        }
        return  sDestConfig;
    }

    public  static  BottomBar getsBottomBar () {
        if(sBottomBar == null) {
            String content = parseFile("main_tabs_config.json");
            sBottomBar = JSON.parseObject(content,BottomBar.class);
        }
        return  sBottomBar;
    }

    public static SofaTab getSofaTabConfig() {
        if (sSofaTab == null) {
            String content = parseFile("sofa_tabs_config.json");
            sSofaTab = JSON.parseObject(content, SofaTab.class);
            Collections.sort(sSofaTab.tabs, new Comparator<SofaTab.Tabs>() {
                @Override
                public int compare(SofaTab.Tabs o1, SofaTab.Tabs o2) {
                    return o1.index < o2.index ? -1 : 1;
                }
            });
        }
        return sSofaTab;
    }

    public static SofaTab getFindTabConfig() {
        if (sFindTabConfig == null) {
            String content = parseFile("find_tabs_config.json");
            sFindTabConfig = JSON.parseObject(content, SofaTab.class);
            Collections.sort(sFindTabConfig.tabs, new Comparator<SofaTab.Tabs>() {
                @Override
                public int compare(SofaTab.Tabs o1, SofaTab.Tabs o2) {
                    return o1.index < o2.index ? -1 : 1;
                }
            });
        }
        return sFindTabConfig;
    }
    private static  String parseFile(String fileName) {
        AssetManager assets = AppGlobals.getApplication().getResources().getAssets();
        InputStream stream = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();;
        try {
            stream = assets.open(fileName);
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if (stream != null) {
                    stream.close();
                }

                if (reader != null) {
                    reader.close();
                }

            } catch (Exception e){

            }
        }

        return  builder.toString();
    }
}
