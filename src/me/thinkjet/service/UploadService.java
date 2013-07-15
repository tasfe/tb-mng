package me.thinkjet.service;

import com.jfinal.kit.PathKit;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadService {

	private final static String FILE_UPLOAD_URL = "http://static.siyanjing.net/fileUpload";
	private final static String IMG_UPLOAD_URL = "http://static.siyanjing.net/imgUpload";
	private final static String AUTHORIZATION = "qwertyuiopasdfghjkl";

	/**
	 * 上传文件
	 *
	 * @param file 文件内容
	 * @return filepath
	 */
	public static String uploadFile(File file) {
		try {
			return uploadFile(file, FILE_UPLOAD_URL);
		} catch (IOException e) {
			return "";
		}
	}

	/**
	 * @param img
	 * @return
	 */
	public static String uploadImg(File img) {
		try {
			return uploadFile(img, IMG_UPLOAD_URL);
		} catch (IOException e) {
			return "";
		}
	}

	/**
	 * 根据原始URL生成缩略图Url
	 */
	public static String generalThumbUrl(String url, int width, int height) {
		if (url != null
				&& !url.equals("")
				&& url.lastIndexOf(".") > 0) {
			StringBuffer new_url = new StringBuffer(url);
			new_url.insert(new_url.lastIndexOf("."), "!" + width + "x" + height);
			return new_url.toString();
		} else {
			return null;
		}
	}

	/**
	 * 上传文件
	 *
	 * @param _file 待上传的文件
	 * @return true or false
	 * @throws java.io.IOException
	 */
	private static String uploadFile(File _file, String _url) throws IOException {

		InputStream is = null;
		OutputStream os = null;
		HttpURLConnection conn = null;

		try {
			// 读取待上传的文件
			is = new FileInputStream(_file);

			// 获取链接
			URL url = new URL(_url);
			conn = (HttpURLConnection) url.openConnection();

			// 设置必要参数
			conn.setConnectTimeout(30 * 1000);
			conn.setRequestMethod("PUT");
			conn.setUseCaches(false);
			conn.setDoOutput(true);

			// 设置签名
			conn.setRequestProperty("authorization", AUTHORIZATION);


			// 创建链接
			conn.connect();

			os = conn.getOutputStream();
			byte[] data = new byte[4096];
			int temp = 0;

			// 上传文件内容
			while ((temp = is.read(data)) != -1) {
				os.write(data, 0, temp);
			}

			// 上传成功
			return getText(conn);

		} catch (IOException e) {
			return "";
		} finally {
			if (os != null) {
				os.close();
				os = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
	}

	/**
	 * 获得连接请求的返回数据
	 *
	 * @param conn
	 * @return 字符串
	 */
	private static String getText(HttpURLConnection conn)
			throws IOException {

		StringBuilder text = new StringBuilder();

		InputStream is = null;
		InputStreamReader sr = null;
		BufferedReader br = null;

		int code = conn.getResponseCode();

		try {
			is = code >= 400 ? conn.getErrorStream() : conn.getInputStream();

			sr = new InputStreamReader(is);
			br = new BufferedReader(sr);

			char[] chars = new char[4096];
			int length = 0;

			while ((length = br.read(chars)) != -1) {
				text.append(chars, 0, length);
			}

		} finally {
			if (br != null) {
				br.close();
				br = null;
			}
			if (sr != null) {
				sr.close();
				sr = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
		}

		if (code >= 400)
			throw new IOException(text.toString());
		return text.toString();
	}

}
