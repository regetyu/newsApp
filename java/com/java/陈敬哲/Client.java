package com.java.陈敬哲;

import java.io.*;
import java.net.*;
import java.util.*;


public class Client {
    private static Socket socket;
    private static PrintWriter os;
    private static BufferedReader is;
    public static boolean isLogedin;
    public static boolean isRegistered;

    public static void connect(String ip){
        try{
            socket = new Socket(ip, 4700);
            os = new PrintWriter(socket.getOutputStream());
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (Exception e){
            System.out.println("Connection Error! " + e);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String str;
                            while((str = is.readLine()) != null){
                        System.out.println(str);
                        if (str.equals("login Success!")){
                            isLogedin = true;
                        }
                        else if (str.equals("register Success!")){
                            isRegistered = true;
                        }
                        else if (str.equals("downloadCollectedSet Success!")){
                            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                            CollectedSet.save(objectInputStream.readObject());
                        }else if (str.equals("downloadHistory Success!")){
                            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                            History.save(objectInputStream.readObject());
                        }else if (str.equals("logout")){
                            isLogedin = false;
                        }
                    }
                    close();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        }).start();
    }

    public static void login(String username, String password){
        try{
            os.println("login");
            os.flush();
            os.println(username);
            os.flush();
            os.println(password);
            os.flush();
        }catch (Exception e){
            System.out.println("Login Error!" + e);
        }
    }

    public static void register(String username, String password){
        try{
            os.println("register");
            os.flush();
            os.println(username);
            os.flush();
            os.println(password);
            os.flush();
        }catch (Exception e){
            System.out.println("Register Error! " + e);
        }
    }

    public static void close(){
        try{
            os.close();
            is.close();
            socket.close();
        }catch (Exception e){
            System.out.println("Close Error! " + e);
        }
    }

    public static void uploadCollectedSet(){
        try{
            os.println("uploadCollectedSet");
            os.flush();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(CollectedSet.get());
            objectOutputStream.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void DownloadCollectedSet(){
        os.println("downloadCollectedSet");
        os.flush();
    }

    public static void uploadHistory(){
        try{
            os.println("uploadHistory");
            os.flush();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(History.get());
            objectOutputStream.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void DownloadHistory(){
        os.println("downloadHistory");
        os.flush();
    }

    public static void logut(){
        os.println("logout");
        os.flush();
    }
}
