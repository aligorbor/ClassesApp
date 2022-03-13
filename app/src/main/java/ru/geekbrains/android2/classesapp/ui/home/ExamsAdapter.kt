package ru.geekbrains.android2.classesapp.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.makeramen.roundedimageview.RoundedImageView
import ru.geekbrains.android2.classesapp.databinding.ItemExamBinding
import ru.geekbrains.android2.classesapp.databinding.ItemExamOptBinding
import ru.geekbrains.android2.classesapp.model.entities.ExamObj
import ru.geekbrains.android2.classesapp.utils.DHMFromNow
import ru.geekbrains.android2.classesapp.utils.dateToStr
import ru.geekbrains.android2.classesapp.utils.getImgObject

class ExamsAdapter(
    private var values: List<ExamObj>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setValues(newValues: List<ExamObj>) {
        values = newValues
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updValues() = notifyDataSetChanged()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_OBLIGATORY) ViewHolder(
            ItemExamBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else
            ViewHolderOpt(
                ItemExamOptBinding.inflate(
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
            holder.examDate.text = dateToStr(item.dateExam)
            val str = DHMFromNow(item.dateExam)
            holder.dhmD0.text = str[0].toString()
            holder.dhmD1.text = str[1].toString()
            holder.dhmH3.text = str[3].toString()
            holder.dhmH4.text = str[4].toString()
            holder.dhmM6.text = str[6].toString()
            holder.dhmM7.text = str[7].toString()
        } else {
            holder as ViewHolderOpt
            val str = DHMFromNow(item.dateExam)
            holder.name.text = item.subjectObj.name
            getImgObject(holder.img, item.subjectObj.imageId)
            holder.examDate.text = dateToStr(item.dateExam)
            holder.dhmD0.text = str[0].toString()
            holder.dhmD1.text = str[1].toString()
            holder.dhmH3.text = str[3].toString()
            holder.dhmH4.text = str[4].toString()
            holder.dhmM6.text = str[6].toString()
            holder.dhmM7.text = str[7].toString()
        }
    }

    override fun getItemCount(): Int = values.size
    override fun getItemViewType(position: Int): Int {
        return if (values[position].subjectObj.obligatory) TYPE_OBLIGATORY else TYPE_OPTIONAL
    }

    inner class ViewHolder(binding: ItemExamBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val img: RoundedImageView = binding.imageSubject
        val examDate: TextView = binding.itemDate
        val dhmD0: Chip = binding.dhmD0
        val dhmD1: Chip = binding.dhmD1
        val dhmH3: Chip = binding.dhmH3
        val dhmH4: Chip = binding.dhmH4
        val dhmM6: Chip = binding.dhmM6
        val dhmM7: Chip = binding.dhmM7
    }

    inner class ViewHolderOpt(binding: ItemExamOptBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.itemName
        val img: RoundedImageView = binding.imageSubject
        val examDate: TextView = binding.itemDate
        val dhmD0: Chip = binding.dhmD0
        val dhmD1: Chip = binding.dhmD1
        val dhmH3: Chip = binding.dhmH3
        val dhmH4: Chip = binding.dhmH4
        val dhmM6: Chip = binding.dhmM6
        val dhmM7: Chip = binding.dhmM7
    }

    companion object {
        private const val TYPE_OBLIGATORY = 1
        private const val TYPE_OPTIONAL = 0
    }

}