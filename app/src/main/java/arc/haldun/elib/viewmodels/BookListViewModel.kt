package arc.haldun.elib.viewmodels

import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arc.haldun.elib.models.BookListModel
import arc.haldun.mylibrary.api.ApiService
import arc.haldun.mylibrary.driver.objects.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookListViewModel: ViewModel() {

    fun fetch() {

        Log.d("BookListViewModel", "Kitaplar yükleniyor...")


        var books: Array<Book>

        val handler = android.os.Handler(Looper.getMainLooper())

        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                books = ApiService().getBooks()
            }

            Log.d("BookListViewModel", "Kitaplar yüklendi: ${books.size}")
            BookListModel.setBookList(books)
        }

        Thread {
            books = ApiService().getBooks()

            handler.post {
                Log.d("BookListViewModel", "Kitaplar yüklendi: ${books.size}")

                BookListModel.setBookList(books)
            }

        }
    }
}