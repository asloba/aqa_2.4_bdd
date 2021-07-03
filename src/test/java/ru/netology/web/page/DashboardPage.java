package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.cssValue;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private SelenideElement firstCard = $(".list__item [data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
    private SelenideElement secondCard = $(".list__item [data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");
    private final SelenideElement firstCardButton = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0'] .button");
    private final SelenideElement secondCardButton = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d'] .button");


    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public MoneyTransferPage clickOnFirstCard() {
        firstCardButton.click();
        return new MoneyTransferPage();
    }

    public MoneyTransferPage clickOnSecondCard() {
        secondCardButton.click();
        return new MoneyTransferPage();
    }

    public int getFirstCardBalance() {
        val text = firstCard.getText();
        return extractBalance(text);
    }

    public int getSecondCardBalance() {
        val text = secondCard.getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

}
