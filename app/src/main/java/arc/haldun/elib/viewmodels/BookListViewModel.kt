package arc.haldun.elib.viewmodels

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

    fun fetch(types: ArrayList<String>) {

        Log.d("BookListViewModel", "Kitaplar yükleniyor...")


        var books: Array<Book>

        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                books = if (types.isEmpty()) ApiService().getBooks()
                else ApiService().findBooksByType(types)
            }

            Log.d("BookListViewModel", "Kitaplar yüklendi: ${books.size}")
            BookListModel.setBookList(books)
        }
    }

    fun fetch() {
        fetch(arrayListOf<String>())
    }
}