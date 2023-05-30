package com.origami.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author origami
 * @date 2020/12/18 0018
 * @description:
 **/
public class ProviderImageUtils {

    public static class FilePath implements Parcelable {
        public String path;
        public Uri uri;

        public FilePath() {}

        protected FilePath(Parcel in) {
            path = in.readString();
            uri = in.readParcelable(Uri.class.getClassLoader());
        }

        public static final Creator<FilePath> CREATOR = new Creator<FilePath>() {
            @Override
            public FilePath createFromParcel(Parcel in) {
                return new FilePath(in);
            }

            @Override
            public FilePath[] newArray(int size) {
                return new FilePath[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(path);
            dest.writeParcelable(uri, flags);
        }
    }

    public static class ResultData{
        public List<String> keys = new ArrayList<>();
        public Map<String, List<FilePath>> dates;
    }


    public static int ONE_PAGE_NUM = 20;

    public static ResultData getImagesPathList(Context context, String[] supportType){
        String selection = null;
        if(supportType != null && supportType.length != 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < supportType.length; i++) {
                builder.append(MediaStore.Images.Media.MIME_TYPE)
                        .append("=? ");
                if(i != supportType.length - 1){ builder.append("or "); }
            }
            selection = builder.toString();
        }
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                supportType,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        );
        ResultData resultData = new ResultData();
        Map<String, List<FilePath>> map = new HashMap<>();
        List<FilePath> list = new ArrayList<>();
        map.put("",list);
        resultData.keys.add("");
        while (cursor.moveToNext()){
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int idIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
            if(columnIndex == -1 || idIndex == -1)  continue;
            FilePath filePath = new FilePath();
            filePath.path = cursor.getString(columnIndex);
            filePath.uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(idIndex));
            list.add(filePath);
            String name = Objects.requireNonNull(new File(filePath.path).getParentFile()).getName();
            if(!map.containsKey(name)){
                List<FilePath> listChild = new ArrayList<>();
                resultData.keys.add(name);
                map.put(name, listChild);
            }
            Objects.requireNonNull(map.get(name)).add(filePath);
        }
        cursor.close();
        resultData.dates = map;
        return resultData;
    }


    public static List<FilePath> getImagesPathByPage(Context context, int page){
        int start = (page - 1) * ONE_PAGE_NUM;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC LIMIT " + start + "," + ONE_PAGE_NUM
        );
        List<FilePath> list = new ArrayList<>();
        while (cursor.moveToNext()){
            FilePath filePath = new FilePath();
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int id = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            filePath.path = cursor.getString(columnIndex);
            filePath.uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            list.add(filePath);
        }
        cursor.close();
        return list;
    }

}
