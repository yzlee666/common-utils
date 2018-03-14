package cn.yzlee.exception;

public class UnableCreateFileException extends RuntimeException {

	private static final long serialVersionUID = -5059875750965690244L;
	
	public UnableCreateFileException(){this("无法创建文件");}
	
	public UnableCreateFileException(String message){
		super(message);
	}
	
	public UnableCreateFileException(String message, Throwable cause){
		super(message,cause);
	}

	public UnableCreateFileException(Throwable cause){
		super(cause);
	}

}
