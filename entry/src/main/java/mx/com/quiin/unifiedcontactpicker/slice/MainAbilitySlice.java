/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package mx.com.quiin.unifiedcontactpicker.slice;

import mx.com.quiin.unifiedcontactpicker.ResourceTable;
import mx.com.quiin.unifiedcontactpicker.SimpleContact;
import mx.com.quiin.unifiedcontactpicker.adapters.SimpleAdapter;
import mx.com.quiin.unifiedcontactpicker.ui.ContactPickerAbility;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ohos.agp.components.Text;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

/**
 *Shows the main page of unified contact picker
 */
public class MainAbilitySlice extends AbilitySlice {

    private Text mSelectedCount;
    public static final int REQ_CONTACT_PICK = 1001;
    private ListContainer contactListCont;
    private final ArrayList<SimpleContact> mSelectedContacts = new ArrayList<>();

    /**
     *Start Main Choose Contact page
     */
    @Override
    public void onStart(final Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        contactListCont = (ListContainer) findComponentById(ResourceTable.Id_cp_listview);
        contactListCont.setVisibility(Component.HIDE);
        mSelectedCount = (Text) findComponentById(ResourceTable.Id_SelectContactCount);
        try {
            mSelectedCount.setText(this.getResourceManager().
                    getElement(ResourceTable.String_mainability_no_contact_info).getString());
        } catch (IOException | NotExistException | WrongTypeException e) {
            e.printStackTrace();
        }
        Button button = (Button) findComponentById(ResourceTable.Id_select_contacts);
        button.setClickedListener(listener -> {
            presentForResult(new ContactPickerAbility(), new Intent(), REQ_CONTACT_PICK);
        });
    }

    /**
     *Shows the result of selected Contact list
     */
   @Override
    protected void onResult(final int requestCode, final Intent resultIntent) {
        try {
            super.onResult(requestCode, resultIntent);
            if (resultIntent.hasParameter("CP_SELECTED_CONTACTS")) {
                final List<SimpleContact> receivedContacts = resultIntent.getSerializableParam("CP_SELECTED_CONTACTS");
                if(mSelectedContacts.size() == 0) {
                    mSelectedContacts.addAll(receivedContacts);
                } else {
                    for (SimpleContact contact : receivedContacts) {
                        if(!mSelectedContacts.contains(contact)) {
                            mSelectedContacts.add(contact);
                        }
                    }
                }
                SimpleAdapter contactListAdapt = new SimpleAdapter(mSelectedContacts, this);
                contactListCont.setVisibility(Component.VISIBLE);
                mSelectedCount.setText(this.getResourceManager().
                        getElement(ResourceTable.String_mainability_contact_select_info).getString());
                contactListCont.setItemProvider(contactListAdapt);
            }
        } catch (IOException | NotExistException | WrongTypeException e) {
            e.printStackTrace();
        }
    }
}



