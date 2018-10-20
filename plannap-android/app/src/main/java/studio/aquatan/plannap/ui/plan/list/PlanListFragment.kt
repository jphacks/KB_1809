package studio.aquatan.plannap.ui.plan.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.android.support.AndroidSupportInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.FragmentPlanListBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import studio.aquatan.plannap.ui.main.MainFragmentType
import studio.aquatan.plannap.ui.main.MainViewModel
import studio.aquatan.plannap.ui.plan.PlanAdapter
import studio.aquatan.plannap.ui.plan.detail.PlanDetailActivity
import javax.inject.Inject

class PlanListFragment : Fragment() {

    companion object {
        fun newInstance() = PlanListFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentPlanListBinding
    private lateinit var viewModel: PlanListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_plan_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)

        val provider = ViewModelProvider(requireActivity(), viewModelFactory)

        viewModel = provider.get(PlanListViewModel::class.java)

        val adapter = PlanAdapter(layoutInflater, viewModel::onPlanClick)
        binding.recyclerView.apply {
            setAdapter(adapter)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        viewModel.subscribe(adapter)
        viewModel.onActivityCreated()

        provider.get(MainViewModel::class.java)
            .onAttachFragment(MainFragmentType.HOME)
    }

    private fun PlanListViewModel.subscribe(adapter: PlanAdapter) {
        val fragment = this@PlanListFragment

        planList.observe(fragment, Observer { list ->
            binding.progressBar.isVisible = false
            adapter.submitList(list)
        })
        startPlanDetailActivity.observe(fragment, Observer { id ->
            startActivity(PlanDetailActivity.createIntent(requireContext(), id))
        })
    }
}
