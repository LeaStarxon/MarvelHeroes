package com.leastarxon.marvelheroes.ui.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.leastarxon.marvelheroes.R
import com.leastarxon.marvelheroes.databinding.ListFragmentBinding
import com.leastarxon.marvelheroes.model.MarvelCharactersListResponse

class ListFragment : Fragment() {
    companion object {
        fun newInstance() =
            ListFragment()

        const val ITEM_POSITION: String = "ITEM_POSITION"
        const val FIRST_TIME: String = "FIRST_TIME"
        const val HEROES: String = "HEROES"
    }

    private var viewModel: ListViewModel? = null
    private lateinit var binding: ListFragmentBinding
    private var firstTime = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.list_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        val boolean = savedInstanceState?.getBoolean(FIRST_TIME)
        firstTime = if (boolean != null) boolean else true
        var charactersListResponse = savedInstanceState?.getSerializable(HEROES) as? MarvelCharactersListResponse
        viewModel?.initViews(activity as AppCompatActivity?, firstTime, charactersListResponse)

        if (savedInstanceState != null) {
            var position = savedInstanceState?.getInt(ITEM_POSITION) ?: 0
            viewModel?.goToIndex(position)
        }
        if(firstTime){
            firstTime = false
        }
        viewModel?.loadList(firstTime, 0)
            ?.observe(this, Observer<MarvelCharactersListResponse> { response ->
                viewModel?.refreshData(response)
            })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }

    override fun onStop() {
        super.onStop()
        if (viewModel != null) {
            viewModel!!.goToIndex(viewModel!!.getCurrentIndex())

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (viewModel != null) {
            outState.putInt(ITEM_POSITION, viewModel!!.getCurrentIndex())
            outState.putBoolean(FIRST_TIME, firstTime)
            outState.putSerializable(HEROES, viewModel?.heroes)
        }
    }
}
