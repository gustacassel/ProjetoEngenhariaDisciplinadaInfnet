# Projeto Engenharia Disciplinada - Infnet (PB)

Este projeto consiste em um sistema de gerenciamento de clientes desenvolvido com Spring Boot, focado em práticas de engenharia de software, testes automatizados e integração contínua.

## Tecnologias Utilizadas
* Java 21
* Spring Boot 3.x (v4.0.2 Managed)
* Maven (Gerenciador de dependências)
* H2 Database (Banco de dados em memória para testes e dev)
* Selenium WebDriver (Testes de Interface)
* jqwik (Testes de Fuzzing/Propriedade)
* JaCoCo (Relatórios de cobertura de código)

---

## Manual de Execução

Siga os passos abaixo para compilar, testar e rodar a aplicação em seu ambiente local.

### 1. Pré-requisitos
Certifique-se de ter instalado:
* JDK 21
* Maven 3.9+
* Google Chrome (Para execução dos testes de UI)

### 2. Compilação e Build
Para limpar builds anteriores e compilar o projeto:
```
mvn clean compile
```

### 3. Execução de Testes Automatizados
O projeto utiliza uma suíte rigorosa de testes que garantem a estabilidade da aplicação.
```
mvn clean verify
```

Nota: O comando verify executará:
- Testes Unitários: Validação de regras de negócio.
- Testes de Fuzzing (jqwik): Executa 1.000 tentativas com dados randômicos no ClienteService.
- Testes de UI (Selenium): Validação de fluxo de tela em modo headless (invisível).

### 4. Relatório de Cobertura (JaCoCo)
Após rodar os testes, o relatório de cobertura detalhado é gerado automaticamente pelo JaCoCo.
- Caminho do arquivo: target/site/jacoco/index.html
- Como visualizar: Abra o arquivo acima em qualquer navegador para verificar a porcentagem de linhas e ramos (branches) testados.

### 5. Executando a Aplicação
Para iniciar o servidor localmente sem o uso de containers:
```
mvn spring-boot:run
```

- Acesse em: http://localhost:8080
- Console H2: http://localhost:8080/h2-console
    - JDBC URL: jdbc:h2:mem:cruddb
    - User: sa | Password: (vazio)

---

## Integração Contínua (CI/CD)
Este repositório está configurado com GitHub Actions. O pipeline é disparado a cada push ou pull_request na branch main, garantindo que o código sempre compile e passe em todos os testes automatizados (Unitários, Fuzzing e UI) antes da integração final.

---