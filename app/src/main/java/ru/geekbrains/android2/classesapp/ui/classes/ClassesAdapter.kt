package ru.geekbrains.android2.classesapp.ui.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import ru.geekbrains.android2.classesapp.R
import ru.geekbrains.android2.classesapp.databinding.ItemClassesBinding
import ru.geekbrains.android2.classesapp.databinding.ItemClassesOptBinding
import ru.geekbrains.android2.classesapp.model.entities.ScheduleObj
import ru.geekbrains.android2.classesapp.utils.*


class ClassesAdapter(
    private val values: List<ScheduleObj>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var positionNearest: Int = 0

    fun setPosition(position: Int) {
        positionNearest = position
        notifyItemChanged(positionNearest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_OBLIGATORY) ViewHolder(
            ItemClassesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else
            ViewHolderOpt(
                ItemClassesOptBinding.inflate(
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
            holder.teacher.text = String.format(
                holder.itemView.context.getString(R.string.teacher),
                item.subjectObj.teacher
            )
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
            holder.dOfW.text = if (item.order == 1) dateToStrDayOfWeek(item.toDate) else ""
            if (position == positionNearest) holder.imgDivider.setImageResource(R.drawable.divider_large)
            else holder.imgDivider.setImageResource(R.drawable.divider_small)
        } else {
            holder as ViewHolderOpt
            holder.name.text = item.subjectObj.name
            holder.teacher.text = String.format(
                holder.itemView.context.getString(R.string.teacher),
                item.subjectObj.teacher
            )
            holder.about.text = item.subjectObj.about
            getImgObject(holder.img, item.subjectObj.imageId)
            holder.interval.text = String.format(
                holder.itemView.context.getString(R.string.interval),
                dateToStrHM(item.fromDate), dateToStrHM(item.toDate)
            )
            holder.dOfW.text = if (item.order == 1) dateToStrDayOfWeek(item.toDate) else ""
            if (position == positionNearest) holder.imgDivider.setImageResource(R.drawable.divider_large)
            else holder.imgDivider.setImageResource(R.drawable.divider_small)
        }
    }

    override fun getItemCount(): Int = values.size
    override fun getItemViewType(position: Int): Int {
        return if (values[position].subjectObj.obligatory) TYPE_OBLIGATORY else TYPE_OPTIONAL
    }

    inner class ViewHolder(binding: ItemClassesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val teacher: TextView = binding.itemTeacher
        val img: RoundedImageView = binding.imageSubject
        val imgOpenIn: RoundedImageView = binding.imageOpenin
        val imgOpenInBack: ImageView = binding.imageOpeninBack
        val interval: TextView = binding.textInterval
        val dOfW: TextView = binding.textDofw
        val imgDivider: ImageView = binding.imageDivider
    }

    inner class ViewHolderOpt(binding: ItemClassesOptBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val teacher: TextView = binding.itemTeacher
        val about: TextView = binding.itemAbout
        val img: RoundedImageView = binding.imageSubject
        val interval: TextView = binding.textInterval
        val dOfW: TextView = binding.textDofw
        val imgDivider: ImageView = binding.imageDivider
    }

    companion object {
        private const val TYPE_OBLIGATORY = 1
        private const val TYPE_OPTIONAL = 0
    }

}