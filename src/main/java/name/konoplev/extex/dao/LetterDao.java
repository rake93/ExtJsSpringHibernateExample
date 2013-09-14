package name.konoplev.extex.dao;

import java.util.List;

import name.konoplev.extex.domain.entities.Letter;

public interface LetterDao {

	public List<Letter> getAllLetters();
	
	public List<Letter> getPublishedLetters();

	public Letter getLetterById(Integer id);

	public Letter getLetterByDocNumber(String docNumber);

	public void saveLetter(Letter letter);

	public void updateLetter(Letter letter);

	public void saveOrUpdate(Letter letter);

}