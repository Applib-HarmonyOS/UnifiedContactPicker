package mx.com.quiin.unifiedcontactpicker.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import mx.com.quiin.unifiedcontactpicker.Contact;
import mx.com.quiin.unifiedcontactpicker.SimpleContact;
import mx.com.quiin.unifiedcontactpicker.adapters.ContactAdapter;
import mx.com.quiin.unifiedcontactpicker.interfaces.ContactSelectionListener;

import mx.com.quiin.unifiedcontactpicker.ResourceTable;
import mx.com.quiin.unifiedcontactpicker.utils.DbUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.bundle.IBundleManager;
import ohos.security.SystemPermission;
import ohos.sysappcomponents.contact.ContactsCollection;
import ohos.sysappcomponents.contact.entity.Email;
import ohos.sysappcomponents.contact.entity.PhoneNumber;

/**
 *shows the contact list page to select.
 */
public class ContactPickerAbility extends AbilitySlice implements ListContainer.ItemClickedListener,
        ContactSelectionListener {

    public static final String CP_SELECTED_CONT = "CP_SELECTED_CONTACTS";
    private TextField mSearchQuery;
    private final ArrayList<SimpleContact> mSelectedContacts = new ArrayList<>();
    private String mSearchqueryText;
    private static final int SIZE_COUNT_ONE = 1;
    public static final int SEMICOL_HASHCODE = 65307;
    private ContactAdapter mContactAdapter;
    private ArrayList<Contact> mContactsList;

    /**
     * Starts the ability.
     */
    @Override
    protected void onStart(final Intent intent) {
        super.onStart(intent);
        if (verifySelfPermission(SystemPermission.READ_CONTACTS)
                != IBundleManager.PERMISSION_GRANTED
                && canRequestPermission(SystemPermission.READ_CONTACTS)) {
            requestPermissionsFromUser(new String[]{SystemPermission.READ_CONTACTS}, 0);
        }
        super.setUIContent(ResourceTable.Layout_contact_picker);
        mSearchQuery = (TextField) findComponentById(ResourceTable.Id_searchbar);
        mSearchQuery.setText("");
        showFabButton();
        readContacts();
    }

    /**
    * shows the fab button(tick button) to send the selected contacts to the choose contact page.
    */
    private void showFabButton() {
        Button fabButton = (Button) findComponentById(ResourceTable.Id_fab);
        fabButton.setText(ContactAdapter.TICK_ICON);
        fabButton.setClickedListener(listener -> {
            sendSelectedContact();
        });
    }

    /**
     *Sends the selected contact to the choose contact page.
     */
    private void sendSelectedContact() {
        Intent result = new Intent();
        result.setParam(CP_SELECTED_CONT, mSelectedContacts);
        setResult(result);
        terminate();
    }

    /**
     *Reading the contacts and adapter initialization.
     */
    private void readContacts() {
        /**
         *Currently we have a limitation to copy contacts from data base in HOS,
         * the static list getTempContactList() is passed for adapter.Future when
         * limitation is fixed mContactsList need to be passed instead of getTempContactList().
         */
        mContactAdapter = new ContactAdapter(getTempContactList(), this, this);
        ListContainer contctListContnr = (ListContainer)
                findComponentById(ResourceTable.Id_cp_listview);
        contctListContnr.setItemProvider(mContactAdapter);
        contctListContnr.setLongClickable(false);
        contctListContnr.setItemClickedListener(this);
        mSearchQuery.addTextObserver(textListener);
    }

    /**
     *Handle the click action for the list item
     */
    @Override
    public void onItemClicked(final ListContainer contctslstContnr,
                              final Component component, final int position, final long listItem) {
        Contact clickedContact = (Contact) contctslstContnr.getItemProvider().getItem(position);
        MaterialLetterIcon contactIcon = (MaterialLetterIcon) component
                .findComponentById(ResourceTable.Id_MaterialFirstLetter);
        ListContainer commListContainer = (ListContainer) component
                .findComponentById(ResourceTable.Id_CommunicationList);
        if (clickedContact.isSelected()) {
            contactIcon.setLetter(clickedContact.getFirstLetter());
            contactIcon.setShapeColor(clickedContact.getLetterBgColor());
            clickedContact.setisSelected(false);
            clickedContact.setisExpanded(false);
            removeContact(clickedContact);
       } else {
           if (clickedContact.getCommunications().size() > SIZE_COUNT_ONE) {
                clickedContact.setisExpanded(!clickedContact.isExpanded());
           } else {
               commListContainer.setVisibility(Component.INVISIBLE);
               clickedContact.setisExpanded(false);
               contactIcon.setLetter(ContactAdapter.TICK_ICON);
               contactIcon.setShapeColor(Color.GREEN);
               clickedContact.setisSelected(true);
               addContacts(clickedContact);
           }
       }
        BaseItemProvider view = contctslstContnr.getItemProvider();
        view.notifyDataChanged();
   }

    /**
     * Removing  the selected contact from Search bar.
     */
    private void removeContact(final Contact removedContact) {
        String searchBarText = mSearchQuery.getText();
        int colonPosition = searchBarText.lastIndexOf(';');
        searchBarText = searchBarText.substring(0, colonPosition + 1);
        String selectedContComm = "< " + removedContact.getCommunication().trim() + " > ;";
        String replaceText = searchBarText.replaceAll(selectedContComm, "");
        mSearchQuery.setText(replaceText);
        for (SimpleContact contact : mSelectedContacts) {
            if (contact.getCommunication().equals(removedContact.getCommunication().trim())) {
                mSelectedContacts.remove(contact);
            }
        }
    }

    /**
     * Adding the selected contacts to Search bar.
     */
    private void addContacts(final Contact selectedContact) {
        String searchBarText = mSearchQuery.getText();
        int colonPosition = searchBarText.lastIndexOf(';');
        String communication = searchBarText.substring(0, colonPosition + 1) + " < "
               + selectedContact.getCommunication().trim() + " > ; ";
        mSearchQuery.setText(communication);
        SimpleContact contact = new SimpleContact(selectedContact.getName().trim(),
               selectedContact.getCommunication().trim());
        mSelectedContacts.add(contact);
    }

    /**
     * Temporary list to show contact list.
     * This method can be removed while populating the list from Contact database.
     */
    private ArrayList<Contact> getTempContactList() {
        ArrayList<Contact> list = new ArrayList<>();
        List<String> list0 = new ArrayList<>();
        list0.add("9878987823");
        list.add(new Contact("Arun", "9878987823", getRandomColor(), list0));
        List<String> list1 = new ArrayList<>();
        list1.add("001182398");
        list1.add("6598780011");
        list1.add("manju@gmail.com");
        list.add(new Contact("manju", "6598780011", getRandomColor(), list1));
        List<String> list2 = new ArrayList<>();
        list2.add("7898782398");
        list2.add("6598782398");
        list2.add("arunima@gmail.com");
        list.add(new Contact("Arunima",  "7898782398",  getRandomColor(),  list2));
        List<String> list3 = new ArrayList<>();
        list3.add("2398782398");
        list3.add("0898782398");
        list3.add("Vijay@gmail.com");
        list.add(new Contact("Vijay", "2398782398", getRandomColor(), list3));
        List<String> list4 = new ArrayList<>();
        list4.add("1298782398");
        list4.add("Ajay@gmail.com");
        list.add(new Contact("Ajay", "1298782398", getRandomColor(), list4));
        List<String> list5 = new ArrayList<>();
        list5.add("0598782398");
        list5.add("Akshay@gmail.com");
        list.add(new Contact("Akshay", "0598782398", getRandomColor(), list5));
        List<String> list6 = new ArrayList<>();
        list6.add("0412349857");
        list6.add("Varun@gmail.com");
        list.add(new Contact("Varun", "235862349857", getRandomColor(), list6));
        List<String> list7 = new ArrayList<>();
        list7.add("999912349857");
        list7.add("777312349857");
        list7.add("Divya@gmail.com");
        list.add(new Contact("Divya", "85468349857", getRandomColor(), list7));
        List<String> list8 = new ArrayList<>();
        list8.add("001234985710");
        list8.add("Suri@gmail.com");
        list.add(new Contact("Suri", "001234985710", getRandomColor(), list8));
        List<String> list9 = new ArrayList<>();
        list9.add("011234985722");
        list9.add("011234985766");
        list9.add("Rayan@gmail.com");
        list.add(new Contact("Rayan", "0112349857", getRandomColor(), list9));
        List<String> list10 = new ArrayList<>();
        list10.add("12349857");
        list10.add("22123498579");
        list10.add("Reshma@gmail.com");
        list.add(new Contact("Reshma", "12349857", getRandomColor(), list10));
        List<String> list11 = new ArrayList<>();
        list11.add("0012349857");
        list11.add("12349857900");
        list11.add("Reshma@gmail.com");
        list.add(new Contact("Riyaz", "0012349857", getRandomColor(), list11));
        List<String> list12 = new ArrayList<>();
        list12.add("00001234");
        list.add(new Contact("Rish", "00001234", getRandomColor(), list12));
        return list;
    }

    /**
     *Selects contact on clicking.
     */
   @Override
   public void onContactSelected(final Contact contact, final String communication) {
       boolean contactExist = false;
       for (SimpleContact simpleContact : mSelectedContacts) {
           if (contact.getName().trim().equals(simpleContact.getDisplayName())) {
               String searchBarText = mSearchQuery.getText();
               int colonPosition = searchBarText.lastIndexOf(';');
               searchBarText = searchBarText.substring(0, colonPosition + 1);
               String oldContactComm = simpleContact.getCommunication();
               String newContactComm = contact.getCommunication().trim();
               String replaceText = searchBarText.replaceAll(oldContactComm, newContactComm);
               mSearchQuery.setText(replaceText + " ");
               simpleContact.setCommunication(newContactComm);
               contactExist = true;
               break;
           }
       }
       if (!contactExist) {
           addContacts(contact);
       }
   }

   Text.TextObserver textListener = new Text.TextObserver() {
       @Override
       public void onTextUpdated(final String searchText, final int start,
                                 final int before, final int changdCharacCnt) {
           if (changdCharacCnt == SIZE_COUNT_ONE) {
               int searchTextLength = searchText.length();
               if (mSearchqueryText == null) {
                   mSearchqueryText = "";
               }
               int lastSearchTxtLen = mSearchqueryText.length();
               boolean isColonSymblAded = isColonSymblAdded(searchTextLength, lastSearchTxtLen,
                       searchText, start);
               boolean isColonSymblRmvd = isColonSymblRemoved(searchTextLength,
                       lastSearchTxtLen, start);
               boolean isFndGrtrthnSyml = false;
               if (start >= 2 && isColonSymblAded) {
                   isFndGrtrthnSyml = Character.toString(searchText.charAt(start - 2)).equals(">");
               } else if (start >= 3 && isColonSymblRmvd) {
                   isFndGrtrthnSyml = Character.toString(mSearchqueryText.charAt(start - 3))
                           .equals(">");
               }

               /**
                * To restrict the modification in between the less than and greater than symbol( Fixed added contact).
                */
               if (!isFndGrtrthnSyml  && start != 0
                       && mSearchqueryText.indexOf(">", start - 1) != -1
                       && mSearchqueryText.lastIndexOf("<", start) != - 1) {
                   mSearchQuery.setText(mSearchqueryText);
                   return;
               }
               if (isColonSymblAded) {
                   addColonSymbol(searchText, start, mContactAdapter.getContactsList());
               } else if (isColonSymblRmvd) {
                   remColonSymbol(searchText, start, lastSearchTxtLen, searchTextLength,
                           mContactAdapter.getContactsList());
               } else {
                   doFilter(searchText, start, lastSearchTxtLen, searchTextLength);
               }
           }
           if (changdCharacCnt != 0) {
               mSearchqueryText = mSearchQuery.getText();
           }
       }

       /**
        *Colon Symbol Removed.
        */
       private boolean isColonSymblRemoved(int searchTextLength, int lastSearchTxtLen, int start) {
           boolean isColonSymblRmvd = false;
           if (searchTextLength <= lastSearchTxtLen && start != 0) {
               String removedCharacter = Character.toString(mSearchqueryText.charAt(start - 1));
               isColonSymblRmvd = removedCharacter.hashCode() == SEMICOL_HASHCODE
                       || removedCharacter.equals(";");
           }
           return isColonSymblRmvd;
       }

       /**
        *Colon Symbol Added.
        */
       private boolean isColonSymblAdded(int searchTextLength,
                                         int lastSearchTxtLen, String searchText, int start) {
           boolean isColonSymblAded = false;
           if (searchTextLength > lastSearchTxtLen && start != 0) {
               String addedCharacter = Character.toString(searchText.charAt(start));
               isColonSymblAded = addedCharacter.equals(";")
                       || addedCharacter.hashCode() == SEMICOL_HASHCODE;
           }
           return isColonSymblAded;
       }

       /**
        *Filtering the contacts based on search query.
        */
       private void doFilter(String searchText, int start, int lastSearchTxtLen,
                             int searchTextLength) {
           int colonPosition = searchText.lastIndexOf(";");
           if (colonPosition == -1) {
               mContactAdapter.getFilter().filter(searchText);
           } else {
               if (lastSearchTxtLen == start) {
                   mContactAdapter.getFilter().filter(searchText.substring(colonPosition + 1,
                           searchText.length()));
               } else {
                   int nextColonPosition = searchText.indexOf(";", start);
                   int prevColonPosition = searchText.lastIndexOf(";", start);
                   String filterString = "";
                   if (prevColonPosition == - 1  && nextColonPosition != - 1) {
                       filterString = searchText.substring(0, nextColonPosition - 3);
                   } else if (nextColonPosition == - 1 && prevColonPosition != - 1) {
                       filterString = searchText.substring(prevColonPosition + 1, searchTextLength);
                   } else if (prevColonPosition != - 1 && nextColonPosition != - 1) {
                       filterString = searchText.substring(prevColonPosition + 3,
                               nextColonPosition - 3);
                   }
                   mContactAdapter.getFilter().filter(filterString);
               }
           }
       }

       /**
        *Colon Symbol Removed with removed contact update.
        */
       private void remColonSymbol(String searchText, int start, int lastSearchTxtLen,
                                   int searchTextLength, final List<Contact> contactsList) {
           int colonPosition = searchText.lastIndexOf(";", start - 2);
           String removedContact;
           if (colonPosition == -1) {
               removedContact = searchText.trim().substring(2, start - 4);
               if (start == lastSearchTxtLen) {
                   mSearchQuery.setText("");
               } else {
                   mSearchQuery.setText(searchText.substring(start, searchTextLength));
               }
           } else {
               removedContact = searchText.substring(colonPosition + 3, start - 3).trim();
               if (start == lastSearchTxtLen) {
                   mSearchQuery.setText(searchText.substring(0, colonPosition + 1) + " ");
               } else {
                   mSearchQuery.setText(searchText.substring(0, colonPosition + 1) + " "
                           + searchText.substring(start, searchTextLength) + " ");
               }
           }
           for (SimpleContact contact : mSelectedContacts) {
               if (contact.getCommunication().trim().equals(removedContact)) {
                   mSelectedContacts.remove(contact);
               }
           }
           refreshListItem(contactsList, removedContact.trim(), false);

       }

       /**
        *Colon Symbol Added with added contact update.
        */
       private void addColonSymbol(String searchText, int start, final List<Contact> contactsList) {
           String addedContact = searchText.substring(0, start);
           int colonPosition = addedContact.lastIndexOf(';');
           addedContact = searchText.substring(colonPosition + 1, start).trim();
           String addConWithSymbol = " < " + addedContact + " > ; ";
           if (colonPosition == - 1) {
               mSearchQuery.setText(addConWithSymbol);
           } else {
               mSearchQuery.setText(searchText.substring(0, colonPosition + 1) + addConWithSymbol);
           }
           mSelectedContacts.add(new SimpleContact(addedContact, addedContact));
           refreshListItem(contactsList, addedContact, true);
       }

       /**
        *Refreshing while adding or removing the contacts.
        */
       private void refreshListItem(final List<Contact> contactsList, final String addedContact,
                                    final boolean selectValue) {
           for (int k = 0; k < contactsList.size(); k++) {
               Contact contact = contactsList.get(k);
               if (contact.getCommunication().trim().equals(addedContact)) {
                   contactsList.get(k).setisSelected(selectValue);
                   mContactAdapter.notifyDataSetItemChanged(k);
               }
           }
       }
   };

   /**
     * Query and Get contact list from contact data base.
    *
    * Currently the function is not getting the used since
    * we have a limitation to query the contact from database
    * In future when this limitation is resolved ,uncomment this
    * method and need to be called before calling
    * the readContacts() in onStart() method.
    */
   /**
   private void getContactInfo() {
       ContactsCollection resultSet = DbUtils.query(ContactPickerAbility.this);
       mContactsList = new ArrayList<Contact>();
       if (resultSet == null || resultSet.isEmpty()) {
           return;
       }
       ohos.sysappcomponents.contact.entity.Contact contact = null;
       while ( ! resultSet.isEmpty()) {
           contact = resultSet.next();
           String primaryCommunication = "";
           List<PhoneNumber> phoneNumbers = contact.getPhoneNumbers();
           List<Email> emailList = contact.getEmails();
           List<String> communicationList = new ArrayList<>();
           for (PhoneNumber phoneNumber : phoneNumbers) {
               communicationList.add(phoneNumber.toString());
           }
           for (Email email : emailList) {
               communicationList.add(email.toString());
           }
           if (communicationList.size() > 0) {
               primaryCommunication = communicationList.get(0);
           }
           mContactsList.add(new Contact(contact.getName().toString(), primaryCommunication, getRandomColor(), communicationList));
       }
   }*/

   /**
    * Pick different color randomly for the contact icon.
    */
   private Color getRandomColor() {
       Color[] colors = {Color.BLUE, Color.CYAN, Color.RED, Color.MAGENTA, Color.YELLOW};
       int randomPosition = new Random().nextInt(colors.length);
       return colors[randomPosition];
   }
 }
