package com.archql.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.archql.rssreader.databinding.FragmentFirstBinding;
import com.archql.rssreader.ui.helpers.RssViewAdapter;
import com.archql.rssreader.ui.helpers.RssViewModel;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private RssViewAdapter adapter;
    private RssViewModel viewModel;
    private RSSParser rssParser;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setup data model
        viewModel = new ViewModelProvider(requireActivity()).get(RssViewModel.class);
        binding.setRssViewModel(viewModel);
        binding.setLifecycleOwner(this);

        // create adapter
        adapter = new RssViewAdapter(getContext(), new RssViewAdapter.OnRssClickListener() {
            @Override
            public void onClick(RSSItem n) {
                if (n.clickable) {
                    // put n to view model
                    viewModel.setSelectedRssItem(n);
                    // go to view
                    NavHostFragment.findNavController(FirstFragment.this)
                                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
            }
        });
        List<RSSItem> items = viewModel.getListOfRssItems().getValue();
        if (items != null) {
            adapter.add(items);
        }

        binding.btAddFromLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                viewModel.setListOfRssItems(new ArrayList<>());
                mGetContent.launch("text/xml");
            }
        });

        binding.btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                viewModel.setListOfRssItems(new ArrayList<>());

                String url = binding.editRssLink.getText().toString();
                rssParser.getRssItems(url);
            }
        });

        binding.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                viewModel.setListOfRssItems(new ArrayList<>());

                String url = binding.editRssLink.getText().toString();
                rssParser.getRssItems(url);
            }
        });

        binding.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                viewModel.setListOfRssItems(new ArrayList<>());
                binding.editRssLink.setText("");
            }
        });

        // set recycler
        binding.recyclerRSS.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.recyclerRSS.setAdapter(adapter);

        //binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        NavHostFragment.findNavController(FirstFragment.this)
        //                .navigate(R.id.action_FirstFragment_to_SecondFragment);
        //    }
        //});
        rssParser = new RSSParser(adapter, binding.progressBar, getContext(), viewModel);
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    rssParser.getRssItemsUri(uri);
                }
            });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}