package android.dev.kalmurzaeff.neonote.ui.adapter

import android.dev.kalmurzaeff.neonote.data.models.OnBoardData
import android.dev.kalmurzaeff.neonote.ui.onboard.PagerDataFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardAdapter(
    fragmentActivity: FragmentActivity,
    val list: List<OnBoardData>
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = list.size


    override fun createFragment(position: Int): Fragment {
        return PagerDataFragment.newInstance(list[position],)
    }
}