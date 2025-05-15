# 📝 Atividade 1: Finalização do CRUD com JDBC - LivroDAO

## 🎯 Objetivo

Completar a implementação do CRUD (Create, Read, Update, Delete) utilizando JDBC na classe `LivroDAO`.

Atualmente, a classe `LivroDAO` implementa apenas o método `salvar`, responsável por inserir um novo livro no banco de dados. Sua tarefa é **implementar os métodos restantes do CRUD**.

---

## 📁 Estrutura Atual

O projeto já contém:

- A classe `LivroDAO` com o método `salvar()`.
- A entidade `Livro`.
- A classe `JDBCUtils` que fornece a conexão com o banco de dados.

---

## ✅ Tarefas

Implemente os seguintes métodos na classe `LivroDAO`:

### 1. `public void atualizar(Livro livro) throws SQLException`

- Deve atualizar os dados de um livro já existente no banco.
- Utilize `UPDATE livro SET titulo=?, autor=?, sinopse=?, isbn=?, ano_lancamento=? WHERE codigo=?`.

### 2. `public void deletar(Integer id) throws SQLException`

- Deve remover um livro com base no `codigo`.
- Utilize `DELETE FROM livro WHERE codigo = ?`.

---

# 📝 Atividade 2: Criar Menu

Crie uma classe `LivroView`, utilize o Scanner e System.out para exibir as seguintes opções:

o Menu deve ter as seguintes opções:

- 1 - Gerenciar Livros
- 2 - Gerenciar Categorias
- 3 - Sair

Caso o cliente escolha a opção 1, exiba as seguintes opções:

- Cadastrar Livro
- Excluir Livro
- Atualizar dados do Livro
- Voltar

# 🧪 Atividade 3: Classe Aplicação

A classe aplicação deve ter apenas o método `main()`. O método `main` invocar o método `exibirMenu()` da classe LivroView. 

```java
package br.com.fuctura;

public class Aplicacao {

	public static void main(String[] args) {
		LivroView livroView = new LivroView();
		livroView.exibirMenu();
		
	}

}
```
