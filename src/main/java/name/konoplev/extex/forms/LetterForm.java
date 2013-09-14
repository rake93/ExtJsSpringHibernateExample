package name.konoplev.extex.forms;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class LetterForm {
	@NotEmpty()
	@Length(min = 3, max = 20)
	private String docNumber;

	@NotEmpty()
	@Length(min = 1, max = 255)
	private String title;

	@Length(max = 1000)
	private String textContent;
	@NotNull
	private CommonsMultipartFile attachedFile;

	@NotNull()
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date creationDate;
	private boolean published;

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public CommonsMultipartFile getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(CommonsMultipartFile attachedFile) {
		this.attachedFile = attachedFile;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

}
