package android.dev.kalmurzaeff.neonote.ui.bookmark

import android.app.AlertDialog
import android.content.DialogInterface
import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import android.dev.kalmurzaeff.neonote.ui.adapter.NoteAdapter
import android.dev.kalmurzaeff.neonote.utils.Resource
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.databinding.FragmentBookmarksBinding
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {
    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NoteAdapter
    private val viewModel: BookmarksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBookmarksBinding.bind(view)

        configureAdapter()

        configureNotesState()

        viewModel.getSavedNotes()


    }

    private fun configureNotesState() {
        viewModel.notes
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        showProgressBar(false)
                        Toast.makeText(requireContext(), result.messages, Toast.LENGTH_SHORT).show()
                    }

                    Resource.Loading -> showProgressBar(true)
                    is Resource.Success -> {
                        showProgressBar(false)
                        adapter.submitList(result.notes)
                        isEmptyList(result.notes)
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun configureAdapter() {
        adapter = NoteAdapter({ noteId ->
            val navigateToDetailFragment =
                BookmarksFragmentDirections.navigateToDetailFragment(noteId)
            findNavController().navigate(navigateToDetailFragment)
        }, { note ->
            configureDeleteDialog(note)
        }, { noteEntity, bool ->
            viewModel.upsertNote(noteEntity.copy(isSave = bool))
        })
        binding.rvNotes.adapter = adapter
    }

    private fun configureDeleteDialog(noteEntity: NoteEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(noteEntity.title)
        builder.setMessage("Are you sure want to delete this note?")
        builder.setIcon(R.drawable.ic_delete)
        builder.setPositiveButton(R.string.yes) { p0, _ ->
            viewModel.deleteNote(noteEntity)
            p0.dismiss()
        }
        builder.setNegativeButton(R.string.no) { p0, _ ->
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

    private fun isEmptyList(notes: List<NoteEntity>) {
        if (notes.isEmpty()) {
            binding.txtNoData.visibility = View.VISIBLE
        } else {
            binding.txtNoData.visibility = View.INVISIBLE
        }
    }


    private fun showProgressBar(bool: Boolean) {
        if (bool) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}