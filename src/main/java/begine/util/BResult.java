package begine.util;

public class BResult {

	private int code;
	
	private String msg;
	
	private Object value;

	public BResult(int i,String msg, Object value) {
		setCode(i);
		setValue(value);
		setMsg(msg);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "BResult [code=" + code + ", msg=" + msg + ", value=" + value + "]";
	}
}
