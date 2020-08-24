package com.imooc.dto;

/**
 * 自定义文件上传和下载的返回结果，无特殊意义。
 */
public class FileInfo {

    private String path;

    public  FileInfo(String path){
        this.path=path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
