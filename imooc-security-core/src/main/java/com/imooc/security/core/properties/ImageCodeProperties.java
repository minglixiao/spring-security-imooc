package com.imooc.security.core.properties;

import lombok.Data;

/**
 * 图形验证码----基本参数配置
 */
@Data
public class ImageCodeProperties  extends SmsCodeProperties {

	public ImageCodeProperties() {
		setLength(4);
	}

	private int width = 67;
	private int height = 23;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
