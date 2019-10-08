package coinLogUtil;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.LogContent;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.GetHistogramsRequest;
import com.aliyun.openservices.log.request.GetLogsRequest;
import com.aliyun.openservices.log.response.GetHistogramsResponse;
import com.aliyun.openservices.log.response.GetLogsResponse;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.Date;

public class LogQueryTest {


    public static void main(String[] args) throws LogException, InterruptedException {
        Object object = null;
        String access_key = "LTAIseUbaRxeHLeu";
        String serect_key = "ayoI3FUJg891bMRfimV4NVYudOy1th";
        String endpoint = "http://ap-southeast-1.log.aliyuncs.com";

        String project = "waf-project-1783799610063532-ap-southeast-1";
        String logstore = "waf-logstore";
//        Client client = LogServiceClient.getLogClient();
        Client client = new Client(endpoint, access_key, serect_key);
        int From = (int) (new Date().getTime() / 1000 - 300);
        int To = (int) (new Date().getTime() / 1000);

        int offset = 0;
        int size = 100;
        String logSotreSubName = "";

        String topic = "";
        String source = "";

        String query = "__topic__: waf_access_log | SELECT ip_to_country(if(real_client_ip='-', remote_addr, real_client_ip)) as country, count(1) as accesstimes group by country";
        GetHistogramsResponse rep1 = null;

        while (true) {
            GetHistogramsRequest req1 = new GetHistogramsRequest(project, logstore, topic, query, From, To);
            rep1 = client.GetHistograms(req1);
            if (rep1 != null && rep1.IsCompleted()) {
                break;
               }
            Thread.sleep(200);
        }
        long total_log_lines = rep1.GetTotalCount();
        int log_offset = 0;
        int log_line = 10;
        GetLogsResponse rep2 = null;



        for (int retry_time = 0; retry_time < 3; retry_time++){
            GetLogsRequest req2 = new GetLogsRequest(project, logstore, From, To ,topic, query,log_offset,log_line,false);
            rep2 = client.GetLogs(req2);
            if (rep2!= null && rep2.IsCompleted()){
                break;
            }
            Thread.sleep(200);
        }

        for ( QueriedLog log : rep2.GetLogs()){
            LogItem logItem = log.GetLogItem();
//            System.out.println(logItem);
            LogUtils.logger.info(logItem);
        }
    }
}