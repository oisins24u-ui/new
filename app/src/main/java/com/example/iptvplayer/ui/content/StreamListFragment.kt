package com.example.iptvplayer.ui.content

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iptvplayer.PlayerActivity
import com.example.iptvplayer.data.AuthManager
import com.example.iptvplayer.data.repository.Repository
import com.example.iptvplayer.databinding.FragmentHomeBinding

class StreamListFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ContentViewModel
    private var categoryId: String = ""
    private var contentType: String = "live"
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getString("CATEGORY_ID") ?: ""
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

        binding.welcomeText.text = when (contentType) {
             "live" -> "Channels"
             "movies" -> "Movies"
             "series" -> "Series"
             else -> "Streams"
        }
        binding.searchEditText.visibility = View.VISIBLE

        authManager = AuthManager(requireContext())
        val repository = Repository(authManager)
        val factory = ContentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ContentViewModel::class.java]

        val adapter = StreamAdapter(emptyList()) { item ->
            val url = authManager.getUrl()
            val user = authManager.getUsername()
            val pass = authManager.getPassword()

            if (url != null && user != null && pass != null) {
                // Construct URL
                // Ensure URL protocol
                 var validUrl = url
                 if (!validUrl.startsWith("http://") && !validUrl.startsWith("https://")) {
                     validUrl = "http://$validUrl"
                 }
                val cleanUrl = if (validUrl.endsWith("/")) validUrl.dropLast(1) else validUrl

                val streamUrl = if (contentType == "live") {
                    "$cleanUrl/live/$user/$pass/${item.streamId}.ts"
                } else {
                    val ext = item.extension ?: "mp4"
                    "$cleanUrl/movie/$user/$pass/${item.streamId}.$ext"
                }

                val intent = Intent(requireContext(), PlayerActivity::class.java)
                intent.putExtra("STREAM_URL", streamUrl)
                startActivity(intent)
            }
        }

        binding.mainMenuRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.mainMenuRecyclerView.adapter = adapter

        when (contentType) {
            "live" -> {
                viewModel.streams.observe(viewLifecycleOwner) { streams ->
                     val items = streams.map {
                         StreamAdapter.StreamItem(it.streamId, it.name, it.streamIcon, it.streamId)
                     }
                     adapter.updateItems(items)
                }
                viewModel.loadLiveStreams(categoryId)
            }
            "movies" -> {
                viewModel.vodStreams.observe(viewLifecycleOwner) { streams ->
                     val items = streams.map {
                         StreamAdapter.StreamItem(it.streamId, it.name, it.streamIcon, it.streamId, it.containerExtension)
                     }
                     adapter.updateItems(items)
                }
                viewModel.loadVodStreams(categoryId)
            }
            "series" -> {
                 viewModel.seriesStreams.observe(viewLifecycleOwner) { streams ->
                     val items = streams.map {
                         StreamAdapter.StreamItem(it.seriesId, it.name, it.cover, it.seriesId)
                     }
                     adapter.updateItems(items)
                }
                viewModel.loadSeries(categoryId)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
