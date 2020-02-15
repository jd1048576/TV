package jdr.tv.feature.shows.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import javax.inject.Inject
import jdr.tv.common.navigation.GlobalActions
import jdr.tv.common.ui.extensions.displayMetrics
import jdr.tv.common.ui.extensions.dpToPixels
import jdr.tv.common.ui.extensions.setupToolbar
import jdr.tv.common.ui.utils.SpacingItemDecoration
import jdr.tv.data.core.di.DataComponent
import jdr.tv.feature.shows.R
import jdr.tv.feature.shows.databinding.FragmentShowsBinding
import jdr.tv.feature.shows.di.inject
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ShowsFragment @Inject constructor(private val component: DataComponent) : Fragment(R.layout.fragment_shows) {

    companion object {
        private const val SPACING = 16
        private const val SPAN = 200
    }

    private lateinit var binding: FragmentShowsBinding

    private lateinit var adapter: ShowsAdapter

    @Inject
    lateinit var viewModel: ShowsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(component)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShowsBinding.inflate(inflater, container, false)
        binding.span = calculateSpan()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        setupToolbar(jdr.tv.common.ui.R.id.toolbar, jdr.tv.common.navigation.R.string.shows)
        setupRecyclerView()
        observe()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(jdr.tv.common.navigation.R.menu.menu_main, menu)
    }

    private fun calculateSpan(): Int {
        return (context!!.displayMetrics().widthPixels.toFloat() / context!!.dpToPixels(SPAN)).roundToInt()
    }

    private fun setupRecyclerView() = with(binding.fragmentSearchRecyclerView) {
        this@ShowsFragment.adapter = ShowsAdapter(this@ShowsFragment::navigate)
        adapter = this@ShowsFragment.adapter
        addItemDecoration(SpacingItemDecoration.GridLayout(context!!.dpToPixels(SPACING)))
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.addedShowList.collect {
                adapter.submitList(it)
            }
        }
    }

    private fun navigate(showId: Long) {
        findNavController().navigate(GlobalActions.actionDetails(showId))
    }
}