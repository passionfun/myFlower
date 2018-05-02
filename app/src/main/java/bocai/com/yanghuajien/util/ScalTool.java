package bocai.com.yanghuajien.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

/**
 * 压缩图片
 * Created by shc on 2018/3/20.
 */

public class ScalTool {
    /**
     * 压缩图片
     */
    public static File scal(String path) {
        // String path = fileUri.getPath();
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        Log.i("ao", "filesize=" + fileSize);
        final long fileMaxSize = 200 * 1024;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;
        if (fileSize >= fileMaxSize) {
            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(path, options);

            String orientation = "" + getExifOrientation(path);// 获取旋转角度
            int angle = 0;
            if (orientation != null && !"".equals(orientation)) {
                angle = Integer.parseInt(orientation);
            }
            if (angle != 0) {
                // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
                Matrix m = new Matrix();
                m.setRotate(angle); // 旋转angle度
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);// 重新生成图片
            }

            outputFile = new File(createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.i("ds", "***" + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(createImageFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }
        }else {
            outputFile = new File(path);
            FileOutputStream fos = null;
            try {
                bitmap = BitmapFactory.decodeFile(path);
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.i("ds", "***" + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(createImageFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }
        }

        return outputFile;
    }


    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }




    public static Uri createImageFile() {
        // Create an image file name
        String imageFileName = UUID.randomUUID().toString();
        File storageDir = getGalleryCacheDir();
        File image = null;
        try {
            image = File.createTempFile(imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            );

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }


    /**
     * 获取图片的旋转角度
     *
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }




    /**
     * 获取缓存的地址
     *
     */
    public static File getGalleryCacheDir() {
        File cacheDir = new File(Environment.getExternalStorageDirectory().getPath() +
                File.separator + "HengQiang" + File.separator + "Gallery");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

}
