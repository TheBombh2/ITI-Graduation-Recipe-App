package com.iti.graduation.recipeapp.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iti.graduation.recipeapp.AuthActivity
import com.iti.graduation.recipeapp.R
import com.iti.graduation.recipeapp.databinding.FragmentLoginBinding
import com.iti.graduation.recipeapp.databinding.FragmentRegisterBinding
import com.iti.graduation.recipeapp.ui.auth.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.registerBtn.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.isSuccess.observe(viewLifecycleOwner){isSuccess ->
            if(isSuccess){
                findNavController().navigateUp()
            }
        }
        viewModel.error.observe(viewLifecycleOwner){error ->
            error?.let {
                Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerBtn.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            viewModel.register(username,email,password)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}