package mx.com.quiin.unifiedcontactpicker.interfaces;

import mx.com.quiin.unifiedcontactpicker.Contact;

/**
 * Created by Carlos Reyna on 21/01/17.
 *
/**
 *This is an interface for handling the sub contact list item selection.
 */
public interface ContactSelectionListener {
    void onContactSelected(Contact contact, String communication);
}
