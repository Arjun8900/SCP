package org.example;

import com.jcraft.jsch.*;

import java.io.InputStream;

public class SCP {
    private static final String hostname = "invarjun0201";
    private static final String username = "root";
    private static final String password = "infa@123";
    private static final String sourceFilePath = "C:\\Users\\akanwal\\Documents\\Software\\ROOT_PEM";
    private static final String destinationFilePath = "/root/Documents/flatFile";
    private static final String fileName = "root.pem";

    public static void main(String[] args) {

        // Local Windows to Linux
        copyFile("put", hostname, username, password, sourceFilePath + "\\root.pem", destinationFilePath +"/root2.pem");
//        copyFile("put");

        // Linux to Local Windows
//        copyFile("get");
        String res = executeCommand("ls ~");
        System.out.println(res);
    }

    private static void copyFile(String op) {
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

    private static void copyFile(String op, String hostname, String username, String password, String sourceFilePath, String destinationFilePath) {
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
                sftpChannel.get(destinationFilePath, sourceFilePath);
            } else {
                sftpChannel.put(sourceFilePath, destinationFilePath);
            }
            sftpChannel.exit();
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String executeCommand(String command) {
        StringBuilder outputBuffer = new StringBuilder();

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);

            channel.connect();
            InputStream commandOutput = channel.getInputStream();
            int readByte = commandOutput.read();

            while(readByte != 0xffffffff)
            {
                outputBuffer.append((char)readByte);
                readByte = commandOutput.read();
            }
            channel.disconnect();
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputBuffer.toString();
    }


}
