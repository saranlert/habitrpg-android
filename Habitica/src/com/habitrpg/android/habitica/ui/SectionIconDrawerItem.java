package com.habitrpg.android.habitica.ui;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.habitrpg.android.habitica.R;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.AbstractDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SectionIconDrawerItem extends AbstractDrawerItem<SectionIconDrawerItem, SectionIconDrawerItem.ViewHolder> implements Nameable<SectionIconDrawerItem>, Typefaceable<SectionIconDrawerItem> {
    protected ImageHolder icon;

    protected boolean iconTinted = false;
    private StringHolder name;
    private boolean divider = true;

    private ColorHolder textColor;

    protected ColorHolder iconColor;

    private Typeface typeface = null;

    public SectionIconDrawerItem withName(StringHolder name) {
        this.name = name;
        return this;
    }

    public SectionIconDrawerItem withName(String name) {
        this.name = new StringHolder(name);
        return this;
    }

    public SectionIconDrawerItem withName(@StringRes int nameRes) {
        this.name = new StringHolder(nameRes);
        return this;
    }

    public SectionIconDrawerItem withDivider(boolean divider) {
        this.divider = divider;
        return this;
    }

    public SectionIconDrawerItem withTextColor(int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public SectionIconDrawerItem withTextColorRes(int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    public SectionIconDrawerItem withIcon(ImageHolder icon) {
        this.icon = icon;
        return this;
    }

    public SectionIconDrawerItem withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public SectionIconDrawerItem withIcon(@DrawableRes int iconRes) {
        this.icon = new ImageHolder(iconRes);
        return this;
    }

    public SectionIconDrawerItem withIcon(IIcon iicon) {
        this.icon = new ImageHolder(iicon);

        return this;
    }

    public SectionIconDrawerItem withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public boolean hasDivider() {
        return divider;
    }

    public ColorHolder getTextColor() {
        return textColor;
    }

    public StringHolder getName() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_section;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_section_icon;
    }

    @Override
    public Typeface getTypeface() {
        return typeface;
    }

    public SectionIconDrawerItem withIconColor(@ColorInt int iconColor) {
        this.iconColor = ColorHolder.fromColor(iconColor);
        return this;
    }

    public SectionIconDrawerItem withIconColorRes(@ColorRes int iconColorRes) {
        this.iconColor = ColorHolder.fromColorRes(iconColorRes);
        return this;
    }

    public SectionIconDrawerItem withIconTintingEnabled(boolean iconTintingEnabled) {
        this.iconTinted = iconTintingEnabled;
        return this;
    }

    @Deprecated
    public SectionIconDrawerItem withIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
        return this;
    }

    public boolean isIconTinted() {
        return iconTinted;
    }

    public ImageHolder getIcon() {
        return icon;
    }

    public ColorHolder getIconColor() {
        return iconColor;
    }

    public int getIconColor(Context ctx) {
        return ColorHolder.color(getIconColor(), ctx, R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //define this item to be not clickable nor enabled
        viewHolder.view.setClickable(false);
        viewHolder.view.setEnabled(false);

        //define the text color
        viewHolder.name.setTextColor(ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_secondary_text, R.color.material_drawer_secondary_text));

        //set the text for the name
        StringHolder.applyTo(this.getName(), viewHolder.name);

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
        }

        //hide the divider if we do not need one
        if (this.hasDivider()) {
            viewHolder.divider.setVisibility(View.VISIBLE);
        } else {
            viewHolder.divider.setVisibility(View.GONE);
        }

        //set the color for the divider
        viewHolder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider));

        viewHolder.icon.setImageDrawable(getIcon().decideIcon(ctx, ctx.getResources().getColor(R.color.material_drawer_secondary_text), true, 0));

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private View divider;
        private TextView name;
        protected ImageView icon;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.divider = view.findViewById(R.id.material_drawer_divider);
            this.name = (TextView) view.findViewById(R.id.material_drawer_name);
            this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);
        }
    }
}