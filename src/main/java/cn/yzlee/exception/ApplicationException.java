package cn.yzlee.exception;

public class ApplicationException extends RuntimeException {

	private static final String MESSAGE="应用异常";
	private static final long serialVersionUID = 2801344950962948616L;
	
	public ApplicationException(){super(MESSAGE);}
	
	public ApplicationException(String message){
		super(message);
	}
	public ApplicationException(Throwable cause){
		super(cause);
	}
	
	public ApplicationException(String message,Throwable cause){
		super(message,cause);
	}

}
