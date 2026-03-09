package arc.haldun.elib.models

import arc.haldun.mylibrary.driver.objects.Book

object FavBooksListModel {

    private var favBookList: Array<Book> = arrayOf()

    fun getFavBookList(): Array<Book> {
        return favBookList
    }

    fun setFavBookList(favList: Array<Book>) {
        this.favBookList = favList
    }
}