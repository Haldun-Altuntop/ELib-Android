package arc.haldun.elib

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arc.haldun.elib.models.BookListModel
import arc.haldun.elib.models.FavBooksListModel
import arc.haldun.elib.viewmodels.BookListViewModel
import arc.haldun.elib.viewmodels.FavBooksListViewModel
import arc.haldun.mylibrary.api.ApiService
import arc.haldun.mylibrary.api.TokenManager
import arc.haldun.mylibrary.driver.objects.Book
import arc.haldun.time.DateTime
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var loginDialogView: View? = null
    private var loginDialogView_et_username: EditText? = null
    private var loginDialogView_et_password: EditText? = null
    private var loginDialogView_btn_login: Button? = null
    private var loginDialogView_cb_rememberMe: CheckBox? = null

    private lateinit var recyclerView: RecyclerView

    private lateinit var progressBar: ProgressBar

    private lateinit var loginDialog: AlertDialog

    private var bookListViewModel = BookListViewModel()
    private var favBooksListViewModel = FavBooksListViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        loginDialogView = layoutInflater.inflate(R.layout.activity_logister, null)
        loginDialogView_et_username = loginDialogView?.findViewById(R.id.activity_logister_et_username)
        loginDialogView_et_password = loginDialogView?.findViewById(R.id.activity_logister_et_password)
        loginDialogView_btn_login = loginDialogView?.findViewById(R.id.activity_logister_btn_login)
        loginDialogView_cb_rememberMe = loginDialogView?.findViewById(R.id.activity_logister_cb_remember_me)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.fragment_home_progressbar)
        recyclerView = view.findViewById<RecyclerView>(R.id.fragment_home_recyclerview)
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager

        handleSelectedCategories(view)
        initRecyclerView()

        BookListModel.action = {
            initRecyclerView()
        }

        bookListViewModel.fetch()
        favBooksListViewModel.fetch()

        val profile: ShapeableImageView = view.findViewById(R.id.fragment_home_profile_image)
        profile.setOnClickListener {

            if (TokenManager().getToken() == null) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(loginDialogView)

                loginDialogView_btn_login?.setOnClickListener {
                    btnLoginClick()
                }

                loginDialog = builder.create()
                loginDialog.show()
            } else {
                val activity = activity as HomeActivity
                activity.loadFragment(ProfileFragment())
            }
        }

        setGreetingText(view)
    }

    fun initRecyclerView() {
        progressBar.visibility = View.GONE
        val adapter = BookAdapter(ArrayList(BookListModel.getBookList().toList()), onItemClick = {
            val intent = Intent(context, BookDetailsActivity::class.java)
            intent.putExtra("book", it.toString())
            startActivity(intent)
        })
        recyclerView.adapter = adapter
    }

    private fun setGreetingText(v: View) {
        val tvGreeting = v.findViewById<TextView>(R.id.fragment_home_tv_greeting)
        val currentHour = DateTime.now().time.hour

        val msg: String = if (currentHour < 4) "İyi Geceler,"
        else if (currentHour < 12) "Günaydın,"
        else if (currentHour < 17) "Tünaydın,"
        else if (currentHour < 22) "İyi Akşamlar,"
        else "İyi Geceler,"

        tvGreeting.text = msg
    }

    private fun btnLoginClick() {
        val username = loginDialogView_et_username?.text.toString()
        val password = loginDialogView_et_password?.text.toString()
        val rememberMe = loginDialogView_cb_rememberMe?.isChecked

        Thread {

            val api = ApiService()
            val res = api.login(username, password)

            lifecycleScope.launch(Dispatchers.Main) {

                if (res != null) {
                    Toast.makeText(context, "Giriş başarılı.", Toast.LENGTH_SHORT).show()

                    TokenManager().setToken(res)

                    if (rememberMe == true) {
                        TokenManager().saveToken(res)
                    }

                    loginDialog.dismiss()

                    val activity = activity as HomeActivity
                    activity.loadFragment(ProfileFragment())

                } else {
                    Toast.makeText(context, "Giriş başarısız.", Toast.LENGTH_SHORT).show()
                }
            }

        }.start()
    }

    private fun handleSelectedCategories(view: View) {
        val bookCategories = view.findViewById<ChipGroup>(R.id.fragment_home_categories)
        bookCategories?.setOnCheckedStateChangeListener { group, checkedIds ->

            if (checkedIds.isNotEmpty()) {

                onCategoryCheckedChange(checkedIds)

            } else {
                Toast.makeText(context, "tümü", Toast.LENGTH_SHORT).show()
                bookListViewModel.fetch()
            }
        }
    }

    private fun onCategoryCheckedChange(checkedIds: List<Int>) {

        val types = ArrayList<String>()

        checkedIds.forEach { chipId ->

            val secilenChip = view?.findViewById<Chip>(chipId)
            val turAdi = secilenChip?.contentDescription.toString()

            types.add(turAdi)
        }

        bookListViewModel.fetch(types)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    class BookAdapter(
        private val bookList: ArrayList<Book>,
        private val onItemClick: (Book) -> Unit
    ): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BookViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
            return BookViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: BookViewHolder,
            position: Int
        ) {
            holder.itemView.setOnClickListener { onItemClick(bookList[position]) }
            holder.setData(bookList[position])

            for (book in FavBooksListModel.getFavBookList()) {
                if (book.id == bookList[position].id) {
                    holder.isFav = true
                    holder.btnFav.setImageResource(R.drawable.favorite_filled)
                    break
                }
            }
        }

        override fun getItemCount(): Int {
            return bookList.size
        }

        class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvBookName: TextView = itemView.findViewById(R.id.item_book_tv_bookname)
            val tvAuthor: TextView = itemView.findViewById(R.id.item_book_tv_author)
            val tvRating: TextView = itemView.findViewById(R.id.item_book_tv_rating)
            val btnFav: ShapeableImageView = itemView.findViewById(R.id.item_book_siv_fav)

            val favBooksListViewModel = FavBooksListViewModel()

            var isFav = false


            fun setData(book: Book) {
                tvBookName.text = book.name
                tvAuthor.text = book.author
                tvRating.text = "123"

                btnFav.setOnClickListener {
                    addToFav(book)
                }
            }

            fun addToFav(book: Book) {

                if (!isFav) {
                    val res = favBooksListViewModel.add(book)
                    if (res) Log.d("BookAdapter", "Kitap favorilere eklendi")
                    else Log.d("BookAdapter", "Kitap favorilere eklenemedi")
                    btnFav.setImageResource(R.drawable.favorite_filled)
                    isFav = true
                } else {
                    val res = favBooksListViewModel.remove(book)
                    if (res) Log.d("BookAdapter", "Kitap favorilerden çıkartıldı")
                    else Log.d("BookAdapter", "Kitap favorilerden çıkartılamadı")
                    btnFav.setImageResource(R.drawable.favorite)
                    isFav = false
                }
            }
        }
    }
}