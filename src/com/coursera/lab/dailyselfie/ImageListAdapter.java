package com.coursera.lab.dailyselfie;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageListAdapter extends BaseAdapter {

	private Context mContext;
	private static LayoutInflater mInflater = null;
	private List<ListItem> mItems = null;

//	public static class ListItem {
//		public String thumbnailPath;
//		public String title;
//
//		public ListItem() {
//			thumbnailPath = "";
//			title = "";
//		}
//	}

	public ImageListAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mItems = new ArrayList<ListItem>();
	}

	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int position) {
		return mItems.get(position);
	}

	public long getItemId(int position) {
		return mItems.indexOf(getItem(position));
	}

	public void add(ListItem item) {

		mItems.add(item);

		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.thumbnails_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.thumbnail = (ImageView) convertView
					.findViewById(R.id.thumbnail);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ListItem listItem = (ListItem) getItem(position);

		if (listItem.thumbnailPath != null) {
			Uri uri = Uri.parse(listItem.thumbnailPath);

			viewHolder.thumbnail.setImageURI(uri);
		}

		String name = listItem.title.substring(listItem.title.indexOf("JPEG"), listItem.title.length() - 4);
		viewHolder.title.setText(name);

		return convertView;
	}

	private class ViewHolder {
		ImageView thumbnail;
		TextView title;
	}
}
