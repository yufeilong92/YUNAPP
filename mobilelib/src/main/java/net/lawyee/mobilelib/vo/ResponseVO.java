package net.lawyee.mobilelib.vo;

public class ResponseVO
{
	/**
	 * 失败
	 */
	public static final int RESPONSE_CODE_FAIL = 2;
	/**
	 * 成功
	 */
	public static final int RESPONSE_CODE_SUCESS = 1;

	private int code; // 返回报文状态码
	private String msg; // 返回报文状态信息；

	public ResponseVO()
	{
		code = RESPONSE_CODE_FAIL;
		msg = "";
	}

	public ResponseVO(int response_code, String msg)
	{
		code = response_code;
		this.msg = msg;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public boolean isSucess()
	{
		return this.code == RESPONSE_CODE_SUCESS;
	}
}
