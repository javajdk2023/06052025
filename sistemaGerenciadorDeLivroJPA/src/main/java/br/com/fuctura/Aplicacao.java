package br.com.fuctura;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import br.com.fuctura.dao.LivroDAO;
import br.com.fuctura.entity.Livro;

public class Aplicacao {

	public static void main(String[] args) {

		// Persistence.createEntityManagerFactory("FUCTURA-PU");

		Livro meuLivro = new Livro();
		// meuLivro.setCodigo(1);
		meuLivro.setTitulo("Java Como Programar - Antigo");
		meuLivro.setAutor("Deitel");
		meuLivro.setSinopse("Melhor Livro do Programacao");
		meuLivro.setISBN("8543004799");

		Date anoLancamento = Date.valueOf(LocalDate.of(1990, 1, 1));

		meuLivro.setAnoLancamento(anoLancamento);

		LivroDAO dao = new LivroDAO();

		dao.save(meuLivro);

		Livro l = dao.findByCodigoSemOptional(890);

		if (l != null) {
			System.out.println("Nome do Livro: " + l.getTitulo());
		}

		Optional<Livro> l2 = dao.findByCodigo(890);

		if (l2.isPresent()) {
			System.out.println("Nome do Livro: " + l2.get().getTitulo());
		}

		List<Livro> livros = dao.findAll();

		for (Livro ls : livros) {
			System.out.println("Código: " + ls.getCodigo());
			System.out.println("Título:  " + ls.getTitulo());
			System.out.println("Autor: " + ls.getAutor());
		}

		dao.findByAutor("Machado de Assis");
		
	}

}