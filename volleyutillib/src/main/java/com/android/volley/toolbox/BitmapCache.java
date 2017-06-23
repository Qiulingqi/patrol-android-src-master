/**
 * 
 */
package com.android.volley.toolbox;

/**
 * @author jenus
 *
 */
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache {
	private BitmapListLruCache mLruCache;

	public BitmapCache(int paramInt) {
		this.mLruCache = new BitmapListLruCache(paramInt);
	}

	//Note: same key, save to cache according to size ascend by order
	public Bitmap get(String key, int width, int height) {
		List<BitmapCacheEntry> arrBitmapCacheEntry = (List<BitmapCacheEntry>) this.mLruCache
				.get(key);

		if (arrBitmapCacheEntry == null) {
			return null;
		}

		int i = 1;
		int j;
		if (width != 0) { //explicit with & height
			j = 1;
		} else {
			i = 0;
			j = 0;
		}

		BitmapCacheEntry tmpBitmapCacheEntry = null;
		int length = arrBitmapCacheEntry.size();
		BitmapCacheEntry bBitmapCacheEntry = null;
		for (int index = 0; index < length; index++) {
			bBitmapCacheEntry = (BitmapCacheEntry) arrBitmapCacheEntry
					.get(index);
			if ((bBitmapCacheEntry.requestedWidth == width)
					&& (bBitmapCacheEntry.requestedHeight == height)) {
				return bBitmapCacheEntry.bitmap;
			}

			if ((tmpBitmapCacheEntry == null)
					&& ((j == 0) || (bBitmapCacheEntry.bitmap.getWidth() >= width))
					&& ((i == 0) || (bBitmapCacheEntry.bitmap.getHeight() >= height))) {
				tmpBitmapCacheEntry = bBitmapCacheEntry;
			}
		}

		if (tmpBitmapCacheEntry == null) { //last one
			tmpBitmapCacheEntry = (BitmapCacheEntry) arrBitmapCacheEntry
					.get(length - 1);
		}
		return tmpBitmapCacheEntry.bitmap;

	}

	public void put(String paramString, int width, int height,
			Bitmap paramBitmap) {
		ArrayList<BitmapCacheEntry> arrBitmapCacheEntry = (ArrayList<BitmapCacheEntry>) this.mLruCache
				.get(paramString);
		if (arrBitmapCacheEntry == null) {
			arrBitmapCacheEntry = new ArrayList<BitmapCacheEntry>();
		}

		int length = arrBitmapCacheEntry.size();
		for (int i = 0; i < length; i++) {
			int cachedWidth = ((BitmapCacheEntry) arrBitmapCacheEntry.get(i)).bitmap
					.getWidth();
			int savingWidth = paramBitmap.getWidth();
			if (cachedWidth < savingWidth) {
				continue;
			} else if (cachedWidth == savingWidth) {//remove cached on, add new one
				arrBitmapCacheEntry.remove(i);
				arrBitmapCacheEntry.add(i, new BitmapCacheEntry(paramBitmap,
						width, height));
			} else {
				arrBitmapCacheEntry.add(i + 1, new BitmapCacheEntry(
						paramBitmap, width, height));
			}

		}
		this.mLruCache.put(paramString, arrBitmapCacheEntry);
	}

	public static class BitmapCacheEntry {
		public Bitmap bitmap;
		public int requestedHeight;
		public int requestedWidth;

		public BitmapCacheEntry(Bitmap paramBitmap, int paramInt1, int paramInt2) {
			this.bitmap = paramBitmap;
			this.requestedWidth = paramInt1;
			this.requestedHeight = paramInt2;
		}
	}

	private class BitmapListLruCache extends
			LruCache<String, ArrayList<BitmapCacheEntry>> {
		public BitmapListLruCache(int maxSize) {
			super(maxSize);
		}

		protected int sizeOf(String key,
				ArrayList<BitmapCacheEntry> arrBitmapCacheEntry) {
			int occupiedSize = 0;
			int j = arrBitmapCacheEntry.size();
			for (int index = 0; index < j; index++) {
				Bitmap bitmap = ((BitmapCacheEntry) arrBitmapCacheEntry
						.get(index)).bitmap;
				occupiedSize += bitmap.getRowBytes() * bitmap.getHeight();
			}
			return occupiedSize;
		}
	}

	@Override
	public Bitmap getBitmap(String url) {
		return get(url, 0, 0);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap.getWidth(), bitmap.getHeight(), bitmap);
	}
}