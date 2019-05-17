package com.anglll.aflow.ui.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyController;
import com.anglll.aflow.R;
import com.anglll.aflow.ui.epoxy.models.MenuItemModel_;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuDialog extends BottomSheetDialogFragment {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private MenuClickCallback callback;
    private Menu menu;
    private String title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_menu, container, false);
        ButterKnife.bind(this, view);
        MenuController controller = new MenuController(callback);
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(controller.getAdapter());
        controller.setMenu(this, menu);
        mTitle.setText(title);
        return view;
    }


    public MenuDialog setMenu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public MenuDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MenuDialog setOnMenuSelectedCallback(MenuClickCallback callback) {
        this.callback = callback;
        return this;
    }

    public static class MenuController extends EpoxyController {

        private final MenuClickCallback callback;
        private Menu menu;
        private DialogFragment dialog;

        public MenuController(MenuClickCallback callback) {
            this.callback = callback;
        }

        public void setMenu(DialogFragment dialog, Menu menu) {
            this.menu = menu;
            this.dialog = dialog;
            requestModelBuild();
        }

        @Override
        protected void buildModels() {
            if (menu == null)
                return;
            for (int i = 0; i < menu.size(); i++) {
                add(new MenuItemModel_()
                        .dialog(dialog)
                        .callback(callback)
                        .menuItem(menu.getItem(i))
                        .id(menu.getItem(i).getItemId()));
            }
        }
    }

    public interface MenuClickCallback {
        void onMenuItemClick(MenuItem menuItem);
    }
}
