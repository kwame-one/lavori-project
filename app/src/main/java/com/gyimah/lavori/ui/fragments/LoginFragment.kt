package com.gyimah.lavori.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.gyimah.lavori.R
import com.gyimah.lavori.databinding.FragmentLoginBinding
import com.gyimah.lavori.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerTxt.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.registerFragment)
        }

        binding.continueBtn.setOnClickListener {
            loginViewModel.loginWithEmail(
                email = binding.email.text.toString(),
                password = binding.password.text.toString()
            )
        }

        binding.loginWithGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            googleLauncher.launch(intent)

        }

        loginViewModel.accountState.observe(viewLifecycleOwner) {
            if (it == 0) {
                //redirect to account page

                loginViewModel.accountState.value = 1
            }
        }


        loginViewModel.successState.observe(viewLifecycleOwner) {
            if (it) {

            }else {
                val errorMessage = loginViewModel.errorState.value

                if (errorMessage != null) {
                    Toast.makeText(requireView().context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            loginViewModel.successState.value = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val googleLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    loginViewModel.loginWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(
                    requireView().context, "Error signing in with google", Toast.LENGTH_SHORT).show()
            }
        }
    }

}