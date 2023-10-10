package android.dev.kalmurzaeff.neonote.ui.adapter

import android.dev.kalmurzaeff.notesapp.databinding.RowItemColorBinding
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ColorAdapter(
    private val colors: List<String>,
    private val onclickItem: (position: Int) -> Unit,
) : RecyclerView.Adapter<ColorAdapter.ColorVh>() {

    private var selectedPosition: Int = -1

    inner class ColorVh(private val binding: RowItemColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(color: String) {
            binding.cardView.setCardBackgroundColor(Color.parseColor(color))
            if (selectedPosition == adapterPosition) {
                binding.ivChecked.visibility = View.VISIBLE
            } else {
                binding.ivChecked.visibility = View.INVISIBLE
            }
            binding.cardView.setOnClickListener {
                selectedPosition = adapterPosition
                binding.ivChecked.visibility = View.VISIBLE
                onclickItem.invoke(adapterPosition)

                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorVh {
        return ColorVh(
            RowItemColorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: ColorVh, position: Int) {
        holder.onBind(colors[position])
    }

    fun setColorSelected(position: Int) {
        selectedPosition = position
    }
}