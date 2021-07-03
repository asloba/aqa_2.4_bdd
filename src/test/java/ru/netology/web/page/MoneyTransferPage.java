package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {
    private SelenideElement heading = $(byText("Пополнение карты"));
    private final SelenideElement amountInput = $("[data-test-id=amount] input");
    private final SelenideElement fromInput = $("[data-test-id=from] input");
    private final SelenideElement transferButton = $("button[data-test-id=action-transfer]");
    private final SelenideElement errorMessage = $(withText("Ошибка"));

    public MoneyTransferPage() {
        heading.shouldBe(Condition.visible);
    }

    public DashboardPage moneyTransfer(DataHelper.CardInfo cardInfo, int amount) {
        amountInput.doubleClick().setValue(String.valueOf(amount));
        fromInput.setValue(cardInfo.getCard());
        transferButton.click();
        return new DashboardPage();
    }

        public void moneyTransferWithCardFieldEmpty(int amount) {
        amountInput.doubleClick().setValue(String.valueOf(amount));
        transferButton.click();
        errorMessage.shouldBe(Condition.visible);
    }
    public void emptyMoneyTransfer() {
        transferButton.click();
        errorMessage.shouldBe(Condition.visible);
    }

    public void errorNotification() {
        errorMessage.shouldBe(Condition.visible);
    }
}
