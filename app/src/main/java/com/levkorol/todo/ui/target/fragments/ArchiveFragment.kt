package com.levkorol.todo.ui.target.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.MainActivity
import com.levkorol.todo.R
import com.levkorol.todo.base.DraggableListDelegate
import com.levkorol.todo.model.Targets
import com.levkorol.todo.ui.target.adapters.AdapterTargets
import com.levkorol.todo.ui.target.fragments.MyTargetsFragment.Companion.itemTouchHelper
import com.levkorol.todo.ui.target.viewmodel.TargetViewModel
import kotlinx.android.synthetic.main.fragment_my_habits.*


class ArchiveFragment : Fragment(), DraggableListDelegate {
    private lateinit var viewModel: TargetViewModel
    private var targets: List<Targets>? = null
    private lateinit var adapterTargets: AdapterTargets


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_habits, container, false)
    }

    override fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_archive)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterTargets = AdapterTargets(activity as MainActivity, this)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapterTargets
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(TargetViewModel::class.java)
        observeTarget()
    }

    private fun observeTarget() {
        viewModel.getTargets().observe(this, Observer { target ->
            this.targets = target
            adapterTargets.notifyDataSetChanged()
            updateTargets()
        })
    }

    private fun updateTargets() {
        if (targets == null) return
        adapterTargets.dataItems = targets!!
            .filter {
                it.inArchive
            }
            .sortedByDescending { it.startData }
            .toMutableList()

        if (adapterTargets.dataItems.isEmpty()) {
            no_target_in_archive.visibility = View.VISIBLE
            pick1.visibility = View.VISIBLE
        } else {
            no_target_in_archive.visibility = View.GONE
            pick1.visibility = View.GONE
        }
    }


}
