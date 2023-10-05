package android.dev.kalmurzaeff.notesapp.ui.bookmark

import android.app.AlertDialog
import android.content.DialogInterface
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.data.local.NoteDatabase
import android.dev.kalmurzaeff.notesapp.data.local.NoteEntity
import android.dev.kalmurzaeff.notesapp.databinding.FragmentBookmarksBinding
import android.dev.kalmurzaeff.notesapp.ui.MainActivity
import android.dev.kalmurzaeff.notesapp.ui.adapter.NoteAdapter
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {
    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NoteAdapter

    private lateinit var database: NoteDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBookmarksBinding.bind(view)

        database = (activity as MainActivity).database

        val list = ArrayList(database.noteDao().getSavedNotes())

        adapter = NoteAdapter({ noteId ->
            val navigateToDetailFragment =
                BookmarksFragmentDirections.actionBookmarksFragmentToDetailNoteFragment(noteId)
            findNavController().navigate(navigateToDetailFragment)
        }, { note ->
            showDeleteDialog(note)
        }, { noteEntity, bool,position ->
            database.noteDao().upsertNote(noteEntity.copy(isSave = bool))
            if (!bool) {
                list.remove(noteEntity)
                adapter.notifyItemRemoved(position)
            }
            isEmptyList()
        })
        binding.rvNotes.adapter = adapter
        adapter.setData(list)

    }

    private fun showDeleteDialog(noteEntity: NoteEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(noteEntity.title)
        builder.setMessage("Are you sure want to delete this note?")
        builder.setIcon(R.drawable.ic_delete)
        builder.setPositiveButton(R.string.yes) { p0, p1 ->
            database.noteDao().deleteNote(noteEntity)
            adapter.setData(database.noteDao().getSavedNotes())
            isEmptyList()
            p0.dismiss()
        }
        builder.setNegativeButton(R.string.no) { p0, p1 ->
            p0.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        val positiveBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveBtn.setTextColor(Color.BLACK)
        val negativeBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeBtn.setTextColor(Color.BLACK)
    }

    private fun isEmptyList() {
        if (database.noteDao().getSavedNotes().isEmpty()) {
            binding.txtNoData.visibility = View.VISIBLE
        } else {
            binding.txtNoData.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        isEmptyList()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}