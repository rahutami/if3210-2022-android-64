package com.example.if3210_64

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.example.if3210_64.databinding.FragmentProvinceBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
lateinit var bindingProvince: FragmentProvinceBinding
var provinces = ArrayList<String>()

class ProvinceFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gets the data from the passed bundle
        val view: View = inflater.inflate(R.layout.fragment_province, container, false)
        val bundle = arguments

        if(bundle != null){
            provinces = bundle!!.getStringArrayList("provinceList")!!

            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.province_item, provinces)
            view.findViewById<AutoCompleteTextView>(R.id.autoCompleteProvince).setAdapter(arrayAdapter)
        }

        return view
    }
}