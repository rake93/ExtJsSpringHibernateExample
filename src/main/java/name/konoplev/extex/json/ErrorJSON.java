package name.konoplev.extex.json;

public class ErrorJSON {
	private String field;
	private String errorText;

	public ErrorJSON(String field, String errorText) {
		super();
		this.field = field;
		this.errorText = errorText;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

}
