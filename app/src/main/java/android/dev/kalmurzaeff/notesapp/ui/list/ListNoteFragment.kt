package android.dev.kalmurzaeff.notesapp.ui.list

import android.app.AlertDialog
import android.content.DialogInterface
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.data.local.NoteDatabase
import android.dev.kalmurzaeff.notesapp.data.local.NoteEntity
import android.dev.kalmurzaeff.notesapp.databinding.FragmentListNotesBinding
import android.dev.kalmurzaeff.notesapp.prefs.SharedPrefsManager
import android.dev.kalmurzaeff.notesapp.ui.MainActivity
import android.dev.kalmurzaeff.notesapp.ui.adapter.NoteAdapter
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController

class ListNoteFragment : Fragment(R.layout.fragment_list_notes), SearchView.OnQueryTextListener {

    private var _binding: FragmentListNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NoteAdapter

    private lateinit var database: NoteDatabase

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
        _binding = FragmentListNotesBinding.bind(view)

        database = (activity as MainActivity).database

        setUpRecyclerView()

        onCreateMenu()

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.navigateToAddFragment)
        }

    }

    private fun setUpRecyclerView() {
        adapter = NoteAdapter({ noteId ->
            val navigateToDetailFragment =
                ListNoteFragmentDirections.navigateToDetailFragment(noteId)
            findNavController().navigate(navigateToDetailFragment)
        }, { note ->
            showDeleteDialog(note)
        }, { noteEntity, bool, _ ->
            database.noteDao().upsertNote(noteEntity.copy(isSave = bool))
        })
        binding.rvNotes.adapter = adapter
        adapter.setData(database.noteDao().getAllNotes())
    }

    private fun onCreateMenu() {

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list_fragment, menu)

                val search = menu.findItem(R.id.search)
                val searView = search.actionView as? SearchView
                searView?.isSubmitButtonEnabled = true
                searView?.setOnQueryTextListener(this@ListNoteFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        adapter.setData(
            database.noteDao().searchByTitle(searchQuery)
        )
    }

    private fun isEmptyList() {
        if (database.noteDao().getAllNotes().isEmpty()) {
            binding.txtNoData.visibility = View.VISIBLE
        } else {
            binding.txtNoData.visibility = View.INVISIBLE
        }
    }

    private fun showDeleteDialog(noteEntity: NoteEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(noteEntity.title)
        builder.setMessage("Are you sure want to delete this note?")
        builder.setIcon(R.drawable.ic_delete)
        builder.setPositiveButton(R.string.yes) { p0, p1 ->
            database.noteDao().deleteNote(noteEntity)
            adapter.setData(database.noteDao().getAllNotes())
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

    override fun onResume() {
        super.onResume()
        isEmptyList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}