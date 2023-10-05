package android.dev.kalmurzaeff.notesapp.ui.update

import android.dev.kalmurzaeff.notesapp.ui.MainActivity
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.data.local.NoteEntity
import android.dev.kalmurzaeff.notesapp.databinding.FragmentUpdateNoteBinding
import android.dev.kalmurzaeff.notesapp.ui.adapter.ColorAdapter
import android.dev.kalmurzaeff.notesapp.utils.colors
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide

class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateNoteFragmentArgs>()
    private lateinit var colorAdapter: ColorAdapter
    private var colorPosition: Int = -1

    private var imageUri: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpdateNoteBinding.bind(view)

        val note = (activity as MainActivity).database.noteDao().getNoteById(args.noteId)

        binding.titleEdt.setText(note.title)
        binding.descriptionEdt.setText(note.description)
        if (note.imageUri != null && !TextUtils.isEmpty(note.imageUri)) {
            Glide.with(requireContext()).load(note.imageUri).into(binding.ivAddImage)
            binding.ivDelete.visibility = View.VISIBLE
        }

        setUpColorRecyclerView(note.colorPosition)

        colorPosition = note.colorPosition
        imageUri = note.imageUri

        binding.btnAdd.setOnClickListener {
            validateData()
        }
        binding.ivAddImage.setOnClickListener {
            selectImageIntent.launch("image/*")
        }

        binding.ivDelete.setOnClickListener {
            imageUri = null
            binding.ivAddImage.setImageResource(R.drawable.place_holder)
            binding.ivDelete.visibility = View.GONE
        }
    }

    // Access your images
    private val selectImageIntent = registerForActivityResult(ActivityResultContracts.GetContent())
    { uri ->
        if (uri != null) {
            imageUri = uri.toString()
            Glide.with(requireContext()).load(imageUri).into(binding.ivAddImage)
            binding.ivDelete.visibility = View.VISIBLE
        } else {
            binding.ivDelete.visibility = View.GONE
        }
    }

    private fun validateData() {
        val database = (activity as MainActivity).database

        val title = binding.titleEdt.text.toString()
        val description = binding.descriptionEdt.text.toString()
        if (title.isEmpty() || description.isEmpty() || colorPosition == -1) {
            Toast.makeText(requireContext(), "Please fill up all fields", Toast.LENGTH_SHORT).show()
        } else {
            val note =
                NoteEntity(
                    title,
                    description,
                    imageUri,
                    colorPosition,
                    System.currentTimeMillis(),
                    args.noteId
                )
            database.noteDao().upsertNote(note)

            findNavController().navigate(UpdateNoteFragmentDirections.navigateToListFragment())
        }

    }

    private fun setUpColorRecyclerView(selectedPosition: Int) {
        colorAdapter = ColorAdapter(colors()) { position ->
            colorPosition = position
        }
        binding.rvColor.adapter = colorAdapter
        colorAdapter.setColorSelected(selectedPosition)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}