package com.example.notesappfragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

lateinit var noteText: TextView
lateinit var noteTextInput: EditText
lateinit var addButton: Button

/**
 * A simple [Fragment] subclass.
 * Use the [AddNewNote.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNewNote : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_new_note, container, false)

        val noteDatabase by lazy { NoteDatabase.getInstance(requireContext()) }

        noteText = view.findViewById(R.id.tvNoteText)
        noteTextInput = view.findViewById(R.id.etNoteInput)
        addButton = view.findViewById(R.id.btnAdd)

        noteTextInput.addTextChangedListener { noteText.text = noteTextInput.text.toString() }

        addButton.setOnClickListener {
            val text = noteTextInput.text.toString()
            if(text.isNotEmpty()){
                CoroutineScope(Dispatchers.IO).launch {
                    noteDatabase.getNoteDao().addNote(Note(0,text))
                    withContext(Dispatchers.Main){
                        closeKeyboard()
                        Navigation.findNavController(view).navigate(R.id.action_addNewNote_to_home2)
                    }
                }
            }else{
                Toast.makeText(requireContext(),"Type a Note", Toast.LENGTH_LONG).show()
            }
        }
        return view
    }

    //https://stackoverflow.com/questions/55505049/how-to-close-the-soft-keyboard-from-a-fragment-using-kotlin
    // how to close Keyboard within fragment
    fun closeKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddNewNote.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddNewNote().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}