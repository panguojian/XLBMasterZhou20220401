package com.example.xlb.socket;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.xlb.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TcpServer {
    private String TAG = "TcpServer";
    private int port = 8080;
    private boolean isListen = true;   //线程监听标志位


    public static Handler ServerHandler;

    public TcpServer(int port){
        this.port = port;
    }





}


