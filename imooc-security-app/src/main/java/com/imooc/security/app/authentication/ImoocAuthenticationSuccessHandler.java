package com.imooc.security.app.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * 实现AuthenticationSuccessHandler接口。SavedRequestAwareAuthenticationSuccessHandler
 * AuthenticationSuccessHandler的实现类，是spring  security默认使用的登录成功处理。
 */
@Component("imoocAuthenticationSuccessHandler")
public class ImoocAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Qualifier("defaultAuthorizationServerTokenServices")
	@Autowired
	private AuthorizationServerTokenServices authorizationServerTokenServices;

	/**
	 * @param authentication 封装了所有的认证信息
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		logger.info("登录成功");

		// 获取请求头中参数Authorization的信息
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Basic ")) {
			// 不被认可的客户端异常
			throw new UnapprovedClientAuthenticationException("没有Authorization请求头");
		}

		// 解析Authorization 获取client信息，返回client-id和client-secret
		String[] tokens = extractAndDecodeHeader(header, request);
		assert tokens.length == 2;
		String clientId = tokens[0];
		String clientSecret = tokens[1];
		// 根据clientId获取ClientDetails
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
		// 判断第三方信息是否匹配。判断第三方clientId和clientSecret是否匹配
		if (clientDetails == null) {
			throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
		} else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
			throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
		}

		/**
		 * 构建TokenRequest
		 * @param requestParameters : 每一种授权模式特有的参数。该参数用来构建Authentication，
		 *                                此处我们不需要构建Authentication，所以传空
		 * @param clientId :
		 * @param scope : 给第三方应用的权限
		 * @param grantType : 随意定义一个grantType名称，用来匹配请求参数的grantType
		 */
		TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_SORTED_MAP, clientId, clientDetails.getScope(), "costom");
		// 构建OAuth2Request
		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
		// 构建OAuth2Authentication
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
		// 使用authorizationServerTokenServices生成令牌
		OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
		// 将令牌写到响应
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(accessToken));
	}

	/**
	 * 从 OAuth获取令牌 请求头中获取client-id和client-secret
	 */
	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException(
					"Failed to decode basic authentication token");
		}
		String token = new String(decoded, "UTF-8");
		int delim = token.indexOf(":");
		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[]{token.substring(0, delim), token.substring(delim + 1)};
	}
}
