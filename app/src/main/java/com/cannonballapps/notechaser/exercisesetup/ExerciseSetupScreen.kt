package com.cannonballapps.notechaser.exercisesetup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.ui.core.DevicePreviews

@Composable
fun ExerciseSetupCard(
    headerText: String,
    descriptionText: String,
    onCardClick: (() -> Unit),
    // todo img resource
    // todo modifier
) {
    /*
     * todo:
     *  - row height
     *  - sensible dimensions
     *    - dimensions
     *  - background
     */
    Surface() {
        Row(
            modifier = Modifier.clickable(onClick = onCardClick),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_music_note_black_40dp),
                modifier = Modifier
                    .padding(20.dp)
                    .requiredSize(40.dp),
//                    .size(40.dp),
                contentDescription = null,
            )
            Column {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.subtitle2,
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun SettingsItemPreview() {
    ExerciseSetupCard(
        headerText = "Header",
        descriptionText = "Longer description. Because I want to see if the text will properly wrap when it is this long.",
        onCardClick = { /* no-op */ },
    )
}
