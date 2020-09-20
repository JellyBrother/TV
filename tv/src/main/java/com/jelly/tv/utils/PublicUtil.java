package com.jelly.tv.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Intents.Insert;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jelly.test.utils.LanguageInfo;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.UUID;

/**
 * 公共工具类
 */
public class PublicUtil {

	/**
	 * 显示软键盘
	 *
	 * @param context
	 * @param view
	 */
	public static void showSoftInput(Context context, View view) {
		((InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(
				view, 0);
	}

	/**
	 * 显示软键盘
	 * @param context
	 */
	public static void showSoftInput(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 隐藏软键盘
	 *
	 * @param context
	 * @param view
	 */
	public static void hidenSoftInput(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean success = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 设置EditText图片全选
	 *
	 * @param et
	 */
	public static void setFocusSelectAll(final EditText et) {
		et.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					et.selectAll();
				}
			}
		});
	}

	/**
	 * 获取imei号码 需添加 android.permission.READ_PHONE_STATE
	 *
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		String deviceId = SharedPreferencesUtil.getString(context, "DEVICEID");
		if (null == deviceId || "".equals(deviceId)) {
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = manager.getDeviceId();
			if (TextUtils.isEmpty(deviceId)) {
				deviceId = UUID.randomUUID().toString();
			}

			// 存入缓存中：
			SharedPreferencesUtil.putString(context, "DEVICEID", deviceId);
		}
		return deviceId;
	}


	/**
	 * 保存InputStream到本地文件中
	 *
	 * @param inputStream
	 * @param fileName
	 * @return
	 */
	public static File writeToSDCard(InputStream inputStream, String fileName) {
		if (null == fileName || "".equals(fileName)) {
			fileName = "FrameDefault.txt";
		}
		File file = null;
		OutputStream output = null;
		try {
			file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + fileName);
			output = new FileOutputStream(file, true);
			byte buffer[] = new byte[4 * 1024];
			while ((inputStream.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 *
	 * @param iv
	 * @return
	 */
	public static int getStatusBarHeight(View iv) {
		Rect rect = new Rect();
		iv.getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top;
		return statusBarHeight;
	}


	/**
	 * 保存文件到SDK上
	 *
	 * @param str
	 * @param fileName
	 * @return
	 */
	public static File writeToSDCard(String str, String fileName) {
		return writeToSDCard(String2InputStream(str), fileName);
	}

	private static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = null;
		try {
			stream = new ByteArrayInputStream(str.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return stream;
	}

	public static String md5(String plainText) {
		try {
			String str = "";
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("utf-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
			// str = buf.toString().toUpperCase();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * WIFI是否可用
	 * @param inContext
	 * @return
	 */
	public static boolean isWifiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}


	/**
	 * 网络是否激活
	 * @param inContext
	 * @return
	 */
	public static boolean isNetActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null) {
				return info.isAvailable();
			}
		}
		return false;
	}


	public static boolean isSDCardFull() {
		File path = Environment.getExternalStorageDirectory();
		StatFs statFs = new StatFs(path.getPath());
		long blockSize = statFs.getBlockSize();
		long availableBlocks = statFs.getAvailableBlocks();
		// 小于10Kb即视为无容量
		if (availableBlocks * blockSize <= 1024 * 10) {
			return true;
		}
		return false;
	}

	public static long getSdFreeSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs statFs = new StatFs(path.getPath());
		long blockSize = statFs.getBlockSize();
		long availableBlocks = statFs.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public static boolean isExistSd() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public static void dismissWaitDialog(Activity mContext, Dialog dialog) {
		if (dialog != null && dialog.isShowing() && !mContext.isFinishing()) {
			dialog.dismiss();
		}
	}

	/**
	 *
	 * 获取时间，包括时区
	 *
	 * @return String
	 */
	public static String queryDate() {
		final String FORMAT = "yyyy-MM-dd HH:mm:ss z";
		return queryDate(FORMAT);
	}

	/**
	 *
	 * 获取时间，包括时区
	 *
	 * @param format
	 *            格式参数，如yyyy-MM-dd HH:mm:ss z
	 *
	 * @return String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String queryDate(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
		Calendar cld = Calendar.getInstance();
		sdf.setTimeZone(new SimpleTimeZone(cld.get(Calendar.ZONE_OFFSET), ""));
		// 登录时间
		String sTime = sdf.format(date);
		if (null != sTime) {
			return sTime;
		}
		return "";
	}

	/**
	 * 获取应用程序版本名称(非mejt版本名称)
	 *
	 * @param c
	 * @return
	 */
	public static String queryVersionName(Context c) {
		PackageInfo info;
		try {
			info = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
			// 当前版本的版本号
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取应用程序版本名称(非mejt版本号)
	 *
	 * @param c
	 * @return
	 */
	public static int queryVersionCode(Context c) {
		PackageInfo info;
		try {
			info = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
			// 当前版本的版本号
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 1;
		}
	}

	/**
	 * 获取权限信息
	 *
	 * @param c
	 * @return
	 */
	public static String[] queryPermissions(Context c) {
		try {
			PackageInfo info = c.getPackageManager().getPackageInfo(
					c.getPackageName(),
					PackageManager.GET_UNINSTALLED_PACKAGES
							| PackageManager.GET_PERMISSIONS);
			String[] permissions = info.requestedPermissions;
			return permissions;
			// 当前版本的版本号
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 字符串转换为数组形式
	 *
	 * @param str
	 * @return
	 */
	public static String[] converArray(String str) {
		if (null == str || "".equals(str)) {
			return null;
		}
		final String BLANK = " ";
		return str.split(BLANK);
	}

	/**
	 * 数组转换为字符串形式
	 *
	 * @param array
	 * @return
	 */
	public static String converString(String... array) {
		if (null == array || "".equals(array)) {
			return "";
		}
		final String BLANK = " ";
		StringBuffer sbf = new StringBuffer();
		for (String str : array) {
			sbf.append(str).append(BLANK);
		}
		return sbf.substring(0, sbf.lastIndexOf(BLANK));
	}

	/**
	 * 时区转换 把GMT+08:00 修改为GMT+8 服务端识别
	 *
	 * @param time
	 * @return
	 */
	public static String spintTimeZone(String time) {
		if (null == time) {
			return "";
		}
		final String COLON = ":";
		final String ZERO = "0";
		if (time.indexOf(COLON) != -1) {
			time = time.substring(0, time.lastIndexOf(COLON));
		}
		if (time.contains(ZERO) && !time.endsWith(ZERO)) {
			time = time.replace(ZERO, "");
		}
		if (time.endsWith(ZERO + ZERO)) {
			time = time.substring(0, time.lastIndexOf(ZERO));
		}
		return time;
	}

	/**
	 * 更改应用程序默认语言
	 */
	public static void updateLanguage(Context context, String language) {
		Locale loc = Locale.ENGLISH;
		if (TextUtils.equals(LanguageInfo.KEY_CHINESE,language)) {
			loc = Locale.CHINESE;
		}
		Configuration config = context.getResources().getConfiguration();
		config.locale = loc;
		context.getResources().updateConfiguration(config, null);
	}


	/**
	 * 获取系统语言
	 * @param context
	 * @return
	 */
	public static String getSystemLanguage(Context context) {
		Resources resource = context.getResources();
		Configuration configuration = resource.getConfiguration();
		Locale locale = configuration.locale;
		return locale.getLanguage();
	}

	/** 删除文件 */
	public static void deleteFile(File f) {
		if (f.exists()) {
			if (f.isFile()) {
				f.delete();
			}
			File[] files = f.listFiles();
			if (files == null) {
				return;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFile(files[i]);
					files[i].delete();
				} else if (files[i].isFile()) {
					files[i].delete();
				}
			}
		}
	}

	/**
	 * 删除文件及文件夹
	 *
	 * @param f
	 */
	public static void deleteFileAndDir(File f) {
		if (f.exists()) {
			if (f.isFile()) {
				f.delete();
				return;
			}
			File[] files = f.listFiles();
			if (files == null) {
				return;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFileAndDir(files[i]);
					files[i].delete();
				} else if (files[i].isFile()) {
					files[i].delete();
				}
			}
			if (f.isDirectory()) {
				f.delete();
			}
		}
	}

	/** 删除文件,但不删除文件夹 */
	public static void deleteFileNoDir(File f) {
		if (f.exists()) {
			if (f.isFile()) {
				f.delete();
			}
			File[] files = f.listFiles();
			if (files == null) {
				return;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFile(files[i]);
				} else if (files[i].isFile()) {
					files[i].delete();
				}
			}
		}
	}

	/**
	 * 迭代目录，获取目录下的“.png”或“.jpg”图片文件
	 *
	 * @param dir
	 *            需要迭代的目录
	 * @param filePathList
	 *            List列表用于保存图片的绝对路径
	 */
	public static void iteratePictureDir(File dir, List<String> filePathList) {

		if (dir != null && dir.exists()) {
			if (dir.isDirectory()) {
				for (File f : dir.listFiles()) {
					iteratePictureDir(f, filePathList);
				}
			} else {
				String fileName = dir.getName();
				if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
					filePathList.add(dir.getAbsolutePath());
				}
			}
		}

	}

	/**
	 * 字符串转换为数字
	 *
	 * @param value
	 * @return
	 */
	public static int valueOf(String value) {
		if (value == null) {
			return 0;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	/**
	 * 字符串转换为数字
	 *
	 * @param value
	 * @return
	 */
	public static int parseInt(Object value, int defalut) {
		if (value == null) {
			return defalut;
		}
		try {
			return Integer.parseInt(getStringNotNull(value));
		} catch (NumberFormatException e) {
		}
		return defalut;
	}

	/**
	 * 字符串转换为double
	 *
	 * @param value
	 * @return
	 */
	public static int parseDouble(String value) {
		if (value == null) {
			return 0;
		}
		try {
			return (int) (Math.round(Double.parseDouble(value)));
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	/**
	 * 字符串转换为Folat
	 *
	 * @param value
	 * @return
	 */
	public static float parseFloat(String value) {
		if (value == null) {
			return 0;
		}
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	/**
	 * 字符串转换为数字
	 *
	 * @param value
	 * @return
	 */
	public static long parseLong(String value, long defalut) {
		if (value == null) {
			return defalut;
		}
		try {
			return Math.round(Double.parseDouble(value));
		} catch (NumberFormatException e) {
		}
		return defalut;
	}
	/**
	 * 字符串转换为数字
	 *
	 * @param value
	 * @return
	 */
	public static Long parseLong(String value, Long defalut) {
		if (value == null) {
			return defalut;
		}
		try {
			return Long.valueOf(getStringNotNull(value));
		} catch (Exception e) {
		}
		return defalut;
	}

	/** 判断是否符合时间差 */
	public static boolean isNeedupdate(long last, int min) {
		long time = new Date().getTime();
		if (last == 0 || (time - last) > min * 60 * 1000) {
			return true;
		}
		return false;
	}

	/**
	 * 文件流读取
	 *
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] inputStreamToByte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}


	/**
	 * 判断字符是否为空
	 *
	 * @param str
	 *            传入参数串
	 * @return 是否是空数据
	 */
	public static boolean isEmpty(CharSequence str) {
		if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str.toString().trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断集合是否为空
	 * @param target
	 * @param <T>
	 * @return
	 */
	public static <T> boolean isEmpty(Collection<T> target) {
		return null == target || target.isEmpty();
	}


	/**
	 * 判断应用程序当前界面是否位于堆栈的顶层
	 *
	 * @param context
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			if (context.getClass().getName()
					.equals(tasksInfo.get(0).topActivity.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否有网络
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 设备是否是平板(或者手机)
	 *
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * 获取一个不为空的字符串
	 *
	 * @return String
	 */
	public static String getStringNotNull(Object s) {
		if (null == s || "null".equals(s)) {
			return "";
		}
		return s.toString();
	}

	/**
	 * 判断是否为模拟器：
	 *
	 * @return
	 */
	public static boolean isEmulator() {
		return (Build.MODEL.equals("sdk"))
				|| (Build.MODEL.equals("google_sdk"))
				|| (Build.MODEL.contains("Android SDK"));
	}

	/**
	 * 判断手机是否为root, true 则为已root
	 *
	 * @return
	 */
	public static boolean isRooted2() {
		try {
			Process localProcess = Runtime.getRuntime().exec("su");
			DataOutputStream localDataOutputStream = new DataOutputStream(
					localProcess.getOutputStream());
			localDataOutputStream
					.writeBytes("echo \"Do I have root?\" &gt;/system/sd/temporary.txt\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			try {
				localProcess.waitFor();
				int i = localProcess.exitValue();
				return i != 255;
			} catch (InterruptedException localInterruptedException) {
				return false;
			}
		} catch (IOException localIOException) {
		}
		return false;
	}

	/**
	 * 判断手机是否为root, true 则为已root
	 *
	 * @return
	 */
	public static boolean isRooted() {
		String binPath = "/system/bin/su";
		String xBinPath = "/system/xbin/su";
		if (new File(binPath).exists() && isExecutable(binPath)) {
			return true;
		}

		if (new File(xBinPath).exists() && isExecutable(xBinPath)) {
			return true;
		}
		return false;
	}

	private static boolean isExecutable(String filePath) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("ls -l " + filePath);
			// 获取返回内容
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String str = in.readLine();
			if (str != null && str.length() >= 4) {
				char flag = str.charAt(3);
				if (flag == 's' || flag == 'x')
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (p != null) {
				p.destroy();
			}
		}
		return false;
	}

	private static Rect rec;

	/**
	 * 获取view 的坐标值
	 *
	 * @param view
	 * @return
	 */
	public static Rect getViewRect(View view) {
		view.getGlobalVisibleRect(rec);
		return rec;
	}

	/**
	 * 从文件中读取缓存对象
	 *
	 * @param file
	 *            文件的全路径
	 * @return
	 */
	public static Object readCache(String file) {
		FileInputStream fis = null;
		ObjectInputStream is = null;
		try {
			fis = new FileInputStream(file);
			is = new ObjectInputStream(fis);

			Object objValue = is.readObject();

			fis.close();
			is.close();
			fis = null;
			is = null;
			if (objValue != null) {
				return objValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fis) {
					fis.close();
					fis = null;
				}
				if (null != is) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static long lastClickTime;

	/**
	 * 判断是否快速点击
	 *
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 800) {
			lastClickTime = time;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 非空判断
	 *
	 * @param entity
	 * @return true 不为空，false 为空
	 */
	public static boolean isNotEmpty(Object entity) {
		boolean empty = false;
		if (entity instanceof Collection) {
			if (null != entity && ((Collection<?>) entity).size() > 0) {
				return true;
			}
		}
		return empty;
	}

	/**
	 * 判断集合是否相等
	 *
	 * @param oldlist
	 * @param newlist
	 * @return
	 */
	public static boolean listequal(ArrayList<?> oldlist, ArrayList<?> newlist) {
		boolean equal = false;
		if (null == oldlist && null == newlist) {
			return true;
		} else if (null != oldlist && null == newlist) {
			return false;
		} else if (null != newlist && null == oldlist) {
			return false;
		}
		int oldSize = oldlist.size();
		int newSize = newlist.size();
		if (oldSize == 0 && newSize == 0) {
			return true;
		} else if (oldSize != newSize) {
			return false;
		}
		List<?> cloneoldlist = (List<?>) oldlist.clone();
		List<?> cloneonewlist = (List<?>) newlist.clone();
		cloneoldlist.retainAll(cloneonewlist);
		if (cloneoldlist.size() == oldSize && cloneonewlist.size() == newSize) {
			equal = true;
		}
		return equal;
	}

	/**
	 * 拨打电话
	 *
	 * @param context
	 * @param telephone
	 */
	public static void telDial(Context context, String telephone) {
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL);
			Uri data = Uri.parse("tel:" + telephone);
			intent.setData(data);
			context.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(context, "NO Intent FoundException",
					Toast.LENGTH_SHORT).show();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 发邮件
	 *
	 * @param emailAdd
	 */
	public static void sendEmail(Context context, String emailAdd) {
		try {
			Intent data = new Intent(Intent.ACTION_SENDTO);
			data.setData(Uri.parse("mailto:" + emailAdd));
			context.startActivity(data);
		} catch (Exception e) {
			Toast.makeText(context, "NO Intent FoundException",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	/**
	 * 调用系统的短信界面
	 *
	 * @param message
	 */
	public static void sendMessage(Context context, String number,
								   String message) {
		try {
			Intent sendIntent;
			if (TextUtils.isEmpty(number)) {
				sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", message);
				sendIntent.setType("vnd.android-dir/mms-sms");
			} else {
				Uri uri = Uri.parse("smsto:" + number);
				sendIntent = new Intent(Intent.ACTION_VIEW, uri);
				sendIntent.putExtra("sms_body", message);
			}
			context.startActivity(sendIntent);
		} catch (Exception e) {
			Toast.makeText(context, "NO Intent FoundException",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	/**
	 * 调用系统编辑添加联系人
	 *
	 * @param context
	 * @param phoneNumber
	 *            电话
	 * @param name
	 *            姓名
	 * @param email
	 */
	public static void addContact(Context context, String[] phoneNumber,
								  String name, String email) {
		try {
			Intent it = new Intent(Intent.ACTION_INSERT);
			it.setType("vnd.android.cursor.dir/person");
			it.setType("vnd.android.cursor.dir/contact");
			it.setType("vnd.android.cursor.dir/raw_contact");
			it.putExtra(Insert.NAME, name);
			it.putExtra(Insert.EMAIL, email);
			if (null == phoneNumber) {
				it.putExtra(Insert.PHONE, "");
			} else {
				int phoneLength = phoneNumber.length;
				for (int i = 0; i < phoneLength; i++) {
					if (i == 0) {
						it.putExtra(Insert.PHONE, phoneNumber[i]);
						it.putExtra(Insert.PHONE_TYPE, Phone.TYPE_MOBILE);
					} else if (i == 1) {
						it.putExtra(Insert.SECONDARY_PHONE, phoneNumber[i]);
						it.putExtra(Insert.SECONDARY_PHONE_TYPE,
								Phone.TYPE_HOME);
					} else if (i == 2) {
						it.putExtra(Insert.TERTIARY_PHONE, phoneNumber[i]);
						it.putExtra(Insert.TERTIARY_PHONE_TYPE, Phone.TYPE_WORK);
					}
				}
			}
			context.startActivity(it);
		} catch (Exception e) {
			Toast.makeText(context, "NO Intent FoundException",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	/**
	 * 获取状态栏的高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		((Activity) context).getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = context.getResources().getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}


	/**
	 * 以最省内存的方式读取本地资源的图片
	 *
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
	 * @param context
	 * @return 平板返回 True，手机返回 False
	 */
	public static boolean isPad(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * 检查用户是否设置锁屏密码
	 * @return
	 */
	public static  boolean isSecured(Context contactm){
		boolean isSecured = false;
		String classPath = "com.android.internal.widget.LockPatternUtils";
		try{
			Class<?> lockPatternClass = Class.forName(classPath);
			Object lockPatternObject = lockPatternClass.getConstructor(Context.class).newInstance(contactm);
			Method method = lockPatternClass.getMethod("isSecure");
			isSecured = (Boolean)method.invoke(lockPatternObject);
		}catch (Exception e){
			isSecured = false;
		}
		return isSecured;
	}

	/**
	 * 判断手机网络是否是2g
	 * 移动和联通的2G为GPRS或EDGE，电信的2G为CDMA
	 */
	public static boolean is2G(Context contextm){
		ConnectivityManager cm = (ConnectivityManager) contextm.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null && info.getType()==ConnectivityManager.TYPE_MOBILE){
			switch (info.getSubtype()){
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return true;
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return true;
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return true;
				default:
					return false;
			}
		}
		return false;
	}


	/**
	 * 获取网络类型
	 * @param mContext
	 * @return
	 */
	public static int getNetworkType(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null) {
			String type = netInfo.getTypeName();
			if(null == type){
				type = "";
			}
			if ("wifi".equals(type.toLowerCase())) {
				return 9;
			}
			else if (type.toLowerCase().equals("mobile") || type.toLowerCase().equals("cellular")) {
				type = netInfo.getSubtypeName();
				if(null == type){
					type = "";
				}
				if (type.toLowerCase().equals("gsm") ||
						type.toLowerCase().equals("gprs") ||
						type.toLowerCase().equals("edge")) {
					return 2;
				}
				else if (type.toLowerCase().startsWith("cdma") ||
						type.toLowerCase().equals("umts") ||
						type.toLowerCase().equals("hspa") ||
						type.toLowerCase().equals("hsupa") ||
						type.toLowerCase().equals("hsdpa") ||
						type.toLowerCase().equals("1xrtt") ||
						type.toLowerCase().equals("ehrpd")) {
					return 3;
				}
				else if (type.toLowerCase().equals("lte") ||
						type.toLowerCase().equals("umb") ||
						type.toLowerCase().equals("hspa+")) {
					return 4;
				}
			}
		}
		else {
			return 0;
		}

		return -1;
	}

	/**
	 * dp转px
	 *
	 * @param context
	 * @param dipValue
	 *            dp值
	 * @return px值
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转dp
	 *
	 * @param context
	 * @param pxValue
	 *            px值
	 * @return dp值
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int px2sp(Context context, float pxValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static String FormatFileSize(long size){
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeStr = "";
		String wrongSize = "0B";
		if(size==0){
			return wrongSize;
		}
		if(size<1024){
			fileSizeStr = df.format((double)size)+"B";
		}else if(size<1048576){
			fileSizeStr = df.format((double)size/1024)+"KB";
		}else if(size<1073741824){
			fileSizeStr = df.format((double)size/1048576)+"MB";
		}else {
			fileSizeStr = df.format((double)size/1073741824)+"GB";
		}
		return fileSizeStr;
	}
}
