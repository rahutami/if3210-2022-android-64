package com.example.if3210_64

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.example.if3210_64.databinding.FragmentKabupatenBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
lateinit var bindingKabupaten: FragmentKabupatenBinding
var kabupaten = ArrayList<String>()

class kabupatenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gets the data from the passed bundle
        val kabupatenView: View = inflater.inflate(R.layout.fragment_kabupaten, container, false)
        val bundle = arguments

        if (bundle != null) {
            kabupaten = bundle.getStringArrayList("kabupatenList")!!
            val arrayAdapter2 = ArrayAdapter(requireContext(), R.layout.province_item, kabupaten)
            kabupatenView.findViewById<AutoCompleteTextView>(R.id.autoCompleteKabupaten)
                .setAdapter(arrayAdapter2)
        }

        return kabupatenView
    }
}