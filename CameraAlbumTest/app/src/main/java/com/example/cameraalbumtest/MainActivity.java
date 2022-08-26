package com.example.cameraalbumtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;

    public static final int CHOOSE_PHOTO = 2;

    private ImageView picture;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button takePhoto = (Button) findViewById(R.id.take_photo);
        Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        picture = (ImageView) findViewById(R.id.picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* 创建file对象，用于存储拍照后的图片
                   将图片存储在应用关联缓存目录下
                   应用关联缓存目录：指SD卡中专门用于存放当前应用缓存数据的位置，调用getExternalCacheDir()即可获取
                   因为从安卓6.0开始，读写SD卡被列为了危险权限，如果将图片存放在SD卡中的任何其他目录，都需要进行运行时
                   权限处理才行，而使用应用关联目录即可跳过这一步
                 */
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                /*
                    exists():测试由这个抽象路径名表示的文件或目录是否存在。
                    delete():删除由这个抽象路径名表示的文件或目录。
                    createNewFile():当且仅当具有此名称的文件还不存在时，原子地创建一个新的、由该抽象路径名命名的空文件。
                 */
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                    如果运行设备的系统版本低于android7.0，就调用Uri的fromFile方法，将File对象转换成一个封装过的
                    Uri对象
                    如果运行设备的系统版本高于等于android7.0，那就调用FileProvider.getUriForFile方法：
                    参数1：传入Context对象
                    参数2：任意的唯一的字符串
                    参数3：刚刚创建的需要转换成Uri对象的File对象
                    因为从android7.0开始，直接使用本地真实路径的Uri被认为是不安全的，会抛出一个FileUriExposedException
                    异常，而FileProvider是一种特殊的内容提供器，它使用了和内容提供器类似的机制来对数据进行保护，可以
                    选择性低地将封装过的Uri共享给外部，从而提高了应用的安全性
                 */
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }

                // 启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                /*
                    指定图片的输出地址，填入刚刚获取到的Uri对象
                    String	EXTRA_OUTPUT
                            用于指示内容解析器Uri用于存储请求的图像或视频的Intent-extra的名称。
                 */
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                /*
                    startActivityForResult:可以用于回传数据，拍完照后会有结果返回到onActivityResult方法
                    参数1：传入意图
                    参数2：请求码
                 */
                startActivityForResult(intent, 1);
            }
        });
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 处理运行时权限
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    // 若有权限将直接访问相册
                    openAlbum();
                }
            }
        });
    }

    // 运行时权限申请
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void openAlbum() {
        // 设置意图的action为：选择文件
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        // 设置过滤类型为图片
        intent.setType("image/*");
        // 打开相册选择图片
        startActivityForResult(intent, CHOOSE_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 对startActivityForResult中的请求码进行处理
        switch (requestCode) {
            case TAKE_PHOTO:
                // 如果拍照成功，则调用BitmapFactory.decodeStream方法，将jpg图片解析为bitmap对象，并设置到imageView中显示出来
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下的系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        /*
            解析这个封装过的uri
            对uri的authority进行判断：
                1.document类型：media和downloads类型
                    如果是media类型，还需要进一步解析，通过字符串分割的方式取出后半部分才能得到真正的数字id，取出id
                    用于构建新的uri和条件语句
                        MediaStore.Images.Media._ID指的是我们获取到指定image文件的id在表中的列名
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI则是相册图片表真正的标示符
                            data：Intent { dat=content://com.android.providers.media.documents/document/image:247887 flg=0x1 }
                            uri：content://com.android.providers.media.documents/document/image%3A247887
                            docId：image:247887
                            id：247887
                            selection：_id=247887
                2.content类型
                3.file类型
            最后通过getImagePath方法获取到图片的真实路径，再调用displayPath显示图片
         */
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];    // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            /*
                如果content类型的uri，则使用普通方式处理
                data:Intent { dat=content://com.meizu.media.gallery.store/external/images/media/48211 flg=0x1 (has extras) }
                content://com.meizu.media.gallery.store/external/images/media/48211
             */
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}




































