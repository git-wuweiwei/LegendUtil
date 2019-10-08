package yunjiankong.YUNJK;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListRequest;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;

import java.io.FileInputStream;
import java.util.*;

public class HPXController {

    public static Map<String, String> getUseInfo(String instanceID, String startTime, String endTime, String info) {
        Map<String, String> useInfoMap = new HashMap<>();
        IAcsClient client = null;
        try {
            client = new DefaultAcsClient(JKClient.getDefaultProfile("leek"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String Maximum = null;
        String Minimum = null;
        String Average = null;
        DescribeMetricListRequest request = new DescribeMetricListRequest();
        request.setDimensions("{\"instanceId\":\"" + instanceID + "\"}");
        request.setEndTime(endTime);
        request.setStartTime(startTime);
        request.setPeriod("86400");
        request.setNamespace("acs_ecs_dashboard");
        request.setMetricName(info);
        try {
            DescribeMetricListResponse response = client.getAcsResponse(request);
            if (response.getDatapoints() == null) {
                ExcelIOP.logger.info("the " + instanceID + " response is null");
                useInfoMap.put("Average", "获取为空");
            }
            String[] strings = response.getDatapoints().split(",");
            List<String> strings1 = Arrays.asList(strings);
            ListIterator<String> stringListIterator = strings1.listIterator();
            while (stringListIterator.hasNext()) {
                String next = stringListIterator.next();
                if (next.contains("Maximum")) {
                    Maximum = getListValue(next);
                } else if (next.contains("Minimum")) {
                    Minimum = getListValue(next);
                } else if (next.contains("Average")) {
                    Average = getListValue(next);
                }
            }
            useInfoMap.put("Maximum", Maximum);
            useInfoMap.put("Average", Average);
            useInfoMap.put("Minimum", Minimum);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return useInfoMap;
    }

    public static Map<String, Map<String, String>> getInstaceInfo(List<String> instanceIDs, String startTime, String endTime, String info) {
        Map<String, Map<String, String>> instanceValueMap = new HashMap<>();
        ListIterator<String> stringListIterator = instanceIDs.listIterator();
        while (stringListIterator.hasNext()) {
            String instanceID = stringListIterator.next();
            Map<String, String> useInfoMap = getUseInfo(instanceID, startTime, endTime, info);
            instanceValueMap.put(instanceID, useInfoMap);
        }
        return instanceValueMap;
    }

    public static String getListValue(String next) throws Exception {
        String value = null;
        if (next != null && next.contains(":")) {
            String[] split = next.replace("}]", "").split(":");
            value = split[1];
            return value;
        } else {
            throw new Exception("next is not applied to the value");
        }
    }

    public static void main(String[] args) {
        String startTime = "2019-09-07 11:00:00";
        String endTime = "2019-09-20 14:00:00";
        Map<String, Map<String, String>> instaceInfo = null;
        String path = "D:\\java\\IdeaProject\\aliyun_LogService\\src\\Legend_utlis\\java\\yunjiankong\\leek_ecs_2019-09-20.xls";
        String[] infos = {"cpu_total", "memory_usedutilization"};
        try {
            List<String> intanceIds = ExcelIOP.getIntanceIds(new FileInputStream(path));
            // info:
            // cpu_total cpu使用总量 单位：%
            // memory_actualusedspace : 用户实际使用内存总量 单位：byte
            for (int i = 0; i < infos.length; i++) {
                instaceInfo = getInstaceInfo(intanceIds, startTime, endTime, infos[i]);
                ExcelIOP.logger.info(instaceInfo);
                ExcelIOP.writeInfoToExcel(path, infos[i].substring(0, 3), instaceInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
