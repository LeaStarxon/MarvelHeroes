package com.leastarxon.marvelheroes.ui.main

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.transition.TransitionInflater
import com.leastarxon.marvelheroes.R
import com.leastarxon.marvelheroes.ui.list.ListFragment
import com.leastarxon.marvelheroes.utils.ui.AnimationUtil
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportFragmentManager.addOnBackStackChangedListener(this);
        if (savedInstanceState == null) {
            val listFragment = ListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, listFragment,"listfragment")
                .commitNow()
        }
        addBack()
    }


    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag("detailsFragment") != null) {
            supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentByTag("detailsFragment")!!).commitNow()
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onBackStackChanged() {
       addBack()
    }

    private  fun addBack(){
        if (supportFragmentManager.findFragmentByTag("detailsFragment") != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setHomeButtonEnabled(false)
        }
    }
}
