package com.wangzs.localselection.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wangzs.localselection.OnUpdateSelectStateListener;
import com.wangzs.localselection.R;
import com.wangzs.localselection.bean.FileInfo;
import com.wangzs.localselection.bean.SubItem;
import com.wangzs.localselection.util.FileUtil;

import java.util.ArrayList;
import java.util.List;


public class LocalDocAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int HEADER = 1;
    public static final int CONTENT = 2;
    private Context mCtx;

    public LocalDocAdapter(Context context, @Nullable List<MultiItemEntity> data, boolean showCheckBox) {
        super(data);
        this.mCtx = context;
        this.showCheckBox = showCheckBox;
        addItemType(HEADER, R.layout.item_head);
        addItemType(CONTENT, R.layout.select_local_item);

    }

    private boolean showCheckBox;

    public void showCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
        if (!showCheckBox) {
            for (MultiItemEntity section : getData()) {
                if (section.getItemType() == HEADER) continue;
                if (section.getItemType() == CONTENT) {
                    FileInfo t = (FileInfo) section;
                    if (t.isCheck()) {
                        t.setCheck(false);
                    }
                }
            }
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case HEADER:
                final SubItem subItem = (SubItem) item;
                helper.setText(R.id.tv_title, subItem.title)
                        .setImageResource(R.id.expanded_menu, subItem.isExpanded() ? R.drawable.ic_arrow_drop_down_grey_700_24dp : R.drawable.ic_arrow_drop_up_grey_700_24dp);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (subItem.isExpanded()) {
                            collapse(pos, false);
                        } else {
                            expand(pos, false);
                        }

                    }
                });
                break;
            case CONTENT:
                final FileInfo fileInfos = (FileInfo) item;

                helper.getView(R.id.down_item_title);
                helper.setText(R.id.down_item_title, fileInfos.getFileName())
                        .setText(R.id.down_item_size, FileUtil.getFormatSize(fileInfos.getFileSize()))
                        .setImageResource(R.id.down_item_image, fileInfos.getFileImage())
                        .setVisible(R.id.isCheck,showCheckBox);
                selectImage(helper, fileInfos.isCheck());

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeCheckboxState(helper, fileInfos);
                    }
                });
                break;
            default:
                break;
        }


    }

    private List<String> seleteFile = new ArrayList<>();

    private void changeCheckboxState(BaseViewHolder contentHolder, FileInfo fileInfos) {
        boolean maxCount = onShowSelectFiles.isMaxCount();
        if (maxCount) {
            if (seleteFile.contains(fileInfos.getFilePath())) {
                seleteFile.remove(fileInfos.getFilePath());
                fileInfos.setCheck(false);
                contentHolder.getView(R.id.isCheck).setBackgroundResource(R.drawable.choose_icon);
                onShowSelectFiles.setOnShowSelectSize(false, fileInfos.getFilePath(), fileInfos.getFileSize());
            } else {
                Toast.makeText( mContext,"最多可选择" + onShowSelectFiles.getMaxCount() + "个文件",Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (seleteFile.contains(fileInfos.getFilePath())) {
            seleteFile.remove(fileInfos.getFilePath());
            onShowSelectFiles.setOnShowSelectSize(false, fileInfos.getFilePath(), fileInfos.getFileSize());
            fileInfos.setCheck(false);
            selectImage(contentHolder, false);
        } else {
            seleteFile.add(fileInfos.getFilePath());
            onShowSelectFiles.setOnShowSelectSize(true, fileInfos.getFilePath(), fileInfos.getFileSize());
            fileInfos.setCheck(true);
            selectImage(contentHolder, true);
        }
    }


    public void selectImage(BaseViewHolder holder, boolean isChecked) {
        holder.getView(R.id.isCheck).setSelected(isChecked);
        if (isChecked) {
            holder.getView(R.id.isCheck).setBackgroundResource(R.drawable.choose_icon_on);

        } else {
            holder.getView(R.id.isCheck).setBackgroundResource(R.drawable.choose_icon);
        }

    }

    private OnUpdateSelectStateListener onShowSelectFiles;

    public void setOnOnShowSelectFilesSizeClickListener(OnUpdateSelectStateListener onOnShowSelectImageSizeClickListener) {
        this.onShowSelectFiles = onOnShowSelectImageSizeClickListener;
    }


}