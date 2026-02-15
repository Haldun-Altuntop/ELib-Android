package arc.haldun.elib

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import arc.haldun.elib.viewmodels.BookListViewModel
import arc.haldun.mylibrary.api.ApiService
import arc.haldun.mylibrary.api.LibraryInitializer
import arc.haldun.mylibrary.api.TokenManager
import arc.haldun.mylibrary.api.UserRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        LibraryInitializer.initialize(applicationContext.filesDir.absolutePath)

        // Check token validity
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val user = UserRepository(ApiService()).getUser()
                if (user == null) {
                    TokenManager().forgetToken()
                }
            }
        }

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        val bottomBar = findViewById<BottomNavigationView>(R.id.activity_home_bottom_navigation)
        bottomBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_bar_home -> loadFragment(HomeFragment())
                R.id.bottom_bar_favourites -> loadFragment(FavouritesFragment())
                R.id.bottom_bar_profile -> loadFragment(ProfileFragment())
            }

            true
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.bottom_bar_home) {
            loadFragment(HomeFragment())
            return true
        }

        if (item.itemId == R.id.bottom_bar_favourites) {
            loadFragment(FavouritesFragment())
            return true
        }

        if (item.itemId == R.id.bottom_bar_profile) {
            loadFragment(ProfileFragment())
            return true
        }

        return super.onContextItemSelected(item)
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_home_main_container, fragment)
            .commit()
    }
}