package name.konoplev.extex.json;

import java.util.List;

public class FormResponseJSON {
	private Boolean success = true;
	private String message;
	private List<ErrorJSON> errors;

	public List<ErrorJSON> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorJSON> errors) {
		this.errors = errors;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
