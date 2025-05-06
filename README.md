# 06052025
Repositório da Turma de Java 2

# 📚 Sistema de Gestão de Biblioteca

Você foi designado para desenvolver um sistema de gerenciamento de biblioteca. O sistema deve permitir o **cadastro** e **consulta** de livros e categorias, com as seguintes especificações:

---

## ✅ Requisitos do Sistema

### 📘 Livros

Cada livro deve conter as seguintes informações:

- Título  
- Autor  
- Sinopse  
- ISBN (único)  
- Ano de lançamento (deve ser **maior ou igual a 1967**)  
- Associação com uma **categoria**

### 🗂️ Categorias

Cada categoria deve conter:

- Nome único  
- Descrição  

> Uma categoria pode conter **múltiplos livros**.

---

## 🛠️ Tarefas a Serem Realizadas

### 1. Configuração do Banco de Dados

- Criar a estrutura necessária no banco de dados para armazenar livros e categorias.

### 2. Conexão com o Banco de Dados

- Configurar a conexão para que o sistema possa interagir com as entidades `Livro` e `Categoria`.

### 3. Classes Utilitárias

- Criar classes para gerenciar a persistência de dados, incluindo métodos para abrir e fechar conexões com o banco.

### 4. Interfaces de Acesso a Dados (DAO)

- Criar interfaces e classes para o acesso aos dados de livros e categorias.
- Implementar operações básicas de **consulta** e **persistência**.

### 5. Classes de Serviço

- Implementar regras de negócio.
- Criar métodos para realizar operações **CRUD** nas entidades de livros e categorias.

---

## 🔄 Regras de Negócio e Funcionalidades CRUD

### 📘 Livro

- **Salvar Livro**  
  Cadastrar um novo livro no banco de dados.

- **Localizar Livro por ID**  
  Buscar um livro pelo seu identificador único.

- **Listar Livros**  
  Exibir todos os livros cadastrados.

- **Listar Livros por Autor**  
  Exibir todos os livros de um autor específico.

- **Listar Livros por Categoria**  
  Exibir todos os livros pertencentes a uma determinada categoria.

- **Atualizar Livro**  
  Atualizar informações de um livro já cadastrado.

- **Remover Livro**  
  Excluir um livro do banco de dados.

---

### 🗂️ Categoria

- **Salvar Categoria**  
  Cadastrar uma nova categoria no banco de dados.

- **Localizar Categoria por ID**  
  Buscar uma categoria pelo seu identificador único.

- **Listar Categorias**  
  Exibir todas as categorias cadastradas.

- **Exibir Categoria com Maior Quantidade de Livros**  
  Mostrar a categoria com o maior número de livros associados.

- **Atualizar Categoria**  
  Atualizar informações de uma categoria já existente.

- **Remover Categoria**  
  Excluir uma categoria **somente se não houver livros associados**.

---

## ⚠️ Observações Importantes

- As **classes de serviço** devem conter as regras de negócio e validações, como:

  - Uma categoria **só pode ser removida** se **não houver livros** vinculados a ela.
  - O **ano de lançamento** do livro deve ser **≥ 1967**.
  - O **ISBN** deve ser **único** no sistema.

- As operações **CRUD** devem ser completamente implementadas para ambas as entidades: `Livro` e `Categoria`.

- Certifique-se de que **todas as regras de negócio estão funcionando corretamente**, especialmente durante **adição** ou **remoção** de registros.

---

## 📝 Arquivo de Comandos SQL

Crie um arquivo `.txt` contendo os comandos SQL utilizados, incluindo:

- Criação das tabelas (`CREATE TABLE`)
- Comandos SQL para:
  - Inserção (`INSERT`)
  - Consulta (`SELECT`)
  - Atualização (`UPDATE`)
  - Remoção (`DELETE`)
  
> O arquivo deve estar bem organizado, com os comandos separados por entidade (`Livro`, `Categoria`) e operação (CRUD).

---

## 📌 Apontamentos

- **Organização do Código**  
  Estrutura clara e bem definida, com separação entre camadas (`DAO`, `Serviço`, etc.).

- **Conexão com o Banco**  
  A configuração da conexão deve estar correta e funcional.

- **Regras de Negócio**  
  Devem ser implementadas de forma completa e eficiente.

- **Comentários no Código**  
  Adicione comentários que expliquem claramente:
  - A lógica de cada método
  - As validações realizadas
  - As etapas principais do fluxo de dados

- **Arquivo de SQL**  
  O `.txt` com comandos SQL deve estar completo e organizado de maneira compreensível.
