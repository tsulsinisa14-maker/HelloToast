package lat.pam.hellotoast

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private var mCount = 0
    private val model: NameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mShowCount = findViewById<TextView>(R.id.show_count)
        val buttonCountUp = findViewById<Button>(R.id.button_count)
        val buttonToast = findViewById<Button>(R.id.button_toast)
        val buttonSwitchPage = findViewById<Button>(R.id.button_switchpage)
        val buttonBrowser = findViewById<Button>(R.id.button_browser)
        val buttonMap = findViewById<Button>(R.id.button_map)
        val buttonContact = findViewById<Button>(R.id.button_contact)
        val buttonDial = findViewById<Button>(R.id.button_dial)

        // Ambil nilai terakhir dari ViewModel (supaya lanjut setelah rotasi)
        mCount = model.currentName.value ?: 0
        mShowCount.text = mCount.toString()

        // Observer: update TextView tiap nilai LiveData berubah
        val nameObserver = Observer<Int> { newName ->
            mShowCount.text = newName.toString()
        }
        model.currentName.observe(this, nameObserver)

        buttonCountUp.setOnClickListener {
            mCount++
            Log.d("mCount", mCount.toString())
            model.currentName.setValue(mCount)
        }

        buttonToast.setOnClickListener {
            val tulisan = mShowCount.text.toString()
            Toast.makeText(this, "Angka yang dimunculkan $tulisan", Toast.LENGTH_LONG).show()
        }

        // Explicit Intent
        buttonSwitchPage.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        // Implicit Intent: browser
        buttonBrowser.setOnClickListener {
            val intentBrowse = Intent(Intent.ACTION_VIEW)
            intentBrowse.data = Uri.parse("https://www.google.com/")
            bukaIntent(intentBrowse)
        }

        // Implicit Intent: lokasi di peta
        buttonMap.setOnClickListener {
            val intentMap = Intent(Intent.ACTION_VIEW)
            intentMap.data = Uri.parse("geo:0,0?q=Banjar, Jawa Barat")
            bukaIntent(intentMap)
        }

        // Implicit Intent: daftar kontak
        buttonContact.setOnClickListener {
            val intentContact = Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI)
            bukaIntent(intentContact)
        }

        // Implicit Intent: dialer telepon
        buttonDial.setOnClickListener {
            val intentDial = Intent(Intent.ACTION_DIAL)
            intentDial.data = Uri.parse("tel:08123456789")
            bukaIntent(intentDial)
        }
    }

    // Supaya aplikasi tidak crash kalau perangkat tidak punya aplikasi yang sesuai
    private fun bukaIntent(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Tidak ada aplikasi yang bisa menangani aksi ini", Toast.LENGTH_SHORT).show()
        }
    }
}