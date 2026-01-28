package arc.haldun.elib

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import arc.haldun.mylibrary.driver.objects.Book
import com.google.android.material.imageview.ShapeableImageView

class BookDetailsActivity : AppCompatActivity() {

    private var book: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_book_details_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bookJsonString = intent.getStringExtra("book")
        book = Book(bookJsonString)

        handleBookmark()
        fillBookDetails()
        handleButtons()
    }

    private fun handleButtons() {
        val btnBack = findViewById<ShapeableImageView>(R.id.activity_book_details_btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val btnMore = findViewById<ShapeableImageView>(R.id.activity_book_details_btn_more)
        btnMore.setOnClickListener {

        }
    }

    private fun fillBookDetails() {
        val tvBookName = findViewById<TextView>(R.id.activity_book_details_tv_bookname)
        tvBookName.text = book?.name

        val tvAuthor = findViewById<TextView>(R.id.activity_book_details_tv_author)
        tvAuthor.text = book?.author

        val tvPage = findViewById<TextView>(R.id.activity_book_details_tv_page)
        tvPage.text = book?.page.toString()

        val tvType = findViewById<TextView>(R.id.activity_book_details_tv_type)
        tvType.text = book?.type

        val tvOzet = findViewById<TextView>(R.id.activity_book_details_tv_ozet)

        val tvPublicationYear = findViewById<TextView>(R.id.activity_book_details_tv_publication)
        tvPublicationYear.text = book?.publicationYear.toString()

        val tvRating = findViewById<TextView>(R.id.activity_book_details_tv_rating)

    }

    private fun handleBookmark() {
        val btnBookmark = findViewById<ImageButton>(R.id.activity_book_details_btn_bookmark)
        var isSaved = false
        btnBookmark.setOnClickListener {
            Toast.makeText(baseContext, "adasdasd", Toast.LENGTH_SHORT).show()
            isSaved = !isSaved
            if (isSaved) {
                btnBookmark.setImageResource(R.drawable.bookmark_filled)
            } else {
                btnBookmark.setImageResource(R.drawable.bookmark)
            }
        }
    }
}