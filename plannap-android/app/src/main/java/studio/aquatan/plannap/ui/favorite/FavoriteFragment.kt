package studio.aquatan.plannap.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import studio.aquatan.plannap.R
import studio.aquatan.plannap.databinding.FragmentFavoriteBinding
import studio.aquatan.plannap.ui.ViewModelFactory
import studio.aquatan.plannap.ui.main.MainFragmentType
import studio.aquatan.plannap.ui.main.MainViewModel
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)

        val provider = ViewModelProvider(requireActivity(), viewModelFactory)

        viewModel = provider.get(FavoriteViewModel::class.java)

        provider.get(MainViewModel::class.java)
            .onAttachFragment(MainFragmentType.FAVORITE)
    }
}
