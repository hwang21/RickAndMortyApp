package com.clover.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.clover.rickandmorty.R
import com.clover.rickandmorty.adapter.MainAdapter
import com.clover.rickandmorty.databinding.FragmentMainBinding
import com.clover.rickandmorty.utils.Status
import com.clover.rickandmorty.viewmodel.MainViewModel


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val adapter = MainAdapter { imageUrl, locationId ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, DetailFragment.newInstance())
                .addToBackStack(null)
                .commit()
            mainViewModel.setImage(imageUrl)
            mainViewModel.setLocationId(locationId)
        }
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mainViewModel.getCharacters().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        binding.progressBarMain.visibility = View.GONE
                        adapter.apply {
                            setData(resource.data!!)
                        }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        binding.progressBarMain.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.progressBarMain.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}