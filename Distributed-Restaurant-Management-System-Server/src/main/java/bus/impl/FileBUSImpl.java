package bus.impl;

import bus.FileBUS;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileBUSImpl extends UnicastRemoteObject implements FileBUS {

    //    private String imgPath =  System.getProperty("user.dir") + "/img";
//
//    public FileBUSImpl() throws RemoteException {
//
//        String currPath = System.getProperty("user.dir");
//        File storageDir = new File(currPath + "/img");
//        storageDir.mkdir();
//    }
    private String imgPath;
    private final String USER_DIR = System.getProperty("user.dir");

    public FileBUSImpl(String savedfolder) throws RemoteException {
        this.imgPath = USER_DIR + savedfolder;
        File storageDir = new File(this.imgPath);
        storageDir.mkdir();
    }

    @Override
    public void uploadFileToServer(byte[] mybyte, String fileName, int length) throws RemoteException {
        File serverFilePath = new File(imgPath + fileName);
        try {
            FileOutputStream out = new FileOutputStream(serverFilePath);
            byte[] fileData = mybyte;

            out.write(mybyte);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("img added to server!");
    }

    @Override
    public byte[] downloadFileFromServer(String servername) throws RemoteException {
        byte[] mydata;
        File serverFilePath = new File(USER_DIR + servername);
        mydata = new byte[(int) serverFilePath.length()];
        FileInputStream in;
        try {
            in = new FileInputStream(serverFilePath);
            try {
                in.read(mydata, 0, mydata.length);
            } catch (IOException e) {

                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        return mydata;
    }

    @Override
    public String[] listFiles(String serverpath) throws RemoteException {
        File serverFilePath = new File(USER_DIR + serverpath);
        return serverFilePath.list();
    }
}
