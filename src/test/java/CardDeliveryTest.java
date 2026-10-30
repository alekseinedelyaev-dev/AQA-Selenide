
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class CardDeliveryTest {

    @BeforeAll
    public static void setup() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.browserSize = "1920x1080";
    }

    @Test
    public void shouldSuccessfullyBookCardDelivery() {

        // Рассчитываем дату встречи:
        // четыре дня от текущей даты
        String meetingDate = LocalDate.now()
                .plusDays(4)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // Открываем страницу
        open("/");

        // Заполняем город напрямую,
        // без выбора из выпадающего списка
        $("[data-test-id='city'] input")
                .setValue("Москва");

        // Очищаем дату, заполненную по умолчанию
        $("[data-test-id='date'] input").click();
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id='date'] input")
                .sendKeys(Keys.BACK_SPACE);

        // Вводим рассчитанную дату вручную
        $("[data-test-id='date'] input")
                .sendKeys(meetingDate);

        // Заполняем фамилию и имя
        $("[data-test-id='name'] input")
                .setValue("Иванов Иван");

        // Заполняем номер телефона
        $("[data-test-id='phone'] input")
                .setValue("+79991234567");

        // Устанавливаем согласие
        $("[data-test-id='agreement'] .checkbox__box")
                .click();

        // Отправляем форму
        $$("button").findBy(text("Забронировать"))
                .click();

        // Уведомление должно появиться не позднее 15 секунд
        $(".notification__title")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно!"));

        // Проверяем содержание уведомления
        $(".notification__content")
                .shouldHave(
                        text("Встреча успешно забронирована на " + meetingDate)
                );
    }
}
