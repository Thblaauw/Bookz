package com.bignerdranch.android.bookz

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*


import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookz.ui.home.HomeFragment

import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_post_list.view.*
import kotlinx.android.synthetic.main.list_item_book.view.*


private const val TAG = "BookListFragment"
class BookListFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        //fun onBookSelected(crimeId: UUID)

        fun onBookSelected(bookId: Post)

        fun onAddBookSelected()
    }

    private var callbacks: Callbacks? = null
    private var parentFrag: HomeFragment? = null

   // private lateinit var bookRecyclerView: RecyclerView
    //private var adapter: BookAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:BookAdapter
    private lateinit var bookRecyclerView: RecyclerView
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(BookListViewModel::class.java)
    }
    //private val viewModel by lazy { activity?.let { ViewModelProviders.of(it).get(BookListViewModel::class.java) } }
    //private val bookListViewModel: BookListViewModel by lazy {
      //  ViewModelProviders.of(this).get(BookListViewModel::class.java)
   // }



    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentFrag = this@BookListFragment.parentFragment as HomeFragment

        //callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        val parentFrag: HomeFragment? = null
        //callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_book_list, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $queryText")

                    return true
                }
                override fun onQueryTextChange(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextChange: $queryText")
                    return false
                }
            })
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_post -> {

                //parentFrag?.onBookSelected(book.id)
                parentFrag?.onAddBookSelected()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
         return true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }

    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

        val view = inflater.inflate(R.layout.fragment_book_list, container, false)

        return view
}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.book_recycler_view) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())


        adapter = BookAdapter(this.requireActivity())

        recyclerView.adapter = adapter
        observeData()

    }
    fun observeData() {
        viewModel.fetchBooks()?.observe(this, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()

        })
    }
    fun filteredData(title: String){
        viewModel.searchBooksByTitle(title)?.observe(this, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()

        })
    }



private inner class BookAdapter(private val context: Context): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var dataList = mutableListOf<Post>()
    fun setListData(data: MutableList<Post>) {
        dataList = data
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var book: Post
        private val titleTextView: TextView = itemView.findViewById(R.id.book_title)
        private val authorTextView: TextView = itemView.findViewById(R.id.book_author)
        private val priceTextView: TextView = itemView.findViewById(R.id.book_price)
        init{
            itemView.setOnClickListener(this)
        }
        fun bindView(book: Post) {

            this.book = book
            Glide.with(context).load(this.book.bookImage).into(itemView.circleImageView)
            titleTextView.text = this.book.bookTitle
            authorTextView.text = this.book.bookAuthor
            priceTextView.text = this.book.bookPrice

        }
        override fun onClick(v: View) {
            parentFrag?.onBookSelected(book)        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book: Post = dataList[position]
        holder.bindView(book)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size

        } else {

            0
        }
    }
    }

    companion object {
        fun newInstance(): BookListFragment {
            return BookListFragment()
        }
    }


}

