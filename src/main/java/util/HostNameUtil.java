package util;

import lombok.Getter;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostNameUtil {
    public static final int PORT = 9090;
    public static String  getHostName() throws Exception {
        return InetAddress.getLocalHost().getHostName();
    }
    public static String getURI(String hostName, Class aClass) throws Exception {
        return String.format("rmi://%s:%d/%s", hostName, PORT, aClass.getSimpleName());
    }


}
