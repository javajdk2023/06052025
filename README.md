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

### 1. `public List<Livro> listarTodos() throws SQLException`

- Deve retornar uma lista com todos os livros cadastrados.
- Utilize `SELECT * FROM livro`.

### 2. `public Livro buscarPorId(Integer id) throws SQLException`

- Deve retornar um único livro com base no `codigo` informado.
- Utilize `SELECT * FROM livro WHERE codigo = ?`.

### 3. `public void atualizar(Livro livro) throws SQLException`

- Deve atualizar os dados de um livro já existente no banco.
- Utilize `UPDATE livro SET titulo=?, autor=?, sinopse=?, isbn=?, ano_lancamento=? WHERE codigo=?`.

### 4. `public void deletar(Integer id) throws SQLException`

- Deve remover um livro com base no `codigo`.
- Utilize `DELETE FROM livro WHERE codigo = ?`.

---

## 🧪 Dica de Teste

Crie uma classe `Main` com exemplos para testar as funcionalidades implementadas. Exemplo:

```java
public class Main {
    public static void main(String[] args) throws Exception {
        LivroDAO dao = new LivroDAO();

        // Teste de inserção
        Livro novoLivro = new Livro(null, "Dom Casmurro", "Machado de Assis", "Romance brasileiro", "123456789", Date.valueOf("1899-01-01"));
        dao.salvar(novoLivro);

        // Teste de listagem
        for (Livro l : dao.listarTodos()) {
            System.out.println(l.getTitulo());
        }

        // Teste de busca por ID
        Livro livro = dao.buscarPorId(novoLivro.getCodigo());

        // Teste de atualização
        livro.setTitulo("Dom Casmurro (Edição Revisada)");
        dao.atualizar(livro);

        // Teste de remoção
        dao.deletar(livro.getCodigo());
    }
}
