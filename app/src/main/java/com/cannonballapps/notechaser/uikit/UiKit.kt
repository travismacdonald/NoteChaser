package com.cannonballapps.notechaser.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cannonballapps.notechaser.R

@Composable
fun SettingsItem() {
    /*
     * todo:
     *  - row height
     *  - sensible dimensions
     *    - dimensions
     *  - text style
     *    - title
     *    - description
     *    - extract
     */
    Row() {
        Icon(
            painter = painterResource(id = R.drawable.ic_music_note_black_40dp),
            modifier = Modifier.size(40.dp),
            contentDescription = null,
        )
        Column {
            Text(
                // todo style
                text = "Title",
            )
            Text(
                // todo style
                text = "Description. Maybe a bit longer",
            )
        }
    }

}

/*
 * todo
 *  - default preview values
 *  - preview with long description
 */
@Preview
@Composable
private fun SettingsItemPreview() {
    SettingsItem()
}
