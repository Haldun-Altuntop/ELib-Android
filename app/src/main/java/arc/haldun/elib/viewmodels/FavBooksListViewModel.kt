package arc.haldun.elib.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arc.haldun.elib.models.FavBooksListModel
import arc.haldun.mylibrary.api.ApiService
import arc.haldun.mylibrary.driver.objects.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavBooksListViewModel: ViewModel() {

    fun fetch() {

        var favList: Array<Book> = arrayOf()

        viewModelScope.launch {
           withContext(Dispatchers.IO) {
               favList = ApiService().getBookFavList()
           }
            FavBooksListModel.setFavBookList(favList)
        }
    }

    fun add(book: Book): Boolean {

        var res = false

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                res = ApiService().addBookToFavList(book)
            }
            fetch()
        }

        return res
    }

    fun remove(book: Book): Boolean {

        var res = false

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                res = ApiService().deleteBookFromFavList(book)
            }
            fetch()
        }

        return res
    }
}