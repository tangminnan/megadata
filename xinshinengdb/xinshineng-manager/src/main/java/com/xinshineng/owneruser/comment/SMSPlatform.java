package com.xinshineng.owneruser.comment;

/**
 * 短信调用平台
 * @author tmn
 * @create 2017-4-13
 */
public enum SMSPlatform {
	xinshineng(1,"【新视能】","username=1111&password=xy-11111","name=bjywhy&password=123qaz789");// 腾信、快用云账号配置

	public int code;
	public String qianming;
	public String url1;
	public String url2;
	
	SMSPlatform(int code, String qianming, String url1, String url2) {
		this.code = code;
		this.qianming = qianming;
		this.url1 = url1;
		this.url2 = url2;
	}
}
