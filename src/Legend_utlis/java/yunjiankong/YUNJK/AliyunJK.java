package yunjiankong.YUNJK;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListRequest;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListResponse;

import java.util.*;

public class AliyunJK {

    public static void main(String[] args) {
        getUseInfo("i-t4namhfewng2kzwxvjyv","2019-09-17 09:00:00","2019-09-18 09:00:00","memory_actualusedspace");
    }

    public static void getUseInfo(String instanceID, String startTime, String endTime, String info) {
        Map<String, String> useInfoMap = new HashMap<>();
        IAcsClient client = null;
        try {
            client = new DefaultAcsClient(JKClient.getDefaultProfile("hpy"));
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
            ExcelIOP.logger.info(response.getDatapoints());
            System.out.println(response.getDatapoints());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
