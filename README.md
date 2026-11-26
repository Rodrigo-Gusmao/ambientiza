# 🌱 Ambientiza API

API backend da plataforma **Ambientiza**, desenvolvida com **Spring Boot 4** e **Java 25**, responsável pelo gerenciamento de dados, autenticação, gamificação (quizzes, cálculo de ofensivas/*streaks*) e controle de catálogo e resgate de recompensas.

O repositório do front-end que consome esta API está disponível em:  
🔗 **[ambientiza-ui (GitHub)](https://github.com/ArthurAndrade588/ambientiza-ui)**

---

## 🎯 Sobre o Projeto

O **Ambientiza** é uma iniciativa educacional com foco em sustentabilidade inspirada em mecânicas de gamificação:
- **Quizzes Diários:** Desafios ecológicos diários com pontuação baseada em dificuldade e limite de 1 tentativa diária por quiz.
- **Sistema de Ofensiva (*Streak*):** Contabilização de dias seguidos de engajamento diário.
- **Loja e Recompensas:** Troca de pontos acumulados por benefícios virtuais, com controle transacional de estoque e saldo.

---

## 🛠️ Tecnologias Utilizadas

- **Java 25** & **Spring Boot 4.1.x**
- **Spring Data JPA** / **Hibernate**
- **MySQL 8+** (banco de dados relacional oficial)
- **Jakarta Bean Validation**
- **Spring Security** (hashing de senhas com SHA-256 / BCrypt)
- **Maven Wrapper**

---

## 📋 Pré-requisitos

Antes de iniciar a API, certifique-se de possuir:

1. **Java JDK 21 ou 25** instalado ([Verifique com `java -version`]).
2. **MySQL Server** em execução na porta padrão `3306`:
   - Usuário: `root`
   - Senha: `root`
   - *(O banco de dados `ambientiza` será criado automaticamente caso não exista graças ao parâmetro `createDatabaseIfNotExist=true`)*.
3. *(Opcional)* **Git** para clonar o repositório.

---

## ⚙️ Configuração do Banco de Dados

As configurações de conexão residem em [src/main/resources/application.properties](file:///c:/Users/Rodrigo/Desktop/ambientiza/src/main/resources/application.properties):

```properties
spring.application.name=api

spring.datasource.url=jdbc:mysql://localhost:3306/ambientiza?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.open-in-view=false
spring.jpa.hibernate.ddl-auto=update

# Usuário Administrador padrão gerado no seed
ambientiza.admin.email=admin@ambientiza.com
ambientiza.admin.senha=admin123
ambientiza.seed.demo=true
```

> **Usuário de Teste / Demonstração:**  
> Email: `arthur@ambientiza.com` | Senha: `123456`

---

## 🚀 Como Executar a API

### 1. Clonar e Acessar o Diretório
```bash
git clone https://github.com/ArthurAndrade588/ambientiza.git
cd ambientiza
```

### 2. Iniciar a Aplicação

Usando o Maven Wrapper embutido:

**No Windows (PowerShell / CMD):**
```powershell
.\mvnw.cmd spring-boot:run
```

**No Linux / macOS:**
```bash
./mvnw spring-boot:run
```

A API estará disponível por padrão em: **`http://localhost:8080`**.

---

## 🧪 Como Rodar os Testes

Para executar os testes unitários da aplicação:

```powershell
.\mvnw.cmd test
```

---

## 🖥️ Conectando com o Front-end

Para utilizar a interface completa de usuário:
1. Acesse o repositório **[ambientiza-ui](https://github.com/ArthurAndrade588/ambientiza-ui)**.
2. Siga as instruções do README da interface para instalar as dependências e iniciar o servidor front-end localmente.
3. Certifique-se de que a API (`http://localhost:8080`) esteja em execução para receber as requisições do front-end.

---

## 📌 Principais Rotas da API

- **`/usuario`**: Cadastro, autenticação e gerenciamento de perfil e ofensivas (*streaks*).
- **`/quiz`**: Quizzes diários, submissão de respostas e histórico de tentativas.
- **`/recompensas`**: Catálogo de itens e transações de resgate.
