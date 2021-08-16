package mx.com.quiin.unifiedcontactpicker;

import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import ohos.agp.utils.Color;
import ohos.app.Context;

public class UnifiedContactPickerTest {
    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("mx.com.quiin.unifiedcontactpicker", actualBundleName);
    }

    @Test
    public void name() {
    }
}