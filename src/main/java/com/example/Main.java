package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(3000);
        while (true) {
        Socket s = ss.accept();
        System.out.println("ciao");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        DataOutputStream outBinary = new DataOutputStream(s.getOutputStream());
        
            
            String request = in.readLine();
            String[] array = request.split(" ", 3);
            String method = array[0];
            String path = array[1];
    
    
            if(!method.equals("GET")){
                out.println("HTTP/1.1 405 Method Not Allowed");
                out.println("");
                out.println("<html><body><h1>405 Method Not Allowed</h1></body></html>");
            }
            else{
                if(path.equals("/")){
                    path += "index.html";
                }
            }
            System.out.println(path);
            File file = new File("htdocs" + path);
            if (file.exists()) {
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Length: " + file.length() + "");
                out.println("Content-Type: " + getContentType(file) + "");
                out.println("");
                InputStream input = new FileInputStream(file);
                byte[] buf = new byte[8192];
                int n;
                while ((n = input.read(buf)) != -1) {
                    outBinary.write(buf, 0, n);
                }
                input.close();
            }
            else{
                out.println("HTTP/1.1 404 Not Found");
                out.println("");
                out.println("<html><body><h1>404 Not Found</h1></body></html>");
            }
            s.close();
        }

    }
    private static String getContentType(File f) {
        String fileName = f.getName().toLowerCase();
        if(fileName.endsWith(".html")){
            return "text/html";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        }
        else{
            return "application/octet-stream"; 
        }
    }
}