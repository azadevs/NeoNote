package android.dev.kalmurzaeff.neonote.ui.detail

import android.dev.kalmurzaeff.neonote.ui.MainActivity
import android.dev.kalmurzaeff.neonote.utils.colors
import android.dev.kalmurzaeff.neonote.utils.convertToDate
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.databinding.FragmentDetailNoteBinding
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide

class DetailNoteFragment : Fragment(R.layout.fragment_detail_note) {

    private var _binding: FragmentDetailNoteBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailNoteFragmentArgs>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailNoteBinding.bind(view)

        val database = (activity as MainActivity).database

        val note = database.noteDao().getNoteById(args.noteId)
        onCreateMenu()

        binding.tvTitle.text = note.title
        binding.tvDescription.text = note.description
        binding.tvDate.text = convertToDate(note.date)
        if (note.imageUri != null && !TextUtils.isEmpty(note.imageUri)) {
            Glide.with(requireContext()).load(note.imageUri).into(binding.ivUser)
        }
        binding.cardViewColor.setCardBackgroundColor(Color.parseColor(colors()[note.colorPosition]))
    }


    private fun onCreateMenu() {
        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.update) {
                    val navigateToUpdateFragment =
                        DetailNoteFragmentDirections.navigateToUpdateFragment(args.noteId)
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