package com.infnet.pb.crud.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteUITest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Modo invisível (essencial para CI/CD)
        options.addArguments("--no-sandbox");    // Necessário para rodar como root no Linux do GitHub
        options.addArguments("--disable-dev-shm-usage"); // Evita falta de memória em containers
        options.addArguments("--window-size=1920,1080");

        this.driver = new ChromeDriver(options);
    }

    @Test
    @DisplayName("Deve exibir erro de validação ao tentar salvar nome curto via UI")
    void deveExibirErroAoSalvarNomeCurto() {
        driver.get("http://localhost:" + port + "/clientes/novo");

        // Preenche os campos
        driver.findElement(By.id("nome")).sendKeys("Ab"); // Nome inválido (< 3 caracteres)
        driver.findElement(By.id("email")).sendKeys("teste@ui.com");
        driver.findElement(By.id("dataNascimento")).sendKeys("10102000");
        driver.findElement(By.id("limiteCredito")).sendKeys("1000");

        // Clica no botão de salvar
        WebElement botaoSalvar = driver.findElement(By.cssSelector("button[type='submit']"));

        botaoSalvar.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Procura a mensagem de erro
        WebElement feedbackErro = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("invalid-feedback"))
        );

        assertTrue(feedbackErro.getText().contains("O nome deve ter entre 3 e 100 caracteres"),
                "A mensagem de validação não apareceu ou está incorreta! Texto atual: " + feedbackErro.getText());
    }

    @Test
    @DisplayName("Deve navegar até a lista e verificar se a tabela existe")
    void deveVerificarTabelaDeClientes() {
        driver.get("http://localhost:" + port + "/clientes");

        // Verifica se o título da nossa lista estilizada está presente
        String h2Text = driver.findElement(By.tagName("h2")).getText();
        assertTrue(h2Text.contains("Lista de Clientes"));

        // Verifica se a tabela está lá
        WebElement tabela = driver.findElement(By.tagName("table"));
        assertTrue(tabela.isDisplayed());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}