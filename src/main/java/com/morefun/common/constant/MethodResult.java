package com.morefun.common.constant;


import java.io.Serializable;


/**
 * 方法调用的返回结果，data的对象类型属性由上下文自行判断处理
 */

public class MethodResult<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 为了方便使用，serverVersion和committer两个属性在应用启动时通过如下代码设置:
	 * MethodResult.setServerVersion("1.0.7");
	 * MethodResult.setCommitter("jxw");
	 * 类的static属性在使用Gson工具转json时会被判断为不可序列化的属性不会被输出，
	 * 而spring mvc在把对象输出时可以调用对应属性的getXXX方法，不论该属性是否为static,
	 * 所以使用_serverVersion和_committer进行中转
	 */
	public static String _serverVersion = "v1";
	public static String _committer = "";

	private String serverVersion = "";//服务端版本号，由服务在启动时统一设置,不能在具体一个接口中设置
	private String committer = "";//当前提交代码的人员，以免多个人同时提交serverVersion时导致的serverVersion覆盖问题


	private Boolean success = false;
	private ErrorCode errorCode;// 操作结果的返回代码

	private String errorMsg = "";// 操作结果的返回消息
	private String requestId = "";
	private T data;// 返回的业务数据

	public MethodResult() {
		this.serverVersion = MethodResult._serverVersion;
		this.committer = MethodResult._committer;
	}

	public MethodResult(ErrorCode errorCode, String errorMsg) {
		this.serverVersion = MethodResult._serverVersion;
		this.committer = MethodResult._committer;

		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public MethodResult(T data) {
		this.serverVersion = MethodResult._serverVersion;
		this.committer = MethodResult._committer;

		this.success = true;
		this.data = data;
	}

	public static<T> MethodResult<T> success(T data, String errorMsg) {
		MethodResult<T> methodResult = new MethodResult<>();
		methodResult.setSuccess(true);
		methodResult.setData(data);
		methodResult.setErrorMsg(errorMsg);
		methodResult.setErrorCode(ErrorCode.ok);
		return methodResult;
	}

	public static<T> MethodResult<T> success(T data) {
		MethodResult<T> methodResult = new MethodResult<>();
		methodResult.setSuccess(true);
		methodResult.setData(data);
		methodResult.setErrorMsg("操作成功!");
		methodResult.setErrorCode(ErrorCode.ok);
		return methodResult;
	}

	public static<T> MethodResult<T> failure(ErrorCode errorCode, String errorMsg) {
		MethodResult<T> methodResult = new MethodResult<>();
		methodResult.setSuccess(false);
		methodResult.setErrorMsg(errorMsg);
		methodResult.setErrorCode(errorCode);
		return methodResult;
	}
	public static<T> MethodResult<T> failure(ErrorCode errorCode, String errorMsg,T data) {
		MethodResult<T> methodResult = new MethodResult<>();
		methodResult.setSuccess(false);
		methodResult.setErrorMsg(errorMsg);
		methodResult.setErrorCode(errorCode);
		methodResult.setData(data);
		return methodResult;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public static void setServerVersion(String serverVersion) {
		MethodResult._serverVersion = serverVersion;
	}

	public String getCommitter() {
		return committer;
	}

	public static void setCommitter(String committer) {
		MethodResult._committer = committer;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		return "{serverVersion=" + MethodResult._serverVersion + ", committer=" + MethodResult._committer  + ", success=" + success + ", errorcode=" + errorCode + ", errormsg=" + errorMsg + "}";
	}

}
