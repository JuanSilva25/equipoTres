package com.example.equipotres.fragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.equipotres.databinding.FragmentRulesBinding


class RulesFragment : Fragment() {

    private lateinit var binding: FragmentRulesBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationFragmentHome()
    }

    private fun navigationFragmentHome(){
        binding.arrowLeft.setOnClickListener() {
            findNavController().navigateUp()
        }
    }
}