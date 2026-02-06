package arc.haldun.elib.viewmodels

import android.os.Looper
import android.util.Log
import arc.haldun.elib.models.BookListModel
import arc.haldun.mylibrary.api.ApiService
import arc.haldun.mylibrary.driver.objects.Book

class BookListViewModel {

    fun fetch() {

        Log.d("BookListViewModel", "Kitaplar yükleniyor...")


        var books: Array<Book>

        val handler = android.os.Handler(Looper.getMainLooper())

        Thread {
            books = ApiService().getBooks()

            handler.post {
                Log.d("BookListViewModel", "Kitaplar yüklendi: ${books.size}")

                BookListModel.setBookList(books)
            }

        }.start()
    }
}