# Projeto Engenharia Disciplinada - Infnet (PB)

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-90%25-brightgreen)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)

Este projeto consiste em um sistema de gerenciamento de clientes e produtos desenvolvido com Spring Boot, focado em práticas rigorosas de engenharia de software, testes automatizados e entrega contínua (CI/CD).

---

## 🏛️ Arquitetura Final do Sistema

A aplicação foi projetada utilizando **Layered Architecture (Arquitetura em Camadas)**, garantindo separação de responsabilidades, alta coesão e baixo acoplamento, em conformidade com os princípios SOLID e Clean Code.

### Camadas e Padrões Adotados:
* **Apresentação (Web):** Controllers (`ClienteController`, `ProdutoController`) responsáveis por orquestrar o tráfego HTTP, realizar Bean Validation e renderizar as views (Thymeleaf + Bootstrap 5).
* **Negócio (Service):** Serviços (`ClienteService`, `ProdutoService`) onde residem as regras de negócio. Utiliza **Cláusulas de Guarda** para evitar aninhamentos (Fail-fast) e foca na **Imutabilidade** dos dados transacionados.
* **Acesso a Dados (Persistence):** Repositórios do Spring Data JPA comunicando-se com o banco de dados.
* **Tratamento de Exceções:** Centralizado via `@ControllerAdvice` (`GlobalExceptionHandler`), garantindo que erros genéricos e de negócio sejam tratados de forma padronizada, retornando feedback adequado ao usuário.

---

## 🚀 Tecnologias Utilizadas
* **Backend:** Java 21, Spring Boot 3.x (v4.0.2 Managed), Spring Data JPA, Hibernate.
* **Frontend:** Thymeleaf, Bootstrap 5.
* **Banco de Dados:** H2 Database (Memória para testes/dev).
* **Testes:** JUnit 5, Mockito, Selenium WebDriver (Testes E2E/UI), jqwik (Testes de Fuzzing/Propriedade).
* **Qualidade e DevOps:** Maven, JaCoCo (Cobertura mínima de 90%), GitHub Actions (CI/CD).

---

## 📂 Estrutura do Projeto
A organização de pacotes segue as melhores práticas do ecossistema Spring, separando claramente as responsabilidades de cada componente:
```
src
├── main
│   ├── java/com/infnet/pb
│   │   ├── controller     # Camada de Exposição (Web/MVC)
│   │   ├── service        # Regras de Negócio e Lógica da Aplicação
│   │   ├── repository     # Acesso ao Banco de Dados (JPA/Hibernate)
│   │   ├── model          # Entidades e Objetos de Domínio
│   │   └── exception      # Tratamento Global de Erros e Exceções Customizadas
│   └── resources
│       ├── static         # Ativos Estáticos (CSS, JS, Imagens)
│       └── templates      # Views (Thymeleaf) organizadas por módulo (cliente, produto)
└── test
    └── java/com/infnet/pb
        ├── crud
        │   ├── controller # Testes de Unidade e MockMvc
        │   ├── service    # Testes de Serviço e Fuzzing (jqwik)
        │   └── ui         # Testes de Interface de Ponta a Ponta (Selenium)
        └── exception      # Validação do Tratamento de Erros
```

---

## ⚙️ Pipeline CI/CD (GitHub Actions)

O projeto utiliza **GitHub Actions** para garantir a integração contínua (CI). O workflow é disparado automaticamente a cada `push` ou `pull_request` na branch `main`, garantindo a integridade do código antes da união de novas funcionalidades.

### Fluxo de Execução:
1. **Checkout & Setup:** O ambiente é preparado com a JDK 21 (Temurin) e o cache do Maven é configurado para otimizar o tempo de execução.
2. **Build & Test:** Execução do comando `mvn clean verify`. Este estágio é o coração do pipeline, onde são validados:
  - Compilação do projeto.
  - Testes Unitários e de Fuzzing (jqwik).
  - Testes de Interface (Selenium em modo headless).
3. **Persistência de Artefatos (Uploads):**
  - **Relatório JaCoCo:** O relatório de cobertura é salvo como artefato do job, permitindo auditoria da qualidade mesmo após o término da execução.
  - **Logs de Erro (Surefire):** Em caso de falha nos testes, os logs detalhados do Maven Surefire são armazenados para facilitar a depuração (debug).

---

## 📖 Manual de Execução

Siga os passos abaixo para compilar, testar e rodar a aplicação em seu ambiente local.

### 1. Pré-requisitos
Certifique-se de ter instalado:
* JDK 21
* Maven 3.9+
* Google Chrome (Para execução dos testes de UI automatizados)

### 2. Compilação e Build
Para limpar builds anteriores e compilar o projeto:
```bash
mvn clean compile
```

### 3. Estratégia de Testes Automatizados
O projeto utiliza uma suíte rigorosa de testes para garantir a integridade. Para rodar:
```bash
mvn clean verify
```
O comando `verify` executará:
* **Testes Unitários**: Validação isolada de regras de negócio com Mockito.
* **Testes de Fuzzing (jqwik)**: Executa milhares de tentativas com dados randômicos no Service para garantir resiliência.
* **Testes de Integração e UI (Selenium):**: Validação de fluxos reais do usuário (ex: submissão de formulários) em modo headless.

### 4. Relatório de Cobertura (JaCoCo)
Atingimos uma cobertura de testes de 90%. O relatório detalhado é gerado automaticamente a cada build bem-sucedido.
* **Caminho**: `target/site/jacoco/index.html`
* **Como visualizar**: Abra o arquivo em qualquer navegador para auditar as linhas e branches testados.

### 5. Executando a Aplicação
Para iniciar o servidor localmente:
```bash
mvn spring-boot:run
```
* Acesse `http://localhost:8080` para interagir com a aplicação via interface web.
* Acesso ao H2 Console: `http://localhost:8080/h2-console`
  * JDBC URL: `jdbc:h2:mem:cruddb`
  * Username: `sa`
  * Password: (deixe em branco)