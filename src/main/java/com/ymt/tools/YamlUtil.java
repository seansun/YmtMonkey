package com.ymt.tools;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.ymt.entity.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by sunsheng on 2017/4/14.
 */
public class YamlUtil {

    private static final Logger logger = LoggerFactory.getLogger(YamlUtil.class);

    public static Config loadYaml() {

        //File confFile = new File(System.getProperty("user.dir") + File.separator + "src/main/resources/config.yml");

        InputStream inputStream = (YamlUtil.class.getResourceAsStream("/config.yml"));

        //logger.info(confFile.getAbsolutePath());

        Config config = null;

        try {


            //config = (Config) Yaml.loadType(confFile, Config.class);
            YamlReader reader = new YamlReader(new InputStreamReader(inputStream));
            config = reader.read(Config.class);


        } catch (Exception e) {

            logger.error("解析ymal 配置文件异常 {}", e);

        }


        return config;

    }


    public static void main(String args[]) {

        Config config = YamlUtil.loadYaml();

        logger.info("devices:{}", config.getAndroidCapability().getDeviceNames().size());

    }


}
