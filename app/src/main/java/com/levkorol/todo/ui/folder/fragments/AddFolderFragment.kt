package com.levkorol.todo.ui.folder.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Folder
import com.levkorol.todo.model.Folder.Background.PURPLE
import com.levkorol.todo.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_add_folder.*

class AddFolderFragment : Fragment() {
    //  private lateinit var viewModel: FolderViewModel
    private var parentFolderId: Long = 0
    private lateinit var background: Folder.Background


    companion object {
        private const val PARENT_FOLDER = "ParentId"

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
        if (arguments != null) {
            parentFolderId = arguments?.getLong(PARENT_FOLDER, 0)!!
        }

        save_folder_btn.setOnClickListener {
            if(add_title_text_folder.text.isNotEmpty()) {
                saveFolder()
                parentFragmentManager.popBackStack()
                hideKeyboard()
            } else {
                Toast.makeText(activity, "Введите название папки", Toast.LENGTH_SHORT).show()
            }
        }

        back_profile.setOnClickListener {
            parentFragmentManager.popBackStack()
            hideKeyboard()
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
            Toast.makeText(activity, "Папка успешно сохранена", Toast.LENGTH_SHORT).show() }
    }

    private fun saveFolder() {
        if (radioButtonGreen.isChecked) {
            background = Folder.Background.GREEN
        }
        if (radioButtonGrey.isChecked) {
            background = Folder.Background.GRAY
        }
        if (radioButtonPurpur.isChecked) {
            background = PURPLE
        }
        if (radioButtonRed.isChecked) {
            background = Folder.Background.PINK
        }

        MainRepository.addFolder(
            Folder(
                nameFolder = add_title_text_folder.text.toString(),
                background = background,
                parentFolderId = parentFolderId,
                date = System.currentTimeMillis()
            )
        )
    }

    private fun validations(): Boolean {
        return !(add_title_text_folder.text.isNullOrEmpty())
    }
}
