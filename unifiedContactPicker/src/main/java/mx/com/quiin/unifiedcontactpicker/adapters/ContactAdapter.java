package mx.com.quiin.unifiedcontactpicker.adapters;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import mx.com.quiin.unifiedcontactpicker.Communication;
import mx.com.quiin.unifiedcontactpicker.Contact;
import mx.com.quiin.unifiedcontactpicker.interfaces.ContactSelectionListener;
import mx.com.quiin.unifiedcontactpicker.ResourceTable;
import ohos.agp.components.*;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import ohos.agp.utils.Color;
import ohos.app.Context;

/**
 * Created by Carlos Reyna on 20/01/17.
 *
 *Shows the contact list.
 **/
public class ContactAdapter extends BaseItemProvider implements ListContainer.ItemClickedListener {

    private List<Contact> mContacts;
    private final Context mContext;
    private ArrayList<Contact> mOriginalValues;
    private ArrayFilter mFilter;
    private final Object mLock = new Object();
    private final ContactSelectionListener mListener;
    public static final String TICK_ICON  = "\u2713";

    /**
     *list the contacts.
     */
    public ContactAdapter(final List<Contact> contactList, final Context context,
                          final ContactSelectionListener listener) {
        this.mContacts = contactList;
        this.mContext = context;
        this.mListener = listener;
    }

    /**
     *Count the contacts.
     */
    @Override
    public int getCount() {
        return mContacts == null ? 0 : mContacts.size();
    }

    @Override
    public Object getItem(final int position) {
        if (mContacts != null && position >= 0 && position < mContacts.size()) {
            return mContacts.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public Component getComponent(final int position, final Component component,
                                  final ComponentContainer compoContainer) {
        Component listComponent;
        Contact contact = mContacts.get(position);
        if (component == null) {
            listComponent = LayoutScatter.getInstance(mContext)
                    .parse(ResourceTable.Layout_cp_contact_row, null, false);
        } else {
            listComponent = component;
        }
        ListContainer commListContainer = (ListContainer) listComponent
                .findComponentById(ResourceTable.Id_CommunicationList);
        commListContainer.setVisibility(Component.INVISIBLE);
        Text displayName = (Text) listComponent.findComponentById(ResourceTable.Id_tvDisplayName);
        Text communication = (Text) listComponent
                .findComponentById(ResourceTable.Id_tvCommunication);
        MaterialLetterIcon mateFirstLetter = (MaterialLetterIcon) listComponent
                    .findComponentById(ResourceTable.Id_MaterialFirstLetter);
        Image expandArrow = (Image) listComponent
                .findComponentById(ResourceTable.Id_cp_ArrowExpand);
        Image selectorsIcon = (Image) listComponent
                .findComponentById(ResourceTable.Id_cp_ivSelectedComm);
        displayName.setText(contact.getName());

        selectorsIcon.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(final Component component) {
                contact.setisExpanded(!contact.isExpanded());
                notifyDataSetItemChanged(position);
            }
        });

        expandArrow.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(final Component component) {
                contact.setisExpanded(!contact.isExpanded());
                notifyDataSetItemChanged(position);
            }
        });
        communication.setText(contact.getCommunication());
        if (contact.getCommunication().contains("@")) {
            selectorsIcon.setImageAndDecodeBounds(ResourceTable.Media_ic_email);
        } else {
            selectorsIcon.setImageAndDecodeBounds(ResourceTable.Media_ic_message);
        }
        selectorsIcon.setScaleMode(Image.ScaleMode.CENTER);
        if (contact.isSelected()) {
            mateFirstLetter.setLetter(TICK_ICON);
            mateFirstLetter.setShapeColor(Color.GREEN);
        } else {
            mateFirstLetter.setLetter(contact.getFirstLetter());
            mateFirstLetter.setShapeColor(contact.getLetterBgColor());
        }
        List<String> contactCommList = contact.getCommunications();
        boolean moreNumOfComm = contactCommList.size() > 1;
        expandArrow.setVisibility(moreNumOfComm ? Component.VISIBLE  : Component.INVISIBLE);
        selectorsIcon.setClickable(moreNumOfComm ? true : false);
        List<Communication> communicationList = new ArrayList<>();
        for (String comm : contactCommList) {
            communicationList.add(new Communication(comm, position));
        }
        CommunicationAdapter commAdapter = new CommunicationAdapter(communicationList, mContext);
        commListContainer.setItemProvider(commAdapter);
        commListContainer.setLongClickable(false);
        commListContainer.setItemClickedListener(this);
        commListContainer.setVisibility(contact.isExpanded() ? Component.VISIBLE : Component.HIDE);
        return listComponent;
    }

        /**
         *On Item Clicked contacts.
         */
        @Override
        public void onItemClicked(final ListContainer listContainer,
                              final Component component, final int position, final long listItem) {
        Communication clickedcomm = (Communication) listContainer
                .getItemProvider().getItem(position);
        Component parentComponent = (Component) listContainer.getComponentParent();
        listContainer.setVisibility(Component.INVISIBLE);
        refreshParentContact(parentComponent, clickedcomm.getParentPosition(),
                clickedcomm.getCommunication());
        }

        /**
         *Refresh Parent Position contact after selecting contact.
         */
        public void refreshParentContact(final Component parentComponent,
                                     final int parentPosition, final String communication) {
        Text selectedCommun = (Text) parentComponent
                .findComponentById(ResourceTable.Id_tvCommunication);
        selectedCommun.setText(communication);
        Image selectedCommIcon = (Image) parentComponent
                .findComponentById(ResourceTable.Id_cp_ivSelectedComm);
        selectedCommIcon.setImageAndDecodeBounds(communication
                .contains("@") ? ResourceTable.Media_ic_email : ResourceTable.Media_ic_message);
        MaterialLetterIcon contactIcon = (MaterialLetterIcon) parentComponent
                .findComponentById(ResourceTable.Id_MaterialFirstLetter);
        contactIcon.setLetter(TICK_ICON);
        contactIcon.setShapeColor(Color.GREEN);
        mContacts.get(parentPosition).setCommunication(communication);
        mContacts.get(parentPosition).setisExpanded(false);
        mContacts.get(parentPosition).setisSelected(true);
        mListener.onContactSelected(mContacts.get(parentPosition), communication);
        parentComponent.invalidate();
        notifyDataSetItemChanged(parentPosition);
        }

        /**
         *Get filtered contacts.
         */
        @Override
        public TextFilter getFilter() {
            if (mFilter == null) {
            mFilter = new ArrayFilter();
            }
        return mFilter;
        }

        /**
         *Apply the filter on contacts.
         */
        private class ArrayFilter extends TextFilter {
        @Override
        protected FilterResults executeFiltering(final CharSequence searchString) {
            final FilterResults results = new FilterResults();
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mContacts);
                }
            }
            String searchText = searchString.toString().toLowerCase(Locale.ENGLISH).trim();
            if (searchText.length() == 0) {
                ArrayList<Contact> contactList;
                synchronized (mLock) {
                    contactList = new ArrayList<>(mOriginalValues);
                }
                results.results = contactList;
                results.size = contactList.size();
            } else {
                ArrayList<Contact> contactList;
                synchronized (mLock) {
                    contactList = new ArrayList<>(mOriginalValues);
                }
                final ArrayList<Contact> filterdContctLst = new ArrayList<>();
                setFilteredList(searchText, contactList, filterdContctLst);
                results.results = filterdContctLst;
                results.size = filterdContctLst.size();
            }
            return results;
        }

        /**
         *Filtered contacts list display.
         */
        @Override
        protected void publishFilterResults(final CharSequence charSequence,
                                            final FilterResults results) {
            mContacts = (ArrayList<Contact>) results.results;
            if (results.size > 0) {
                notifyDataChanged();
            } else {
                notifyDataInvalidated();
            }
        }

        /**
         *Shows the filtered contacts from the list based on search query.
         */
        private void setFilteredList(final String searchText,
                                     final ArrayList<Contact> contactsList,
                                     final ArrayList<Contact> filterdContctLst) {
            final int contactCount = contactsList.size();
            for (int i = 0; i<contactCount; i++) {
                final Contact contact = contactsList.get(i);
                final String contactName = contact.getName().toLowerCase(Locale.ENGLISH);
                final List<String> communicationList = contact.getCommunications();
                if (contactName.startsWith(searchText.trim())) {
                    filterdContctLst.add(contact);
                    continue;
                } else {
                    final String[] splittedContact = contactName.split("");
                    checkSplitConExist(splittedContact, searchText, filterdContctLst, contact);
                }
                for (String commn : communicationList) {
                    String communication = commn.toLowerCase(Locale.ENGLISH);
                    if (communication.startsWith(searchText)) {
                        filterdContctLst.add(contact);
                        continue;
                    } else {
                        final String[] splittedComm = communication.split(" ");
                        checkSplitConExist(splittedComm, searchText, filterdContctLst, contact);
                    }
                }
            }
        }

        /**
         *Check function to add the splitted contact list in the filtered list.
         */
        private void checkSplitConExist(String[] splittedContact, String searchText,
                                        final ArrayList<Contact> filterdContctLst,
                                        Contact contact) {
            for (String splitContact : splittedContact) {
                if (splitContact.startsWith(searchText)) {
                    filterdContctLst.add(contact);
                    break;
                }
            }
        }
    }

    /**
     *Gets the contact list.
     */
    public List<Contact> getContactsList() {
        return mContacts;
    }
 }