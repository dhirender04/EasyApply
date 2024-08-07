package com.example.easyapply.ui.mailtempalte

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.easyapply.R
import com.example.easyapply.ui.mailtempalte.adpater.ApplicationListAdapter
import com.example.easyapply.databinding.FragmentMailTemplateListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MailTemplateListFragment : Fragment() {

    val bundle = Bundle()
    val adpterItemClick: (Int) -> Unit = { itemPos ->
        bundle.putBoolean(getString(R.string.mailtemplatefromedit),true)
        findNavController().navigate(R.id.action_mailTemplateListFragment_to_mailDetailFragment,bundle)
    }

    private lateinit var binding: FragmentMailTemplateListBinding
    val applicationListAdapter by lazy { ApplicationListAdapter(adpterItemClick) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMailTemplateListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvApplicationList.adapter = applicationListAdapter
            btAdd.setOnClickListener {
                bundle.putBoolean(getString(R.string.mailtemplatefromedit),false)
                findNavController().navigate(R.id.action_mailTemplateListFragment_to_mailDetailFragment,bundle)
                Toast.makeText(requireContext(), "Add Button Click", Toast.LENGTH_SHORT).show()
            }


        }
    }


}