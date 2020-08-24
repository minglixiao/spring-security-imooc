package com.imooc.security.core.properties;

public class BrowserProperties {

	// 默认/imooc-signIn.html
	private String loginPage = "/imooc-signIn.html";

	private String signUpUrl = "/imooc-signUp.html";

	/** 退出成功页面，默认空  */
	private String signOutUrl;

	private SessionProperties session = new SessionProperties();

	// 默认JSON
	private LoginResponseType loginType = LoginResponseType.JSON;

	private int rememberMeSeconds = 60*60 ;

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public String getSignUpUrl() {
		return signUpUrl;
	}

	public void setSignUpUrl(String signUpUrl) {
		this.signUpUrl = signUpUrl;
	}

	public LoginResponseType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginResponseType loginType) {
		this.loginType = loginType;
	}

	public int getRememberMeSeconds() {
		return rememberMeSeconds;
	}

	public void setRememberMeSeconds(int rememberMeSeconds) {
		this.rememberMeSeconds = rememberMeSeconds;
	}

	public SessionProperties getSession() {
		return session;
	}

	public void setSession(SessionProperties session) {
		this.session = session;
	}

	public String getSignOutUrl() {
		return signOutUrl;
	}

	public void setSignOutUrl(String signOutUrl) {
		this.signOutUrl = signOutUrl;
	}
}
