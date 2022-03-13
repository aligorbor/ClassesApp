package ru.geekbrains.android2.classesapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.makeramen.roundedimageview.RoundedImageView
import ru.geekbrains.android2.classesapp.R

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

fun View.showSnackBar(
    resIdText: Int,
    resIdActionText: Int,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    this.showSnackBar(
        this.context.getString(resIdText),
        this.context.getString(resIdActionText),
        action,
        length
    )
}

fun View.showSnackBar(
    text: String,
    actionText: String = "Ok",
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    this.showSnackBar(text, actionText, {}, length)
}

fun View.showSnackBar(
    resIdText: Int,
    actionText: String = "Ok",
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    this.showSnackBar(this.context.getString(resIdText), actionText, {}, length)
}

fun getImgObject(img: RoundedImageView, imageId: Int) {
    img.setImageResource(
        when (imageId) {
            1 -> R.drawable.icons1
            2 -> R.drawable.icons2
            3 -> R.drawable.icons3
            4 -> R.drawable.icons4
            5 -> R.drawable.icons5
            6 -> R.drawable.icons6
            7 -> R.drawable.icons7
            8 -> R.drawable.icons8
            9 -> R.drawable.icons9
            10 -> R.drawable.icons10
            11 -> R.drawable.icons11
            0 -> R.drawable.skype
            else -> R.drawable.icons0
        }
    )
}