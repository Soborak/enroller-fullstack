package com.company.enroller.e2e.meetingsManagement;

import com.company.enroller.e2e.BasePage;
import com.company.enroller.e2e.Const;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class MeetingsPage extends BasePage {

    @FindBy(xpath = "//*[contains(text(), \"" + Const.NEW_MEETING_BTN_LABEL + "\")]")
    @CacheLookup
    public WebElement addNewMeetingBtn;

    @FindBy(css = "form > input")
    public WebElement meetingTitleInput;

    @FindBy(css = "form > textarea")
    public WebElement meetingDescInput;

    @FindBy(css = "form > button")
    public WebElement confirmMeetingBtn;

    public MeetingsPage(WebDriver driver) {
        super(driver);
    }

    public void addNewMeeting(String title, String desc) {
        this.click(this.addNewMeetingBtn);
        this.meetingTitleInput.sendKeys(title);
        this.meetingDescInput.sendKeys(desc);
        this.click(this.confirmMeetingBtn);

    }

    // Dołączenie do spotkania (przycisk "Dołącz" w wierszu spotkania)
    public void joinMeeting(String title) {
        WebElement meeting = getMeetingByTitle(title);
        if (meeting != null) {
            WebElement joinBtn = meeting.findElement(By.xpath(".//button[contains(text(), 'Dołącz')]"));
            joinBtn.click();
        }
    }

    // Sprawdzenie obecności użytkownika na liście uczestników
    public boolean isUserParticipant(String meetingTitle, String userName) {
        WebElement meeting = getMeetingByTitle(meetingTitle);
        if (meeting != null) {
            // Załóżmy, że uczestnicy są w ul/li z klasą participant-list
            List<WebElement> participants = meeting.findElements(By.cssSelector(".participant-list li"));
            for (WebElement participant : participants) {
                if (participant.getText().equals(userName)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Usuwanie spotkania (przycisk "Usuń" w wierszu spotkania)
    public void deleteMeeting(String title) {
        WebElement meeting = getMeetingByTitle(title);
        if (meeting != null) {
            WebElement deleteBtn = meeting.findElement(By.xpath(".//button[contains(text(), 'Usuń')]"));
            deleteBtn.click();
        }
    }

    public int getMeetingsCount() {
        // Zakładam, że każdy wiersz spotkania ma klasę .meeting-row
        return driver.findElements(By.cssSelector(".meeting-row")).size();
    }

    public void openAddMeetingForm() {
        this.addNewMeetingBtn.click();
    }

    public String getMeetingDescription(String title) {
        WebElement meeting = getMeetingByTitle(title);
        if (meeting != null) {
            // Załóżmy, że opis spotkania jest w elemencie z klasą "meeting-description"
            WebElement descElem = meeting.findElement(By.cssSelector(".meeting-description"));
            return descElem.getText();
        }
        return null;
    }

}
