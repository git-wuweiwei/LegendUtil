package yunjiankong.NewJK;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Instance {

    private String id;
    private static Date timestamp;
    private Map<String, Map<String,String>> cpu;
    private Map<String, Map<String,String>> memory;

    public Instance(String id, Map<String, Map<String, String>> cpu, Map<String, Map<String, String>> memory) {
        this.id = id;
        this.cpu = cpu;
        this.memory = memory;
    }

    public Instance(String id) {
        this.id = id;
    }

    static {

    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public static Date getTimestamp() {
        return timestamp;
    }

    public static void setTimestamp(Date timestamp) {
        Instance.timestamp = timestamp;
    }

    public Map<String, Map<String,String>> getCpu() {
        return cpu;
    }

    public void setCpu(Map<String, Map<String,String>> cpu) {
        this.cpu = cpu;
    }

    public Map<String, Map<String,String>> getMemory() {
        return memory;
    }

    public void setMemory(Map<String, Map<String,String>> memory) {
        this.memory = memory;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "id='" + id + '\'' +
                ", cpu=" + cpu +
                ", memory=" + memory +
                '}';
    }
}