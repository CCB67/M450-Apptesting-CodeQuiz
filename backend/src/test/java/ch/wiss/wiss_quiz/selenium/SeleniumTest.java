package ch.wiss.wiss_quiz.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class SeleniumTest {

    private static final String BASE_URL =
            System.getProperty("quiz.baseUrl", "http://localhost:3000");

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void quizPage_showsQuestionAndFourAnswerButtons() {
        // Arrange / Vorbereitung: Quiz-Seite öffnen
        driver.get(BASE_URL + "/quiz");

        // Act / Aktion: Fragetext und Antwortbuttons auf der Seite suchen
        WebElement questionText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")));

        wait.until(driver -> driver.findElements(
                By.cssSelector("[data-testid^='answer-button-']")).size() > 0);

        List<WebElement> answerButtons = driver.findElements(
                By.cssSelector("[data-testid^='answer-button-']"))
                .stream()
                .filter(WebElement::isDisplayed)
                .toList();

        // Assert / Prüfung
        assertFalse(questionText.getText().isBlank(),
                "Der Fragetext sollte sichtbar sein.");
        assertEquals(4, answerButtons.size(),
                "Es sollten 4 sichtbare Antwortbuttons vorhanden sein.");
    }

    @Test
    void selectingAnswer_showsNextButton() {
        // Arrange / Vorbereitung: Quiz-Seite öffnen
        driver.get(BASE_URL + "/quiz");

        wait.until(driver -> driver.findElements(
                By.cssSelector("[data-testid^='answer-button-']")).size() > 0);

        List<WebElement> answerButtons = driver.findElements(
                By.cssSelector("[data-testid^='answer-button-']"))
                .stream()
                .filter(WebElement::isDisplayed)
                .toList();

        assertFalse(answerButtons.isEmpty(),
                "Es sollte mindestens ein Antwortbutton sichtbar sein.");

        // Act / Aktion: Erste Antwort anklicken
        answerButtons.get(0).click();

        // Assert / Prüfung: Weiter-Button erscheint und ist klickbar
        WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-testid='next-button']")));

        assertTrue(nextButton.isDisplayed(),
                "Nach Auswahl einer Antwort sollte der Weiter-Button sichtbar sein.");
        assertTrue(nextButton.isEnabled(),
                "Nach Auswahl einer Antwort sollte der Weiter-Button aktiv sein.");
    }

    @Test
    void clickingNextButton_loadsNextQuestion() {
        // Arrange / Vorbereitung: Quiz-Seite öffnen und erste Frage merken
        driver.get(BASE_URL + "/quiz");

        WebElement firstQuestionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")));

        String firstQuestion = firstQuestionElement.getText();

        wait.until(driver -> driver.findElements(
                By.cssSelector("[data-testid^='answer-button-']")).size() > 0);

        List<WebElement> answerButtons = driver.findElements(
                By.cssSelector("[data-testid^='answer-button-']"))
                .stream()
                .filter(WebElement::isDisplayed)
                .toList();

        assertFalse(answerButtons.isEmpty(),
                "Es sollte mindestens ein Antwortbutton sichtbar sein.");

        // Act / Aktion: Antwort wählen und auf Weiter klicken
        answerButtons.get(0).click();

        WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-testid='next-button']")));
        nextButton.click();

        // Warten, bis sich der Fragetext geändert hat
        wait.until(driver -> {
            String currentQuestion = driver.findElement(
                    By.cssSelector("[data-testid='question-text']")).getText();
            return !currentQuestion.isBlank() && !currentQuestion.equals(firstQuestion);
        });

        String secondQuestion = driver.findElement(
                By.cssSelector("[data-testid='question-text']")).getText();

        // Assert / Prüfung
        assertNotEquals(firstQuestion, secondQuestion,
                "Nach Klick auf Weiter sollte eine neue Frage angezeigt werden.");
    }
}
