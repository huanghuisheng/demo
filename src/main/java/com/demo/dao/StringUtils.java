package com.demo.dao;

public class StringUtils {
	public static boolean[] digitalAndDotPool = new boolean[256];
	private static char[] XML_SPECIAL_CHAR;
	private static String[] XML_CHAR;
	private static final char[] HTML_SPECIAL_CHAR;
	private static final String[] HTML_CHAR;

	public static boolean isNull(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static boolean isString(Object p_obj) {
		return p_obj == null ? false : p_obj instanceof String;
	}

	public static byte[] convert(byte[] data, String targetEncoding) {
		if (data != null && data.length != 0) {
			try {
				return (new String(data)).getBytes(targetEncoding);
			} catch (Exception arg2) {
				arg2.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	public static char[] replaceWithCharByChar(char[] source, char[] oldChar,
			char[] newChar) {
		for (int i = 0; i < source.length; ++i) {
			for (int j = 0; j < oldChar.length; ++j) {
				if (source[i] == oldChar[j]) {
					source[i] = newChar[j];
					break;
				}
			}
		}

		return source;
	}

	public static String replaceWithCharByString(String srcString,
			char[] oldChar, String[] newChar) {
		if (srcString != null && srcString.length() != 0 && oldChar != null) {
			StringBuffer result = null;
			String filtered = null;

			for (int i = 0; i < srcString.length(); ++i) {
				filtered = null;

				for (int j = 0; j < oldChar.length; ++j) {
					if (srcString.charAt(i) == oldChar[j]) {
						filtered = newChar[j];
						break;
					}
				}

				if (result == null) {
					if (filtered != null) {
						result = new StringBuffer(srcString.length() + 50);
						if (i > 0) {
							result.append(srcString.substring(0, i));
						}

						result.append(filtered);
					}
				} else if (filtered == null) {
					result.append(srcString.charAt(i));
				} else {
					result.append(filtered);
				}
			}

			return result == null ? srcString : result.toString();
		} else {
			return srcString;
		}
	}

	public static String encodeXML(String xml) {
		return xml != null && xml.length() != 0 ? replaceWithCharByString(xml,
				XML_SPECIAL_CHAR, XML_CHAR) : "";
	}

	public static String encodeHTML(String html) {
		return html != null && html.length() != 0 ? replaceWithCharByString(
				html, HTML_SPECIAL_CHAR, HTML_CHAR) : "";
	}

	public static String replace(String source, String target,
			String replacement) {
		if (source == null) {
			return null;
		} else if (source.indexOf(target) <= -1) {
			return source;
		} else {
			StringBuffer retu = new StringBuffer();

			do {
				retu.append(source.substring(0, source.indexOf(target)));
				retu.append(replacement);
				source = source.substring(source.indexOf(target)
						+ target.length());
			} while (source.indexOf(target) > -1);

			retu.append(source);
			return retu.toString();
		}
	}

	public static boolean matchUrl(String urlPattern, String requestPath) {
		if (requestPath == null) {
			return false;
		} else if (urlPattern == null) {
			return false;
		} else if (urlPattern.equals(requestPath)) {
			return true;
		} else if (urlPattern.equals("/*")) {
			return true;
		} else if (urlPattern.endsWith("/*")) {
			if (urlPattern.regionMatches(0, requestPath, 0,
					urlPattern.length() - 2)) {
				if (requestPath.length() == urlPattern.length() - 2) {
					return true;
				}

				if (47 == requestPath.charAt(urlPattern.length() - 2)) {
					return true;
				}
			}

			return false;
		} else {
			if (urlPattern.startsWith("*.")) {
				int slash = requestPath.lastIndexOf(47);
				int period = requestPath.lastIndexOf(46);
				if (slash >= 0
						&& period > slash
						&& period != requestPath.length() - 1
						&& requestPath.length() - period == urlPattern.length() - 1) {
					return urlPattern.regionMatches(2, requestPath, period + 1,
							urlPattern.length() - 2);
				}
			}

			return false;
		}
	}

	public static boolean isDigitalAndDot(String str) {
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) > 255 || digitalAndDotPool[str.charAt(i)]) {
				return false;
			}
		}

		return true;
	}

	static {
		for (int i = 0; i < digitalAndDotPool.length; ++i) {
			digitalAndDotPool[i] = true;
		}

		digitalAndDotPool[48] = false;
		digitalAndDotPool[49] = false;
		digitalAndDotPool[50] = false;
		digitalAndDotPool[51] = false;
		digitalAndDotPool[52] = false;
		digitalAndDotPool[53] = false;
		digitalAndDotPool[54] = false;
		digitalAndDotPool[55] = false;
		digitalAndDotPool[56] = false;
		digitalAndDotPool[57] = false;
		digitalAndDotPool[46] = false;
		digitalAndDotPool[69] = false;
		XML_SPECIAL_CHAR = new char[]{'&', '\\', '<', '>', '\r', '\n', '\"'};
		XML_CHAR = new String[]{"&amp;", "&quot;", "&lt;", "&gt;", "&#10;",
				"&#13;", "&quot;"};
		HTML_SPECIAL_CHAR = new char[]{'<', '>', '&', '\"', '\''};
		HTML_CHAR = new String[]{"&lt;", "&gt;", "&amp;", "&quot;", "&#39;"};
	}
}
