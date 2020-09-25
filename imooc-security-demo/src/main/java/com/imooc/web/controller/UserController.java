package com.imooc.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.imooc.dto.User;
import com.imooc.exception.UserNotExistException;
import com.imooc.security.core.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@RestController
/**
 * 在类上面加上@RequestMapping的作用： 本类所有接口的URL都会自动加上这个前缀。
 * 我个人不喜欢这种，因为你把URL拆开后，当前端问我一个接口时，我搜索不到URL对应在代码的哪个位置。
 * 如果你说URL拆分的很规范，很好找，那你就当我这句话没说。
 */
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

/*    @Autowired
    private SignUpUtils signUpUtils;*/

    @Autowired
    private SecurityProperties securityProperties;


    /**
     * @param authentication : 不包含我们扩展的jwt信息，扩展信息需要我们自己去解析。
     *
     */
    @GetMapping("/me")
    public Object getCurrentUser(Authentication authentication, HttpServletRequest request) throws UnsupportedEncodingException {
        String authorization = request.getHeader("Authorization");
        String token = StringUtils.substringAfter(authorization, "Bearer ");
        logger.info("jwt token:{}", token);
        String jwtSigningKey = securityProperties.getOauth2().getJwtSigningKey();
        /*
         * 生成的时候使用的是org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter源码里面把signingkey变成utf8了
         */
        Claims body = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes("utf-8"))
                .parseClaimsJws(token).getBody();
        String company =  (String) body.get("company");
        logger.info("公司名称:{}", company);
        return authentication;
    }



    /**
     * 注册用户
     */
    @PostMapping("/regist")
    public void regist(User user, HttpServletRequest request) {
        //不管是注册用户还是绑定用户，都会拿到一个用户唯一标识。
        String userId = user.getUsername();
        /*  spring social根据request，能够从session获取到服务提供商提供的用户信息。
            该方法会把providerId、providerUserId、userId等信息插入到UserConnection数据表中。
            并继续后面的流程。
         */
        //providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
        /*signUpUtils.doPostSignUp(userId, new ServletWebRequest(request));*/
    }

    /* */
    @GetMapping("/me/1")
    public Object getCurrentUser1(){
        // 从SecurityContextHolder获取SecurityContext，再获取Authentication
        // 只要是同一个session（会话），即便是多次不同的请求，我们都能通过SecurityContextHolder获取到当前用户的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    @GetMapping("/me/2")
    public Object getCurrentUser2(Authentication authentication){
        return authentication;
    }

    @GetMapping("/me/3")
    // 我们通过这种注解的方式，可以直接获取到我们返回的UserDetails对象
    public Object getCurrentUser3(@AuthenticationPrincipal UserDetails user){
        return user;
    }

    /**
     * @GetMapping:  等同 @RequestMapping(value = "", method = RequestMethod.GET)
     * 类似的还有@PostMapping、@PutMapping、@DeleteMapping。
     */
    @GetMapping
    @JsonView(User.UserSimpleView.class)
    public  ArrayList<User> query(@RequestParam(name="nickName", required=false, defaultValue="狗蛋")String userName ,
                                  @PageableDefault(page=1, size=20, sort="username desc") Pageable pageable) {
        System.out.println("------------/user ----get---------------");
        System.out.println(pageable.getPageSize()+"---"+pageable.getPageNumber()+"---"+pageable.getSort());
        ArrayList<User>  arr=new ArrayList<User>();
        User user0 = new User();
        user0.setUsername("zhangsan");
        user0.setPassword("21151");
        User user1 = new User();
        user1.setUsername("lisi");
        user1.setPassword("58441");
        User user2 = new User();
        user2.setUsername("wangwu");
        user2.setPassword("6984156");
        arr.add(user0);
        arr.add(user1);
        arr.add(user2);
        return arr;
    }

    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    @ResponseBody
    public  User getInfo(@PathVariable(name = "id", required = false)   Integer idxxx){
        System.out.println("------------/user/{id} ----get---------------");
        System.out.println("进入getInfo服务");
        User user = new User();
        user.setId(idxxx);
        user.setUsername("tom");
        user.setPassword("233");
        return user;
    }

    /**
     * 创建用户。
     */
    @PostMapping
    @ResponseBody
    public  User create(@RequestBody User user){
        user.setId(1);
        return user;
    }

    /**
     * 修改用户
     */
    @PutMapping("/{id:\\d+}")
    public  User update(@Valid  @RequestBody User user, BindingResult errors){
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach(error->{
                // error实际上是FieldError这个类。
                FieldError fieldError= (FieldError)error;
                // 可以从fieldError对象中获取校验的字段和校验后的消息
                System.out.println("校验的字段："+fieldError.getField() +"; 校验后的消息："+fieldError.getDefaultMessage());
            });
        }
        System.out.println(user);
        user.setId(1);
        return user;
    }

    /**
     * 删除用户。
     * 注意：按照restful风格，接口没有返回值也可以，根据http状态码就能判断是否执行成功。
     */
    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable Integer id){
        System.out.println(id);
    }

    /**
     * 演示服务异常处理
     */
    @GetMapping("/error")
    public  User createError(@Valid  @RequestBody User user){
        System.out.println(user);
        user.setId(1);
        return user;
    }

    /**
     * 演示服务异常处理。
     */
    @GetMapping("/error/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public  User getInfoError(@PathVariable(name = "id", required = false)   Integer idxxx){
        throw new UserNotExistException("202005081806");
        // 自定义浏览器和非浏览器请求异常时的返回，适用于浏览器和非浏览器请求异常时的情况。
        // throw new RuntimeException("程序运行错误");
    }

    /**
     * 1. 前端写假数据，多个前端
     * 2. 后端写死数据，接口会随时变化
     * 3. wiremock  改变接口的功能  是一个独立的服务器  连wiremock  连  正式服务器
     */

}

