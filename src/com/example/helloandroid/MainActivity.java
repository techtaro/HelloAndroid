package com.example.helloandroid;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.Swatch;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Cursor c = getContentResolver().query( //
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 画像ファイルを外部ストレージから検索
				null, null, null, null);
		c.moveToFirst();

		final int dataIndex = c.getColumnIndex(Images.Media.DATA);
		final String data = c.getString(dataIndex);

		Bitmap bm = BitmapFactory.decodeFile(data);
		ImageView view = (ImageView) findViewById(R.id.imageView1);
		view.setImageBitmap(bm);

		ListView lv = (ListView) findViewById(R.id.listView1);
		SwatchAdapter adapter = new SwatchAdapter(this);

		lv.setAdapter(adapter);

		Palette p = Palette.generate(bm);
		System.out.println(p);
		adapter.add("dark muted", p.getDarkMutedSwatch());
		adapter.add("dark vibrant", p.getDarkVibrantSwatch());
		adapter.add("light muted", p.getLightMutedSwatch());
		adapter.add("light vibrant", p.getLightVibrantSwatch());
		adapter.add("muted", p.getMutedSwatch());
		adapter.add("vibrant", p.getVibrantSwatch());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class SwatchAdapter extends ArrayAdapter<Swatch> {

		public SwatchAdapter(Context context) {
			this(context, 0);
		}

		public SwatchAdapter(Context context, int resource) {
			super(context, resource);
		}

		ArrayList<String> colorNames = new ArrayList<String>();

		public void add(String colorName, Swatch object) {
			if (object != null) {
				super.add(object);
				colorNames.add(colorName);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout v = (LinearLayout) convertView;
			TextView title, body, colorName;

			if (v == null) {
				v = new LinearLayout(getContext());
				v.setOrientation(LinearLayout.VERTICAL);
				v.addView(colorName = new TextView(getContext()));
				v.addView(title = new TextView(getContext()));
				v.addView(body = new TextView(getContext()));
				
				colorName.setTextSize(20);
				title.setTextSize(20);
				body.setTextSize(10);
			} else {
				colorName = (TextView) v.getChildAt(0);
				title = (TextView) v.getChildAt(1);
				body = (TextView) v.getChildAt(2);
			}

			Swatch item = getItem(position);

			v.setBackgroundColor(item.getRgb());
			colorName.setText("colorName: " + colorNames.get(position));

			title.setText("title: #"
					+ Integer.toHexString(item.getTitleTextColor()));
			title.setTextColor(item.getTitleTextColor());
			body.setText("body: #"
					+ Integer.toHexString(item.getBodyTextColor()));
			body.setTextColor(item.getBodyTextColor());

			return v;
		}

	}
}
