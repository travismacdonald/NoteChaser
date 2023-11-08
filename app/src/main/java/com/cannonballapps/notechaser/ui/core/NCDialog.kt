package com.cannonballapps.notechaser.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cannonballapps.notechaser.exercisesetup.DialogData

@Composable
fun NCDialog(
    dialogData: DialogData,
    onDismissRequest: () -> Unit,
) {
    var selectedIx: Int by remember { mutableStateOf(dialogData.initialSelectedValue) } // TODO allow passing in selected ix

    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            Surface(
                color = Color.Cyan,
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
            ) {
                Column {
                    LazyColumn {
                        itemsIndexed(dialogData.values) { index, value ->
                            Text(
                                text = value,
                                fontSize = 24.sp,
                                modifier = Modifier
                                    .clickable { selectedIx = index }
                                    .background(color = if (index == selectedIx) Color.Red else Color.Cyan)
                            )
                        }
                    }
                    Button(onClick = {
                        dialogData.onValueSelected(selectedIx)
                        onDismissRequest()
                    }) {
                        Text(text = "fuck")
                    }
                }
            }
        },
    )
}

@DevicePreviews
@Composable
private fun NCDialogPreview() {
    NCDialog(
        DialogData(
            values = listOf(
                "C",
                "D",
                "E",
                "F",
                "G",
            ),
            onValueSelected = { /* No-op */ },
            initialSelectedValue = 3,
        ),
        onDismissRequest = { /* No-op */ },
    )
}
