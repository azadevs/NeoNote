package android.dev.kalmurzaeff.neonote.ui.list

import android.app.AlertDialog
import android.content.DialogInterface
import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import android.dev.kalmurzaeff.neonote.data.local.prefs.SharedPrefsManager
import android.dev.kalmurzaeff.neonote.ui.adapter.NoteAdapter
import android.dev.kalmurzaeff.neonote.utils.Resource
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.databinding.FragmentNotesBinding
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes), SearchView.OnQueryTextListener {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NoteAdapter
    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPrefsManager.init(requireContext())
        val bool = SharedPrefsManager.readData(false)
        if (!bool) {
            findNavController().navigate(R.id.action_listNoteFragment_to_fragmentOnBoarding)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotesBinding.bind(view)

        configureRecyclerView()

        onCreateMenu()

        configureNotesState()

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.navigateToAddFragment)
        }

        viewModel.getAllNotes()
    }

    private fun configureNotesState() {
        viewModel.notes
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            result.messages,
                            Toast.LENGTH_SHORT
                        ).show()
                        showProgressBar(false)
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

    private fun configureRecyclerView() {
        adapter = NoteAdapter({ noteId ->
            val navigateToDetailFragment =
                NotesFragmentDirections.navigateToDetailFragment(noteId)
            findNavController().navigate(navigateToDetailFragment)
        }, { note ->
            configureDeleteDialog(note)
        }, { noteEntity, bool ->
            viewModel.upsertNote(noteEntity.copy(isSave = bool))
        })
        binding.rvNotes.adapter = adapter
    }

    private fun onCreateMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list_fragment, menu)
                val search = menu.findItem(R.id.search)
                val searView = search.actionView as? SearchView
                searView?.isSubmitButtonEnabled = true
                searView?.setOnQueryTextListener(this@NotesFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        }, viewLifecycleOwner)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    @OptIn(FlowPreview::class)
    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchByTitle(searchQuery)
        viewLifecycleOwner.lifecycleScope
            .launch {
                viewModel.searchingNotes
                    .debounce(500)
                    .collectLatest {
                        adapter.submitList(it)
                    }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}