package name.konoplev.extex.domain.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Letter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	private String docNumber;
	private String title;
	@Column(length = 1000)
	private String textContent;

	@Lob()
	@Column(columnDefinition = "longblob")
	private byte[] attachedFile;

	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date creationDate;
	private boolean published = false;

	public Letter() {
		super();
	}

	public Letter(String docNumber, String title, String textContent,
			byte[] attachedFile, Date creationDate, boolean published) {
		super();
		this.docNumber = docNumber;
		this.title = title;
		this.textContent = textContent;
		this.attachedFile = attachedFile;
		this.creationDate = creationDate;
		this.published = published;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public byte[] getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(byte[] attachedFile) {
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

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

}
