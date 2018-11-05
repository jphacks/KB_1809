package studio.aquatan.plannap.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.AndroidSupportInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.data.NetworkState
import studio.aquatan.plannap.databinding.FragmentProfileBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import studio.aquatan.plannap.ui.main.MainFragmentType
import studio.aquatan.plannap.ui.main.MainViewModel
import studio.aquatan.plannap.ui.plan.PlanAdapter
import studio.aquatan.plannap.ui.plan.detail.PlanDetailActivity
import javax.inject.Inject

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.setLifecycleOwner(this)
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            binding.swipeRefreshLayout.isEnabled = offset >= 0
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)

        val provider = ViewModelProvider(requireActivity(), viewModelFactory)

        viewModel = provider.get(ProfileViewModel::class.java)
        binding.viewModel = viewModel

        val adapter = PlanAdapter(layoutInflater, viewModel::onPlanClick, viewModel::onRetryClick, isSmallLayout = true)
        binding.recyclerView.apply {
            setAdapter(adapter)
            setHasFixedSize(true)
        }

        viewModel.subscribe(adapter)

        provider.get(MainViewModel::class.java)
            .onAttachFragment(MainFragmentType.PROFILE)
    }

    private fun ProfileViewModel.subscribe(adapter: PlanAdapter) {
        val fragment = this@ProfileFragment

        planList.observe(fragment, Observer { list ->
            adapter.submitList(list)
        })
        initialLoad.observe(fragment, Observer { state ->
            binding.swipeRefreshLayout.isRefreshing = state == NetworkState.LOADING
        })
        networkState.observe(fragment, Observer { adapter.setNetworkState(it) })

        startPlanDetailActivity.observe(fragment, Observer { id ->
            startActivity(PlanDetailActivity.createIntent(requireContext(), id))
        })
    }
}
