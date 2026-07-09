package ch.wiss.wiss_quiz.selenium;

import static org.junit.jupiter.api.Assertions.*;

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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

class SeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void quizPage_showsQuestionText() {
        driver.get("http://localhost:3000/quiz");

        WebElement questionText = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")
            )
        );

        String text = questionText.getText();

        assertFalse(text.isBlank(), "Der Fragetext sollte sichtbar und nicht leer sein.");
    }

    @Test
    void quizPage_showsAnswerButtons() {
        driver.get("http://localhost:3000/quiz");

        wait.until(driver ->
            driver.findElements(By.cssSelector("[data-testid^='answer-button-']")).size() > 0
        );

        List<WebElement> buttons = driver.findElements(
            By.cssSelector("[data-testid^='answer-button-']")
        );

        assertFalse(buttons.isEmpty(), "Es sollten Antwort-Buttons sichtbar sein.");
        assertEquals(4, buttons.size(), "Es sollten genau 4 Antwort-Buttons vorhanden sein.");
    }

    @Test
    void clickingAnswer_changesPageState() {
        driver.get("http://localhost:3000/quiz");

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")
            )
        );

        wait.until(driver ->
            driver.findElements(By.cssSelector("[data-testid^='answer-button-']")).size() > 0
        );

        List<WebElement> buttons = driver.findElements(
            By.cssSelector("[data-testid^='answer-button-']")
        );

        buttons.get(0).click();

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='next-button']")
            )
        );

        String bodyText = driver.findElement(By.tagName("body")).getText();

        assertFalse(bodyText.isBlank(), "Nach dem Klick sollte weiterhin Inhalt sichtbar sein.");
    }

    @Test
    void nextButton_isVisible_afterAnswer() {
        driver.get("http://localhost:3000/quiz");

        wait.until(driver ->
            driver.findElements(By.cssSelector("[data-testid^='answer-button-']")).size() > 0
        );

        List<WebElement> buttons = driver.findElements(
            By.cssSelector("[data-testid^='answer-button-']")
        );

        buttons.get(0).click();

        WebElement nextButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='next-button']")
            )
        );

        assertFalse(nextButton.getText().isBlank(), "Der Next-Button sollte sichtbar sein.");
    }

    @Test
    void quiz_can_progress_through_multiple_questions() {
        driver.get("http://localhost:3000/quiz");

        WebElement firstQuestionElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")
            )
        );

        String firstQuestion = firstQuestionElement.getText();

        wait.until(driver ->
            driver.findElements(By.cssSelector("[data-testid^='answer-button-']")).size() > 0
        );

        List<WebElement> firstButtons = driver.findElements(
            By.cssSelector("[data-testid^='answer-button-']")
        );

        firstButtons.get(0).click();

        WebElement firstNextButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='next-button']")
            )
        );

        firstNextButton.click();

        WebElement secondQuestionElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")
            )
        );

        String secondQuestion = secondQuestionElement.getText();

        assertFalse(secondQuestion.isBlank(), "Die zweite Frage sollte sichtbar sein.");

        wait.until(driver ->
            driver.findElements(By.cssSelector("[data-testid^='answer-button-']")).size() > 0
        );

        List<WebElement> secondButtons = driver.findElements(
            By.cssSelector("[data-testid^='answer-button-']")
        );

        secondButtons.get(0).click();

        WebElement secondNextButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='next-button']")
            )
        );

        secondNextButton.click();

        WebElement thirdQuestionElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")
            )
        );

        String thirdQuestion = thirdQuestionElement.getText();

        assertFalse(thirdQuestion.isBlank(), "Die dritte Frage sollte sichtbar sein.");

        assertFalse(firstQuestion.equals(secondQuestion) && secondQuestion.equals(thirdQuestion),
                "Das Quiz sollte zu weiteren Fragen fortschreiten.");
    }

    @Test
    void nextButton_loadsNextQuestion() {
        driver.get("http://localhost:3000/quiz");

        String firstQuestion = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")
            )
        ).getText();

        WebElement answerButton = driver.findElement(
            By.cssSelector("[data-testid='answer-button-1']")
        );

        answerButton.click();

        WebElement nextButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='next-button']")
            )
        );

        nextButton.click();

        String secondQuestion = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")
            )
        ).getText();

        assertFalse(secondQuestion.isBlank(), "Die nächste Frage sollte sichtbar sein.");
        assertNotEquals(firstQuestion, secondQuestion,
                "Nach Klick auf Next sollte eine andere Frage erscheinen.");
    }

    // Attention: the next tests will only run green,
    // if the random seed 42 is set in QuizService->pickQuizQuestions()
    @Test
    void correctAnswer_increasesScore() {
        driver.get("http://localhost:3000/quiz");

        WebElement correctAnswerButton = driver.findElement(
            By.cssSelector("[data-testid='answer-button-1']")
        );

        correctAnswerButton.click();

        WebElement scoreText = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='score-text']")
            )
        );

        assertTrue(scoreText.getText().contains("100"),
                "Nach einer richtigen Antwort sollte der Score 100 sein.");
    }

    @Test
    void score_accumulates_over_multiple_questions() {
        driver.get("http://localhost:3000/quiz");

        String firstQuestion = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")
            )
        ).getText();

        WebElement firstAnswerButton = driver.findElement(
            By.cssSelector("[data-testid='answer-button-1']")
        );

        firstAnswerButton.click();

        WebElement nextButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='next-button']")
            )
        );

        nextButton.click();

        String secondQuestion = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")
            )
        ).getText();

        assertFalse(secondQuestion.isBlank(), "Die nächste Frage sollte sichtbar sein.");
        assertNotEquals(firstQuestion, secondQuestion,
                "Nach Klick auf Next sollte eine andere Frage erscheinen.");

        WebElement secondAnswerButton = driver.findElement(
            By.cssSelector("[data-testid='answer-button-3']")
        );

        secondAnswerButton.click();

        WebElement scoreText = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='score-text']")
            )
        );

        assertTrue(scoreText.getText().contains("200"),
                "Nach zwei richtigen Antworten sollte der Score 200 sein.");
    }

    @Test
    void selectCategory_and_answerFirstQuestion_correctly() {
        driver.get("http://localhost:3000/quiz");

        WebElement selectElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='category-select']")
            )
        );

        Select dropdown = new Select(selectElement);
        dropdown.selectByVisibleText("Applikationen testen");

        WebElement startButton = driver.findElement(
            By.cssSelector("[data-testid='start-quiz-button']")
        );

        startButton.click();

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='question-text']")
            )
        );

        WebElement correctAnswer = driver.findElement(
            By.cssSelector("[data-testid='answer-button-2']")
        );

        correctAnswer.click();

        WebElement scoreText = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-testid='score-text']")
            )
        );

        assertTrue(scoreText.getText().contains("100"),
                "Score sollte nach richtiger Antwort 100 sein");
    }
}