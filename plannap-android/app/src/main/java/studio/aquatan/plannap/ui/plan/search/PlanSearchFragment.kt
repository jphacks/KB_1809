package studio.aquatan.plannap.ui.plan.search


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
import studio.aquatan.plannap.databinding.FragmentPlanSearchBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import studio.aquatan.plannap.ui.main.MainFragmentType
import studio.aquatan.plannap.ui.main.MainViewModel
import studio.aquatan.plannap.ui.plan.searchresult.PlanSearchResultActivity
import javax.inject.Inject

class PlanSearchFragment : Fragment() {

    companion object {
        fun newInstance() = PlanSearchFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentPlanSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_search, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        AndroidSupportInjection.inject(this)

        val provider = ViewModelProvider(requireActivity(), viewModelFactory)

        val viewModel = provider.get(PlanSearchViewModel::class.java)
        binding.viewModel = viewModel

        provider.get(MainViewModel::class.java)
            .onAttachFragment(MainFragmentType.SEARCH)
    }

    private fun PlanSearchViewModel.subscribe() {
        val fragment = this@PlanSearchFragment

        startSearchResultActivity.observe(fragment, Observer { areaName ->
            startActivity(PlanSearchResultActivity.createIntent(requireContext(), areaName))
        })
    }
}
