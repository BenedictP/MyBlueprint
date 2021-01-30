package io.benedictp.myblueprint.presentation.launches

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.benedictp.domain.model.Launch
import io.benedictp.myblueprint.R
import io.benedictp.myblueprint.databinding.FragmentLaunchesBinding
import io.benedictp.myblueprint.presentation.util.ViewState
import io.benedictp.myblueprint.presentation.util.viewBinding

const val TAG = "LaunchesFragment"

@AndroidEntryPoint
class LaunchesFragment : Fragment(R.layout.fragment_launches) {

	private val viewModel: LaunchesViewModel by viewModels()
	private val uiBinding by viewBinding(FragmentLaunchesBinding::bind)

	private lateinit var launchesAdapter: LaunchesAdapter

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		launchesAdapter = LaunchesAdapter {
			Toast.makeText(requireContext(), "Clicked $it", Toast.LENGTH_LONG).show()
		}

		initUi()
		initObservers()
	}

	override fun onDestroyView() {
		//see https://charlesmuchene.hashnode.dev/a-subtle-memory-leak-fragment-recyclerview-and-its-adapter-ck805s7jd03frzns17uapi3vh
		view?.findViewById<RecyclerView>(R.id.launches)?.adapter = null
		super.onDestroyView()
	}

	private fun initUi() {
		uiBinding.swipeRefresh.setOnRefreshListener { loadUpcomingLaunches() }
		uiBinding.errorView.listErrorRetryButton.setOnClickListener { loadUpcomingLaunches() }
		uiBinding.launches.apply {
			layoutManager = LinearLayoutManager(requireContext())
			itemAnimator = DefaultItemAnimator()
			adapter = launchesAdapter
		}
	}

	private fun initObservers() {
		viewModel.upcomingLaunchesLiveData.observe(viewLifecycleOwner) { upcomingLaunchViewState ->
			when (upcomingLaunchViewState) {
				ViewState.Init -> loadUpcomingLaunches()
				ViewState.Loading -> showLaunchLoading()
				is ViewState.Data -> showLaunchData(upcomingLaunchViewState.value)
				is ViewState.Error -> showLaunchError(upcomingLaunchViewState.error.message ?: "Error")
			}
		}
	}

	private fun loadUpcomingLaunches() {
		viewModel.loadUpcomingLaunches()
	}

	private fun showLaunchLoading() {
		uiBinding.emptyView.listEmptyContainer.visibility = View.GONE
		uiBinding.errorView.listErrorContainer.visibility = View.GONE
		uiBinding.swipeRefresh.visibility = View.VISIBLE
		uiBinding.launches.visibility = View.VISIBLE
		uiBinding.swipeRefresh.isRefreshing = true
	}

	private fun showLaunchData(launches: List<Launch>) {
		if (launches.isEmpty()) {
			showLaunchesEmpty()
			return
		}
		uiBinding.emptyView.listEmptyContainer.visibility = View.GONE
		uiBinding.errorView.listErrorContainer.visibility = View.GONE
		uiBinding.swipeRefresh.visibility = View.VISIBLE
		uiBinding.launches.visibility = View.VISIBLE
		uiBinding.swipeRefresh.isRefreshing = false
		launchesAdapter.setData(launches)
	}

	private fun showLaunchesEmpty() {
		uiBinding.emptyView.listEmptyText.text = "No Data found"
		uiBinding.emptyView.listEmptyContainer.visibility = View.VISIBLE
		uiBinding.errorView.listErrorContainer.visibility = View.GONE
		uiBinding.swipeRefresh.visibility = View.VISIBLE
		uiBinding.launches.visibility = View.GONE
		uiBinding.swipeRefresh.isRefreshing = false
	}

	private fun showLaunchError(errorText: String) {
		uiBinding.emptyView.listEmptyContainer.visibility = View.GONE
		uiBinding.errorView.listErrorText.text = errorText
		uiBinding.errorView.listErrorContainer.visibility = View.VISIBLE
		uiBinding.swipeRefresh.visibility = View.GONE
		uiBinding.launches.visibility = View.GONE
		uiBinding.swipeRefresh.isRefreshing = false
	}

}