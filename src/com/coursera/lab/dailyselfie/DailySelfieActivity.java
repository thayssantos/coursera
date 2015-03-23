package com.coursera.lab.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.coursera.lab.dailyselfie.ThumbnailsListFragment.ListSelectionListener;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DailySelfieActivity extends Activity {

	public static final int UNSELECTED = -1;
	private FragmentManager mFragmentManager = null;
	private ListSelectionListener mSelectionListener = null;
	private ThumbnailsListFragment mListFragment = null;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private String mCurrentPhotoPath;
	private String PREFIX = "COURSERA_JPEG_";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_selfie);
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case R.id.action_camera:
			openCamera();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void openCamera() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			File photoFile = null;

			try {
				photoFile = createImageFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
				addPicToGallery();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_IMAGE_CAPTURE) {
			if (resultCode == RESULT_OK) {
				mListFragment.add(mCurrentPhotoPath);
			} else if (resultCode == RESULT_CANCELED) {
				String path = mCurrentPhotoPath.replace("file:", "");
				File photoFile = new File(path);

				if (photoFile.exists()) {
					photoFile.delete();
				}
			}
		}
	}

	public ListSelectionListener getListSelectionListener() {
		return mSelectionListener;
	}

	private void initialize() {
		mListFragment = new ThumbnailsListFragment();
		mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.fragment_container, mListFragment);
		fragmentTransaction.commit();

		mSelectionListener = new ListSelectionListener() {
			@Override
			public void onListSelection(int index) {

				displayPhotoFragment(index);
			}
		};
	}

	private void addPicToGallery() {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = PREFIX + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		if (storageDir != null) {
			if (!storageDir.mkdirs()) {
				if (!storageDir.exists()) {
					return null;
				}
			}
		}
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}
	
	private void displayPhotoFragment(int index) {
		Log.d("debug", "displayPhotoFragment");

		String photoPath = ((ListItem) mListFragment.getListAdapter().getItem(
				index)).thumbnailPath;

		PhotoFragment photoFragment = new PhotoFragment(photoPath);
		FragmentTransaction fragmentTransaction = mFragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.fragment_container, photoFragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

		mFragmentManager.executePendingTransactions();
	}
}
