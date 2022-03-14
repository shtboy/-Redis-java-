package com.song.common.constant;

/**
 * 全局错误代码
 * @author jiaxiaowei
 *
 */
public enum ErrorCode {

	ok,//成功
	error,//失败

	user_not_found, //用户不存在
	password_error, //用户存在，密码匹配错误
	login_fail, //登录失败

	not_login, //未登录
	session_timeout, //session或jwt登录超时
	unauthorized, //未授权,指操作权限

	no_token, //请求头未携带token
	invalid_token, //无效的token

	data_not_found,//数据不存在
	data_was_used,//数据在使用

	http_method_error,//http请求方法错误
	http_method_argument,//请求参数错误
	error_500,//服务端错误

	no_account_of_orgId,//该组织下没有账号
	not_exist_account,//不存在该账户
	no_data_authority,//无权限，指数据权限，包括当前用户的所属组织与请求数据不符

	request_params_error, // 请求参数错误
	unique_error, // 唯一性错误
	data_existed, // 数据已存在
	role_auth_not_null, // 角色权限不能为空
	userId_not_null, // 用户id不能为空
	no_authority_of_delete, // 没有删除的权限
	no_operation_authority, //你没有操作权限

	accountExisted,//账号已存在

	user_validation_code_not_found, //验证码不存在或已过期
	user_validation_code_error, //验证码不正确

	repeat_submit,//重复提交

	unsupported_file_type, // 不支持的文件类型

	inconsistent_org, //组织不一致

	expire_interaction, //互动已失效

	message_too_much, //短信发送已达上限

	track_event_params_error //埋点事件的参数错误

}
