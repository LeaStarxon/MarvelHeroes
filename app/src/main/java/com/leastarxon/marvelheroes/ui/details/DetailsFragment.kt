package com.leastarxon.marvelheroes.ui.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.leastarxon.marvelheroes.R
import com.leastarxon.marvelheroes.databinding.DetailsFragmentBinding
import kotlinx.android.synthetic.main.details_fragment.*


class DetailsFragment : Fragment() {
    companion object {
        fun newInstance() =
            DetailsFragment()

        const val HERO_NAME: String = "HERO_NAME"
        const val HERO_INFO: String = "HERO_INFO"
        const val HERO_IMAGE_URL: String = "HERO_IMAGE_URL"
        const val TRANSITION_NAME: String = "TRANSITION_NAME"
    }

    private lateinit var viewModel: DetailsViewModel
    private lateinit var binding: DetailsFragmentBinding
    private var transitionName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.details_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        addData(arguments)
        if(savedInstanceState!= null){
            addData(savedInstanceState)
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(HERO_NAME, viewModel.name.value)
        outState.putString(HERO_IMAGE_URL, viewModel.url.value)
        outState.putString(HERO_INFO, viewModel.info.value)
        outState.putString(TRANSITION_NAME, transitionName)
    }

    private fun addData(bundle: Bundle?) {
        val heroName = bundle?.getString(HERO_NAME) ?: ""
        val heroInfo = bundle?.getString(HERO_INFO) ?: ""
        val heroImageUrl = bundle?.getString(HERO_IMAGE_URL) ?: ""
        transitionName = bundle?.getString(TRANSITION_NAME) ?: ""
        if(!transitionName.isEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                photoHeroDetails?.setTransitionName(transitionName)
            }
        }
        viewModel.initViews(heroName, heroImageUrl, heroInfo)
    }
}
