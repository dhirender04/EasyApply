package com.example.easyapply.ui.mailtempalte

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.easyapply.R
import com.example.easyapply.ui.mailtempalte.adpater.ApplicationListAdapter
import com.example.easyapply.databinding.FragmentMailTemplateListBinding
import com.example.easyapply.ui.mailDetail.MailTemplateListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MailTemplateListFragment : Fragment() {

    private val viewModel: MailTemplateListViewModel by viewModels()

    val bundle = Bundle()
    val adpterItemClick: (Int) -> Unit = { itemPos ->
        bundle.putBoolean(getString(R.string.mailtemplatefromedit), true)
        findNavController().navigate(
            R.id.action_mailTemplateListFragment_to_mailDetailFragment,
            bundle
        )
    }

    private lateinit var binding: FragmentMailTemplateListBinding
   private val applicationListAdapter by lazy { ApplicationListAdapter(adpterItemClick) }

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
            viewModel.allEmailTemplateList.observe(viewLifecycleOwner, Observer { mails ->
                Log.e(TAG, "onViewCreated:MailsListingDATA " + mails)
                applicationListAdapter.setList(mails)
            })
            rvApplicationList.adapter = applicationListAdapter
            btAdd.setOnClickListener {
                bundle.putBoolean(getString(R.string.mailtemplatefromedit), false)
                findNavController().navigate(
                    R.id.action_mailTemplateListFragment_to_mailDetailFragment,
                    bundle
                )
                Toast.makeText(requireContext(), "Add Button Click", Toast.LENGTH_SHORT).show()
            }


        }
    }


}