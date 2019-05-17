package com.anglll.aflow.ui.epoxy.models;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseEpoxyHolder;
import com.anglll.aflow.ui.dialog.MenuDialog;

import butterknife.BindView;
import butterknife.OnClick;

@EpoxyModelClass(layout = R.layout.model_menu_item)
public abstract class MenuItemModel extends EpoxyModelWithHolder<MenuItemModel.MenuViewHolder> {
    @EpoxyAttribute
    MenuItem menuItem;
    @EpoxyAttribute
    MenuDialog.MenuClickCallback callback;
    @EpoxyAttribute
    DialogFragment dialog;

    @Override
    public void bind(@NonNull MenuViewHolder holder) {
        holder.bindData(menuItem);
    }

    public class MenuViewHolder extends BaseEpoxyHolder<MenuItem> {
        @BindView(R.id.menu_icon)
        ImageView mMenuIcon;
        @BindView(R.id.menu_text)
        TextView mMenuText;
        private MenuItem mMenuItem;


        @OnClick(R.id.item_layout)
        void onMenuClick() {
            if (callback != null) {
                dialog.dismiss();
                callback.onMenuItemClick(mMenuItem);
            }
        }

        @Override
        protected void bindData(MenuItem data) {
            this.mMenuItem = data;
            mMenuText.setText(data.getTitle());
            mMenuIcon.setImageDrawable(data.getIcon());
        }
    }
}
