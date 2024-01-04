package android.dev.kalmurzaeff.neonote.ui.detail

import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import android.dev.kalmurzaeff.neonote.utils.colors
import android.dev.kalmurzaeff.neonote.utils.convertToDate
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.databinding.FragmentDetailNoteBinding
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailNoteFragment : Fragment(R.layout.fragment_detail_note) {

    private var _binding: FragmentDetailNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    private var noteId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailNoteBinding.bind(view)

        viewModel.noteById
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .filterNotNull()
            .onEach { note ->
                configureUI(note)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        onCreateMenu()

    }

    private fun configureUI(note: NoteEntity) {
        binding.tvTitle.text = note.title
        binding.tvDescription.text = note.description
        binding.tvDate.text = convertToDate(note.date)
        if (note.imageUri != null && !TextUtils.isEmpty(note.imageUri)) {
            Glide.with(requireContext()).load(note.imageUri).into(binding.ivUser)
        }
        binding.cardViewColor.setCardBackgroundColor(Color.parseColor(colors()[note.colorPosition]))
        noteId = note.id
    }


    private fun onCreateMenu() {
        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.update) {
                    val navigateToUpdateFragment =
                        DetailNoteFragmentDirections.navigateToUpdateFragment(noteId)
                    findNavController().navigate(navigateToUpdateFragment)
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}