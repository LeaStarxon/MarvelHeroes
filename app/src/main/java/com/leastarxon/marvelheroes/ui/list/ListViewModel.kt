package com.leastarxon.marvelheroes.ui.list

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leastarxon.marvelheroes.BR
import com.leastarxon.marvelheroes.R
import com.leastarxon.marvelheroes.api.HeroesRepository
import com.leastarxon.marvelheroes.model.Hero
import com.leastarxon.marvelheroes.model.MarvelCharactersListResponse
import com.leastarxon.marvelheroes.model.Thumbnail
import com.leastarxon.marvelheroes.utils.ui.BaseAdapter

class ListViewModel() : ViewModel() {
    lateinit var adapter: BaseAdapter<Hero>
    lateinit var layoutManager: GridLayoutManager
    var heroesResponse = MutableLiveData<MarvelCharactersListResponse>()
        get() = field
    lateinit var scrollListener: RecyclerView.OnScrollListener
    var isRefreshing = MutableLiveData<Boolean>()
        get() = field
    var emptyList = MutableLiveData<Boolean>()
        get() = field
    var onTopRecycler = MutableLiveData<Boolean>()
        get() = field
    var currentItemInRecycler = MutableLiveData<Int>()
        get() = field
    var heroes = ArrayList<Hero>()
    private var currentOffset = 0;
    lateinit var heroesRepository: HeroesRepository
    private var willLoad = true

    init {
        isRefreshing.value = false
        currentItemInRecycler.value = 0
        onTopRecycler.value = true
        emptyList.value = false
    }

    internal fun initViews(
        context: AppCompatActivity?,
        firstTime: Boolean,
        charactersListResponse: MarvelCharactersListResponse?
    ) {
        if (context != null) {
            scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    onTopRecycler.value = (recyclerView.layoutManager as GridLayoutManager)
                        .findFirstCompletelyVisibleItemPosition() == 0
                    if (dy > 0) {
                        val lastPosition =
                            (recyclerView.layoutManager as GridLayoutManager)
                                .findLastVisibleItemPosition()
                        val totalItemCount: Int =
                            (recyclerView.layoutManager as GridLayoutManager).getItemCount()
                        if (lastPosition == totalItemCount-1) {
                            if(!isRefreshing.value!!) {
                                loadList(true, totalItemCount)
                            }
                        }
                    }
                }
            }
            heroesRepository = HeroesRepository(context.application)
            layoutManager = GridLayoutManager(context,3)
            if (charactersListResponse != null && charactersListResponse.data != null && charactersListResponse.data?.results != null) {
                heroes.clear()
                heroes.addAll(charactersListResponse.data!!.results!!)
            }
            if (heroes.size <= 0) {
                adapter = BaseAdapter(
                    R.layout.hero_item,
                    BR.hero,
                    context
                )
            } else {
                adapter.refreshContext(context)
            }
        }
    }

    fun onRefresh() {
        isRefreshing.value = true
        loadList(true, 0)
    }

    fun goToIndex(position: Int) {
        currentItemInRecycler.value = position
    }

    fun getCurrentIndex(): Int {
        return adapter.getCurrentChildPosition()
    }

    fun loadList(needLoad: Boolean, offset: Int): LiveData<MarvelCharactersListResponse> {
        willLoad = !(!needLoad && heroes.size > 0)
        if (willLoad) {
            isRefreshing.value = true
        }
        return heroesRepository.getMutableLiveData(willLoad, offset)
    }

    fun refreshData(response: MarvelCharactersListResponse) {
        if (willLoad) {
            heroesResponse.value = response
            if (response.data?.results != null) {
                heroes.clear()
                for (hero: Hero in response.data?.results!!) {
                    heroes.add(Hero(hero.id, hero.name, hero.description, Thumbnail(hero.thumbnail?.path,hero.thumbnail?.extension)))
                }
                if (response.data?.offset == 0) {
                    adapter.updateListWithClear(heroes)
                } else {
                    adapter.addToList(heroes)
                }
                currentOffset = response.data?.offset ?: 0
            } else {
                if (currentOffset == 0 && (onTopRecycler.value ?: false)) {
                    heroes.clear()
                    adapter.list = ArrayList()
                }
            }
            val itemCount = adapter.itemCount
            if (itemCount > 0) {
                emptyList.value = false
            } else {
                emptyList.value = true
            }
            isRefreshing.value = false
        }
    }
}