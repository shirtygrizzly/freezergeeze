package com.example.freezergeez

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.freezergeez.databinding.FragmentDetailsBinding


class DetailsFragment : DialogFragment() {
    private val sharedView : SharedView by activityViewModels()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        sharedView.itemName.observe(viewLifecycleOwner,{ itemName ->
            binding.itemName.setText(itemName)})
        sharedView.itemDesc.observe(viewLifecycleOwner,{ itemDesc ->
            binding.description.setText(itemDesc)})
        sharedView.itemQty.observe(viewLifecycleOwner,{ itemQty ->
            binding.showItemQty.setText(itemQty)})
        sharedView.itemUrl.observe(viewLifecycleOwner,{ itemUrl ->
            binding.itemPhoto.setImageURI(itemUrl.toUri())})


        return binding.root
    }


    }
