package remix.myplayer.ui.screen.setting.logic.playingscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import remix.myplayer.R
import remix.myplayer.data.prefs.SettingPrefs
import remix.myplayer.ui.widget.common.LineSlider
import remix.myplayer.ui.widget.common.TextPrimary
import remix.myplayer.ui.widget.common.TextSecondary
import remix.myplayer.ui.widget.common.defaultLineSliderProperties
import remix.myplayer.viewmodel.settingViewModel
import kotlin.math.roundToInt

@Composable
fun SeekStepLogic() {
  val settingVM = settingViewModel
  val settingState by settingVM.settingsState.collectAsStateWithLifecycle()
  val playingScreen = settingState.playingScreen

  TextSecondary(
    text = stringResource(R.string.seek_step_title),
    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp),
    fontSize = 14.sp,
    fontWeight = FontWeight.Medium
  )

  SeekStepSecondsSlider(
    title = stringResource(R.string.seek_backward_1),
    seconds = playingScreen.seekBackward1Seconds,
    onSecondsChanged = settingVM::setSeekBackward1Seconds
  )

  SeekStepSecondsSlider(
    title = stringResource(R.string.seek_backward_2),
    seconds = playingScreen.seekBackward2Seconds,
    onSecondsChanged = settingVM::setSeekBackward2Seconds
  )

  SeekStepSecondsSlider(
    title = stringResource(R.string.seek_forward_1),
    seconds = playingScreen.seekForward1Seconds,
    onSecondsChanged = settingVM::setSeekForward1Seconds
  )

  SeekStepSecondsSlider(
    title = stringResource(R.string.seek_forward_2),
    seconds = playingScreen.seekForward2Seconds,
    onSecondsChanged = settingVM::setSeekForward2Seconds
  )
}

@Composable
private fun SeekStepSecondsSlider(
  title: String,
  seconds: Int,
  onSecondsChanged: (Int) -> Unit
) {
  var sliderValue by remember(seconds) { mutableFloatStateOf(seconds.toFloat()) }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 10.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      TextPrimary(
        title,
        modifier = Modifier.weight(1f),
        fontSize = 16.sp
      )
      TextPrimary(
        stringResource(R.string.play_skip_seconds_value, sliderValue.roundToInt()),
        fontSize = 16.sp
      )
    }

    LineSlider(
      modifier = Modifier
        .fillMaxWidth()
        .height(32.dp),
      value = sliderValue,
      onValueChange = { sliderValue = it },
      onValueChangeFinished = { onSecondsChanged(sliderValue.roundToInt()) },
      valueRange = SettingPrefs.SEEK_STEP_MIN_SECONDS.toFloat()..
        SettingPrefs.SEEK_STEP_MAX_SECONDS.toFloat(),
      steps = SettingPrefs.SEEK_STEP_MAX_SECONDS -
        SettingPrefs.SEEK_STEP_MIN_SECONDS - 1,
      properties = defaultLineSliderProperties.copy()
    )
  }
}
