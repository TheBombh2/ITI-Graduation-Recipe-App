package com.iti.graduation.recipeapp.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.iti.graduation.recipeapp.AuthActivity
import com.iti.graduation.recipeapp.R
import com.iti.graduation.recipeapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
         }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.loginBtn.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.loggedInUser.observe(viewLifecycleOwner){user ->
            if(user != null){
                (requireActivity() as AuthActivity).navigateToRecipeActivity(user)
            }
        }

        viewModel.error.observe(viewLifecycleOwner){error ->
            error?.let {
                Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            viewModel.login(email,password)
        }

        binding.registerBtn.setOnClickListener {
            (requireActivity() as AuthActivity).navigateToRegister()

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}