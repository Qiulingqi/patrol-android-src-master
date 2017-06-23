package com.library.ui.application;

import android.app.Application;

import java.util.ArrayList;

/**
 * 当前类注释:自定义全局 application 主要进全局引用,行存储全局变量,全局配置/设置,初始化等相关工作
 */
public class LibraryApplication extends Application {
    public class Node{
        public Node() {}
        public float x;
        public float y;
        public int PenColor;
        public int TouchEvent;
        public int PenWidth;
        public boolean IsPaint;
        public long time;
        public int EraserWidth;

    }
    private ArrayList<Node> PathList;

    public ArrayList<Node> getPathList() {
        return PathList;
    }

    public void addNode(Node node){
        PathList.add(node);
    }

    public Node NewAnode(){
        return new Node();
    }


    public void clearList(){
        PathList.clear();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PathList = new ArrayList<Node>();
    }

    public void setPathList(ArrayList<Node> pathList) {
        PathList = pathList;
    }

    public Node getTheLastNote(){
        return PathList.get(PathList.size()-1);
    }

    public void deleteTheLastNote(){
        PathList.remove(PathList.size()-1);
    }

    public LibraryApplication() {
        PathList = new ArrayList<Node>();
    }

}
