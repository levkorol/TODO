package com.levkorol.todo.ui.folder


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.levkorol.todo.R
import com.levkorol.todo.data.note.NoteRepository
import com.levkorol.todo.model.Folder
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.note.AddNoteFragment
import com.levkorol.todo.ui.notes.NotesFragment
import kotlinx.android.synthetic.main.fragment_add_folder.*

class AddFolderFragment : Fragment() {
 //  private lateinit var viewModel: FolderViewModel
    private var parentFolderId: Long = 0

    companion object {
        private  const val PARENT_FOLDER = "ParentId"

        fun newInstance(parentFolderId: Long): AddFolderFragment {
            val fragment = AddFolderFragment()
            val arguments = Bundle()
            arguments.apply {
                putLong(PARENT_FOLDER, parentFolderId)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_folder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         if(arguments != null) {
             parentFolderId = arguments?.getLong(PARENT_FOLDER, 0)!!
         }

        save_folder_btn.setOnClickListener {
            (activity as MainActivity).loadFragment(NotesFragment())
            saveFolder()
        }
    }

    override fun onStart() {
        super.onStart()
      //  viewModel = ViewModelProvider(requireActivity()).get(FolderViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveFolderToDatabase()
    }

    private fun saveFolderToDatabase() {
        if (validations()) {
            Toast.makeText(activity, "Папка успешно сохранена", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(
                activity,
                "Вы создали папку",
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun saveFolder() {

        NoteRepository.addFolder(
            Folder(
                nameFolder = add_title_text_folder.text.toString(),
                descriptionFolder = add_description_folder_text.text.toString(),
                color = 1,
                parentFolderId =  parentFolderId,
                date = 1
            )
        )

    }

    private fun validations(): Boolean {
        return !(add_title_text_folder.text.isNullOrEmpty()
                && add_description_folder_text.text.isNullOrEmpty()
                )
    }
}
