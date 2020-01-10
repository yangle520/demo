package com.yangle.demo.util;


import com.google.common.collect.Maps;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.Map;

public class FreemarkerDemo {


    public static void main(String[] args) throws Exception {

        String temp = "<html>\n" +
                "<head>\n" +
                "    <title>欢迎  ${username}!</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>欢迎 ${username}!</h1>\n" +
                "    <h2>年龄: ${age}</h2>\n" +
                "    <h2>${id}：${username}</h2>\n" +
                "</body>\n" +
                "</html>\n";

        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");

        StringTemplateLoader loader = new StringTemplateLoader();
        loader.putTemplate("emailTemp", temp);

        configuration.setTemplateLoader(loader);

        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("id", "xxxxxx");
        dataMap.put("username", "aaa");
        dataMap.put("age", "12");

        Template t = configuration.getTemplate("emailTemp");

        StringWriter writer = new StringWriter();
        t.process(dataMap, writer);

        System.out.println(writer.toString());


    }

}