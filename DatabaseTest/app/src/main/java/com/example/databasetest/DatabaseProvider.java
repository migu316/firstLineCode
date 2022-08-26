package com.example.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

    // 首先是定义了4个常量用于访问Book表和Category表中的所有数据，以及单条数据

    public static final int BOOK_DIR = 0;

    public static final int BOOK_ITEM = 1;

    public static final int CATEGORY_DIR = 2;

    public static final int CATEGORY_ITEM = 3;

    public static final String AUTHORITY = "com.example.databasetest.provider";

    // 初始化了一个UriMatcher对象，可以实现匹配内容URI的功能
    // 当调用UriMatcher的match方法时，就可以将一个Uri对象传入，返回值便是某个能够匹配这个Uri的自定义代码，利用这个
    // 代码，就可以判断出调用方期望访问的是哪张表中的数据了
    // match():对传入的Uri对象进行匹配，如果发现UriMatcher中某个内容URI格式成功匹配了该对象，则会返回相应的自定义代码
    private static UriMatcher uriMatcher;

    private MyDatabaseHelper dbHelper;

    // 然后在静态代码块中对UriMatcher进行了初始化，将期望匹配的几种URI格式添加了进去
    // addURI方法接收3个参数：authority，path，自定义代码
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    public DatabaseProvider() {
    }

    /**
     * 根据Uri参数判断用户想要删除哪张表里的数据，调用delete方法删除即可
     * @param uri               确定查询哪张表
     * @param selection         用于约束查询哪些列
     * @param selectionArgs     用于约束查询哪些列
     * @return                  返回删除的行数
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        // 删除数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deletedRows = db.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Book", "id = ?", new String[] { bookId });
                break;
            case CATEGORY_DIR:
                deletedRows = db.delete("Category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Category", "id = ?", new String[] { categoryId });
                break;
            default:
                break;
        }
        return deletedRows;
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 获取URI对象的MIME类型
     * @param uri   调用ContentResolver的CRUD方法时传递过来的
     * @return      返回MIME类型对象
     * 一个内容URI对应的MIME字符串主要由3部分注册，Android对这三部分做了如下格式规定
     * * 必须以vnd开头
     * * 如果内容URI以路径结果，则后接android.cursor.dir/，如果内容URI以id结尾，则后接android.cursor.item/
     * * 最后接上vnd.<authority>.<path>
     */
    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.category";
        }

        return null;
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 注意：insert方法返回一个能够表示这条新增数据的URI，因此我们还需要调用parse方法将一个内容URI解析成URI对象
     *      当然这个URI是以id结尾的
     * @param uri       确定插入数据的表
     * @param values    确定插入的值
     * @return          返回一个能够表示这条新增数据的URI,这个URI是以id结尾的
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        // 添加数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReture = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book", null, values);
                uriReture = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category", null, values);
                uriReture = Uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);
                break;
            default:
                break;
        }
        return uriReture;
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 创建了一个MyDatabaseHelper实例
     * @return 返回true表示内容提供器初始化成功
     */
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        return false;
    }


    /**
     *
     * @param uri               确定查询哪张表
     * @param projection        确定查询哪些列
     * @param selection         用于约束查询哪些列
     * @param selectionArgs     用于约束查询哪些列
     * @param sortOrder         用于对结果进行排序
     * @return                  将查询的结果返回
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        // 查询数据
        // 首先获取到了SQLiteDatabase的实例
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        // 然后根据传入的参数判断用户想要访问哪张表，再调用query进行查询，并将其cursor对象返回
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                // 细节：访问单条数据的时候，调用了uri对象的getPathSegments方法，它会将内容URI权限之后
                // 的部分以"/"符号进行分割，并把分割之后的结果放入到一个字符串列表中，那这个列表的第0个位
                // 置存放的就是路径，第一个位置存放的就是id了
                // * 内容URI的标准格式
                //      content://com.example.app.provider/table1   表示table1表
                //      content://com.example.app.provider/table1/1 表示table1表中id为1的数据
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book", projection, "id = ?", new String[] { bookId }, null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category", projection, "id = ?", new String[] { categoryId }, null, null, sortOrder);
                break;
            default:
                break;
        }
    return cursor;
    }

    /**
     * 也是先获取 SQLiteDatabase 实例，然后根据传入的Uri参数判断用户想要更新哪张表里的数据，调用SQLiteDatabase
     * 的update方法更新即可，将返回受影响的行数
     * @param uri               确定查询哪张表
     * @param values            确定更新的值
     * @param selection         用于约束查询哪些列
     * @param selectionArgs     用于约束查询哪些列
     * @return                  返回受影响的行数
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        // 更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updatedRows = db.update("Book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updatedRows = db.update("Book", values, "id = ?", new String[] { bookId });
                break;
            case CATEGORY_DIR:
                updatedRows = db.update("Category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updatedRows = db.update("Category", values, "id = ?", new String[] { categoryId });
                break;
            default:
                break;
        }
        return updatedRows;
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}

























