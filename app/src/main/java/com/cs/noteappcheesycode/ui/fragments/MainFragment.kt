package com.cs.noteappcheesycode.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cs.noteappcheesycode.R
import com.cs.noteappcheesycode.databinding.FragmentMainBinding
import com.cs.noteappcheesycode.models.NotesResponse
import com.cs.noteappcheesycode.ui.adapters.NoteAdapter
import com.cs.noteappcheesycode.utils.NetworkResult
import com.cs.noteappcheesycode.viewmodel.NotesViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val notesViewModel by viewModels<NotesViewModel>()
    private lateinit var adapter : NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = NoteAdapter(::onNoteClick)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        notesViewModel.getNotes()
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter

        binding.addNote.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    private fun bindObservers() {
        notesViewModel.notesLivedata.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    adapter.submitList(it.data)
                }
                is NetworkResult.Error -> Toast.makeText(
//                    requireContext()
                    context,
                    it.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })

    }

    private fun onNoteClick(notesResponse: NotesResponse){

        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(notesResponse))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }

    // by using serializable
//    private fun onNoteClick(noteResponse: NotesResponse) {
//        val bundle = Bundle()
//        bundle.putSerializable("note", noteResponse)
//        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
//    }

}