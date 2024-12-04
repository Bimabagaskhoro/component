package com.bimabk.component

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

val LocalActivity = staticCompositionLocalOf<ComponentActivity> { noLocalProvided() }

private fun noLocalProvided(): Nothing {
    error("CompositionLocal LocalActivity not present")
}

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun BaseScreen(
    baseScreenArgs: ScreenAttr.ScreenArgs,
    isUseSystemBarsPadding: Boolean = true,
    overlappingTopBar: Boolean = false,
    hideTopBar: Boolean = false,
    isLockOrientation: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = with(baseScreenArgs) {
    val activity = LocalActivity.current
    if (isLockOrientation) activity.requestedOrientation = SCREEN_ORIENTATION_PORTRAIT

    Box {
        statusBarColor?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(it)
                    .windowInsetsTopHeight(WindowInsets.statusBars)
            )
        }
        navigationBarColor?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(it)
                    .align(BottomCenter)
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
            )
        }
        BaseScreenContent(
            baseScreenArgs = this@with,
            isUseSystemBarsPadding = isUseSystemBarsPadding,
            overlappingTopBar = overlappingTopBar,
            hideTopBar = hideTopBar,
            modifier = modifier,
            content = content
        )
    }
}

@Composable
private fun BaseScreenContent(
    baseScreenArgs: ScreenAttr.ScreenArgs,
    isUseSystemBarsPadding: Boolean,
    overlappingTopBar: Boolean,
    hideTopBar: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (overlappingTopBar) {
        Box(
            modifier = modifier
                .navigationBarsPadding()
                .then(if (isUseSystemBarsPadding) Modifier.systemBarsPadding() else Modifier)
        ) {
            content()
            if (hideTopBar.not()) TopBar(
                baseScreenArgs = baseScreenArgs,
                modifier = modifier.padding(horizontal = 24.dp)
            )
        }
    } else {
        Column(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
                .then(if (isUseSystemBarsPadding) Modifier.systemBarsPadding() else Modifier)
        ) {
            TopBar(baseScreenArgs)
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    baseScreenArgs: ScreenAttr.ScreenArgs,
    modifier: Modifier = Modifier
) = with(baseScreenArgs) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = screenName.orEmpty(),
                style = typography.bodySmall,
                color = colorScheme.onPrimary
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(screenName?.let { R.drawable.ic_back }
                    ?: R.drawable.ic_back),
                colorFilter = tint(color = colorBackIcon ?: colorScheme.onPrimary),
                contentDescription = "Back",
                modifier = screenName?.let {
                    Modifier
                        .clip(CircleShape)
                        .clickableSingle { onClickBack?.invoke() }
                } ?: Modifier
            )
        },
        actions = {
            Row(horizontalArrangement = spacedBy(16.dp)) {
                actionMenus.forEach { (resource, name, showBadge, onClickAction) ->
                    ActionButton(
                        resource = resource,
                        showBadge = showBadge
                    ) { onClickAction?.invoke(name) }
                }
            }
        },
        colors = TopAppBarColors(
            containerColor = topBarColor ?: colorScheme.background,
            navigationIconContentColor = colorScheme.onPrimary,
            titleContentColor = colorScheme.onPrimary,
            scrolledContainerColor = colorScheme.background,
            actionIconContentColor = colorScheme.onPrimary
        ),
        modifier = modifier
    )
}
