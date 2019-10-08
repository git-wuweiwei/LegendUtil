package yunjiankong.YUNJK;

import com.aliyuncs.profile.DefaultProfile;


public class JKClient {

    /**
     * private static String regionID;
     * private static String AccesskeyID;
     * private static String SecretKeyID;
     * ap-southeast-1
     * cn-hongkong
     */
    private static DefaultProfile defaultProfile;

    public static DefaultProfile getDefaultProfile(String name) throws Exception {
        if (name == "hpx") {
            defaultProfile = DefaultProfile.getProfile("ap-southeast-1", "LTAID9aawceKCwjR", "JPGDtuS9JeChEZxQyw0lUW0j5xlRDW");
        } else if (name == "hpy") {
            defaultProfile = DefaultProfile.getProfile("ap-southeast-1", "LTAI4FrcM7t7wno3QxiHNLXz", "C6T6tPIZ9KY50Ze2Yd18zv4fkl4rZA");
        } else if (name == "leek") {
            defaultProfile = DefaultProfile.getProfile("cn-hongkong", "LTAI4FmF8sACucTNdE3WY9Df", "3DGXBJ48gF2Jo2lTE4JYSS3NlmRiLL");
        } else if (name == "coinw"){
            defaultProfile = DefaultProfile.getProfile("cn-hongkong","LTAIseUbaRxeHLeu","ayoI3FUJg891bMRfimV4NVYudOy1th");
        } else {
            throw new Exception("the projectname is not right");
        }
        return defaultProfile;
    }
}
