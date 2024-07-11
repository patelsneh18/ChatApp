package com.example.chatapp.helper

import androidx.compose.runtime.MutableState
import com.example.chatapp.ui.comp.ImageState
import com.mr0xf00.easycrop.AspectRatio
import com.streamliners.pickers.media.FromGalleryType
import com.streamliners.pickers.media.MediaPickerCropParams
import com.streamliners.pickers.media.MediaPickerDialogState
import com.streamliners.pickers.media.MediaType
import com.streamliners.pickers.media.PickedMedia
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun launchMediaPickerDialogForImage(
    mediaPickerDialogState: MutableState<MediaPickerDialogState>,
    scope: CoroutineScope,
    imageState: MutableState<ImageState>
) {
    mediaPickerDialogState.value = MediaPickerDialogState.ShowMediaPicker(
        type = MediaType.Image,
        allowMultiple = false,
        fromGalleryType = FromGalleryType.VisualMediaPicker,
        cropParams = MediaPickerCropParams.Enabled(
            showAspectRatioSelectionButton = false,
            showShapeCropButton = false,
            lockAspectRatio = AspectRatio(1, 1)
        )
    ) { getList ->
        scope.launch {
            val list = getList()
            list.firstOrNull()?.let { pickedMedia ->
                imageState.value = ImageState.New(pickedMedia)
            }
        }
    }
}

@JvmName("launchMediaPickerDialogForImage1")
fun launchMediaPickerDialogForImage(
    mediaPickerDialogState: MutableState<MediaPickerDialogState>,
    scope: CoroutineScope,
    image: MutableState<PickedMedia?>
) {
    mediaPickerDialogState.value = MediaPickerDialogState.ShowMediaPicker(
        type = MediaType.Image,
        allowMultiple = false,
        fromGalleryType = FromGalleryType.VisualMediaPicker,
        cropParams = MediaPickerCropParams.Enabled(
            showAspectRatioSelectionButton = false,
            showShapeCropButton = false,
            lockAspectRatio = AspectRatio(1, 1)
        )
    ) { getList ->
        scope.launch {
            val list = getList()
            list.firstOrNull()?.let { pickedMedia ->
                image.value = pickedMedia
            }
        }
    }
}