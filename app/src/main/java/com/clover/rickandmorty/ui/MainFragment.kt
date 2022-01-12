package com.clover.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.clover.rickandmorty.R
import com.clover.rickandmorty.adapter.MainAdapter
import com.clover.rickandmorty.databinding.FragmentMainBinding
import com.clover.rickandmorty.utils.ResponseState
import com.clover.rickandmorty.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val adapter = MainAdapter { imageUrl, locationId ->
            mainViewModel.imageUrl.value = imageUrl
            mainViewModel.locationId.value = locationId
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, DetailFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launchWhenStarted {
            mainViewModel.charactersState.also {
                mainViewModel.getCharacters()
            }
            .collectLatest {
                when (it) {
                    is ResponseState.Loading -> {
                        binding.progressBarMain.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                    is ResponseState.Success -> {
                        recyclerView.visibility = View.VISIBLE
                        binding.progressBarMain.visibility = View.GONE
                        adapter.apply {
                            setData(it.data)
                        }
                    }
                    is ResponseState.Error -> {
                        recyclerView.visibility = View.VISIBLE
                        binding.progressBarMain.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

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