package com.candraibra.moviecatalog4.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candraibra.moviecatalog4.R;
import com.candraibra.moviecatalog4.activity.DetailTvActivity;
import com.candraibra.moviecatalog4.adapter.TvPageAdapter;
import com.candraibra.moviecatalog4.model.Tv;
import com.candraibra.moviecatalog4.network.OnGetPageTv;
import com.candraibra.moviecatalog4.network.TvRepository;
import com.candraibra.moviecatalog4.utils.ItemClickSupport;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvFragment extends Fragment {
    private final static String LIST_STATE_KEY2 = "STATE2";
    private ArrayList<Tv> tvArrayList = new ArrayList<>();
    private final GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
    private TvRepository tvRepository;
    private RecyclerView recyclerView;
    private boolean isFetchingTv;
    private int currentPage = 1;
    private TvPageAdapter adapter;
    private ArrayList<Tv> tvArrayList2 = new ArrayList<>();

    public TvFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvRepository = TvRepository.getInstance();
        recyclerView = view.findViewById(R.id.rv_discover_tv);
        if (savedInstanceState != null) {
            //  progressBar.setVisibility(View.INVISIBLE);
            final ArrayList<Tv> tvState = savedInstanceState.getParcelableArrayList(LIST_STATE_KEY2);
            assert tvState != null;
            tvArrayList.addAll(tvState);
            adapter = new TvPageAdapter(getContext());
            adapter.setTvList(tvState);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
                Intent intent = new Intent(getActivity(), DetailTvActivity.class);
                intent.putExtra(DetailTvActivity.EXTRA_TV, tvState.get(position));
                startActivity(intent);
            });
        } else {
            getTv(currentPage);
            setupOnScrollListener();
        }
    }

    private void setupOnScrollListener() {
        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingTv) {
                        getTv(currentPage + 1);
                    }
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE_KEY2, tvArrayList);
    }

    private void getTv(int page) {
        isFetchingTv = true;
        tvRepository.getTvPage(page, new OnGetPageTv() {
            @Override
            public void onSuccess(int page, ArrayList<Tv> tvs) {
                if (adapter == null) {
                    adapter = new TvPageAdapter(getContext());
                    adapter.setTvList(tvs);
                    tvArrayList.addAll(tvs);
                    recyclerView.setAdapter(adapter);
                    ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
                        Intent intent = new Intent(getActivity(), DetailTvActivity.class);
                        intent.putExtra(DetailTvActivity.EXTRA_TV, tvs.get(position));
                        startActivity(intent);
                    });
                } else {
                    adapter.appendTv(tvs);
                }
                currentPage = page;
                isFetchingTv = false;
            }

            @Override
            public void onError() {
                String toast_msg = getString(R.string.toastmsg);
                Toast.makeText(getActivity(), toast_msg, Toast.LENGTH_SHORT).show();
            }

        });
    }
}
