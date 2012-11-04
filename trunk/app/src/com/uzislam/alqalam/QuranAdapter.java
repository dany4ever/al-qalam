/*
 * Copyright 2010 (c) Al-Qalam Project
 *
 * This file is part of Al-Qalam (com.uzislam.alqalam) package.
 *
 * Al-Qalam is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Al-Qalam is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.uzislam.alqalam;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuranAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<QuranIconifiedText> mItems = new ArrayList<QuranIconifiedText>();

    public QuranAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return mItems.get(arg0).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.quran_row, null);

            vHolder = new ViewHolder();
            vHolder.SurahTitle = (TextView) convertView.findViewById(R.id.QuranRow_Title);
            vHolder.SurahOrder = (TextView) convertView.findViewById(R.id.QuranRow_Order);
            vHolder.SurahInfo = (TextView) convertView.findViewById(R.id.QuranRow_Info);
            vHolder.SurahGroup = (LinearLayout) convertView.findViewById(R.id.QuranRow_Group);

            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }

        vHolder.SurahTitle.setText(mItems.get(position).getSurahTitle());
        vHolder.SurahOrder.setText(mItems.get(position).getSurahOrder() + "");
        vHolder.SurahInfo.setText(mItems.get(position).getSurahInfo());
        vHolder.SurahGroup.setBackgroundColor((position % 2 == 0)
                ? Color.WHITE
                : mContext.getResources().getColor(R.color.light_blue));

        return convertView;
    }

    private static class ViewHolder {
        TextView SurahTitle;
        TextView SurahOrder;
        TextView SurahInfo;
        LinearLayout SurahGroup;
    }

    public void addItem(QuranIconifiedText it) {
        mItems.add(it);
    }

    public void clear() {
        mItems.clear();
    }
}