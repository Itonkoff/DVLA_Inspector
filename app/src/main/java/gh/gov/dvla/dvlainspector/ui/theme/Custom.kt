package com.dvla.pvts.dvlainspectorapp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PassFailSelector(check: Boolean?, onCheckChanged: (Boolean?) -> Unit) {
    var successCheckBox by remember { mutableStateOf(false) }
    var failCheckBox by remember { mutableStateOf(false) }

    if (check != null) {
        if (check) successCheckBox = check
        if (!check) failCheckBox = !check
    }

    Row {
        Checkbox(
            checked = successCheckBox,
            onCheckedChange = {
                successCheckBox = it

                if (failCheckBox && it)
                    failCheckBox = false

                if (it)
                    onCheckChanged(true)

                if (!it && !failCheckBox)
                    onCheckChanged(null)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.error,
                uncheckedColor = MaterialTheme.colorScheme.error
            )
        )
        Checkbox(
            checked = failCheckBox,
            onCheckedChange = {
                failCheckBox = it

                if (successCheckBox && it)
                    successCheckBox = false

                if (it)
                    onCheckChanged(false)

                if (!it && !successCheckBox)
                    onCheckChanged(null)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Green.copy(green = .452f),
                uncheckedColor = Color.Green.copy(green = .452f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PassFailSelectorPrev() {
//    PassFailSelector(false)
}