package ganteng.hendrawd.footballmatchschedule.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ganteng.hendrawd.footballmatchschedule.R
import org.jetbrains.anko.dip
import org.jetbrains.anko.padding
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView
import org.jetbrains.anko.textView

/**
 * @author hendrawd on 08/08/18
 */
class TeamDescriptionFragment : Fragment() {

    companion object {
        fun newInstance(description: String): TeamDescriptionFragment {
            val args = Bundle()
            args.putString("description", description)
            val teamDescriptionFragment = TeamDescriptionFragment()
            teamDescriptionFragment.arguments = args
            return teamDescriptionFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val description = arguments?.getString("description") ?: ""
        return UI {
            nestedScrollView {
                textView {
                    text = description
                    padding = dip(16)
                }
                // need to give id to retain scroll position when orientation changed
                id = R.id.nestedScrollViewDescription
            }
        }.view
    }
}