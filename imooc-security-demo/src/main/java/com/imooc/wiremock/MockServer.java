package com.imooc.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class MockServer {

    public static void main(String[] args) throws IOException {
        // 去连接WireMock服务端。一参：服务端ip，二参：服务端的端口
        WireMock.configureFor("127.0.0.1",8062);
        // 清空之前的设置
        WireMock.removeAllMappings();
        mock("/mock/01", "01");
        mock("/mock/02", "02");
    }

    /**
     * 我们实现一个工具类。访问某个接口，只需要在/resources/resmock/response/添加响应的文件即可
     */
    private static void mock(String url, String file) throws IOException{
        // 加载resources目录下文件，使用spring的方式
        ClassPathResource resource= new ClassPathResource("mock/response/"+file + ".txt");
        // 工具类，将一个文档的内容 读取到  一个List<String>
        List<String> stringList =  FileUtils.readLines(resource.getFile(), "UTF-8");
        // 将List<String>拼装成一个字符串，替换掉换行符。
        String content = StringUtils.join(stringList.toArray(),"\n");
        // 伪造一个get服务，URL完全等同参数url，响应结果是content，响应状态是200
        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo(url))
                .willReturn(WireMock.aResponse().withBody(content).withStatus(200)));
    }
    /*
    主线程
     */
}
