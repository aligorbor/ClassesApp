package ru.geekbrains.android2.classesapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import ru.geekbrains.android2.classesapp.R
import ru.geekbrains.android2.classesapp.databinding.ItemHomeworkBinding
import ru.geekbrains.android2.classesapp.databinding.ItemHomeworkOptBinding
import ru.geekbrains.android2.classesapp.model.entities.HomeworkObj
import ru.geekbrains.android2.classesapp.utils.getImgObject

class HomeworkAdapter(
    private val values: List<HomeworkObj>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_OBLIGATORY) ViewHolder(
            ItemHomeworkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else
            ViewHolderOpt(
                ItemHomeworkOptBinding.inflate(
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
            holder.name.text = item.subjectObj.name
            holder.daysLeft.text =
                String.format(holder.itemView.context.getString(R.string.days_left), item.daysLeft)
            holder.daysLeft.setTextColor(
                holder.itemView.context.resources.getColor(
                    if (item.daysLeft < 3) R.color.days_left1 else R.color.days_left2,
                    null
                )
            )
            getImgObject(holder.img, item.subjectObj.imageId)
            holder.task.text = item.task
        } else {
            holder as ViewHolderOpt
            holder.name.text = item.subjectObj.name
            holder.daysLeft.text =
                String.format(holder.itemView.context.getString(R.string.days_left), item.daysLeft)
            holder.daysLeft.setTextColor(
                holder.itemView.context.resources.getColor(
                    if (item.daysLeft < 3) R.color.days_left1 else R.color.days_left2,
                    null
                )
            )
            getImgObject(holder.img, item.subjectObj.imageId)
            holder.task.text = item.task
        }
    }

    override fun getItemCount(): Int = values.size
    override fun getItemViewType(position: Int): Int {
        return if (values[position].subjectObj.obligatory) TYPE_OBLIGATORY else TYPE_OPTIONAL
    }

    inner class ViewHolder(binding: ItemHomeworkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val daysLeft: TextView = binding.itemDaysLeft
        val img: RoundedImageView = binding.imageSubject
        val task: TextView = binding.itemTask

    }

    inner class ViewHolderOpt(binding: ItemHomeworkOptBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val daysLeft: TextView = binding.itemDaysLeft
        val img: RoundedImageView = binding.imageSubject
        val task: TextView = binding.itemTask
    }

    companion object {
        private const val TYPE_OBLIGATORY = 1
        private const val TYPE_OPTIONAL = 0
    }

}