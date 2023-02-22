package com.archql.rssreader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.archql.rssreader.databinding.FragmentSecondBinding;
import com.archql.rssreader.ui.helpers.RssViewModel;

import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private RssViewModel viewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setup data model
        viewModel = new ViewModelProvider(requireActivity()).get(RssViewModel.class);
        binding.setRssViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // hide progress
                binding.progressBar.setVisibility(View.INVISIBLE);
            }
        });

        RSSItem item = viewModel.getSelectedRssItem().getValue();
        if (item != null) {
            binding.webView.loadUrl(item.link);
            binding.txtUrl.setText(item.link);
        } else {
            binding.webView.loadUrl("http://www.google.com/404");
            binding.txtUrl.setText("Woops! Something went wrong!");
        }
        binding.progressBar.setVisibility(View.VISIBLE);

//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}