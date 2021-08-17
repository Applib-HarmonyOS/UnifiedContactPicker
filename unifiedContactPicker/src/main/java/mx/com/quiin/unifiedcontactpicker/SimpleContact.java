package mx.com.quiin.unifiedcontactpicker;

import java.util.Objects;
import ohos.agp.utils.Color;

/**
 * Created by Carlos Reyna on 22/01/17.
 */
/**
 *For to show simple contact.
 */
public class SimpleContact {
    private String displayName;
    private String communication;
    private String firstLetter;
    private Color letterBgColor;

    /**
     *Shows the simple contact.
     */
    public SimpleContact(final String displayName, final String communication) {
        this.displayName = displayName;
        this.communication = communication;
        this.firstLetter = displayName.substring(0, 1);
    }

    /**
     *Get the display name for the contact.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *Set the display name for the contact.
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     *Get the communication for the contact.
     */
    public String getCommunication() {
        return communication;
    }

    /**
     *Set the communication for the contact.
     */
    public void  setCommunication(final String communication) {
        this.communication = communication;
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, communication,
                firstLetter, letterBgColor);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SimpleContact) {
            SimpleContact contact = (SimpleContact) obj;
            return this.communication.equals(contact.communication);
        }
     return false;
    }

    /**
     *Gets the Background color.
     *
     */
    public Color getLetterBgColor() {
        return letterBgColor;
    }

    /**
     *Sets the Background color.
     * @param
     */
    public void setLetterBgColor(final Color letterBgColor) {
        this.letterBgColor = letterBgColor;
    }

    /**
     *Get the first letter of the contact.
     */
    public String getFirstLetter() {
        return firstLetter;
    }


    /**
     *Set the first letter of the contact.
     */
    public void setFirstLetter(final String firstLetter) {
        this.firstLetter = firstLetter;
    }
}

