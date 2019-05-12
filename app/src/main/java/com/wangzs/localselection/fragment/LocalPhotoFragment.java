package com.wangzs.localselection.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.wangzs.localselection.OnUpdateSelectStateListener;
import com.wangzs.localselection.R;
import com.wangzs.localselection.util.FileUtil;
import com.wangzs.localselection.util.FolderWindow;
import com.wangzs.localselection.util.GridSpacingItemDecoration;
import com.wangzs.localselection.util.ImageFolderAdapter;
import com.wangzs.localselection.util.ImageListAdapter;
import com.wangzs.localselection.util.LocalMedia;
import com.wangzs.localselection.util.LocalMediaFolder;
import com.wangzs.localselection.util.LocalMediaLoader;

import java.util.List;


public class LocalPhotoFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageListAdapter imageAdapter;
    private LinearLayout folderLayout;
    private TextView folderName;
    private FolderWindow folderWindow;
    private RelativeLayout relativeLayout;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo_local, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        folderWindow = new FolderWindow(getActivity());
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.folder_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, FileUtil.dip2px(2), false));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        folderLayout = (LinearLayout) view.findViewById(R.id.folder_layout);
        folderName = (TextView) view.findViewById(R.id.folder_name);
        imageAdapter = new ImageListAdapter(getActivity());
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnShowSelectFilesSizeClickListener((OnUpdateSelectStateListener) getActivity());
        new LocalMediaLoader(getActivity(), LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {

            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                folderWindow.bindFolder(folders);
                imageAdapter.bindImages(folders.get(0).getImages());

            }
        });
        folderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folderWindow.isShowing()) {
                    folderWindow.dismiss();
                } else {
                    folderWindow.showAsDropDown(relativeLayout);
                }
            }
        });

        folderWindow.setOnItemClickListener(new ImageFolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, List<LocalMedia> images) {
                folderWindow.dismiss();
                imageAdapter.bindImages(images);
                folderName.setText(name);
            }
        });
    }
}
