package yunjiankong.NewJK;

import com.aliyuncs.cms.model.v20190101.DescribeMetricListRequest;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;


public class YunSDK {

    public static Logger logger = Logger.getLogger(YunSDK.class);

    public static Map<String, String> region = new HashMap<>();
    public static Map<String, List<String>> countInfo = new HashMap<>();

    static {
        region.put("xjp", "ap-southeast-1");
        region.put("hk", "cn-hongkong");
        countInfo.put("hpx", new ArrayList<>(Arrays.asList("LTAID9aawceKCwjR", "JPGDtuS9JeChEZxQyw0lUW0j5xlRDW")));
        countInfo.put("hpy", new ArrayList<>(Arrays.asList("LTAI4FrcM7t7wno3QxiHNLXz", "C6T6tPIZ9KY50Ze2Yd18zv4fkl4rZA")));
        countInfo.put("leek", new ArrayList<>(Arrays.asList("LTAI4FmF8sACucTNdE3WY9Df", "3DGXBJ48gF2Jo2lTE4JYSS3NlmRiLL")));
        countInfo.put("coinw", new ArrayList<>(Arrays.asList("LTAIseUbaRxeHLeu", "ayoI3FUJg891bMRfimV4NVYudOy1th")));
    }

    public static DefaultProfile getDefaultProfile(String regionID, String count) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile(region.get(regionID), countInfo.get(count).get(0), countInfo.get(count).get(1));
        return profile;
    }

    // 判断获取的是cpu信息还是memory信息，然后调用setInfo方法设置信息值
    public static Instance setUseInfo(DescribeMetricListResponse response, String id, String info){
        if (response.getDatapoints().length() < 10 ){
            try {
                throw new Exception(response.getMessage() + ",the response is null");
            } catch (Exception e) {
                logger.error(e);
            }
        }
        Instance instance = new Instance(id, new HashMap<>(), new HashMap<>());
        if (info.contains("cpu")){
            setInfo(response, instance.getCpu());
        }else if(info.contains("memory")){
            setInfo(response, instance.getMemory());
        }else {
            try {
                throw new Exception("the info is not right");
            } catch (Exception e) {
                logger.error(e.getStackTrace());
            }
        }
        return instance;
    }

    // 传递从阿里云获取的reponse对象，并且将getDataoptions的字符串进行分割，封装成instance实例返回
    public static void setInfo(DescribeMetricListResponse reponse, Map<String, Map<String,String>> map) {
        // 通过Gson将字符串转换成map集合
        Gson gson = new Gson();
        Map<String, String> jsonMap = new HashMap<>();
        // 将response对象的数据进行切割，保留{}的格式
        String[] split = reponse.getDatapoints().replace("[", "").replace("]", "").split("},");
        for (int i = 0; i < split.length; i++) {
            if (i != split.length - 1) {
                // 在尾部增添被删除的 }
                split[i] = split[i] + "}";
            }
            // 将每个{}都组合成单独的新map集合
            jsonMap = gson.fromJson(split[i], jsonMap.getClass());
            logger.debug(jsonMap);
            // 获取最大值，并转换成String字符串，给下面的比较大小时做准备
            String maximum = String.valueOf(jsonMap.get("Maximum"));
            // 比较最大值与百分之90直接大小
            if (Double.valueOf(maximum) >= 80.0) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jsonMap.get("timestamp"));
                logger.debug(timestamp);
                // instance的cpu map集合存放时间字符串和map集合
                map.put(timestamp, jsonMap);
                logger.debug(map);
            }
        }
    }

    public static DescribeMetricListRequest getDescribeMetricRequest(String instanceID) {
        DescribeMetricListRequest request = new DescribeMetricListRequest();
        request.setDimensions("{\"instanceId\":\"" + instanceID + "\"}");
        logger.debug(instanceID);
        request.setStartTime("2019-09-24 11:00:00");
        request.setEndTime("2019-09-25 11:00:00");
        request.setPeriod("60");
        request.setNamespace("acs_ecs_dashboard");
        request.setMetricName("cpu_total");
        return request;
    }
}