package org.example;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SCP {
    private static final String hostname = "invarjun0201";
    private static final String username = "root";
    private static final String password = "infa@123";
    private static final String sourceFilePath = "C:\\Users\\akanwal\\Documents\\Software\\ROOT_PEM";
    private static final String destinationFilePath = "/root/Documents/flatFile";
    private static final String fileName = "root.pem";

    public static void main(String[] args) {

        // Local Windows to Linux
        operationType("put");

        // Linux to Local Windows
//        operationType("get");
    }

    private static void operationType(String op) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            Channel channel = session.openChannel("sftp");

            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            if ("get".equals(op)) {
                sftpChannel.get(destinationFilePath + "//" + fileName, sourceFilePath + "\\" + fileName);
            } else {
                sftpChannel.put(sourceFilePath + "\\" + fileName, destinationFilePath + "//" + fileName);
            }
            sftpChannel.exit();
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
