package com.pavlig43.roofapp.navigation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pavlig43.roofapp.ui.kit.ArrowIconButton

@Composable
fun MenuWrapperItem(
    menuWrapper: MenuWrapper,
    selectedScreen: String,
    changeExpanded: (MenuWrapper) -> Unit,
    toNavigate: (String) -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    Card(
        modifier = modifier.clickable {
            changeExpanded(menuWrapper)
        }
    ) {
        Column(modifier = Modifier.Companion.fillMaxWidth()) {
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArrowIconButton(
                    expanded = menuWrapper.isShowNested,
                    changeExpanded = {
                        changeExpanded(menuWrapper)
                    },
                )
                Text(text = stringResource(menuWrapper.title))
            }
            if (menuWrapper.isShowNested) {
                menuWrapper.nestedMenuItems.forEach { nested ->
                    NestedDrawerItem(
                        screenDestination = nested,
                        selectedScreen = selectedScreen,
                        toNavigate = { toNavigate(nested.route) },
                    )
                }
            }
        }
    }
}
