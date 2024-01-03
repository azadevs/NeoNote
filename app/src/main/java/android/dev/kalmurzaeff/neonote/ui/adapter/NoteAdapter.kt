package android.dev.kalmurzaeff.neonote.ui.adapter

import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import android.dev.kalmurzaeff.neonote.utils.colors
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.databinding.RowItemNoteBinding
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NoteAdapter(
    private val onClickItem: (Int) -> Unit,
    private val onLongClickItem: (NoteEntity) -> Unit,
    private val onClickIsSaveItem: (NoteEntity, Boolean) -> Unit,
) : ListAdapter<NoteEntity, NoteAdapter.NoteViewHolder>(NoteDiffUtil()) {

    inner class NoteViewHolder(private val binding: RowItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(noteEntity: NoteEntity) {
            with(binding) {
                rootCardView.setCardBackgroundColor(Color.parseColor(colors()[noteEntity.colorPosition]))
                tvTitle.text = noteEntity.title
                descriptionTxt.text = noteEntity.description

                /***
                 * Here's how the image uri couldn't be empty or null.
                 * If empty or null, we make the imageView invisible.
                 * If it's not empty, we'll set uri to imageView
                 */
                if (validateUri(noteEntity)) {
                    cardView.visibility = View.VISIBLE
                    Glide.with(binding.root.context).load(noteEntity.imageUri).into(binding.ivUser)
                } else {
                    cardView.visibility = View.INVISIBLE
                }

                ivSave.setImageResource(if (noteEntity.isSave) R.drawable.ic_bookmark_checked else R.drawable.ic_bookmark_unchecked)

                var bool = noteEntity.isSave
                ivSave.setOnClickListener {
                    bool = if (bool) {
                        ivSave.setImageResource(R.drawable.ic_bookmark_unchecked)
                        false
                    } else {
                        ivSave.setImageResource(R.drawable.ic_bookmark_checked)
                        true
                    }
                    onClickIsSaveItem.invoke(noteEntity, bool)
                }

                root.setOnClickListener {
                    onClickItem.invoke(noteEntity.id)
                }

                // If the user presses the item for a long time, the item will be deleted
                root.setOnLongClickListener {
                    onLongClickItem.invoke(noteEntity)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            RowItemNoteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    private fun validateUri(noteEntity: NoteEntity): Boolean {
        return noteEntity.imageUri != null && !TextUtils.isEmpty(noteEntity.imageUri)
    }

    class NoteDiffUtil : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem == newItem
        }
    }

}