package com.lwkandroid.library.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * @description: 通用工具
 * @author: LWK
 * @date: 2021/6/2 9:28
 */
public final class Utils
{
    private Utils()
    {
        throw new UnsupportedOperationException("Can't instantiate this class !");
    }

    /**
     * 判断指定缓存路径是否可用，不可用时返回默认缓存地址
     *
     * @param context
     * @param dirPath
     * @return
     */
    public static String getAvailableCacheDirPath(Context context, String dirPath)
    {
        if (dirPath != null)
        {
            File dirFile = new File(dirPath);
            if (dirFile.exists() || dirFile.mkdirs())
            {
                return dirFile.getAbsolutePath();
            }
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            return context.getExternalCacheDir().getAbsolutePath();
        } else
        {
            return context.getCacheDir().getAbsolutePath();
        }
    }

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    public static File uri2File(Context context, Uri uri)
    {
        if (uri == null)
        {
            return null;
        }
        File file = uri2FileReal(context, uri);
        if (file != null)
        {
            return file;
        }
        return copyUri2Cache(context, uri);
    }

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    private static File uri2FileReal(Context context, Uri uri)
    {
        String authority = uri.getAuthority();
        String scheme = uri.getScheme();
        String path = uri.getPath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && path != null)
        {
            String[] externals = new String[]{"/external/", "/external_path/"};
            File file = null;
            for (String external : externals)
            {
                if (path.startsWith(external))
                {
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + path.replace(external, "/"));
                    if (file.exists())
                    {
                        Log.d("UriUtils", uri.toString() + " -> " + external);
                        return file;
                    }
                }
            }
            file = null;
            if (path.startsWith("/files_path/"))
            {
                file = new File(context.getFilesDir().getAbsolutePath()
                        + path.replace("/files_path/", "/"));
            } else if (path.startsWith("/cache_path/"))
            {
                file = new File(context.getCacheDir().getAbsolutePath()
                        + path.replace("/cache_path/", "/"));
            } else if (path.startsWith("/external_files_path/"))
            {
                file = new File(context.getExternalFilesDir(null).getAbsolutePath()
                        + path.replace("/external_files_path/", "/"));
            } else if (path.startsWith("/external_cache_path/"))
            {
                file = new File(context.getExternalCacheDir().getAbsolutePath()
                        + path.replace("/external_cache_path/", "/"));
            }
            if (file != null && file.exists())
            {
                Log.d("UriUtils", uri.toString() + " -> " + path);
                return file;
            }
        }
        if (ContentResolver.SCHEME_FILE.equals(scheme))
        {
            if (path != null)
            {
                return new File(path);
            }
            Log.d("UriUtils", uri.toString() + " parse failed. -> 0");
            return null;
        }// end 0
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, uri))
        {
            if ("com.android.externalstorage.documents".equals(authority))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type))
                {
                    return new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
                } else
                {
                    // Below logic is how External Storage provider build URI for documents
                    // http://stackoverflow.com/questions/28605278/android-5-sd-card-label
                    StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                    try
                    {
                        Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
                        Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
                        Method getUuid = storageVolumeClazz.getMethod("getUuid");
                        Method getState = storageVolumeClazz.getMethod("getState");
                        Method getPath = storageVolumeClazz.getMethod("getPath");
                        Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
                        Method isEmulated = storageVolumeClazz.getMethod("isEmulated");

                        Object result = getVolumeList.invoke(mStorageManager);

                        final int length = Array.getLength(result);
                        for (int i = 0; i < length; i++)
                        {
                            Object storageVolumeElement = Array.get(result, i);
                            //String uuid = (String) getUuid.invoke(storageVolumeElement);

                            final boolean mounted = Environment.MEDIA_MOUNTED.equals(getState.invoke(storageVolumeElement))
                                    || Environment.MEDIA_MOUNTED_READ_ONLY.equals(getState.invoke(storageVolumeElement));

                            //if the media is not mounted, we need not get the volume details
                            if (!mounted)
                            {
                                continue;
                            }

                            //Primary storage is already handled.
                            if ((Boolean) isPrimary.invoke(storageVolumeElement)
                                    && (Boolean) isEmulated.invoke(storageVolumeElement))
                            {
                                continue;
                            }

                            String uuid = (String) getUuid.invoke(storageVolumeElement);

                            if (uuid != null && uuid.equals(type))
                            {
                                return new File(getPath.invoke(storageVolumeElement) + "/" + split[1]);
                            }
                        }
                    } catch (Exception ex)
                    {
                        Log.d("UriUtils", uri.toString() + " parse failed. " + ex.toString() + " -> 1_0");
                    }
                }
                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_0");
                return null;
            }// end 1_0
            else if ("com.android.providers.downloads.documents".equals(authority))
            {
                String id = DocumentsContract.getDocumentId(uri);
                if (TextUtils.isEmpty(id))
                {
                    Log.d("UriUtils", uri.toString() + " parse failed(id is null). -> 1_1");
                    return null;
                }
                if (id.startsWith("raw:"))
                {
                    return new File(id.substring(4));
                } else if (id.startsWith("msf:"))
                {
                    id = id.split(":")[1];
                }

                long availableId = 0;
                try
                {
                    availableId = Long.parseLong(id);
                } catch (Exception e)
                {
                    return null;
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/all_downloads",
                        "content://downloads/my_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry)
                {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), availableId);
                    try
                    {
                        File file = getFileFromUri(context, contentUri, "1_1");
                        if (file != null)
                        {
                            return file;
                        }
                    } catch (Exception ignore)
                    {
                    }
                }

                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_1");
                return null;
            }// end 1_1
            else if ("com.android.providers.media.documents".equals(authority))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri;
                if ("image".equals(type))
                {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type))
                {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type))
                {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else
                {
                    Log.d("UriUtils", uri.toString() + " parse failed. -> 1_2");
                    return null;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getFileFromUri(context, contentUri, selection, selectionArgs, "1_2");
            }// end 1_2
            else if (ContentResolver.SCHEME_CONTENT.equals(scheme))
            {
                return getFileFromUri(context, uri, "1_3");
            }// end 1_3
            else
            {
                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_4");
                return null;
            }// end 1_4
        }// end 1
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme))
        {
            return getFileFromUri(context, uri, "2");
        }// end 2
        else
        {
            Log.d("UriUtils", uri.toString() + " parse failed. -> 3");
            return null;
        }// end 3
    }

    private static File getFileFromUri(Context context, final Uri uri, final String code)
    {
        return getFileFromUri(context, uri, null, null, code);
    }

    private static File getFileFromUri(Context context,
                                       final Uri uri,
                                       final String selection,
                                       final String[] selectionArgs,
                                       final String code)
    {
        if ("com.google.android.apps.photos.content".equals(uri.getAuthority()))
        {
            if (!TextUtils.isEmpty(uri.getLastPathSegment()))
            {
                return new File(uri.getLastPathSegment());
            }
        } else if ("com.tencent.mtt.fileprovider".equals(uri.getAuthority()))
        {
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path))
            {
                File fileDir = Environment.getExternalStorageDirectory();
                return new File(fileDir, path.substring("/QQBrowser".length(), path.length()));
            }
        } else if ("com.huawei.hidisk.fileprovider".equals(uri.getAuthority()))
        {
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path))
            {
                return new File(path.replace("/root", ""));
            }
        }

        final Cursor cursor = context.getContentResolver().query(
                uri, new String[]{"_data"}, selection, selectionArgs, null);
        if (cursor == null)
        {
            Log.d("UriUtils", uri.toString() + " parse failed(cursor is null). -> " + code);
            return null;
        }
        try
        {
            if (cursor.moveToFirst())
            {
                final int columnIndex = cursor.getColumnIndex("_data");
                if (columnIndex > -1)
                {
                    return new File(cursor.getString(columnIndex));
                } else
                {
                    Log.d("UriUtils", uri.toString() + " parse failed(columnIndex: " + columnIndex + " is wrong). -> " + code);
                    return null;
                }
            } else
            {
                Log.d("UriUtils", uri.toString() + " parse failed(moveToFirst return false). -> " + code);
                return null;
            }
        } catch (Exception e)
        {
            Log.d("UriUtils", uri.toString() + " parse failed. -> " + code);
            return null;
        } finally
        {
            cursor.close();
        }
    }

    private static File copyUri2Cache(Context context, Uri uri)
    {
        InputStream is = null;
        try
        {
            is = context.getContentResolver().openInputStream(uri);
            File file = new File(context.getCacheDir(), "" + System.currentTimeMillis());
            writeFileFromIS(file, is, false);
            return file;
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将输入流写入文件
     *
     * @param file   文件
     * @param is     输入流
     * @param append 是否追加在文件末
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    private static boolean writeFileFromIS(final File file,
                                           final InputStream is,
                                           final boolean append)
    {
        if (!createOrExistsFile(file) || is == null)
        {
            return false;
        }
        OutputStream os = null;
        try
        {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte[] data = new byte[4096];
            int len;
            while ((len = is.read(data, 0, 4096)) != -1)
            {
                os.write(data, 0, len);
            }
            os.flush();
            return true;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        } finally
        {

            closeIO(is, os);
        }
    }

    private static boolean createOrExistsFile(final File file)
    {
        if (file == null)
        {
            return false;
        }
        if (file.exists())
        {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile()))
        {
            return false;
        }
        try
        {
            return file.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file)
    {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 关闭 IO
     *
     * @param closeables closeables
     */
    private static void closeIO(final Closeable... closeables)
    {
        if (closeables == null)
        {
            return;
        }
        for (Closeable closeable : closeables)
        {
            if (closeable != null)
            {
                try
                {
                    closeable.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}