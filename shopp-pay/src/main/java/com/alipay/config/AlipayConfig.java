package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016101900723628";

	// 商户私钥，您的PKCS8格式RSA2私钥
	public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCGghja8qXwJOx12uj6Vamn746+AF/wJgFCL1ZTxXYKsEdb7riiDWJ9JPRElqNx5SUdlnBGqoBcpJSqBUrOdcWAc0glzyhXcxY+WPZ/yUbw3otgZcnuW7C1+DXhPansWyVCk77+6b3g+E27us7Fpm7aBSu8NpbmF2roWwDCuNKxaDwimF2eYdTPmoeo7A7CywX5ax9TIpy3ybv2rIoTFxGhnVnXvnqyix/ZYQdM8TjFWqhL50aoXDu8lyVnpjZwTjq4kJ0BH58mitJN8616gtfBUT7cbdwFT30DT0HdjjwgMcdujrMVgWPaZgWcUkRmXCj0S3yD8JcuZ2NE64DpftnTAgMBAAECggEAF0KGE86kHVv1PAgoA6oulWjasUaVUIzx6MUQ05mw6PZE0MP+T5+q6WMrjnTSPM7YVT5/mejAG8UmJCSYJA2/oo/M6STioAS50vxFXCGtT9fcGEZh3gMjEsSiZJnqOYIldlbgT2qSOEYYAx+xxAIc5zy/rGNwQsfH5/hcsE+Pt7fDIBEfpbk4uLXhLzSYblidz5V/gtCS8t9lPphujaShEeP8mgwCWggQaJMGVXdGNDxClFg/odZx10+GQvDZ0ZeSqfon/YcbaBreH/5WdkgBeJ6bgxAbbRecr7KyvD/o3TI7xuRUdIL6Edh42t8c0FWPZCSFdC5gtz3jbQxbHXNjCQKBgQDPuPl0l2xZ8Fp/fTH3rCfUcqjbHsIpkEqKjsczVZkv/BQrVpXssJ/LMYRsSCtPF5eOmdLN6a+0r6A3huPPaPoGMswO7tPBIau6o02BlAWvrGpS5uSQ7ZLpLRrtQBOzzyztHGUpwwjQ8QLSUdSfSTUKNZ91JukrDTcrDa2xmlP7jQKBgQClxQhLP0aniCgFtOoN/IYM3C7fKEouuPBiIVun99t5Ri/U2TI2uRsYpSnzttPhVPotIYmYevNfo3dVjO3vy15/+shIahjitnWNSkY/rJsGUdhZUcZ/iiYHCHqG6/ursDTVDg3nAHxyLzKSvnC7MhtbhAK2I/NdotuKk4tr7CYi3wKBgQCUELsoA4PFfFFVzOzgKYPeqLh6MKugKu0nZOzTR7HBBYNu/PIzXBcbJLnK4YqVfzCWAknEY03Okt22lt0uDBskbfMMteqVISYjDKKDufpvpYvIejL0p5PvyCoIcYNZyVSx02q84PwYy6MCPjg8LlezZlO6kzjjajEn7EEidupkRQKBgCaRgSyAvF+ePjGVth8q8cVsNTuWlCvqu4MZX8nk0RuWSOqs3hH9X2CMjQY3M3Tp4Is/klP3x0gpMEqnT3TLTGFaZ97xUHjvGsqUzWNjNW5y/HNLo3NpIQ/h2SArJ5L6nFA1xOZKJqoqcQnTZUcokVTpkffuxWCz04Cac4XYm8bhAoGANk5fEXmQ2zzMLRdxAlrSXuE9OLrxiJ2uH38qVlClvObhdhkE8I9jR/pjh5ezK900ei8pOY2CUHv0lVDqmBOJn6kxis5ry8eGRtTQoKPfJNKVp/YjLeVvH0U+PMwJnS5qJGVIwVVDAEGUuQ7xtE5ucZWh0LzZEyyYl8Z00SXhcXE=";
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
	// 对应APPID下的支付宝公钥。
	public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlvGiCI52OrVFHo0EfElCtTw+YwbyRHrqam2MUmHsenGBe9JvvVKX1vXmiVEjPFS9ubeMdDIsUdOo6miwpMTB8rNu3reEfoIgNe/lByHySl6PeeT1HwJN4dgNev9IkO2GZmENBC/kZQgK5HFTZpZ6wKk+uilOdd2Iwtn5AEdLyc15+6X4OhBYYh6aBJ04DDo/ztIhIjhKz7vRtMMXY+orL1qgnsEcMCEgudk6eVeitwcQ5YxElt0zsV0jxNZTyhRki/TcJOtd+hFuQoiQ7L6d1ySv41NbQvorq5UXEgnV70cB0KhrbpSa0qzBIs+4tsnLZ9cL68k1Ye0JzHOxFWtThQIDAQAB";
	// 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://127.0.0.1/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://127.0.0.1/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";

	// 字符编码格式
	public static String charset = "utf-8";

	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

	// 支付宝网关
	public static String log_path = "C:\\";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	/**
	 * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
	 * 
	 * @param sWord
	 *            要写入日志里的文本内容
	 */
	public static void logResult(String sWord) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
			writer.write(sWord);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
