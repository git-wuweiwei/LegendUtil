package coinLogUtil;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.*;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.*;
import com.aliyun.openservices.log.response.*;
import org.apache.http.impl.conn.Wire;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class logTest {

    public static void main(String[] args) throws LogException, InterruptedException {
        String access_key = "LTAIseUbaRxeHLeu";
        String serect_key = "ayoI3FUJg891bMRfimV4NVYudOy1th";
        String endpoint = "http://ap-southeast-1.log.aliyuncs.com";

        String project = "waf-project-1783799610063532-ap-southeast-1";
        String logstore = "waf-logstore";

        Client client = new Client(endpoint, access_key, serect_key);

        int offset = 0;
        int size = 100;
        String logSotreSubName = "";

        ListLogStoresRequest req1 = new ListLogStoresRequest(project, offset, size, logSotreSubName);
        ArrayList<String> logStores = client.ListLogStores(req1).GetLogStores();
//        System.out.println(logStores.toString());


        // 写入日志
//        String topic = "";
//        String source = "";
//
//        for (int i = 0; i < 10; i++) {
//            Vector<LogItem> logGroup = new Vector<LogItem>();
//            for (int j = 0; j < 10; j++) {
//                LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
//                logItem.PushBack("index" + String.valueOf(j), String.valueOf(i * 10 + j));
//                logGroup.add(logItem);
//            }
//            PutLogsRequest req2 = new PutLogsRequest(project,logstore,topic,source,logGroup);
//            client.PutLogs(req2);
//        }
//        int shard_id = 0;
//        long curTimesec = System.currentTimeMillis() / 1000;
//        GetCursorResponse cursorRes = client.GetCursor(project, logstore, shard_id, curTimesec - 60);
//        String beginCursor = cursorRes.GetCursor();
//        cursorRes = client.GetCursor(project, logstore, shard_id, Consts.CursorMode.END);
//        String endCursor = cursorRes.GetCursor();
//
//        String curCursor = beginCursor;
//
//        while (curCursor.equals(endCursor) == false){
//            int loggroup_count = 2;
//            BatchGetLogResponse logDataRes = client.BatchGetLog(project, logstore, shard_id, loggroup_count,curCursor,endCursor);
//
//            List<LogGroupData> logGroups = logDataRes.GetLogGroups();
//            for (LogGroupData logGroup : logGroups){
//                FastLogGroup flg = logGroup.GetFastLogGroup();
//                System.out.println(String.format("\tcategory\t:\t%s\n\tsource\t:\t%s\n\ttopic\t:\t%s\n\tmachineUUID\t:\t%s",
//                        flg.getCategory(), flg.getSource(), flg.getTopic(), flg.getMachineUUID()));
//                System.out.println("Tags");
//                for (int tagIdx = 0; tagIdx < flg.getLogTagsCount(); ++tagIdx) {
//                    FastLogTag logtag = flg.getLogTags(tagIdx);
//                    System.out.println(String.format("**************"+"\t%s\t:\t%s", logtag.getKey(), logtag.getValue()));
//                }
//                for (int lIdx = 0; lIdx < flg.getLogsCount(); ++lIdx) {
//                    FastLog log = flg.getLogs(lIdx);
//                    System.out.println("--------\nLog: " + lIdx + ", time: " + log.getTime() + ", GetContentCount: " + log.getContentsCount());
//                    for (int cIdx = 0; cIdx < log.getContentsCount(); ++cIdx) {
//                        FastLogContent content = log.getContents(cIdx);
//                        System.out.println(content.getKey() + "\t:\t" + content.getValue());
//                    }
//                }
//            }


//        }
        String topic = "";
        // 查询日志分布情况
        String query = "";
        int from = (int) (new Date().getTime() / 1000 - 300);
        int to = (int) (new Date().getTime() / 1000);
        GetHistogramsResponse res3 = null;
        while (true) {
            GetHistogramsRequest req3 = new GetHistogramsRequest(project, logstore, topic, query, from, to);
            res3 = client.GetHistograms(req3);
            if (res3 != null && res3.IsCompleted()) // IsCompleted() 返回
            // true，表示查询结果是准确的，如果返回
            // false，则重复查询
            {
                break;
            }
            Thread.sleep(200);
        }
        System.out.println("Total count of logs is " + res3.GetTotalCount());
        for (Histogram ht : res3.GetHistograms()) {
            System.out.printf("from %d, to %d, count %d.\n", ht.GetFrom(), ht.GetTo(), ht.GetCount());
        }

        // 查询日志数据
        query = "__topic__: waf_access_log and (block_action:* and not block_action: \"\") | SELECT ip_to_country(if(real_client_ip='-', remote_addr, real_client_ip)) as country, count(1) as \"Attack count\"  group by country";
        long total_log_lines = res3.GetTotalCount();
        int log_offset = 0;
        int log_line = 10;//log_line 最大值为100，每次获取100行数据。若需要读取更多数据，请使用offset翻页。offset和lines只对关键字查询有效，若使用SQL查询，则无效。在SQL查询中返回更多数据，请使用limit语法。
        while (log_offset <= total_log_lines) {
            GetLogsResponse res4 = null;
            // 对于每个 log offset,一次读取 10 行 log，如果读取失败，最多重复读取 3 次。
            for (int retry_time = 0; retry_time < 3; retry_time++) {
                GetLogsRequest req4 = new GetLogsRequest(project, logstore, from, to, topic, query, log_offset,
                        log_line, false);
                res4 = client.GetLogs(req4);
                if (res4 != null && res4.IsCompleted()) {
                    break;
                }
                Thread.sleep(200);
            }
//            System.out.println("Read log count:" + String.valueOf(res4.GetCount()));
            log_offset += log_line;
            System.out.println(res4.getTerms());
        }

        //打开分析功能,只有打开分析功能，才能使用SQL 功能。 可以在控制台开通分析功能，也可以使用SDK开启分析功能
//        IndexKeys indexKeys = new IndexKeys();
//        ArrayList<String> tokens = new ArrayList<String>();
//        tokens.add(",");
//        tokens.add(".");
//        tokens.add("#");
//        IndexKey keyContent = new IndexKey(tokens,false,"text");
//        indexKeys.AddKey("index0",keyContent);
//        keyContent = new IndexKey(new ArrayList<String>(),false,"long");
//        indexKeys.AddKey("index1",keyContent);
//        keyContent = new IndexKey(new ArrayList<String>(),false,"double");
//        indexKeys.AddKey("index2",keyContent);
//        IndexLine indexLine = new IndexLine(new ArrayList<String>(),false);
//        Index index = new Index(7,indexKeys,indexLine);
//        CreateIndexRequest createIndexRequest = new CreateIndexRequest(project,logstore,index);
//        client.CreateIndex(createIndexRequest);
//        //使用分析功能
//        GetLogsRequest req4 = new GetLogsRequest(project, logstore, from, to, "", " index0:value | select avg(index1) as v1,sum(index2) as v2, index0 group by index0");
//        GetLogsResponse res4 = client.GetLogs(req4);
//        if(res4 != null && res4.IsCompleted()){
//            for (QueriedLog log : res4.GetLogs()){
//                LogItem item = log.GetLogItem();
//                for(LogContent content : item.GetLogContents()){
//                    System.out.print(content.GetKey()+":"+content.GetValue());
//                }
//                System.out.println();
//            }
//        }
    }




}
