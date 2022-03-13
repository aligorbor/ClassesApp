package ru.geekbrains.android2.classesapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import ru.geekbrains.android2.classesapp.R
import ru.geekbrains.android2.classesapp.databinding.ItemClassesHomeBinding
import ru.geekbrains.android2.classesapp.databinding.ItemClassesHomeOptBinding
import ru.geekbrains.android2.classesapp.model.entities.ScheduleObj
import ru.geekbrains.android2.classesapp.utils.dateToStrHM
import ru.geekbrains.android2.classesapp.utils.getImgObject
import ru.geekbrains.android2.classesapp.utils.showSnackBar
import ru.geekbrains.android2.classesapp.utils.startSkype

class ClassesHomeAdapter(
    private val values: List<ScheduleObj>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var positionNearest: Int = 0

    fun setPosition(position: Int) {
        positionNearest = position
        notifyItemChanged(positionNearest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_OBLIGATORY) ViewHolder(
            ItemClassesHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else
            ViewHolderOpt(
                ItemClassesHomeOptBinding.inflate(
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
            getImgObject(holder.img, item.subjectObj.imageId)
            if (item.subjectObj.openIn) {
                getImgObject(holder.imgOpenIn, 0)
                holder.imgOpenInBack.visibility = View.VISIBLE
                holder.imgOpenIn.visibility = View.VISIBLE
                holder.imgOpenIn.setOnClickListener {
                    if (!startSkype(holder.itemView.context)) holder.itemView.showSnackBar("Can't launch Skype!")
                }
                holder.imgOpenInBack.setOnClickListener {
                    if (!startSkype(holder.itemView.context)) holder.itemView.showSnackBar("Can't launch Skype!")
                }
            } else {
                holder.imgOpenIn.visibility = View.GONE
                holder.imgOpenInBack.visibility = View.GONE
            }
            holder.interval.text = String.format(
                holder.itemView.context.getString(R.string.interval),
                dateToStrHM(item.fromDate), dateToStrHM(item.toDate)
            )
            holder.interval.setTextColor(
                holder.itemView.context.resources.getColor(
                    if (position == positionNearest) R.color.days_left1 else R.color.days_left2,
                    null
                )
            )
        } else {
            holder as ViewHolderOpt
            holder.name.text = item.subjectObj.name
            getImgObject(holder.img, item.subjectObj.imageId)
            holder.interval.text = String.format(
                holder.itemView.context.getString(R.string.interval),
                dateToStrHM(item.fromDate), dateToStrHM(item.toDate)
            )
            holder.interval.setTextColor(
                holder.itemView.context.resources.getColor(
                    if (position == positionNearest) R.color.days_left1 else R.color.days_left2,
                    null
                )
            )
        }
    }

    override fun getItemCount(): Int = values.size
    override fun getItemViewType(position: Int): Int {
        return if (values[position].subjectObj.obligatory) TYPE_OBLIGATORY else TYPE_OPTIONAL
    }

    inner class ViewHolder(binding: ItemClassesHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val img: RoundedImageView = binding.imageSubject
        val imgOpenIn: RoundedImageView = binding.imageOpenin
        val imgOpenInBack: ImageView = binding.imageOpeninBack
        val interval: TextView = binding.itemInterval

    }

    inner class ViewHolderOpt(binding: ItemClassesHomeOptBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val img: RoundedImageView = binding.imageSubject
        val interval: TextView = binding.itemInterval
    }

    companion object {
        private const val TYPE_OBLIGATORY = 1
        private const val TYPE_OPTIONAL = 0
    }

}