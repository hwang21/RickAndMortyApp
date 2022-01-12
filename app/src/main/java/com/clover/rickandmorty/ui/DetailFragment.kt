package com.clover.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.clover.rickandmorty.databinding.FragmentDetailBinding
import com.clover.rickandmorty.model.LocationDetail
import com.clover.rickandmorty.utils.ResponseState
import com.clover.rickandmorty.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        lifecycleScope.launchWhenStarted {
            mainViewModel.getImageStateFlow().collectLatest { imageUrl ->
                setImage(imageUrl)
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.getLocationIdStateFlow()
                .flatMapLatest { locationId ->
                    mainViewModel.locationState.also {
                        mainViewModel.getLocationDetails(locationId)
                    }
                }
                .collectLatest {
                    when (it) {
                        is ResponseState.Loading -> {
                            binding.progressBarDetail.visibility = View.VISIBLE
                        }
                        is ResponseState.Success -> {
                            binding.progressBarDetail.visibility = View.GONE
                            setLocationDetails(it.data)
                        }
                        is ResponseState.Error -> {
                            binding.progressBarDetail.visibility = View.GONE
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

    private fun setImage(imageUrl: String) {
        Glide.with(binding.image.context)
            .load(imageUrl)
            .into(binding.image)
    }

    private fun setLocationDetails(location: LocationDetail) {
        binding.name.text = location.name
        binding.type.text = location.type
        binding.dimension.text = location.dimension
        binding.numberOfResidents.text = location.residents.size.toString()
    }

    companion object {
        fun newInstance() = DetailFragment()
    }
}