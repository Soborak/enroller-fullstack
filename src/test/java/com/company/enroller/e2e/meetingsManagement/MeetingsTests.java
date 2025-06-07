package com.company.enroller.e2e.meetingsManagement;
import com.company.enroller.e2e.BaseTests;
import com.company.enroller.e2e.Const;
import com.company.enroller.e2e.authentication.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;
import com.company.enroller.e2e.Const;
import com.company.enroller.e2e.Const;
public class MeetingsTests extends BaseTests {
    WebDriver driver;
    MeetingsPage page;
    LoginPage loginPage;
    @BeforeEach
    void setup() {
        this.dbInit();
        this.driver = WebDriverManager.chromedriver().create();
        this.page = new MeetingsPage(driver);
        this.loginPage = new LoginPage(driver);
        this.page.get(Const.HOME_PAGE);
    }
    @Test
    @DisplayName("[SPOTKANIA.1] The meeting should be added to your meeting list. It should contain a title and description.")
    void addNewMeeting() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        int meetingsBefore = this.page.getMeetingsCount();
        this.page.addNewMeeting(Const.MEETING_III_TITLE, Const.MEETING_DESC);
        // Asserts
        assertThat(this.page.getMeetingByTitle(Const.MEETING_III_TITLE)).isNotNull();
        // TODO: Dodaj sprawdzenie czy poprawnie został dodany opis.
        assertThat(this.page.getMeetingDescription(Const.MEETING_III_TITLE))
                .isEqualTo(Const.MEETING_DESC);
        // TODO: Dodaj sprawdzenie czy zgadza się aktualna liczba spotkań.
        assertThat(this.page.getMeetingsCount()).isEqualTo(meetingsBefore + 1);
    }
    //TODO: Sprawdź czy użytkownik może dodać spotkanie bez nazwy. Załóż że nie ma takiej możliwości a warunkiem jest nieaktywny przycisk "Dodaj".
    @Test
    @DisplayName("[SPOTKANIA.2] Nie można dodać spotkania bez nazwy – przycisk 'Dodaj' jest nieaktywny.")
    void cannotAddMeetingWithoutTitle() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.openAddMeetingForm();
        this.page.meetingTitleInput.clear();
        this.page.meetingDescInput.sendKeys("Dowolny opis");
        assertThat(this.page.confirmMeetingBtn.isEnabled()).isFalse();
    }
    //TODO: Sprawdź czy użytkownik może poprawnie zapisać się do spotkania.
    @Test
    @DisplayName("[SPOTKANIA.3] Użytkownik może poprawnie zapisać się do spotkania.")
    void canJoinMeeting() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.addNewMeeting(Const.MEETING_IV_TITLE, Const.MEETING_DESC);

        // Wyloguj się i zaloguj jako drugi użytkownik na nowym driverze
        this.page.quit();
        this.driver = WebDriverManager.chromedriver().create();
        this.page = new MeetingsPage(driver);
        this.loginPage = new LoginPage(driver);
        this.page.get(Const.HOME_PAGE);
        this.loginPage.loginAs(Const.USER_II_NAME);

        this.page.joinMeeting(Const.MEETING_IV_TITLE);
        assertThat(this.page.isUserParticipant(Const.MEETING_IV_TITLE, Const.USER_II_NAME)).isTrue();
    }
    //TODO: Sprawdź czy użytkownik może usunąć puste spotkanie.
    @Test
    @DisplayName("[SPOTKANIA.4] Użytkownik może usunąć puste spotkanie.")
    void canDeleteEmptyMeeting() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.addNewMeeting(Const.MEETING_V_TITLE, Const.MEETING_DESC);
        this.page.deleteMeeting(Const.MEETING_V_TITLE);
        assertThat(this.page.getMeetingByTitle(Const.MEETING_V_TITLE)).isNull();
    }
    @AfterEach
    void exit() {
        this.page.quit();
        this.removeAllMeeting();
    }
}