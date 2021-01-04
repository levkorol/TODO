package com.levkorol.todo.ui.target.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.todo.MainActivity
import com.levkorol.todo.R
import com.levkorol.todo.base.DraggableListDelegate
import com.levkorol.todo.model.Targets
import com.levkorol.todo.ui.target.adapters.AdapterTargets
import com.levkorol.todo.ui.target.viewmodel.TargetViewModel
import com.levkorol.todo.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_my_targets.*


class MyTargetsFragment : Fragment(), DraggableListDelegate {
    private lateinit var viewModel: TargetViewModel
    private var targets: List<Targets>? = null
    private lateinit var adapterTargets: AdapterTargets
    private val TAG = "MyTargetsFragment"


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
        adapterTargets = AdapterTargets(activity as MainActivity, this)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapterTargets

        itemTouchHelper.attachToRecyclerView(recyclerView)

        add_target.setOnClickListener {
            replaceFragment(AddTargetFragment())
        }
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
        adapterTargets.dataItems = targets!!
            .filter {
                !it.inArchive
            }
            .sortedByDescending { it.startData }
            .toMutableList()
        if (adapterTargets.dataItems.isEmpty()) {
            no_target.visibility = View.VISIBLE
            pick.visibility = View.VISIBLE
            add_target.visibility = View.VISIBLE
        } else {
            no_target.visibility = View.GONE
            pick.visibility = View.GONE
            add_target.visibility = View.GONE
        }
    }

    override fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)

    }

    companion object {

        val itemTouchHelper by lazy {
            val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                0
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = recyclerView.adapter as AdapterTargets
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    adapter.moveItem(from, to)
                    adapter.notifyItemMoved(from, to)

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)

                    if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                        viewHolder?.itemView?.alpha = 0.5f
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)

                    viewHolder.itemView.alpha = 1.0f
                }
            }

            ItemTouchHelper(simpleItemTouchCallback)
        }
    }
}
