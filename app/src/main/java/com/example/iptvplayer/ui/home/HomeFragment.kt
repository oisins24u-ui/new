package com.example.iptvplayer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.iptvplayer.R
import com.example.iptvplayer.databinding.FragmentHomeBinding
import com.example.iptvplayer.ui.content.CategoryListFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuItems = listOf(
            CategoryAdapter.MenuItem("live", "Live TV", android.R.drawable.ic_media_play),
            CategoryAdapter.MenuItem("movies", "Movies", android.R.drawable.ic_media_next),
            CategoryAdapter.MenuItem("series", "Series", android.R.drawable.ic_menu_agenda),
            CategoryAdapter.MenuItem("settings", "Settings", android.R.drawable.ic_menu_preferences)
        )

        val adapter = CategoryAdapter(menuItems) { item ->
            when (item.id) {
                "live" -> {
                    navigateToCategoryList("live")
                }
                "movies" -> {
                    navigateToCategoryList("movies")
                }
                "series" -> {
                    navigateToCategoryList("series")
                }
                "settings" -> {
                     Toast.makeText(requireContext(), "Settings not implemented", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "Clicked: ${item.title}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.mainMenuRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.mainMenuRecyclerView.adapter = adapter
    }

    private fun navigateToCategoryList(type: String) {
        val fragment = CategoryListFragment()
        val args = Bundle()
        args.putString("CONTENT_TYPE", type)
        fragment.arguments = args

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
