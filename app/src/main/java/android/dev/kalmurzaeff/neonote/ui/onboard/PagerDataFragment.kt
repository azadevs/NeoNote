package android.dev.kalmurzaeff.neonote.ui.onboard

import android.dev.kalmurzaeff.neonote.models.OnBoardData
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.databinding.FragmentPagerDataBinding
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class PagerDataFragment : Fragment(R.layout.fragment_pager_data) {
    private var _binding: FragmentPagerDataBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPagerDataBinding.bind(view)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(KEY_PAGER_DATA, OnBoardData::class.java)
        } else {
            arguments?.getSerializable(KEY_PAGER_DATA) as OnBoardData
        }

        if (data != null) {
            binding.apply {
                ivImage.setImageResource(data.image)
                tvTitle.text = data.title
                tvDescription.text = data.description
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_PAGER_DATA = "pagerData"
        fun newInstance(data: OnBoardData): Fragment {
            val fragment = PagerDataFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_PAGER_DATA, data)
            fragment.arguments = bundle

            return fragment
        }
    }

}