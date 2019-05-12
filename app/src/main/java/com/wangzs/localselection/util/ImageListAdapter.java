package com.wangzs.localselection.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wangzs.localselection.OnUpdateSelectStateListener;
import com.wangzs.localselection.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;

    private Context context;

    private List<LocalMedia> images = new ArrayList<LocalMedia>();
    private List<String> selectImages = new ArrayList<String>();

    public ImageListAdapter(Context context) {
        this.context = context;
    }

    public void bindImages(List<LocalMedia> images) {
        this.images = images;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ViewHolder contentHolder = (ViewHolder) holder;
        final LocalMedia image = images.get(position);

        Glide.with(context)
                .load(new File(image.getPath()))
                .centerCrop()
                .thumbnail(0.5f)
                .placeholder(R.drawable.pictures_no)
                .error(R.drawable.pictures_no)
                .dontAnimate()
                .into(contentHolder.picture);
        selectImage(contentHolder, isSelected(image));
        contentHolder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCheckboxState(contentHolder, image);
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    private void changeCheckboxState(ViewHolder contentHolder, LocalMedia image) {
        boolean maxCount = onShowSelectFiles.isMaxCount();
        if (maxCount) {
            if (selectImages.contains(image.getPath())) {
                selectImages.remove(image.getPath());
                contentHolder.check.setImageResource(R.drawable.choose_icon);
                onShowSelectFiles.setOnShowSelectSize(false, image.getPath(),image.getFileSize());
            } else {
               Toast.makeText(context,"最多可选择" + onShowSelectFiles.getMaxCount() + "个文件",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        boolean isChecked = contentHolder.check.isSelected();
        if (isChecked) {
            if (selectImages.contains(image.getPath())) {
                selectImages.remove(image.getPath());
                onShowSelectFiles.setOnShowSelectSize(false, image.getPath(),image.getFileSize());
            }
        } else {
            selectImages.add(image.getPath());
            onShowSelectFiles.setOnShowSelectSize(true, image.getPath(),image.getFileSize());
        }
        selectImage(contentHolder, !isChecked);
    }

    public List<LocalMedia> getImages() {
        return images;
    }

    public boolean isSelected(LocalMedia image) {
        if (selectImages.contains(image.getPath())) {
            return true;
        }
        return false;
    }

    public void selectImage(ViewHolder holder, boolean isChecked) {
        holder.check.setSelected(isChecked);
        if (isChecked) {
            holder.check.setImageResource(R.drawable.choose_icon_on);
            holder.picture.setColorFilter(Color.parseColor("#77000000"), PorterDuff.Mode.SRC_ATOP);

        } else {
            holder.check.setImageResource(R.drawable.choose_icon);
            holder.picture.setColorFilter(null);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        ImageView check;

        View contentView;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            picture = (ImageView) itemView.findViewById(R.id.id_item_image);
            check = (ImageView) itemView.findViewById(R.id.id_item_select);
        }

    }


    private OnUpdateSelectStateListener onShowSelectFiles;

    public void setOnShowSelectFilesSizeClickListener(OnUpdateSelectStateListener onOnShowSelectImageSizeClickListener) {
        this.onShowSelectFiles = onOnShowSelectImageSizeClickListener;

    }
}
