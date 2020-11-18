package com.levkorol.todo.ui.target


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.levkorol.todo.R
import com.levkorol.todo.data.note.MainRepository
import com.levkorol.todo.model.Targets
import com.levkorol.todo.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_edit_target.*


class EditTargetFragment : androidx.fragment.app.Fragment() {
    private var targetId: Long = -1
    private lateinit var viewModel: TargetViewModel
    private var targets: Targets? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_target, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        targetId = arguments?.getLong(TARGET_ID, -1)!!

        initViews()
        viewModel = ViewModelProvider(requireActivity()).get(TargetViewModel::class.java)
        observeTargets()
    }

    private fun initViews() {
        edit_save_target_btn.setOnClickListener {
            saveEditTarget()
            Toast.makeText(activity, "Изменения сохранены", Toast.LENGTH_LONG).show()
            parentFragmentManager.popBackStack()
            (activity as MainActivity).loadFragment(TargetFragment())
        }

        edit_back_click.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        edit_delete_target.setOnClickListener {

            val builder = activity?.let { it1 -> MaterialAlertDialogBuilder(it1) }
            builder?.setMessage("Удалить: ${targets?.name} ?")
            builder?.setPositiveButton("Да") { _, _ ->
                targets?.id?.let { it1 -> MainRepository.deleteTarget(it1) }
                parentFragmentManager.popBackStack()
            }
            builder?.setNegativeButton("Отмена") { _, _ ->
            }
            val dialog: AlertDialog = builder?.create()!!
            dialog.show()
            true

        }

    }

    private fun saveEditTarget() {
        targets!!.name = edit_name_target.text.toString()
        targets!!.description = edit_disc_target.text.toString()
        targets!!.background

        if (edit_white_btn.isChecked) targets!!.background = Targets.BackgroundTarget.WHITE
        if (edit_grey_btn.isChecked) targets!!.background = Targets.BackgroundTarget.PURPLE
        if (edit_darkblu_btn.isChecked) targets!!.background = Targets.BackgroundTarget.DARKER_BLU
        if (edit_oranzh_btn.isChecked) targets!!.background = Targets.BackgroundTarget.ORANGE
        if (edit_kiwi_btn.isChecked) targets!!.background = Targets.BackgroundTarget.KIWI
        if (edit_blu_btn.isChecked) targets!!.background = Targets.BackgroundTarget.LIGHT_BLU
        if (edit_pink_btn.isChecked) targets!!.background = Targets.BackgroundTarget.PINK_LIGHT

        if (edit_ic_0.isChecked) targets!!.image = 0
        if (edit_ic_1.isChecked) targets!!.image = 1
        if (edit_ic_2.isChecked) targets!!.image = 2
        if (edit_ic_3.isChecked) targets!!.image = 3
        if (edit_ic_4.isChecked) targets!!.image = 4
        if (edit_ic_5.isChecked) targets!!.image = 5
        if (edit_ic_6.isChecked) targets!!.image = 6
        if (edit_ic_7.isChecked) targets!!.image = 7
        if (edit_ic_8.isChecked) targets!!.image = 8
        if (edit_ic_9.isChecked) targets!!.image = 9
        if (edit_ic_10.isChecked) targets!!.image = 10
        if (edit_ic_11.isChecked) targets!!.image = 11
        if (edit_ic_12.isChecked) targets!!.image = 12
        if (edit_ic_13.isChecked) targets!!.image = 13
        if (edit_ic_14.isChecked) targets!!.image = 14
        if (edit_ic_15.isChecked) targets!!.image = 15
        if (edit_ic_16.isChecked) targets!!.image = 16

        MainRepository.updateTarget(targets!!)
    }

    private fun observeTargets() {
        viewModel.getTargets().observe(viewLifecycleOwner, Observer<List<Targets>> { target ->
            targets = target.firstOrNull { n -> n.id == targetId }

            edit_name_target.setText(targets?.name)
            edit_disc_target.setText(targets?.description)
            when (targets?.image) {
                1 -> edit_ic_1.isChecked = true
                2 -> edit_ic_2.isChecked = true
                3 -> edit_ic_3.isChecked = true
                4 -> edit_ic_4.isChecked = true
                5 -> edit_ic_5.isChecked = true
                6 -> edit_ic_6.isChecked = true
                7 -> edit_ic_7.isChecked = true
                8 -> edit_ic_8.isChecked = true
                9 -> edit_ic_9.isChecked = true
                10 -> edit_ic_10.isChecked = true
                11 -> edit_ic_11.isChecked = true
                12 -> edit_ic_12.isChecked = true
                13 -> edit_ic_13.isChecked = true
                14 -> edit_ic_14.isChecked = true
                15 -> edit_ic_15.isChecked = true
                16 -> edit_ic_16.isChecked = true
            }

            when (targets?.background) {
                Targets.BackgroundTarget.WHITE -> edit_white_btn.isChecked = true
                Targets.BackgroundTarget.PURPLE -> edit_grey_btn.isChecked = true
                Targets.BackgroundTarget.DARKER_BLU -> edit_darkblu_btn.isChecked = true
                Targets.BackgroundTarget.ORANGE -> edit_oranzh_btn.isChecked = true
                Targets.BackgroundTarget.KIWI -> edit_kiwi_btn.isChecked = true
                Targets.BackgroundTarget.LIGHT_BLU -> edit_blu_btn.isChecked = true
                Targets.BackgroundTarget.PINK_LIGHT -> edit_pink_btn.isChecked = true
            }
        })
    }

    companion object {
        private const val TARGET_ID = "TARGET_ID"

        fun newInstance(targetId: Long): EditTargetFragment {
            val fragment = EditTargetFragment()
            val arguments = Bundle()
            arguments.apply {
                putLong(TARGET_ID, targetId)
            }
            fragment.arguments = arguments
            return fragment
        }
    }
}
