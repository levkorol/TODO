package com.levkorol.todo.ui.target.viewpagertargets

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.levkorol.todo.R
import com.levkorol.todo.model.Targets
import com.levkorol.todo.ui.MainActivity
import com.levkorol.todo.ui.target.TargetViewModel
import com.levkorol.todo.ui.target.adapters.AdapterTargets
import kotlinx.android.synthetic.main.fragment_my_targets.*


class MyTargetsFragment : Fragment() {
    private lateinit var viewModel: TargetViewModel
    private var targets: List<Targets>? = null
    private lateinit var adapterTargets: AdapterTargets
    private val TAG = "MyTargetsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_targets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_targets)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapterTargets = AdapterTargets(activity as MainActivity)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapterTargets
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(requireActivity()).get(TargetViewModel::class.java)
        observeTarget()
        Log.d(TAG,"$targets")
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
        adapterTargets.dataItems = targets!!.sortedByDescending { it.dateCreate }
        if (adapterTargets.dataItems.isEmpty()) {
            no_target.visibility = View.VISIBLE
        } else {
            no_target.visibility = View.GONE
        }
    }
}
