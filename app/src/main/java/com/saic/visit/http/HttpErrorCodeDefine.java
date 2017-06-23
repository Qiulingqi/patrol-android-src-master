package com.saic.visit.http;

public class HttpErrorCodeDefine {
	public static final int ERROR_OK = 1;//	请求正常
	public static final int ERROR_BAD_REQUEST = 400;	////错误的请求
	public static final int ERROR_NO_SESSION = 401;	////Session 缺失
	public static final int ERROR_SESSION_EXPIRED = 402;	//Session 已过期
	public static final int ERROR_NO_LOGIN = 403;	//没有登录
	public static final int ERROR_NO_RESOURCE = 404;	//资源未找到
	public static final int ERROR_BAD_METHOD = 405;	//Action 不正确
	public static final int ERROR_BAD_KEY = 406;	//Key 不正确
	public static final int ERROR_BAD_CREDENTIAL = 407;	//Credential 不正确
	public static final int ERROR_BAD_REQUESTID = 408;	//Credential 中的 RequestId 不正确
	public static final int ERROR_BAD_APPLICATION = 409;	//Credential 中的 Application 不正确
	public static final int ERROR_BAD_DEVICEKIND = 410;	//Credential 中的 DeviceKind 不正确
	public static final int ERROR_NO_USERINFO = 411;	//用户信息丢失
	public static final int ERROR_NO_RENEWSESSION = 412;	//	Renew Session 不存在
	public static final int ERROR_READ_DATA = 415;	//读取数据失败
	public static final int ERROR_RSA_DECRPT = 416;	//RSA 解密失败
	public static final int ERROR_AES_DECRPT = 417;	//AES 解密失败
	public static final int ERROR_GZIP_UNZIP = 418;	//GZIP 解压失败
	public static final int ERROR_BAD_JSON = 419;	//JSON 格式不正确
	public static final int ERROR_LOGIN_AGAIN = 420;	//不允许再次登录
	public static final int ERROR_FORBIDDEN_LOGIN = 421;	//不允许登录
	public static final int ERROR_SAME_AESKEY = 422;	//AES Key 和上次一样
	public static final int ERROR_NO_ACCOUNT  = 423;    //用户名不存在
	public static final int ERROR_FAULT_PASSWORD = 424;  //密码错误
	public static final int ERROR_FAULT_SESSION_LIVE = 425;  //Session 未过期
	public static final int ERROR_FAULT_LOCKACCOUNT = 426;  //锁定账号
	public static final int ERROR_FAULT_MANYPWDERROR = 427;  //多次输入密码错误锁定账号

	
	public static final int ERROR_VERCODE_FAST = 430;	//发送验证码过于频繁
	public static final int ERROR_BAD_VERCODE = 431;	//发送短信验证码失败
	public static final int ERROR_BAD_VOICECODE = 432;	//发送语音验证码失败
	public static final int ERROR_VERIFY_MANYFAILED = 433;	//校验验证码次数过多
	public static final int ERROR_VERCODE_EXPIRED = 434;	//验证码已过期
	public static final int ERROR_VERCODE_MISMATCH = 435;	//验证码不匹配
	public static final int ERROR_SHORTAGE_AMOUNT = 440;	//账户可用金额不足
	public static final int ERROR_UNLOCKNUM_ACCOUNT = 441;	//账户解锁金额失败
	public static final int ERROR_UNLOCK_ACCOUNT = 442;	//账户明细不是锁定状态
	public static final int ERROR_INNER_ERROR = 500;	//内部错误
}
