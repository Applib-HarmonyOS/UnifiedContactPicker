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

import java.util.List;
import mx.com.quiin.unifiedcontactpicker.Communication;
import mx.com.quiin.unifiedcontactpicker.ResourceTable;
import ohos.agp.components.*;
import ohos.app.Context;

/**
 *List the communication for the contacts.
 */
public class CommunicationAdapter extends BaseItemProvider {

    private final List<Communication> mCommunctionList;
    private final Context context;

    /**
     *Shows the communication list.
     */
    public CommunicationAdapter(final List<Communication> communicationList,
                                final Context context) {
        this.mCommunctionList = communicationList;
        this.context = context;
    }

    /**
     *Count the contacts.
     */
    @Override
    public int getCount() {
        return mCommunctionList == null ? 0 : mCommunctionList.size();
    }

    @Override
    public Object getItem(final int position) {
        if (mCommunctionList != null && position >= 0 && position < mCommunctionList.size()) {
            return mCommunctionList.get(position);
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
        Component cpt;
        Communication communication = mCommunctionList.get(position);
        if (component == null) {
            cpt = LayoutScatter.getInstance(context)
                    .parse(ResourceTable.Layout_cp_communication_row, null, false);
        } else {
            cpt = component;
        }
        Text comm = (Text) cpt.findComponentById(ResourceTable.Id_tvCommunication);
        Image commIcon = (Image) cpt.findComponentById(ResourceTable.Id_ivCommunicationIcon);
        comm.setText(communication.getCommunication());
        if (communication.getCommunication().contains("@")) {
            commIcon.setImageAndDecodeBounds(ResourceTable.Media_ic_email);
        } else {
            commIcon.setImageAndDecodeBounds(ResourceTable.Media_ic_message);
        }
      return cpt;
    }
 }
