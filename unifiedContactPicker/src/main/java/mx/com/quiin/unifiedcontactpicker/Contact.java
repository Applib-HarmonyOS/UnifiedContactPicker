package mx.com.quiin.unifiedcontactpicker;

import java.util.List;
import ohos.agp.utils.Color;

/**
 * Created by Carlos Reyna on 20/01/17.
 */
public class Contact {
    private String firstLetter;
    private String name;
    private String communication;
    private boolean mSelected;
    private Color letterBgColor;
    private boolean mExpanded;
    private List<String> mcommunication;

    /**
     *Gets the Background color.
     */
    public Color getLetterBgColor() {
        return letterBgColor;
    }

    /**
     *Sets the Background color.
     */
    public void setLetterBgColor(final Color letterBgColor) {
        this.letterBgColor = letterBgColor;
    }

    /**
     *Contact display.
     */
    public Contact(final String name, final String communication,
                   final Color color, final List<String> communications) {
        this.firstLetter = name.substring(0, 1);
        this.letterBgColor = color;
        this.name = name;
        this.communication = communication;
        this.mSelected = false;
        this.mcommunication = communications;
    }

    /**
     *get name of the contact.
     */
    public String getName() {
        return name;
    }

    /**
     *Set name of the contact.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *get first letter of contact.
     */
    public String getFirstLetter() {
        return firstLetter;
    }

    /**
     *Set first letter of contact.
     */
    public void setFirstLetter(final String firstLetter) {
        this.firstLetter = firstLetter;
    }

    /**
     *Get Communication of contact.
     */
    public String getCommunication() {
        return communication;
    }

    /**
     *Set Communication of contact.
     */
    public void setCommunication(final String communication) {
        this.communication = communication;
    }

    /**
     *contact is selected.
     */
    public boolean isSelected() {
    return mSelected;
    }

    /**
     *
     */
    public void setisSelected(final boolean isSelected) {
    this.mSelected = isSelected;
    }

    /**
     *contact is expanded.
     */
    public boolean isExpanded() {
        return mExpanded;
    }

    /**
     *
     */
    public void setisExpanded(final boolean isExpanded) {
    this.mExpanded = isExpanded;
    }

    /**
     *Get communication of contact.
     */
    public List<String> getCommunications() {
        return mcommunication;
    }

    /**
     *Set communication of contact.
     */
    public void setCommunication(final List<String> mCommunications) {
    this.mcommunication = mCommunications;
    }

}
