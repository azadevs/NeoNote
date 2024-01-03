package android.dev.kalmurzaeff.neonote.ui.add

import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import android.dev.kalmurzaeff.neonote.ui.adapter.ColorAdapter
import android.dev.kalmurzaeff.neonote.utils.colors
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.databinding.FragmentAddNoteBinding
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment(R.layout.fragment_add_note) {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var colorAdapter: ColorAdapter
    private var imageUri: String? = null
    private var colorPosition = -1
    private val viewModel: AddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddNoteBinding.bind(view)

        configureAdapter()

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
        val title = binding.titleEdt.text.toString()
        val description = binding.descriptionEdt.text.toString()
        if (!(TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || colorPosition == -1)) {
            saveNoteToDatabase(title, description)
        } else {
            Toast.makeText(requireContext(), "Please fill up all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveNoteToDatabase(title: String, description: String) {
        val note =
            NoteEntity(title, description, imageUri, colorPosition, System.currentTimeMillis())
        viewModel.upsertNote(note)
        findNavController().popBackStack()
    }

    private fun configureAdapter() {
        colorAdapter = ColorAdapter(colors()) { position ->
            colorPosition = position
        }
        binding.rvColor.adapter = colorAdapter
    }

    // Access your images
    private val selectImageIntent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri.toString()
                Glide.with(requireContext()).load(imageUri).into(binding.ivAddImage)
                showDeleteIcon(true)
            } else {
                showDeleteIcon(false)
            }
        }

    private fun showDeleteIcon(boolean: Boolean) {
        if (boolean) {
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
