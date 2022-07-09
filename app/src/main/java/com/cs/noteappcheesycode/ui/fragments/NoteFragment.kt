package com.cs.noteappcheesycode.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cs.noteappcheesycode.databinding.FragmentNoteBinding
import com.cs.noteappcheesycode.models.NoteRequest
import com.cs.noteappcheesycode.models.NotesResponse
import com.cs.noteappcheesycode.utils.NetworkResult
import com.cs.noteappcheesycode.viewmodel.NotesViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private var note: NotesResponse? = null
    private val noteViewModel by viewModels<NotesViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
//        val jsonNote = arguments?.getString("note") as NotesResponse
        if (jsonNote != null) {
//            note = jsonNote
            note = Gson().fromJson(jsonNote, NotesResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }

        } else {
            binding.addEditText.text = "Add Note"
        }
    }
    // by using serializable
//    private fun setInitialData() {
//        val jsonNote = arguments?.getSerializable("note") as NotesResponse
//        note = jsonNote
//        note?.let {
//            binding.txtTitle.setText(it.title)
//            binding.txtDescription.setText(it.description)
//        }
//    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let { noteViewModel.deleteNote(it._id) }
        }
        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(description, title)
            if (note == null) {
                noteViewModel.createNote(noteRequest)
            } else {
                noteViewModel.updateNote(note!!._id, noteRequest)
            }
        }
    }


    private fun bindObservers() {
        noteViewModel.statusLivedata.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
            }
        })
    }

}