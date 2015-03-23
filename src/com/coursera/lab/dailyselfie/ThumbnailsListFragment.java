package com.coursera.lab.dailyselfie;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;

public class ThumbnailsListFragment extends ListFragment {

	Activity mContext;
	ImageListAdapter mListAdapter = null;
	ListSelectionListener mSelectionListener = null;

	public interface ListSelectionListener {
		public void onListSelection(int index);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mContext = activity;

			DailySelfieActivity dailySelfieActivity = (DailySelfieActivity) activity;

			mSelectionListener = dailySelfieActivity.getListSelectionListener();

		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnArticleSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		mListAdapter = new ImageListAdapter(mContext);

		setListAdapter(mListAdapter);

		loadSavedImages();
	}

	private void loadSavedImages() {
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File[] contents = storageDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toUpperCase().startsWith("COURSERA_JPEG_");
			}
		});

		if (contents != null) {
			if (contents.length > 0) {
				for (File file : contents) {
					String path = "file:" + file.getAbsolutePath();

					add(path);
				}
			}
		}
	}

	public void add(String path) {
		ListItem listItem = new ListItem();
		listItem.thumbnailPath = path;
		listItem.title = path;

		mListAdapter.add(listItem);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {

		mSelectionListener.onListSelection(position);
	}

}