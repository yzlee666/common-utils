package cn.yzlee.exception;

public class CurrentUserInfoMissingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CurrentUserInfoMissingException() {
		this("当前操作用户信息丢失");
	}

	public CurrentUserInfoMissingException(String message) {
		super(message);
	}
	
	public CurrentUserInfoMissingException(String message,Throwable cause){
		super(message,cause);
	}

	public CurrentUserInfoMissingException(Throwable cause) {
		super(cause);
	}

}
