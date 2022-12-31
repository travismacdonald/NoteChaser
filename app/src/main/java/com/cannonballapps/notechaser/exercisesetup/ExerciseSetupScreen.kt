// ktlint-disable filename
package com.cannonballapps.notechaser.exercisesetup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
     *  - background color
     */
    Surface() {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .clickable(onClick = onCardClick),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_music_note_black_40dp),
                modifier = Modifier
                    .padding(16.dp)
                    .requiredSize(32.dp),
                contentDescription = null,
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        end = 16.dp,
                    ),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(
                        bottom = 16.dp,
                        end = 16.dp,
                    ),
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun ExerciseSetupCardPreview() {
    ExerciseSetupCard(
        headerText = "Header",
        descriptionText = "Longer description. This description shows that the text wraps when it exceeds the screen width.",
        onCardClick = { /* no-op */ },
    )
}
