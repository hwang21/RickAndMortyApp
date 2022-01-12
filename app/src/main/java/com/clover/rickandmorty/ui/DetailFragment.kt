package com.clover.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.clover.rickandmorty.databinding.FragmentDetailBinding
import com.clover.rickandmorty.model.LocationDetail
import com.clover.rickandmorty.utils.Status
import com.clover.rickandmorty.viewmodel.MainViewModel

class DetailFragment:  Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        mainViewModel.imageUrl.observe(viewLifecycleOwner, { imageUrl ->
            setImage(imageUrl)
        })
        mainViewModel.locationId.observe(viewLifecycleOwner, { locationId ->
            setLocationDetails(locationId)
        })
        return binding.root
    }

    private fun setImage(imageUrl: String) {
        Glide.with(binding.image.context)
            .load(imageUrl)
            .into(binding.image)
    }

    private fun setLocationDetails(locationId: Int) {
        mainViewModel.getLocationDetails(locationId).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progressBarDetail.visibility = View.GONE
                        setLocationDetails(resource.data!!)
                    }
                    Status.ERROR -> {
                        binding.progressBarDetail.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.progressBarDetail.visibility = View.VISIBLE
                    }
                }
            }
        })
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