package yunjiankong.YUNJK;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelIOP {

    public static Logger logger = Logger.getLogger(ExcelIOP.class);

    public static List<String> getIntanceIds(FileInputStream file) throws Exception {
        List<String> stringList = new ArrayList<>();
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(file);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
        HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
        for (int i = 1; i < sheetAt.getLastRowNum(); i++) {
            stringList.add(sheetAt.getRow(i).getCell(0).toString());
        }
        return stringList;
    }

    public static void writeInfoToExcel(String path, String info, Map<String, Map<String, String>> Map) {
        /**
         * @path
         * @info
         * @Map
         */
        HSSFWorkbook hssfWorkbook = null;
        FileOutputStream fileOutputStream = null;
        try {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(path));
            fileOutputStream = new FileOutputStream(path);
            // 获取ecel解析页
            hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
            // 获取第一个页签
            HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
            // 获取页签的第一行
            HSSFRow row = sheetAt.getRow(0);
            int cpuNum = 0;
            // 在第一行中找到cpu的那列
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).toString().contains(info)) {
                    cpuNum = i;
                    logger.info(i);
                }
            }
            for (int i = 1; i < sheetAt.getLastRowNum(); i++) {
                HSSFRow row1 = sheetAt.getRow(i);
                String id = row1.getCell(0).toString();
                if (Map.get(id).get("Average") != null) {
                    try {
                        String average = Map.get(id).get("Average").substring(0, 4);
                        logger.debug(average);
                        if (row1.getCell(cpuNum) == null) {
                            row1.createCell(cpuNum).setCellValue(average);
                        }
                        row1.getCell(cpuNum).setCellValue(average);
                    } catch (NullPointerException e) {
                        logger.error(e.getStackTrace());
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        } finally {
            try {
                hssfWorkbook.write(fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void add() {
        int neetNum = 0;
        try {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream("D:\\java\\IdeaProject\\aliyun_LogService\\src\\Legend_utlis\\java\\yunjiankong\\accessKey.xlsx"));
            FileOutputStream outputStream = new FileOutputStream("D:\\java\\IdeaProject\\aliyun_LogService\\src\\Legend_utlis\\java\\yunjiankong\\accessKey.xls");
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
            HSSFSheet sheet = hssfWorkbook.getSheetAt(1);
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 0; i <= lastRowNum; i++) {
                HSSFRow row = sheet.getRow(i);
                int lastCellNum = row.getLastCellNum();
                System.out.println("lastcellnum " + lastCellNum);
                for (int j = 0; j <= lastCellNum; j++) {
                    Cell cell = null;
                    if (j == 3) {
                        cell = row.createCell(j);
                        cell.setCellValue(j + i + 1);
                        System.out.println(".......................");
                    } else {
                        cell = row.getCell(j);
                    }
                    if (cell.toString() == "AccessKeyId") {
                        neetNum = j;
                    }
                    System.out.println(cell.toString());
                }
            }
            hssfWorkbook.write(outputStream);
            outputStream.close();
            System.out.println("the num of neetNum is " + neetNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
