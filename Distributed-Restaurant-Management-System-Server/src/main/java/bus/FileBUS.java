package bus;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileBUS extends Remote {
    public void uploadFileToServer(byte[] mybyte, String serverpath, int length) throws RemoteException;
    public byte[] downloadFileFromServer(String servername) throws RemoteException;

    public String[] listFiles(String serverpath) throws RemoteException;
}
