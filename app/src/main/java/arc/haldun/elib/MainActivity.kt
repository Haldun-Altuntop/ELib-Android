package arc.haldun.elib

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import arc.haldun.math.matrix.Main
import arc.haldun.math.matrix.Matrix
import arc.haldun.mylibrary.driver.objects.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import java.io.DataInputStream
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        val bottomBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_bar_home -> loadFragment(HomeFragment())
                R.id.bottom_bar_favourites -> loadFragment(FavouritesFragment())
                R.id.bottom_bar_profile -> loadFragment(ProfileFragment())
            }

            true
        }

        //DEBUG
        loadExistingUser()
    }

    fun loadExistingUser() {
        val userFileName = "user"
        val userFile = File(filesDir, userFileName)
        if  (!userFile.exists()) return

        val userData = ArrayList<Double>()
        val userFileInputStream = openFileInput(userFileName)
        val userFileDIS = DataInputStream(userFileInputStream)
        while (userFileDIS.available() > 0) {
            val d = userFileDIS.readDouble()
            userData.add(d)
        }
        userFileDIS.close()
        userFileInputStream.close()

        val keyFileName = "key"
        val keyFile = File(filesDir, keyFileName)
        if (!keyFile.exists()) {
            Toast.makeText(applicationContext, "Yeniden giriş yapmalısınız.", Toast.LENGTH_SHORT).show()
            Log.w("Remember Me Key File", "Cannot found!")
            return
        }

        val keyIS = openFileInput(keyFileName)
        val key = Matrix.deserialize(keyIS)
        keyIS.close()

        val decryptedUserData = Main.decrypt(userData.toDoubleArray(), key)
        val userStringData = String(decryptedUserData)
        val userJson = JSONObject(userStringData)
        val user = User(userJson)

        Log.d("Remember Me", user.toString())
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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }
}