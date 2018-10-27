package studio.aquatan.plannap.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.FragmentHomeBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import studio.aquatan.plannap.ui.main.MainFragmentType
import studio.aquatan.plannap.ui.main.MainViewModel
import studio.aquatan.plannap.ui.plan.PlanAdapter
import studio.aquatan.plannap.ui.plan.detail.PlanDetailActivity
import javax.inject.Inject

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.setLifecycleOwner(this)
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)

        val provider = ViewModelProvider(requireActivity(), viewModelFactory)

        viewModel = provider.get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        val adapter = PlanAdapter(layoutInflater, viewModel::onPlanClick, viewModel::onFavoriteClick)
        binding.recyclerView.apply {
            setAdapter(adapter)
            setHasFixedSize(true)
        }

        binding.swipeRefreshLayout.isRefreshing = true

        viewModel.subscribe(adapter)

        provider.get(MainViewModel::class.java)
            .onAttachFragment(MainFragmentType.HOME)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onActivityResumed()
    }

    private fun HomeViewModel.subscribe(adapter: PlanAdapter) {
        val fragment = this@HomeFragment

        planList.observe(fragment, Observer { list ->
            binding.swipeRefreshLayout.isRefreshing = false

            adapter.submitList(list)
        })
        startPlanDetailActivity.observe(fragment, Observer { id ->
            startActivity(PlanDetailActivity.createIntent(requireContext(), id))
        })
    }
}
