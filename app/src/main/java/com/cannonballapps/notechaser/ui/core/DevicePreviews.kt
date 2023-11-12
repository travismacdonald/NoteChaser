package com.cannonballapps.notechaser.ui.core

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "1. phone (light) - portrait", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "2. phone (light) - landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "3. tablet (light) - landscape", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Preview(name = "4. phone (dark) - portrait", uiMode = UI_MODE_NIGHT_YES, device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "5. phone (dark) - landscape", uiMode = UI_MODE_NIGHT_YES, device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "6. tablet (dark) - landscape", uiMode = UI_MODE_NIGHT_YES, device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
annotation class DevicePreviews
