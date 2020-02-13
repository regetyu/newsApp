package com.java.陈敬哲;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

public class Server {
    private static ServerSocket server;
    private static Socket socket;
    private static BufferedReader is;
    private static PrintWriter os;
    private static String username;
    private static HashMap<String, String> map = new HashMap<>();

    public static void main(String[] args) {
        waitForConnection();
        close();
    }

    public static void waitForConnection() {
        try {
            server = new ServerSocket(4700);
            socket = server.accept();
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new PrintWriter(socket.getOutputStream());
            String str;
            while ((str = is.readLine()) != null) {
                if (str.equals("login")) {
                    if (login()) {
                        System.out.println("login Success!");
                        os.println("login Success!");
                        os.flush();
                    } else {
                        System.out.println("login Error!");
                        os.println("login Error!");
                        os.flush();
                    }
                } else if (str.equals("register")) {
                    if (register()) {
                        System.out.println("register Success!");
                        os.println("register Success!");
                        os.flush();
                    } else {
                        System.out.println("register Error!");
                        os.println("register Error!");
                        os.flush();
                    }
                } else if (str.equals("uploadCollectedSet")) {
                    if (uploadCollectedSet()) {
                        System.out.println("uploadCollectedSet Success!");
                        os.println("uploadCollectedSet Success!");
                        os.flush();
                    } else {
                        System.out.println("uploadCollectedSet Error!");
                        os.println("uploadCollectedSet Error!");
                        os.flush();
                    }
                }
                else if (str.equals("downloadCollectedSet")){
                    if (downloadCollectedSet()){
                        System.out.println("downloadCollectedSet Success!");
                    } else{
                        System.out.println("downloadCollectedSet Error");
                    }
                }else if (str.equals("uploadHistory")) {
                    if (uploadCollectedSet()) {
                        System.out.println("uploadHistory Success!");
                        os.println("uploadHistory Success!");
                        os.flush();
                    } else {
                        System.out.println("uploadHistory Error!");
                        os.println("uploadHistory Error!");
                        os.flush();
                    }
                }
                else if (str.equals("downloadHistory")){
                    if (downloadCollectedSet()){
                        System.out.println("downloadHistory Success!");
                    } else{
                        System.out.println("downloadHistory Error");
                    }
                }
                else if (str.equals("logout")){
                    logout();
                }
            }
        } catch (Exception e) {
            System.out.println("Connection Error!" + e);
        }
    }

    public static boolean login() {
        try {
            String user = is.readLine();
            String password = is.readLine();
            boolean flag = map.containsKey(user) && map.get(user).equals(password);
            if (flag)
                username = user;
            return flag;
        } catch (Exception e) {
            System.out.println("Login Error!" + e);
            return false;
        }
    }

    public static boolean register() {
        try {
            String username = is.readLine();
            String password = is.readLine();
            map.put(username, password);
            return true;
        } catch (Exception e) {
            System.out.println("Register Error!" + e);
            return false;
        }
    }

    public static boolean uploadCollectedSet() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream("collectedSet");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(objectInputStream.readObject());
            objectOutputStream.flush();
            return true;
        } catch (Exception e) {
            System.out.println("uploadCollectedSet Error!" + e);
            return false;
        }
    }

    public static boolean downloadCollectedSet(){
        try{
            os.println("downloadCollectedSet Success!");
            os.flush();
            FileInputStream fileInputStream = new FileInputStream("collectedSet");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(objectInputStream.readObject());
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public static boolean uploadHistory() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream("history");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(objectInputStream.readObject());
            objectOutputStream.flush();
            return true;
        } catch (Exception e) {
            System.out.println("uploadHistory Error!" + e);
            return false;
        }
    }

    public static boolean downloadHistory(){
        try{
            os.println("downloadHistory Success!");
            os.flush();
            FileInputStream fileInputStream = new FileInputStream("history");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(objectInputStream.readObject());
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public static void logout(){
        username = "";
        os.println("logout");
        os.flush();
    }

    public static void close() {
        try {
            os.close();
            is.close();
            socket.close();
            server.close();
        } catch (Exception e) {
            System.out.println("Close Error!" + e);
        }
    }

}