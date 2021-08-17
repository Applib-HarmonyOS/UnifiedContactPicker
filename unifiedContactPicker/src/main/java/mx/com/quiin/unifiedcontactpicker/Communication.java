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

package mx.com.quiin.unifiedcontactpicker;

/**
 *Communication object for the contacts.
 */
public class Communication {
    private String mCommunication;
    private int mParentPosition;

    /**
     *To Display the communication of contacts.
     */
    public Communication(final String communication, final int parentPosition) {
        this.mCommunication = communication;
        this.mParentPosition = parentPosition;
    }

    /**
     *Get the communication for the contact.
     */
    public String getCommunication() {
        return mCommunication;
    }

    /**
     *Set the communication for the contact.
     */
    public void setCommunication(final String communication) {
        this.mCommunication = communication;
    }

    /**
     *Get the parent.
     */
    public int getParentPosition() {
        return mParentPosition;
    }

    /**
     *Set the parent position.
     */
    public void setParentPosition(final int position) {
        mParentPosition = position;
    }

}
