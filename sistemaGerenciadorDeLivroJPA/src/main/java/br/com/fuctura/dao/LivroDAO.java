package br.com.fuctura.dao;

import java.util.List;
import java.util.Optional;

import br.com.fuctura.entity.Livro;
import br.com.fuctura.utils.JPAUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

public class LivroDAO {

	public Optional<Livro> findByCodigo(Integer codigo) {
		EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		return Optional.ofNullable(em.find(Livro.class, codigo));
	}

	public Livro findByCodigoSemOptional(Integer codigo) {
		EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		return em.find(Livro.class, codigo);
	}

	public List<Livro> findAll() {
		String comandoJPQL = "SELECT l FROM Livro l";
		EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();

		TypedQuery<Livro> tq = em.createQuery(comandoJPQL, Livro.class);

		return tq.getResultList();
	}

	public List<Livro> findByAutor(String autor) {
		String comandoJPQL = "SELECT l FROM Livro l Where autor = :autor";
		EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();

		TypedQuery<Livro> tq = em.createQuery(comandoJPQL, Livro.class);

		tq.setParameter("autor", autor);//aqui jpa vai substituir hugo 
		
		return tq.getResultList();
	}
	
	public Livro update(Livro livro) {
		EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.merge(livro);
		em.getTransaction().commit();
		return livro;
	}

	public void delete(Livro livro) {
		EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.remove(livro);
		em.getTransaction().commit();
	}

	public Livro save(Livro livro) {

		// EntityManagerFactory emf = Persistence
		// .createEntityManagerFactory("FUCTURA-PU");

		EntityManagerFactory emf = JPAUtils.getEntityManagerFactory();

		EntityManager em = emf.createEntityManager();

		// método para salvar no banco de dados
		// operações: inclusao, exclusao e alteracao
		em.getTransaction().begin();
		em.persist(livro);
		em.getTransaction().commit();

		return livro;
	}

}