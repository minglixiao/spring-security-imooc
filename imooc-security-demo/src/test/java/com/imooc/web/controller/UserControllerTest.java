package com.imooc.web.controller;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Restful API的测试用例
 */
// 加载spring环境
//@RunWith(SpringRunner.class)
// 声明这是一个测试用例
//@SpringBootTest
public class UserControllerTest {

    //@Autowired
    private WebApplicationContext wac;

    // MockMvc是一个伪造的spring mvc的环境
    private MockMvc mockMvc;

    // @Before：在每一个测试用例方法执行之前，去执行。左边不能是0，不能超过1位
    // cpu结构（运算器和控制器的组成）
    //@Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    //@Test
    public void whenUploadSuccess() throws Exception {
        // MockMvcRequestBuilders.fileUpload：单元测试上传文件类型的接口。参数是接口的URL
        String  result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/file")
                // 一参：请求参数的名称， 二参：上传文件的文件名，三参：类型， 四参：上传文件的内容（字节数组）
                .file(new MockMultipartFile("file", "test.txt",
                "multipart/form-data","hello wold".getBytes("UTF-8"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }



    //@Test
    public void whenQuerySuccess() throws Exception {
        String result = mockMvc.perform(
                MockMvcRequestBuilders.get("/user")
                .param("nickName", "张三")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                // 获取响应结果，将响应结果转换为字符串
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    //@Test
    public void whenGetInfoSuccess() throws Exception {
        String result = mockMvc.perform(
                MockMvcRequestBuilders.get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("tom"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    //@Test
    public void whenGetInfoFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/a")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                // 断言，http响应码在400-500
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResponse().getContentAsString();

    }

    //@Test
    public void whenCreateSuccess() throws Exception {
        // 前端传递时间，使用时间戳方式，后端自动转为Date类型，后端返回Date类型，自动转换为时间戳，前端收到就是时间戳。
        String  content = "{\"userName\":\"tom\",\"password\":null,\"birthday\":1587861247772}";
        String result = mockMvc.perform(
                // 发送一个psot请求
                MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        // 指定psot请求的参数。
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 断言：返回结果中id的值等于1
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    //@Test
    public void whenUpdateSuccess() throws Exception {
        // 明天这个点的时间
        Date date = new Date(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        String  content = "{\"id\":1,\"userName\":\"tom\",\"password\":null,\"birthday\":"+date.getTime()+"}";
        System.out.println(content);
        String result = mockMvc.perform(
                MockMvcRequestBuilders.put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }



    /**
     * 1. 前端写假数据。缺点：IOS，android，web等，都需要前端去写，工作量重复，容易出现误差。
     * 2. 后端的接口，写假数据。缺点：接口返回值和请求参数变化，需要改代码。
     * 3. WireMock伪造服务。优点：伪造RESTful服务，高效，灵活。缺点：需要学习WireMock的使用。
     *  WireMock是独立的服务器，我们的代码是客户端，使用代码去编辑服务器，改变服务器的行为。
     *  官网WireMock，本课程
     */
    //@Test
    public void whenDeleteSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                // 我们只需要判断http状态码即可
                .andExpect(MockMvcResultMatchers.status().isOk());
    }















}