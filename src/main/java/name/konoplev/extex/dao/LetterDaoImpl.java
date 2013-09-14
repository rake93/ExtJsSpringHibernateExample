package name.konoplev.extex.dao;

import java.util.List;

import name.konoplev.extex.domain.entities.Letter;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LetterDaoImpl implements LetterDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Letter> getAllLetters() {
		Session hSession = sessionFactory.getCurrentSession();
		return hSession.createCriteria(Letter.class).list();
	}

	@Override
	public List<Letter> getPublishedLetters() {
		Session hSession = sessionFactory.getCurrentSession();
		return hSession.createCriteria(Letter.class)
				.add(Restrictions.eq("published", true)).list();
	}

	@Override
	public void saveLetter(Letter letter) {
		Session hSession = sessionFactory.getCurrentSession();
		hSession.save(letter);
	}

	@Override
	public Letter getLetterById(Integer id) {
		Session hSession = sessionFactory.getCurrentSession();
		return (Letter) hSession.get(Letter.class, id);
	}

	@Override
	public Letter getLetterByDocNumber(String docNumber) {
		Session hSession = sessionFactory.getCurrentSession();
		List<Letter> letters = hSession.createCriteria(Letter.class)
				.add(Restrictions.like("docNumber", docNumber)).list();
		if (0 < letters.size())
			return letters.get(0);
		return null;
	}

	@Override
	public void updateLetter(Letter letter) {
		Session hSession = sessionFactory.getCurrentSession();
		hSession.update(letter);
	}

	@Override
	public void saveOrUpdate(Letter letter) {
		Session hSession = sessionFactory.getCurrentSession();
		hSession.saveOrUpdate(letter);

	}

}
