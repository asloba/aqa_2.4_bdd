package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoneyTransferTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }


    @Test
    @Order(1)
    void shouldTransferMoneyFromSecondToFirst() {
        val dashBoardPage = new DashboardPage();
        val firstCardBalanceBeforeTransfer = dashBoardPage.getFirstCardBalance();
        val secondCardBalanceBeforeTransfer = dashBoardPage.getSecondCardBalance();
        val moneyTransferPage = dashBoardPage.clickOnFirstCard();
        val secondCardNumber = DataHelper.getSecondCardNumber();
        moneyTransferPage.moneyTransfer(secondCardNumber, 1000);
        val expectedFirstCardBalanceAfterTransfer = DataHelper.balanceIncrease(firstCardBalanceBeforeTransfer, 1000);
        val expectedSecondCardBalanceAfterTransfer = DataHelper.balanceDecrease(secondCardBalanceBeforeTransfer, 1000);
        val realFirstCardBalanceAfterTransfer = dashBoardPage.getFirstCardBalance();
        val realSecondCardBalanceAfterTransfer = dashBoardPage.getSecondCardBalance();
        assertEquals(expectedFirstCardBalanceAfterTransfer, realFirstCardBalanceAfterTransfer);
        assertEquals(expectedSecondCardBalanceAfterTransfer, realSecondCardBalanceAfterTransfer);
    }

    @Test
    @Order(2)
    void shouldTransferMoneyFromFirstToSecond() {
        val dashBoardPage = new DashboardPage();
        val firstCardBalanceBeforeTransfer = dashBoardPage.getFirstCardBalance();
        val secondCardBalanceBeforeTransfer = dashBoardPage.getSecondCardBalance();
        val moneyTransferPage = dashBoardPage.clickOnSecondCard();
        val firstCardNumber = DataHelper.getFirstCardNumber();
        moneyTransferPage.moneyTransfer(firstCardNumber, 100);
        val expectedFirstCardBalanceAfterTransfer = DataHelper.balanceDecrease(firstCardBalanceBeforeTransfer, 100);
        val expectedSecondCardBalanceAfterTransfer = DataHelper.balanceIncrease(secondCardBalanceBeforeTransfer, 100);
        val realFirstCardBalanceAfterTransfer = dashBoardPage.getFirstCardBalance();
        val realSecondCardBalanceAfterTransfer = dashBoardPage.getSecondCardBalance();
        assertEquals(expectedFirstCardBalanceAfterTransfer, realFirstCardBalanceAfterTransfer);
        assertEquals(expectedSecondCardBalanceAfterTransfer, realSecondCardBalanceAfterTransfer);
    }

    // Этот тест проверяет возможность перевода на сумму большую, чем на балансе. Он должен упасть, т.к. ошибка не появляется
    @Test
    @Order(3)
    void shouldTransferMoneyOverLimit() {
        val dashBoardPage = new DashboardPage();
        val moneyTransferPage = dashBoardPage.clickOnFirstCard();
        val secondCardNumber = DataHelper.getSecondCardNumber();
        moneyTransferPage.moneyTransfer(secondCardNumber, 10000);
        moneyTransferPage.errorNotification();
    }

    @Test
    @Order(4)
    void shouldTransferMoneyEqualLimit() {
        val dashBoardPage = new DashboardPage();
        val firstCardBalanceBeforeTransfer = dashBoardPage.getFirstCardBalance();
        val secondCardBalanceBeforeTransfer = dashBoardPage.getSecondCardBalance();
        val moneyTransferPage = dashBoardPage.clickOnSecondCard();
        val firstCardNumber = DataHelper.getFirstCardNumber();
        moneyTransferPage.moneyTransfer(firstCardNumber, firstCardBalanceBeforeTransfer);
        val expectedFirstCardBalanceAfterTransfer = DataHelper.balanceDecrease(firstCardBalanceBeforeTransfer, firstCardBalanceBeforeTransfer);
        val expectedSecondCardBalanceAfterTransfer = DataHelper.balanceIncrease(secondCardBalanceBeforeTransfer, firstCardBalanceBeforeTransfer);
        val realFirstCardBalanceAfterTransfer = dashBoardPage.getFirstCardBalance();
        val realSecondCardBalanceAfterTransfer = dashBoardPage.getSecondCardBalance();
        assertEquals(expectedFirstCardBalanceAfterTransfer, realFirstCardBalanceAfterTransfer);
        assertEquals(expectedSecondCardBalanceAfterTransfer, realSecondCardBalanceAfterTransfer);
    }

    // Этот тест проверяет возможность перевода суммы, равной нулю. Он должен упасть, т.к. ошибка не появляется
    @Test
    @Order(5)
    void shouldTransferZero() {
        val dashBoardPage = new DashboardPage();
        val moneyTransferPage = dashBoardPage.clickOnFirstCard();
        val secondCardNumber = DataHelper.getSecondCardNumber();
        moneyTransferPage.moneyTransfer(secondCardNumber, 0);
        moneyTransferPage.errorNotification();
    }

    @Test
    @Order(6)
    void shouldTransferMoneyFromNonexistentCard() {
        val dashBoardPage = new DashboardPage();
        val moneyTransferPage = dashBoardPage.clickOnSecondCard();
        val invalidCardNumber = DataHelper.getInvalidCardNumber();
        moneyTransferPage.moneyTransfer(invalidCardNumber, 5000);
        moneyTransferPage.errorNotification();
    }

    @Test
    @Order(7)
    void shouldTransferMoneyWithEmptyFields() {
        val dashBoardPage = new DashboardPage();
        val moneyTransferPage = dashBoardPage.clickOnSecondCard();
        moneyTransferPage.emptyMoneyTransfer();
    }

    @Test
    @Order(8)
    void shouldTransferMoneyWithEmptyCard() {
        val dashBoardPage = new DashboardPage();
        val moneyTransferPage = dashBoardPage.clickOnSecondCard();
        moneyTransferPage.moneyTransferWithCardFieldEmpty(100);
    }

    // этот тест проверяет возможность перевода денег с карты 1 на карту 1. Он должен упасть, т.к. ошибка не появляется
    @Test
    @Order(9)
    void shouldTransferMoneyFromTheSameCard() {
        val dashBoardPage = new DashboardPage();
        val moneyTransferPage = dashBoardPage.clickOnFirstCard();
        val firstCardNumber = DataHelper.getFirstCardNumber();
        moneyTransferPage.moneyTransfer(firstCardNumber, 100);
        moneyTransferPage.errorNotification();
    }
}

