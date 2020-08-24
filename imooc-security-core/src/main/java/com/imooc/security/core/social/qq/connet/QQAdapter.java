package com.imooc.security.core.social.qq.connet;

import com.imooc.security.core.social.qq.api.QQ;
import com.imooc.security.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * “spring social逻辑流程图” 第6步：根据令牌（accessToken）获取用户信息。
 * 不同服务提供商获取到的用户信息是各不相同的，ApiAdapter就是将获取到的个性化的用户信息（如此处的QQUserInfo）
 * 转化为 spring social标准的用户信息（Connection）。
 * 泛型：就是 自定义ApiBinding的数据类型。
 */
public class QQAdapter implements ApiAdapter<QQ> {

	/**
	 * 测试服务提供商是否是通的。
	 */
	@Override
	public boolean test(QQ api) {
		// 我们省略此处代码，认为一直是通的。
		return true;
	}

	/**
	 * 将我们自定义的ApiBinding获取到的用户信息（QQUserInfo） 转化为 spring social标准的用户信息（Connection）。
	 * ConnectionValues封装了创建Connection所需要的信息。
	 */
	@Override
	public void setConnectionValues(QQ api, ConnectionValues values) {
		QQUserInfo userInfo = api.getUserInfo();
		// 设置用户昵称
		values.setDisplayName(userInfo.getNickname());
		// 设置用户头像
		values.setImageUrl(userInfo.getFigureurl_qq_1());
		// 设置个人主页
		values.setProfileUrl(null);
		// 设置用户在服务提供商的唯一标识id
		values.setProviderUserId(userInfo.getOpenId());
	}

	/**
	 * 后面讲
	 */
	@Override
	public UserProfile fetchUserProfile(QQ api) {
		return null;
	}

	/**
	 * QQ没有这个功能
	 */
	@Override
	public void updateStatus(QQ api, String message) {
	}
}
