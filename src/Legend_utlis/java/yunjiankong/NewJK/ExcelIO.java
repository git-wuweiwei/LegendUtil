package yunjiankong.NewJK;

import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelIO {

    public String path;
    public static Logger logger = Logger.getLogger(ExcelIO.class);
    public HSSFWorkbook hssfWorkbook ;

    public ExcelIO(String path) {
        this.path = path;
        try {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(path));
            this.hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public HSSFSheet creatSheet(String name){
        return this.hssfWorkbook.createSheet(name);
    }

    public void setFirstrow(HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        logger.info("begin to create row");
        row.createCell(0).setCellValue("实例ID");
        logger.info("begin to create 实例ID");
        row.createCell(0).setCellValue("时间");
        row.createCell(0).setCellValue("最大值cpuMax");
        row.createCell(0).setCellValue("平均值cpuAve");
        row.createCell(0).setCellValue("最小值cpuMin");
        row.createCell(0).setCellValue("最大值memoryMin");
        row.createCell(0).setCellValue("平均值memoryMin");
        row.createCell(0).setCellValue("最小值memoryMin");
    }

    public void setNewSheet(List<Instance> list, HSSFSheet sheet) {
//        HSSFSheet sheet = this.hssfWorkbook.createSheet(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + "cpu统计");

        try {

            this.setFirstrow(sheet);
            for (Instance instance : list) {
                this.setNewSheet(instance.getCpu(), sheet);
            }
        } catch (Exception e){
            logger.error(e);
            e.printStackTrace();
        }

    }

    public void setNewSheet(Map<String, Map<String ,String>> map ,HSSFSheet sheet) {
        for (String key : map.keySet()) {
            HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(map.get(key).get("instanceId"));
            row.createCell(1).setCellValue(key);
            row.createCell(2).setCellValue(map.get(key).get("Maximum"));
            row.createCell(3).setCellValue(map.get(key).get("Average"));
            row.createCell(4).setCellValue(map.get(key).get("Minimum"));
        }
    }

    // 增加写入新的列
    public void writeNewInfo(int sheetNum, String info, Map<String,String> map) {
        try {
//            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(this.path));
//            HSSFWorkbook sheets = new HSSFWorkbook(poifsFileSystem);
            HSSFSheet sheet = this.hssfWorkbook.getSheetAt(sheetNum);
            for (int i=1; i < sheet.getLastRowNum(); i++){
                HSSFRow row = sheet.getRow(i);
                int newcell = row.getLastCellNum() + 1;
                logger.info(newcell);
                logger.info(row.getCell(0));
                row.getCell(newcell).setCellValue(map.get(row.getCell(0)));
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public List<String> getInstanceID(int sheetNum,int cell) throws Exception {
        List<String> stringList = new ArrayList<String>();
        try {
//            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(path));
//            HSSFWorkbook sheets = new HSSFWorkbook(poifsFileSystem);
            HSSFSheet sheet = this.hssfWorkbook.getSheetAt(sheetNum);
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                stringList.add(sheet.getRow(i).getCell(cell).toString());
            }
            logger.debug(stringList);
        } catch (Exception e) {
            logger.error(e);
        }
        if (stringList.size() != 0){
            return stringList;
        }else {
            throw new Exception("the list is null");
        }
    }
}