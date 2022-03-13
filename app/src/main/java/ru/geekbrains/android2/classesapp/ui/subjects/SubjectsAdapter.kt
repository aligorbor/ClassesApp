package ru.geekbrains.android2.classesapp.ui.subjects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import ru.geekbrains.android2.classesapp.R
import ru.geekbrains.android2.classesapp.databinding.ItemSubjectBinding
import ru.geekbrains.android2.classesapp.databinding.ItemSubjectOptBinding
import ru.geekbrains.android2.classesapp.model.entities.SubjectObj
import ru.geekbrains.android2.classesapp.utils.getImgObject
import ru.geekbrains.android2.classesapp.utils.showSnackBar
import ru.geekbrains.android2.classesapp.utils.startSkype

class SubjectsAdapter(
    private val values: List<SubjectObj>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_OBLIGATORY) ViewHolder(
            ItemSubjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else
            ViewHolderOpt(
                ItemSubjectOptBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = values[position]
        if (getItemViewType(position) == TYPE_OBLIGATORY) {
            holder as ViewHolder
            holder.name.text = item.name
            holder.teacher.text =
                String.format(holder.itemView.context.getString(R.string.teacher), item.teacher)
            getImgObject(holder.img, item.imageId)
            if (item.openIn) {
                getImgObject(holder.imgOpenIn, 0)
                holder.imgOpenInBack.visibility = View.VISIBLE
                holder.imgOpenIn.visibility = View.VISIBLE
                holder.imgOpenIn.setOnClickListener {
                    if (!startSkype(holder.itemView.context)) holder.itemView.showSnackBar("Can't launch Skype!")
                }
            } else {
                holder.imgOpenIn.visibility = View.GONE
                holder.imgOpenInBack.visibility = View.GONE
            }
        } else {
            holder as ViewHolderOpt
            holder.name.text = item.name
            holder.teacher.text =
                String.format(holder.itemView.context.getString(R.string.teacher), item.teacher)
            holder.about.text = item.about
            getImgObject(holder.img, item.imageId)
        }
    }

    override fun getItemCount(): Int = values.size
    override fun getItemViewType(position: Int): Int {
        return if (values[position].obligatory) TYPE_OBLIGATORY else TYPE_OPTIONAL
    }

    inner class ViewHolder(binding: ItemSubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val teacher: TextView = binding.itemTeacher
        val img: RoundedImageView = binding.imageSubject
        val imgOpenIn: RoundedImageView = binding.imageOpenin
        val imgOpenInBack: ImageView = binding.imageOpeninBack
    }

    inner class ViewHolderOpt(binding: ItemSubjectOptBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val teacher: TextView = binding.itemTeacher
        val about: TextView = binding.itemAbout
        val img: RoundedImageView = binding.imageSubject
    }

    companion object {
        private const val TYPE_OBLIGATORY = 1
        private const val TYPE_OPTIONAL = 0
    }

}