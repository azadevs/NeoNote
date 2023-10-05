package android.dev.kalmurzaeff.notesapp.ui.add

import android.dev.kalmurzaeff.notesapp.ui.MainActivity
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.data.local.NoteEntity
import android.dev.kalmurzaeff.notesapp.databinding.FragmentAddNoteBinding
import android.dev.kalmurzaeff.notesapp.ui.adapter.ColorAdapter
import android.dev.kalmurzaeff.notesapp.utils.colors
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide


class AddNoteFragment : Fragment(R.layout.fragment_add_note) {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var colorAdapter: ColorAdapter
    private var imageUri: String? = null
    private var colorPosition = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddNoteBinding.bind(view)

        setUpRecyclerView()

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

    private fun validateData() {
        val database = (activity as MainActivity).database
        val title = binding.titleEdt.text.toString()
        val description = binding.descriptionEdt.text.toString()
        if (title.isEmpty() || description.isEmpty() || colorPosition == -1) {
            Toast.makeText(requireContext(), "Please fill up all fields", Toast.LENGTH_SHORT).show()
        } else {
            val note =
                NoteEntity(title, description, imageUri, colorPosition, System.currentTimeMillis())
            database.noteDao().upsertNote(note)

            findNavController().popBackStack()
        }

    }

    private fun setUpRecyclerView() {
        colorAdapter = ColorAdapter(colors()) { position ->
            colorPosition = position
        }
        binding.rvColor.adapter = colorAdapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
