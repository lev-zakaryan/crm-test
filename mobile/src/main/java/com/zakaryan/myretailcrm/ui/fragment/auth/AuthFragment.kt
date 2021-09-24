package com.zakaryan.myretailcrm.ui.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zakaryan.myretailcrm.R
import com.zakaryan.myretailcrm.data.result.Result
import com.zakaryan.myretailcrm.databinding.FragmentAuthBinding
import com.zakaryan.myretailcrm.ui.MyRetailCrmApp
import com.zakaryan.myretailcrm.ui.fragment.auth.viewmodel.AuthViewModel
import com.zakaryan.myretailcrm.ui.viewmodel.SavedStateViewModelFactory
import javax.inject.Inject

class AuthFragment : Fragment() {

    @Inject
    lateinit var authViewModelFactory: AuthViewModel.Factory

    private val authViewModel by viewModels<AuthViewModel> {
        SavedStateViewModelFactory(authViewModelFactory, this)
    }

    private var _viewBinding: FragmentAuthBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentAuthBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyRetailCrmApp.get(requireActivity())
            .appComponent
            .authComponent().create()
            .inject(this)

        initializeViews()
        initializeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun initializeViews() {
        viewBinding.buttonAuth.setOnClickListener {
            val clientUrl = viewBinding.clientUrl.text?.toString()
            val username = viewBinding.username.text?.toString()
            val password = viewBinding.password.text?.toString()
            if (clientUrl.isNullOrBlank() || username.isNullOrBlank() || password.isNullOrBlank()) {
                showErrorToast(R.string.message_auth_incorrect_credentials)
            } else {
                authViewModel.auth(clientUrl, username, password)
            }
        }
    }

    private fun initializeObservers() {
        authViewModel.isOperationInProgress.observe(viewLifecycleOwner, { inProgress ->
            viewBinding.progress.visibility = if (inProgress) View.VISIBLE else View.GONE
            viewBinding.buttonAuth.visibility = if (inProgress) View.GONE else View.VISIBLE
        })
        authViewModel.authResultEvent.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success -> {
                    findNavController().navigate(R.id.action_AuthFragment_to_ListFragment)
                }
                else -> {
                    showErrorToast(R.string.message_auth_error)
                }
            }
        })
    }

    private fun showErrorToast(resId: Int) {
        Toast.makeText(requireContext().applicationContext, resId, Toast.LENGTH_SHORT).show()
    }
}