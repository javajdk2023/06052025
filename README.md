# 📝 Atividade 2: Consultas JPQL

### 🧩 Passo a passo para clonar o projeto no Eclipse

#### 1. Abrir a visão do Git

- No Eclipse, vá em:  
  `Window` > `Show View` > `Other...`
- Digite `Git` e selecione **Git Repositories**, depois clique em **OK**

#### 2. Clonar o repositório

- Na aba **Git Repositories**, clique em:  
  **Clone a Git repository** (ícone com uma nuvem e seta para baixo)

- Preencha os campos:
  - **URI**: cole o link HTTPS deste repositório: https://github.com/javajdk2023/06052025/tree/atividade2
  - Os outros campos serão preenchidos automaticamente
  - Clique em **Next**

- Selecione o(s) branch(es) desejados (geralmente `main` ou `master`)  
  Clique em **Next**

- Escolha o diretório local onde o projeto será salvo (ou use o padrão)  
  Clique em **Finish**

---

#### 3. Importar o projeto no Eclipse

- Vá em:  
  `File` > `Import...` > `Git` > **Projects from Git** > `Next`

- Selecione: **Existing local repository**  
  Escolha o repositório que acabou de clonar > `Next`

- Escolha a forma de importação:
  - **Import existing Eclipse projects** (se houver arquivos `.project`)
  - **Import as general project** (caso contrário)

- Clique em **Finish**

---
## 📝 Lista de Exercício 1 – Criar Unidade de Persistencia

Crie uma nova unidade de persistencia utilizando as credencias que recebeu no grupo do Whatsapp.

```
persistence-unit name="FUCTURA-PU-CLOUD"
```

## 📝 Lista de Exercício 1 – Popular a Base

- Cadastre 30 Livros

## 📝 Lista de Exercício 2 – Consultas SQL (30 itens)

### 🔍 Consultas básicas (SELECT)

1. Selecione todos os dados da tabela `livro`.
2. Selecione apenas os títulos e autores de todos os livros.
3. Selecione os livros publicados no ano de 2020.
4. Liste os livros cujo autor seja `'Machado de Assis'`.
5. Selecione os livros cuja sinopse contenha a palavra `'aventura'`.

### 🧾 Filtros e condições

6. Liste os livros com ISBN diferente de `NULL`.
7. Liste os livros lançados após o ano de 2015.
8. Liste os livros com título iniciado por `'A'`.
9. Liste os livros cujo título tenha mais de 20 caracteres.
10. Liste os livros com `anoLancamento` entre 2010 e 2020.

### 🧮 Ordenação e limite

11. Liste os 5 livros mais recentes.
12. Liste os 10 livros mais antigos, ordenados pelo título.
13. Liste os livros ordenados por autor em ordem decrescente.
14. Liste os livros com título mais longo primeiro.
15. Mostre os 3 primeiros livros cujo autor começa com a letra `'J'`.

### 🧠 Funções e expressões

16. Mostre o ano (`EXTRACT(YEAR FROM ano_lancamento)`) de todos os livros.
17. Concatene título e autor em uma coluna chamada `detalhe`.
18. Conte quantos livros foram cadastrados na tabela.
19. Conte quantos autores distintos existem.
20. Mostre a média de anos de lançamento dos livros.

### 🔄 Operadores e subconsultas

21. Liste os livros cujo autor é o mesmo do livro com `codigo = 1`.
22. Liste os livros com o mesmo ISBN de algum outro livro.
23. Mostre os livros cujo título aparece mais de uma vez.
24. Liste os livros que não têm sinopse cadastrada.
25. Liste os livros que têm sinopse cadastrada.

### 🧩 Agrupamentos (GROUP BY e HAVING)

26. Agrupe os livros por autor e conte quantos livros cada autor escreveu.
27. Mostre apenas os autores que têm mais de 1 livro cadastrado.
28. Mostre o ano de lançamento e quantos livros
