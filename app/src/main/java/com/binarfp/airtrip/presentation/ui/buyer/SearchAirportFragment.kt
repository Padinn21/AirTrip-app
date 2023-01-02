package com.binarfp.airtrip.presentation.ui.buyer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.binarfp.airtrip.R
import com.binarfp.airtrip.databinding.FragmentSearchAirportBinding
import com.binarfp.airtrip.presentation.MainViewModel
import com.zaahid.challenge6.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchAirportFragment : Fragment() {
    private lateinit var binding:FragmentSearchAirportBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val adapter:AirportAdapter by lazy {
        AirportAdapter{
            val bundle = Bundle()
            if (arguments?.getInt("id")==1){
                arguments?.getInt("id")?.let { it1 -> bundle.putInt("id", it1) }
                bundle.putSerializable("airport1",it)
                findNavController().navigate(R.id.action_searchAirportFragment_to_searchAirport2,bundle)
            }
            if (arguments?.getInt("id")==2){
                arguments?.getInt("id")?.let { it1 -> bundle.putInt("id", it1) }
                bundle.putSerializable("airport1",it)
                findNavController().navigate(R.id.action_searchAirportFragment_to_searchAirport2,bundle)
            }
            if (requireArguments().getInt("id")==3){
                arguments?.getInt("id")?.let { it1 -> bundle.putInt("id", it1) }
                bundle.putSerializable("airport1",it)
                findNavController().navigate(R.id.action_searchAirportFragment2_to_searchAirport22,bundle)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchAirportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleSearch.text = "Search Departure Airport"
        mainViewModel.getAirports()
        val searchview = binding.searchMovie
        searchview.queryHint = getString(R.string.search)
        searchview.isIconified = false
        initList(view)
        mainViewModel.getAirports.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success->{
                    val result = it.payload?.data
                    if (result != null) {
                        adapter.submitData(result)
                    }
                    binding.rvAirport.visibility=View.VISIBLE
                    binding.pbAirport.visibility=View.GONE
                    binding.tvError.visibility = View.GONE
                }
                is Resource.Loading->{
                    binding.rvAirport.visibility=View.GONE
                    binding.pbAirport.visibility=View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                is Resource.Error->{
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = it.exception.toString()
                    binding.rvAirport.visibility=View.GONE
                    binding.pbAirport.visibility=View.GONE
                }
                is Resource.Empty ->{}
            }
        }
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {a->
                    mainViewModel.getAirports.observe(viewLifecycleOwner){
                        when(it){
                            is Resource.Success->{
                                val result = it.payload?.data?.filter {data->
                                    (data.name.lowercase().indexOf(a) >-1) ||
                                            (data.iata.lowercase().indexOf(a)>-1)||
                                            (data.address.lowercase().indexOf(a)>-1)
                                }
                                if (result != null) {
                                    adapter.submitData(result)
                                }
                                binding.rvAirport.visibility=View.VISIBLE
                                binding.pbAirport.visibility=View.GONE
                                binding.tvError.visibility = View.GONE
                            }
                            is Resource.Loading->{
                                binding.rvAirport.visibility=View.GONE
                                binding.pbAirport.visibility=View.VISIBLE
                                binding.tvError.visibility = View.GONE
                            }
                            is Resource.Error->{
                                binding.tvError.visibility = View.VISIBLE
                                binding.tvError.text = it.exception.toString()
                                binding.rvAirport.visibility=View.GONE
                                binding.pbAirport.visibility=View.GONE
                            }
                            is Resource.Empty ->{

                            }
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText != null) {
//                    mainViewModel.searchListWName(newText,viewLifecycleOwner)
//                }
//                mainViewModel.listAirport.observe(viewLifecycleOwner, Observer {
//                    adapter.submitData(it)
//                    Log.e("list",it.toString())
//                })
                if (newText != null) {
                    Log.e("search",newText)}
                return false
            }
        })
    }
    private fun initList(view: View) {
        binding.rvAirport.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL,false)
            adapter = this@SearchAirportFragment.adapter
        }
    }
}