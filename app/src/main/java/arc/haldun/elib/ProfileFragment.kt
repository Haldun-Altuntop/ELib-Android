package arc.haldun.elib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import arc.haldun.mylibrary.api.ApiService
import arc.haldun.mylibrary.api.UserRepository
import arc.haldun.mylibrary.driver.objects.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    // VIEWS
    private lateinit var progressBar: ProgressBar
    private lateinit var ivProfilePicture: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvBio: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvLanguagePreference: TextView
    private lateinit var switchNotifications: Switch
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button

    // LAYOUTS
    private lateinit var profileLayout: View
    private lateinit var logisterLayout: View

    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.fragment_profile_progress_bar)
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture)
        tvUsername = view.findViewById(R.id.tvUsername)
        tvBio = view.findViewById(R.id.tvBio)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        tvLanguagePreference = view.findViewById(R.id.tvLanguagePreference)
        switchNotifications = view.findViewById(R.id.switchNotifications)
        btnSave = view.findViewById(R.id.btnSave)
        btnLogout = view.findViewById(R.id.btnLogout)
        profileLayout = view.findViewById(R.id.fragment_profile_logged_in)
        logisterLayout = view.findViewById(R.id.fragment_profile_not_logged_in)

        switchNotifications.setOnCheckedChangeListener { compoundButton, isChecked ->
            handleNotificationsSwitch(isChecked)
        }

        btnSave.setOnClickListener {
            handleSaveButtonClick()
        }

        CoroutineScope(Dispatchers.IO).launch {

            user = UserRepository(ApiService()).getUser()
            Thread.sleep(100)

            withContext(Dispatchers.Main) {

                progressBar.visibility = View.GONE

                if (user == null) logisterLayout.visibility = View.VISIBLE
                else profileLayout.visibility = View.VISIBLE

                tvUsername.text = user?.name

            }
        }
    }

    private fun handleNotificationsSwitch(isChecked: Boolean) {

        if (isChecked) {
            Toast.makeText(context, "Bildirimler açıldı", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Bildirimler kapatıldı", Toast.LENGTH_SHORT).show()
        }

    }

    private fun handleSaveButtonClick() {

    }
}