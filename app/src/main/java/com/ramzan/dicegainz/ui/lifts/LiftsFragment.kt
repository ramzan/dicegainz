package com.ramzan.dicegainz.ui.lifts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ramzan.dicegainz.MainFragmentDirections
import com.ramzan.dicegainz.R
import com.ramzan.dicegainz.database.LiftDatabase
import com.ramzan.dicegainz.databinding.LiftsFragmentBinding

/**
 * A fragment representing a list of Lifts.
 */
class LiftsFragment : Fragment() {

    private lateinit var binding: LiftsFragmentBinding

    private lateinit var viewModel: LiftsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and get instance of binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.lifts_fragment, container, false
        )

        // Get ViewModel Factory
        val application = requireNotNull(this.activity).application
        val dataSource = LiftDatabase.getInstance(application).liftDatabaseDao
        val viewModelFactory = LiftsViewModelFactory(dataSource, application)

        // Get ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(LiftsViewModel::class.java)
        binding.liftsViewModel = viewModel

        // Set the recyclerview adapter
        val adapter = LiftAdapter(LiftAdapter.OnClickListener { Log.d("clicker", "${it.name} clicked!")})
        binding.liftList.adapter = adapter

        viewModel.lifts.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.lifecycleOwner = this

        val navController = Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)

        binding.fab.setOnClickListener {
            navController.navigate(MainFragmentDirections.actionMainFragmentToEditorFragment())
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LiftsFragment()
    }
}