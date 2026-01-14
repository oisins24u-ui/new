package com.example.iptvplayer.ui.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.iptvplayer.R
import com.example.iptvplayer.data.AuthManager
import com.example.iptvplayer.data.repository.Repository
import com.example.iptvplayer.databinding.FragmentHomeBinding
import com.example.iptvplayer.ui.home.CategoryAdapter

class CategoryListFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ContentViewModel
    private var contentType: String = "live"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentType = arguments?.getString("CONTENT_TYPE") ?: "live"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.welcomeText.text = when(contentType) {
            "live" -> "Live TV"
            "movies" -> "Movies"
            "series" -> "Series"
            else -> "Content"
        }
        binding.searchEditText.visibility = View.VISIBLE

        val authManager = AuthManager(requireContext())
        val repository = Repository(authManager)
        val factory = ContentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ContentViewModel::class.java]

        val adapter = CategoryAdapter(emptyList()) { item ->
            val fragment = StreamListFragment()
            val args = Bundle()
            args.putString("CATEGORY_ID", item.id)
            args.putString("CONTENT_TYPE", contentType)
            fragment.arguments = args

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.mainMenuRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.mainMenuRecyclerView.adapter = adapter

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
             // Use a placeholder icon for now
             val items = categories.map {
                 CategoryAdapter.MenuItem(it.categoryId, it.categoryName, android.R.drawable.ic_menu_sort_by_size)
             }
             adapter.updateItems(items)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        when (contentType) {
            "live" -> viewModel.loadLiveCategories()
            "movies" -> viewModel.loadVodCategories()
            "series" -> viewModel.loadSeriesCategories()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
