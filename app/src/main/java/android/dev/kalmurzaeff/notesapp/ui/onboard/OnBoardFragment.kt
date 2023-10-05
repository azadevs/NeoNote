package android.dev.kalmurzaeff.notesapp.ui.onboard

import android.dev.kalmurzaeff.notesapp.ui.MainActivity
import android.dev.kalmurzaeff.notesapp.R
import android.dev.kalmurzaeff.notesapp.databinding.FragmentOnboardBinding
import android.dev.kalmurzaeff.notesapp.prefs.SharedPrefsManager
import android.dev.kalmurzaeff.notesapp.ui.adapter.OnboardAdapter
import android.dev.kalmurzaeff.notesapp.utils.getOnBoardDataList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

class OnBoardFragment : Fragment(R.layout.fragment_onboard) {
    private var _binding: FragmentOnboardBinding? = null
    private val binding get() = _binding!!
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        SharedPrefsManager.init(requireContext())


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardBinding.bind(view)

        val pagerAdapter = OnboardAdapter(requireActivity(), getOnBoardDataList())

        binding.vpOnBoarding.adapter = pagerAdapter

        binding.btnNext.setOnClickListener {
            position++
            binding.vpOnBoarding.setCurrentItem(position, true)
        }



        binding.vpOnBoarding.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position > 1) {
                    binding.btnNext.text = "Finish"
                    binding.btnNext.setOnClickListener {
                        SharedPrefsManager.writeData(true)
                        findNavController().navigate(R.id.action_fragmentOnBoarding_to_listNoteFragment)
                    }
                } else {
                    binding.btnNext.text = "Next"
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).supportActionBar?.show()
        _binding = null
    }
}