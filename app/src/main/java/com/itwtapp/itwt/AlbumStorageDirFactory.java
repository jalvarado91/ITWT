package com.itwtapp.itwt;

import java.io.File;

abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}
