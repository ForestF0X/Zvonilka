package com.eric.zvonilka.fragmentsNav.meets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.eric.zvonilka.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_sheet.*


class MeetsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_meets, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<View>(bottom_sheet)
        val initState = BottomSheetBehavior.STATE_COLLAPSED
        bottom_sheet.post(Runnable { behavior.state = initState })
    }
}