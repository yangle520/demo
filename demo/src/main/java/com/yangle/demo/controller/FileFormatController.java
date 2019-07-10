package com.yangle.demo.controller;

import com.google.common.collect.ArrayListMultimap;
import com.yangle.demo.model.SDetailDto;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/uco")
public class FileFormatController {

    @RequestMapping("/test/file")
    public boolean formatFile() {

        try {
            InputStream inputStream = new FileInputStream("D:\\各节点组件(1).xlsx");
            XSSFWorkbook sourceFile = new XSSFWorkbook(inputStream);

            // key -> 镜像名称 ，value -> 版本，节点
            ArrayListMultimap<String, SDetailDto> map = ArrayListMultimap.create();

            // 1 - 13 页
            for (int i = 1; i <= 13; i++) {
                XSSFSheet sheetAt = sourceFile.getSheetAt(i);
                String zone = sheetAt.getSheetName();
                for (Row row : sheetAt) {
                    String line = row.getCell(0).getStringCellValue();
                    String[] str = line.split(":");
                    String name = str[1];
                    String version = str[2];
                    SDetailDto d = new SDetailDto(zone, version);
                    map.put(name, d);
                }
            }

            // 写入新文件
            HSSFWorkbook targetFile = new HSSFWorkbook();
            HSSFSheet sheet = targetFile.createSheet("UCA版本对比");
            HSSFRow titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("镜像名称");
            titleRow.createCell(1).setCellValue("镜像版本");
            titleRow.createCell(2).setCellValue("节点");

            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 10000);
            sheet.setColumnWidth(2, 3000);

            List<String> nameList = map.keySet().stream().sorted(Comparator.comparing(t -> t)).collect(Collectors.toList());

            // 与上一个单元格内容相同就不写入
            String lastName = "";
            String lastVersion = "";
            String lastZone = "";
            for (String name : nameList) {
                List<SDetailDto> details = map.get(name).stream().sorted(Comparator.comparing(SDetailDto::getVersion)).collect(Collectors.toList());
                for (SDetailDto d : details) {
                    int lastRowNum = sheet.getLastRowNum();
                    HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
                    if (!lastName.equals(name)) {
                        dataRow.createCell(0).setCellValue(name);
                        lastName = name;
                    }
                    if (!lastVersion.equals(d.getVersion())) {
                        dataRow.createCell(1).setCellValue(d.getVersion());
                        lastVersion = d.getVersion();
                    }
                    if (!lastZone.equals(d.getZone())) {
                        dataRow.createCell(2).setCellValue(d.getZone());
                        lastZone = d.getZone();
                    }
                }
            }

            FileOutputStream fout = new FileOutputStream("D:\\UCA版本对比_3.xls");
            targetFile.write(fout);
            fout.close();
            sourceFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }
}
