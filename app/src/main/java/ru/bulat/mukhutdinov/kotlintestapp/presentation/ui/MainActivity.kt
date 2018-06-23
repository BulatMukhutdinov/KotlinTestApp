package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import ru.bulat.mukhutdinov.kotlintestapp.R
import ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable.PhotoBindable
import ru.bulat.mukhutdinov.kotlintestapp.presentation.datasource.contentprovider.SuggestionProvider
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photo.PhotoFragment
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.PhotoFeedFragment
import ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.contract.PhotoFeedView


class MainActivity : AppCompatActivity(), MainRouter {
    companion object {
        private const val PHOTOS_FRAGMENT_TAG = "photos_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            showPhotoFeed()
        }
        handleIntent(intent);
    }

    lateinit var searchView: SearchView

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)


        val menuItem = menu.findItem(R.id.menu_search)
        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                val photoFeedView = supportFragmentManager.findFragmentByTag(PHOTOS_FRAGMENT_TAG) as PhotoFeedView?
                if (photoFeedView != null && photoFeedView.isVisible()) {
                    photoFeedView.onSearchClose()
                }
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.menu_clear_suggestions -> {
                    showClearSuggestionsDialog()
                    true
                }
                android.R.id.home -> {
                    onBackPressed();
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    private fun showClearSuggestionsDialog() {
        AlertDialog.Builder(this, android.R.style.ThemeOverlay_Material_Dialog_Alert)
                .setTitle(R.string.alert_title)
                .setMessage(R.string.alert_message)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    val suggestions = SearchRecentSuggestions(this,
                            SuggestionProvider.AUTHORITY, SuggestionProvider.MODE)
                    suggestions.clearHistory()
                }
                .setNegativeButton(android.R.string.no) { _, _ ->
                    // do nothing
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            val photoFeedView = supportFragmentManager.findFragmentByTag(PHOTOS_FRAGMENT_TAG) as PhotoFeedView?
            if (photoFeedView != null && photoFeedView.isVisible()) {
                photoFeedView.onSearch(query)

                val suggestions = SearchRecentSuggestions(this,
                        SuggestionProvider.AUTHORITY, SuggestionProvider.MODE)
                suggestions.saveRecentQuery(query, null)
            }
        }
    }

    override fun showPhotoFeed() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, PhotoFeedFragment.newInstance(), PHOTOS_FRAGMENT_TAG)
                .commitNow()
    }

    override fun showPhoto(photo: PhotoBindable, image: ImageView) {
        ViewCompat.getTransitionName(image)?.let {
            supportFragmentManager.beginTransaction()
                    .addSharedElement(image, it)
                    .replace(R.id.container, PhotoFragment.newInstance(photo, ViewCompat.getTransitionName(image)))
                    .addToBackStack(null)
                    .commit()
        };
    }
}