package br.com.alaksion.myapplication.ui.home.photolist.components.photoinfobottomsheet

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import br.com.alaksion.myapplication.ui.theme.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val PhotoInfoBottomSheet_TAG = "PhotoInfoBottomSheet"

class PhotoInfoBottomSheet : BottomSheetDialogFragment() {

    private var imageLink: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PhotoInfoBottomSheetContent()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    private fun copyLinkToClipboard(value: String) {
        val manager = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        manager.text = AnnotatedString(value)

        Toast.makeText(
            context,
            "Image link copied to clipboard",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun notImplementedToast() {
        Toast.makeText(
            context,
            "Feature not yet implemented",
            Toast.LENGTH_SHORT
        ).show()
    }

    @Composable
    fun PhotoInfoBottomSheetContent() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(BlackRussian)
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            PhotoInfoBottomSheetItem(label = "Link") {
                IconButton(
                    onClick = { imageLink?.let { copyLinkToClipboard(it) } },
                    modifier = Modifier
                        .height(48.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, shape = CircleShape, color = DimGray)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Link,
                        contentDescription = null,
                        tint = OffWhite,
                        modifier = Modifier
                            .size(30.dp)
                            .rotate(-45f)
                    )
                }
            }
            PhotoInfoBottomSheetItem(label = "Share") {
                IconButton(
                    onClick = { notImplementedToast() },
                    modifier = Modifier
                        .height(48.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, shape = CircleShape, color = DimGray)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = null,
                        tint = OffWhite,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
            PhotoInfoBottomSheetItem(label = "Report", isError = true) {
                IconButton(
                    onClick = { notImplementedToast() },
                    modifier = Modifier
                        .height(48.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            shape = CircleShape,
                            color = ErrorLightRed
                        )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Report,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }
    }

    companion object {
        fun show(manager: FragmentManager, imageLink: String) {
            val bottomSheet = PhotoInfoBottomSheet().apply { this.imageLink = imageLink }
            bottomSheet.show(manager, PhotoInfoBottomSheet_TAG)
        }
    }

}