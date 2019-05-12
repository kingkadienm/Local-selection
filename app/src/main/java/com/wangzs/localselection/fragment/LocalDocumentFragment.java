package com.wangzs.localselection.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wangzs.localselection.OnUpdateSelectStateListener;
import com.wangzs.localselection.R;
import com.wangzs.localselection.adapter.LocalDocAdapter;
import com.wangzs.localselection.bean.FileInfo;
import com.wangzs.localselection.util.LocalFileLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 2017/11/2.
 */

public class LocalDocumentFragment extends Fragment {
    private RecyclerView docRecycler;
    private LinearLayout progressDialog;
    private LinearLayout empty_view;
    private LocalDocAdapter docAdapter;
    private List<FileInfo> fileInfos = new ArrayList<>();
    private List<MultiItemEntity> skyPhotoList;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_document_local, container, false);
        fileInfos.clear();
        progressDialog = (LinearLayout) view.findViewById(R.id.load_local_file_pro_view);
        empty_view = (LinearLayout)view. findViewById(R.id.load_local_file_empty_view);

        docRecycler = (RecyclerView) view.findViewById(R.id.document_recycler);
        docRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        if (skyPhotoList == null) {
            skyPhotoList = new ArrayList<>();
        }

        docAdapter = new LocalDocAdapter(getContext(), skyPhotoList, true);
        docAdapter.bindToRecyclerView(docRecycler);
        docAdapter.setOnOnShowSelectFilesSizeClickListener((OnUpdateSelectStateListener) getActivity());

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findView();
        new LocalFileLoader(getContext(), new LocalFileLoader.IReadCallBack() {
            @Override
            public void complete(List<MultiItemEntity> map) {
                if (progressDialog != null)
                    progressDialog.setVisibility(View.GONE);
                docAdapter.setNewData(map);
            }

            @Override
            public void fail() {
                if (progressDialog != null)
                    progressDialog.setVisibility(View.GONE);
                if (empty_view != null)
                    empty_view.setVisibility(View.VISIBLE);
            }
        });

    }


    public void findView() {

    }


}
