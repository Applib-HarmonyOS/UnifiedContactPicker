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

package mx.com.quiin.unifiedcontactpicker.adapters;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import mx.com.quiin.unifiedcontactpicker.SimpleContact;
import java.util.List;
import java.util.Random;

import mx.com.quiin.unifiedcontactpicker.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.app.Context;

/**
 *Shows the simple contact list.
 */
public class SimpleAdapter extends BaseItemProvider {

    private final List<SimpleContact> mSelectedContacts;
    private final Context mContext;

    public SimpleAdapter(final List<SimpleContact> mSelectedContacts, final Context context) {
        this.mSelectedContacts = mSelectedContacts;
        this.mContext = context;
    }

    /**
     *Count the contacts.
     */
    @Override
    public int getCount() {
        return mSelectedContacts == null ? 0 : mSelectedContacts.size();
    }

    @Override
    public Object getItem(final int position) {
        if (mSelectedContacts != null && position >= 0
                && position < mSelectedContacts.size()) {
            return mSelectedContacts.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public Component getComponent(final int position, final Component component,
                                  final ComponentContainer compContainer) {
        Component cpt;
        SimpleContact contact = mSelectedContacts.get(position);
        if (component == null) {
            cpt = LayoutScatter.getInstance(mContext)
                    .parse(ResourceTable.Layout_cp_contact_row, null, false);
        } else {
            cpt = component;
        }
        Text displayName = (Text) cpt.findComponentById(ResourceTable.Id_tvDisplayName);
        Text communication = (Text) cpt.findComponentById(ResourceTable.Id_tvCommunication);
        MaterialLetterIcon mateFirstLetter = (MaterialLetterIcon) cpt
                .findComponentById(ResourceTable.Id_MaterialFirstLetter);
        Image selectorsIcon = (Image) cpt.findComponentById(ResourceTable.Id_cp_ivSelectedComm);
        displayName.setText(contact.getDisplayName());
        Image expandArrow = (Image) cpt.findComponentById(ResourceTable.Id_cp_ArrowExpand);
        expandArrow.setVisibility(Component.HIDE);
        communication.setText(contact.getCommunication());
        if (contact.getCommunication().contains("@")) {
            selectorsIcon.setImageAndDecodeBounds(ResourceTable.Media_ic_email);
        } else {
            selectorsIcon.setImageAndDecodeBounds(ResourceTable.Media_ic_message);
        }
        selectorsIcon.setScaleMode(Image.ScaleMode.CENTER);
        mateFirstLetter.setLetter(contact.getFirstLetter());
        mateFirstLetter.setShapeColor(getRandomColor());
        return cpt;
    }

    /**
     * Pick different color randomly for the contact icon.
     */
    private Color getRandomColor() {
        Color[] colors = {Color.BLUE, Color.CYAN, Color.RED, Color.MAGENTA, Color.YELLOW};
        int randomPosition = new Random().nextInt(colors.length);
        return colors[randomPosition];
    }
}
