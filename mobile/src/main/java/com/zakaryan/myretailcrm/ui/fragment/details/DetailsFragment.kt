package com.zakaryan.myretailcrm.ui.fragment.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.zakaryan.myretailcrm.databinding.FragmentDetailsBinding
import com.zakaryan.myretailcrm.ui.MyRetailCrmApp
import com.zakaryan.myretailcrm.ui.fragment.details.viewmodel.DetailsViewModel
import com.zakaryan.myretailcrm.ui.viewmodel.SavedStateViewModelFactory
import javax.inject.Inject

class DetailsFragment : Fragment() {

    @Inject
    lateinit var detailsViewModelFactory: DetailsViewModel.Factory

    private val detailsViewModel by viewModels<DetailsViewModel> {
        SavedStateViewModelFactory(detailsViewModelFactory, this)
    }

    private val args by navArgs<DetailsFragmentArgs>()

    private var _viewBinding: FragmentDetailsBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyRetailCrmApp.get(requireActivity())
            .appComponent
            .detailsComponent().create()
            .inject(this)

        initializeObservers()

        if (!detailsViewModel.isLoaded) {
            detailsViewModel.loadCustomerDetails(args.argscustomerid)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun initializeObservers() {
        detailsViewModel.isOperationInProgress.observe(viewLifecycleOwner, { inProgress ->
            viewBinding.progress.visibility = if (inProgress) View.VISIBLE else View.GONE
            viewBinding.userDetails.visibility = if (inProgress) View.GONE else View.VISIBLE
        })
        detailsViewModel.firstNameLiveData.observe(viewLifecycleOwner, { name ->
            viewBinding.firstName.text = name
        })
        detailsViewModel.lastNameLiveData.observe(viewLifecycleOwner, { surname ->
            viewBinding.lastName.text = surname
        })
        detailsViewModel.patronymicLiveData.observe(viewLifecycleOwner, { patronymic ->
            viewBinding.patronymic.text = patronymic
        })
    }
}