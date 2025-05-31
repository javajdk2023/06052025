package br.com.fuctura.utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtils {

	private static EntityManagerFactory emf = null;

	public static EntityManagerFactory getEntityManagerFactory() {

		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("FUCTURA-PU");
		}

		return emf;
	}

}
