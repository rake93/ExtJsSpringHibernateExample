package name.konoplev.extex.json;

import java.util.Date;

import name.konoplev.extex.domain.entities.Letter;

public class LetterJSON {
	private Integer id;
	private String docNumber;
	private String title;
	private String textContent;
	private Date creationDate;
	private boolean published = false;

	public LetterJSON(Letter letter) {
		this.id = letter.getId();
		this.docNumber = letter.getDocNumber();
		this.title = letter.getTitle();
		this.textContent = letter.getTextContent();
		this.creationDate = letter.getCreationDate();
		this.published = letter.isPublished();

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
