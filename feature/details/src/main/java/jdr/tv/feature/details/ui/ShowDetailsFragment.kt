package jdr.tv.feature.details.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import jdr.tv.common.ui.extensions.dpToPixels
import jdr.tv.common.ui.utils.SpacingItemDecoration
import jdr.tv.feature.details.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ShowDetailsFragment : Fragment(R.layout.fragment_base) {

    companion object {
        const val SPACING = 16
    }

    private val viewModel: DetailsViewModel by viewModels(::requireParentFragment)

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ShowDetailsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindResources()
        setupRecyclerView()
        observe()
    }

    private fun bindResources() = with(view!!) {
        recyclerView = findViewById(R.id.recycler_view)
    }

    private fun setupRecyclerView() {
        adapter = ShowDetailsAdapter()
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = null
        recyclerView.addItemDecoration(SpacingItemDecoration.LinearLayout(context!!.dpToPixels(SPACING)))
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.detailedShow.collect {
                adapter.submitList(listOf(it))
            }
        }
    }
}