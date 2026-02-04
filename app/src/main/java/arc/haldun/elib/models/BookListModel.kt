package arc.haldun.elib.models

import android.util.Log
import arc.haldun.mylibrary.driver.objects.Book

object BookListModel {

    var action: Runnable? = null


    private var bookList: Array<Book> = arrayOf()

    fun getBookList(): Array<Book> {
        return bookList
    }

    fun setBookList(books: Array<Book>) {
        bookList = books
        if (action == null) Log.d("BookListModel", "Action null idi")
        action?.run()
    }

}