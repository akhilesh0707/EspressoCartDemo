package com.demo.espresso.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.espresso.R
import com.demo.espresso.data.DummyProductData.FAKE_NETWORK_DELAY
import com.demo.espresso.data.Product
import com.demo.espresso.data.source.ProductDataSource
import com.demo.espresso.ui.adapter.ProductListAdapter
import com.demo.espresso.util.EspressoIdlingResource
import com.demo.espresso.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ClassCastException

class ProductListFragment(
    private val productDataSource: ProductDataSource
) : Fragment(),
    ProductListAdapter.Interaction
{
    override fun onItemSelected(position: Int, item: Product) {
        activity?.run {
            val bundle = Bundle()
            bundle.putInt("product_id", item.id)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductDetailFragment::class.java, bundle)
                .addToBackStack("ProductDetailFragment")
                .commit()
        }
    }

    lateinit var listAdapter: ProductListAdapter
    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        getData()
    }

    private fun getData(){
        EspressoIdlingResource.increment()
        uiCommunicationListener.loading(true)
        val job = GlobalScope.launch(IO) {
            delay(FAKE_NETWORK_DELAY)
        }
        job.invokeOnCompletion{
            GlobalScope.launch(Main){
                EspressoIdlingResource.decrement()
                uiCommunicationListener.loading(false)
                listAdapter.submitList(productDataSource.getProducts())
            }
        }
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            removeItemDecoration(TopSpacingItemDecoration(30))
            addItemDecoration(TopSpacingItemDecoration(30))
            listAdapter = ProductListAdapter(this@ProductListFragment)
            adapter = listAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            uiCommunicationListener = context as UICommunicationListener
        }catch (e: ClassCastException){
            Log.e("ProductListFragment", "Must implement interface in $activity: ${e.message}")
        }
    }
}