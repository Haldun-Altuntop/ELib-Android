package arc.haldun.elib

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import arc.haldun.math.matrix.Main
import arc.haldun.math.matrix.Matrix
import arc.haldun.mylibrary.driver.objects.User
import java.io.DataOutputStream
import java.io.File
import java.io.RandomAccessFile

class LogisterActivity : AppCompatActivity() {

    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var cbRememberMe: CheckBox
    lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logister)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_logister_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etUsername = findViewById(R.id.activity_logister_et_username)
        etPassword = findViewById(R.id.activity_logister_et_password)
        cbRememberMe = findViewById(R.id.activity_logister_cb_remember_me)
        btnLogin = findViewById(R.id.activity_logister_btn_login)

        cbRememberMe.setOnCheckedChangeListener { v, b ->
            onCbRememberMeCheckedChange(b)
        }

        btnLogin.setOnClickListener { onBtnLoginClick() }
    }

    fun onBtnLoginClick() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val rememberMe = cbRememberMe.isChecked

        //TODO: Login
        val userTmp = User.createTemplate(username, password)


        if (rememberMe) {

            // Check encryption key
            val keyFileName = "key"
            val keyFile = File(filesDir, keyFileName)
            if (!keyFile.exists()) {
                if (!keyFile.createNewFile()) {
                    Toast.makeText(applicationContext, "Anahtar oluşturulamadı. Beni hatırla çalışmayacak.", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            var inputStream = openFileInput(keyFileName)
            if (inputStream.available() == 0) {
                inputStream.close()

                val key = Matrix(4,4)
                key.fillRandom()

                val os = openFileOutput("key", MODE_PRIVATE)
                key.serialize(os)
                os.close()
            }

            inputStream = openFileInput(keyFileName)
            val key = Matrix.deserialize(inputStream)
            inputStream.close()

            // Encrypt username and password

            val data = userTmp.toString().toByteArray()
            val encryptedData = Main.encrypt(data, key)

            val os = openFileOutput("user", MODE_PRIVATE)
            val dos = DataOutputStream(os)
            for (i in 0 until encryptedData.size) {
                dos.writeDouble(encryptedData[i])
            }
            dos.close()
            os.close()
        }
    }

    fun onCbRememberMeCheckedChange(status: Boolean) {

    }
}