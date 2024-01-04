package android.dev.kalmurzaeff.neonote.ui.update

import android.dev.kalmurzaeff.neonote.data.local.entity.NoteEntity
import android.dev.kalmurzaeff.neonote.ui.adapter.ColorAdapter
import android.dev.kalmurzaeff.neonote.utils.colors
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.databinding.FragmentUpdateNoteBinding
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateNoteFragmentArgs>()
    private lateinit var colorAdapter: ColorAdapter
    private var colorPosition: Int = -1

    private var imageUri: String? = null

    private val viewModel: UpdateViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpdateNoteBinding.bind(view)

        viewModel.noteById
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .filterNotNull()
            .onEach { note ->
                configureUI(note)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

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

    private fun configureUI(note: NoteEntity) {
        binding.titleEdt.setText(note.title)
        binding.descriptionEdt.setText(note.description)
        if (note.imageUri != null && !TextUtils.isEmpty(note.imageUri)) {
            Glide.with(requireContext()).load(note.imageUri).into(binding.ivAddImage)
            binding.ivDelete.visibility = View.VISIBLE
        }

        configureAdapter(note.colorPosition)

        colorPosition = note.colorPosition
        imageUri = note.imageUri
    }

    // Access your images
    private val selectImageIntent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri.toString()
                Glide.with(requireContext()).load(imageUri).into(binding.ivAddImage)
                binding.ivDelete.visibility = View.VISIBLE
            } else {
                binding.ivDelete.visibility = View.GONE
            }
        }

    private fun validateData() {
        val title = binding.titleEdt.text.toString()
        val description = binding.descriptionEdt.text.toString()
        if (title.isEmpty() || description.isEmpty() || colorPosition == -1) {
            Toast.makeText(requireContext(), "Please fill up all fields", Toast.LENGTH_SHORT).show()
        } else {
            val note = NoteEntity(
                title, description, imageUri, colorPosition, System.currentTimeMillis(), args.noteId
            )
            viewModel.upsertNote(note)
            findNavController().navigate(UpdateNoteFragmentDirections.navigateToListFragment())
        }

    }

    private fun configureAdapter(selectedPosition: Int) {
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