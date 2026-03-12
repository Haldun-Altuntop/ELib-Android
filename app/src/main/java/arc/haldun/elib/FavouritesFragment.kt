package arc.haldun.elib

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arc.haldun.elib.models.FavBooksListModel
import arc.haldun.elib.viewmodels.FavBooksListViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    private val favBooksListViewModel = FavBooksListViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.fragment_favourites_search)

        favBooksListViewModel.fetch {
            initRecyclerView(view)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    private fun initRecyclerView(v: View) {

        recyclerView = v.findViewById<RecyclerView>(R.id.fragment_favourites_recycler_view)

        val adapter = HomeFragment.BookAdapter(
            ArrayList(FavBooksListModel.getFavBookList().toList()),
            { book ->
                val intent = Intent(context, BookDetailsActivity::class.java)
                intent.putExtra("book", book.toString())
                startActivity(intent)
            },
            { }
        )

        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavouritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}