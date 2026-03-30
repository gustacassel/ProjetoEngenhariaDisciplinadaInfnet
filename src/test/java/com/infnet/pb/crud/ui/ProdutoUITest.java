package com.infnet.pb.crud.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProdutoUITest {
    private final int port = 8080;
    private WebDriver driver;

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080"); // Evita quebra de layout

        this.driver = new ChromeDriver(options);
    }

    @Test
    @DisplayName("Deve exibir erro de validação ao tentar salvar produto com preço negativo via UI")
    void deveExibirErroAoSalvarPrecoInvalido() {
        driver.get("http://localhost:" + port + "/produtos/novo");

        // Preenche os campos do produto
        driver.findElement(By.id("nome")).sendKeys("Monitor Gamer");
        driver.findElement(By.id("descricao")).sendKeys("Monitor 144hz");
        driver.findElement(By.id("preco")).sendKeys("-150"); // Valor inválido
        driver.findElement(By.id("quantidadeEstoque")).sendKeys("10");

        // Clique seguro via JavaScript para evitar ElementClickInterceptedException
        WebElement botaoSalvar = driver.findElement(By.cssSelector("button[type='submit']"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", botaoSalvar);

        // Procura a mensagem de erro definida no seu layout/model
        WebElement feedbackErro = driver.findElement(By.className("error-message"));

        assertTrue(feedbackErro.getText().contains("O preço do produto deve ser maior que zero"),
                "A mensagem de validação de preço não apareceu!");
    }

    @Test
    @DisplayName("Deve navegar até a lista de produtos e verificar se a tabela está visível")
    void deveVerificarTabelaDeProdutos() {
        driver.get("http://localhost:" + port + "/produtos");

        // Verifica o título da página de produtos
        String h2Text = driver.findElement(By.tagName("h2")).getText();
        assertTrue(h2Text.contains("Lista de Produtos"), "O título da página de produtos está incorreto!");

        // Verifica se a tabela de produtos está sendo renderizada
        WebElement tabela = driver.findElement(By.tagName("table"));
        assertTrue(tabela.isDisplayed(), "A tabela de produtos não está visível!");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
