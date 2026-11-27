package remix.myplayer.ui.screen.playing

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import remix.myplayer.R
import remix.myplayer.service.Command
import remix.myplayer.service.MusicService
import remix.myplayer.util.Util.sendLocalBroadcast
import remix.myplayer.util.ext.clickWithRipple
import remix.myplayer.viewmodel.settingViewModel

@Composable
internal fun PlayingSeekStepControl(
  modifier: Modifier = Modifier,
  swatch: Palette.Swatch,
  iconSize: Dp = 36.dp,
  buttonSize: Dp? = null
) {
  val settingState by settingViewModel.settingsState.collectAsStateWithLifecycle()
  val playingScreen = settingState.playingScreen
  val swatchColor = Color(swatch.rgb)

  Row(
    modifier = modifier.fillMaxSize(),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    SeekStepButton(
      seconds = playingScreen.seekBackward1Seconds,
      isForward = false,
      tint = swatchColor,
      iconSize = iconSize,
      buttonSize = buttonSize
    )
    SeekStepButton(
      seconds = playingScreen.seekBackward2Seconds,
      isForward = false,
      tint = swatchColor,
      iconSize = iconSize,
      buttonSize = buttonSize
    )
    SeekStepButton(
      seconds = playingScreen.seekForward1Seconds,
      isForward = true,
      tint = swatchColor,
      iconSize = iconSize,
      buttonSize = buttonSize
    )
    SeekStepButton(
      seconds = playingScreen.seekForward2Seconds,
      isForward = true,
      tint = swatchColor,
      iconSize = iconSize,
      buttonSize = buttonSize
    )
  }
}

@Composable
private fun SeekStepButton(
  seconds: Int,
  isForward: Boolean,
  tint: Color,
  iconSize: Dp,
  buttonSize: Dp?
) {
  val targetButtonSize = buttonSize ?: (iconSize + 12.dp)
  val fontScaleFactor = if (seconds >= 100) 0.23f else 0.29f
  // Convert dp directly to sp via LocalDensity so the overlay digit stays fixed relative
  // to iconSize even when system font scale or app UiFontScale is maximized.
  val digitFontSize = with(LocalDensity.current) { (iconSize * fontScaleFactor).toSp() }
  val iconRes = if (isForward) R.drawable.ic_seek_forward else R.drawable.ic_seek_backward
  val contentDesc = stringResource(
    if (isForward) R.string.seek_forward_desc else R.string.seek_backward_desc,
    seconds
  )

  Box(
    modifier = Modifier
      .size(targetButtonSize)
      .clickWithRipple {
        val deltaMs = (if (isForward) seconds else -seconds) * 1000L
        sendLocalBroadcast(
          Intent(MusicService.ACTION_CMD)
            .putExtra(MusicService.EXTRA_COMMAND, Command.SEEK_BY)
            .putExtra(MusicService.EXTRA_SEEK_DELTA, deltaMs)
        )
      },
    contentAlignment = Alignment.Center
  ) {
    Image(
      modifier = Modifier.size(iconSize),
      painter = painterResource(iconRes),
      contentDescription = contentDesc,
      colorFilter = ColorFilter.tint(tint)
    )
    Text(
      text = seconds.toString(),
      modifier = Modifier.offset(y = iconSize * 0.04f),
      style = TextStyle(
        color = tint,
        fontSize = digitFontSize,
        lineHeight = digitFontSize,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
      ),
      maxLines = 1,
      softWrap = false
    )
  }
}
