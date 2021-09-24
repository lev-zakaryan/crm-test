package com.zakaryan.myretailcrm.ui.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zakaryan.myretailcrm.databinding.FragmentListBinding
import com.zakaryan.myretailcrm.ui.MyRetailCrmApp
import com.zakaryan.myretailcrm.ui.activity.viewmodel.MainViewModel
import com.zakaryan.myretailcrm.ui.fragment.list.adapter.UserListAdapter
import com.zakaryan.myretailcrm.ui.fragment.list.viewmodel.ListViewModel
import com.zakaryan.myretailcrm.ui.viewmodel.SavedStateViewModelFactory
import javax.inject.Inject

class ListFragment : Fragment() {

    @Inject
    lateinit var mainViewModelFactory: MainViewModel.Factory

    @Inject
    lateinit var listViewModelFactory: ListViewModel.Factory

    private val mainViewModel by activityViewModels<MainViewModel> {
        SavedStateViewModelFactory(mainViewModelFactory, requireActivity())
    }

    private val listViewModel by viewModels<ListViewModel> {
        SavedStateViewModelFactory(listViewModelFactory, this)
    }

    private var _viewBinding: FragmentListBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val userListAdapter = UserListAdapter { listUser ->
        findNavController().navigate(
            ListFragmentDirections
                .actionListFragmentToDetailsFragment(listUser.id, listUser.firstName)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyRetailCrmApp.get(requireActivity())
            .appComponent
            .listComponent().create()
            .inject(this)

        initializeViews()
        initializeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.list.adapter = null
        _viewBinding = null
    }

    private fun initializeViews() {
        viewBinding.list.isVerticalScrollBarEnabled = true
        viewBinding.list.setHasFixedSize(true)
        viewBinding.list.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.list.adapter = userListAdapter

        viewBinding.logout.setOnClickListener {
            mainViewModel.clearSession()
        }
    }

    private fun initializeObservers() {
        mainViewModel.isAuthorizedLiveData.observe(viewLifecycleOwner) { isAuthorized ->
            if (isAuthorized && !listViewModel.isLoaded) {
                listViewModel.loadCustomersList()
            }
        }
        listViewModel.isOperationInProgress.observe(viewLifecycleOwner, { inProgress ->
            viewBinding.progress.visibility = if (inProgress) View.VISIBLE else View.GONE
        })
        listViewModel.customersListLiveData.observe(viewLifecycleOwner, { list ->
            userListAdapter.setItems(list)
        })
    }
}