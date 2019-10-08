package yunjiankong.NewJK;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListRequest;
import com.aliyuncs.cms.model.v20190101.DescribeMetricListResponse;
import com.aliyuncs.profile.DefaultProfile;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class YunGet {

    public static Logger logger = Logger.getLogger(YunSDK.class);

    public static void main(String[] args) {
        ExcelIO excelIO = new ExcelIO("./src/Legend_utlis/java/yunjiankong/HPY_2019-09-20.xls");
        IAcsClient client = null;
        HSSFSheet sheet = excelIO.creatSheet(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + "cpu统计");
        List<Instance> list = new ArrayList<>();
        try {
            List<String> ListinstanceID = excelIO.getInstanceID(0, 0);
            DefaultProfile profile = YunSDK.getDefaultProfile("xjp", "hpy");
            client = new DefaultAcsClient(profile);
            DescribeMetricListRequest request;
            DescribeMetricListResponse reponse;
            for (String instanceID : ListinstanceID) {
                if (instanceID.contains("实例")) {
                    continue;
                }
                logger.debug("the really instanceID is :" + instanceID);
                request = YunSDK.getDescribeMetricRequest(instanceID);
                reponse = client.getAcsResponse(request);
                if (reponse.getDatapoints().length() < 10) {
                    continue;
                }
                logger.debug(reponse.getDatapoints());
                Instance instance = YunSDK.setUseInfo(reponse, instanceID, "cpu");
                logger.debug(instance);
                list.add(instance);
                logger.debug(reponse.getMessage());
//HSSFSheet sheet = this.hssfWorkbook.createSheet(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + "cpu统计");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            e.printStackTrace();
        }
        for (Instance instance : list) {
            System.out.println();
//            System.out.println(instance.getId() + " : " + instance.getCpu());
            System.out.println("******************   " + instance.getId() + "   ******************");
            if (instance.getCpu().size() == 0) {
                System.out.println("this " + instance.getId() + " high cpu is null;");
            }
            for (String key : instance.getCpu().keySet()) {
                System.out.println(key + ": " + String.valueOf(instance.getCpu().get(key).get("Maximum")));
            }
            System.out.println();
        }
    }
}