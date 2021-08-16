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

import mx.com.quiin.unifiedcontactpicker.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import ohos.security.SystemPermission;

/**
 *Sets the Background color
 */
public class MainAbility extends Ability {
    @Override
    public void onStart(final Intent intent) {
        super.onStart(intent);
        if (verifySelfPermission(SystemPermission.READ_CONTACTS) != IBundleManager.PERMISSION_GRANTED
                && canRequestPermission(SystemPermission.READ_CONTACTS)) {
            requestPermissionsFromUser(new String[]{SystemPermission.READ_CONTACTS}, 0);
        }
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
