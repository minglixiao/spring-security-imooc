package com.imooc.web.controller;

import com.imooc.dto.FileInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {

    String path = "D:\\raohao\\linshi\\spring security";

    /**
     * 文件上传
     *  上传文件一定是post请求
     */
    @PostMapping
    public FileInfo upload (MultipartFile file) throws IOException {
        System.out.println("上传文件的参数名："+file.getName() + "，上传文件的文件名：" +file.getOriginalFilename() +
                "，上传文件的内容大小："+file.getSize());
        // 工作中我们常把文件上传到 阿里云OSS服务 或 fastDFS文件服务器。此处我们只演示把文件存到本地。
        // 一参：路径，二参：文件名称
        File localFile = new File(path, new Date().getTime()+".txt");
        // 将该文件内容 全部拷贝到 另一个文件
        file.transferTo(localFile);
        return new FileInfo(localFile.getAbsolutePath());
    }


    /**
     * 文件下载
     *  上传文件一定是post请求
     */
    @GetMapping("/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse  response) throws IOException {
        // jdk1.7以后 把 流 声明在括号里面，jdk自动关闭这些流
        try(InputStream inputStream =  new FileInputStream(new File(path, id+".txt"));
            OutputStream outputStream = response.getOutputStream();
            ){
            // 下载需要改这里才行
            response.setContentType("application/x-download");
            // 这里我们可以自定义下载文件的文件名，此处我们就指定下载的文件名是haha.txt
            response.addHeader("Content-Disposition","attachment;filename=haha.txt");
            // 把输入流拷贝到输出流
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
        }
    }
}
