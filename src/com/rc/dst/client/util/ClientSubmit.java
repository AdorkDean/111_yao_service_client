package com.rc.dst.client.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.rc.dst.client.config.ClientConfig;
import com.rc.dst.client.sign.MD5;
import com.rc.dst.client.util.httpClient.HttpProtocolHandler;
import com.rc.dst.client.util.httpClient.HttpRequest;
import com.rc.dst.client.util.httpClient.HttpResponse;
import com.rc.dst.client.util.httpClient.HttpResultType;


/* *
 *功能：各接口请求提交类
 *详细：构造各接口表单HTML文本，获取远程HTTP数据
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 */

public class ClientSubmit {

	/**
	 * 提供给商户的服务接入网关URL(新)
	 */
	// private static final String YAO_GATEWAY_NEW =
//	private static final String YAO_GATEWAY_NEW = "http://common.111yao.com/sltRouter?";


	
	public static void main(String[] args) throws Exception {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("parnterid", "1001");
//		map.put("method", "search");
//		
//		map.put("from", "3");
//		map.put("sort", "1");
//		map.put("order", "4");
//		map.put("pricea", "0");
//		map.put("priceb", "90000");
//		map.put("typeid", "-1");
//		map.put("brandid", "-1");
//		map.put("keyword","商品");
//		map.put("filtertype","1");
//		map.put("page", "1");
//		map.put("size", "20");
//		String YAO_GATEWAY_URL = "http://localhost:8080/111_index_search/sltRouter?";
//		ClientSubmit.buildRequestForSearch(map,YAO_GATEWAY_URL);
//		Map<String, String> map1 = new HashMap<String, String>();
//		map1.put("keyword","商品");
//		ClientSubmit.buildRequestForSearchFilter(map1,YAO_GATEWAY_URL);
		
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("mobiles", "15801313717");
//		map.put("smsContent", "本地最后测试");
//		String YAO_GATEWAY_URL = "http://localhost:8080/drugcommon/sltRouter?method=sms";
//		ClientSubmit.buildRequestBySMS(map,YAO_GATEWAY_URL);
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "1");
		String YAO_GATEWAY_URL = "http://localhost:8080/drugcommon/sltRouter?method=getHealthyPlanContentInfo";
		System.out.println(ClientSubmit.getHealthyPlanInfo(map,YAO_GATEWAY_URL));
	}
	/**
	 * 异步获取健康方案数据
	 * 访问参数   方案id
	 */
	public static String getHealthyPlanInfo(Map<String, String> sParaTemp,String Url)
		throws Exception {
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp);
		//test(sPara);
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
		
		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset(ClientConfig.input_charset);
		
		request.setParameters(generatNameValuePair(sPara));
		request.setUrl(Url);
		//System.out.println(request.getUrl());
		
		HttpResponse response = httpProtocolHandler.execute(request, "", "");
		if (response == null) {
			return null;
		}
		String strResult = response.getStringResult();
		//System.out.println("strResult=>"+strResult);
	return strResult;
	}
	/**
	 * 短信接口服务器请求数据
	 * @param sParaTemp 参数
	 * Map<String, String> map = new HashMap<String, String>();
	 * map.put("mobiles", "13188888888,18288888888");   多个电话用','隔开
	 * map.put("smsContent", "内容");
	 * 
	 * @param Url  接口服务器url
	 * http://localhost:8080/drugcommon//sltRouter?method=sms
	 * 
	 * @return 0 成功
	 * @throws Exception
	 */
	public static String buildRequestBySMS(Map<String, String> sParaTemp,String Url)
			throws Exception {
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp);
//		test(sPara);
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset(ClientConfig.input_charset);

		request.setParameters(generatNameValuePair(sPara));
		request.setUrl(Url);
//		System.out.println(request.getUrl());

		HttpResponse response = httpProtocolHandler.execute(request, "", "");
		if (response == null) {
			return null;
		}
		String strResult = response.getStringResult();
//		System.out.println("strResult=>"+strResult);
		return strResult;
	}


	/**
	 * @param sParaTemp
	  	map.put("parnterid", "1001");固定参数
		map.put("method", "search");固定参数
		
		map.put("from", "1"); 来源 1-pc 2-wap 3-app
		map.put("sort", "1"); 排序字段 1-销量 2-评论 3-价格 4-折扣 （注意：-1是默认排序）
		map.put("order", "2"); 排序 1-升序 2-降序
		map.put("pricea", "0"); 价格区间 低价格
		map.put("priceb", "9000"); 价格区间 高价格
		map.put("typeid", "-1"); 药品类型 1-OTC 2-处方A 3-处方B
		map.put("brandid", "-1"); 品牌id
		map.put("keyword","显瘦简约百"); 搜索关键字
		map.put("filtertype","1"); 搜索类型 1-返回搜索结果 2-返回品牌的集合 
		map.put("page", "1"); 分页 页码
		map.put("size", "20"); 分页 每页数量
	 * @param Url http://localhost:8080/111_index_search/sltRouter?
	 */
	public static String buildRequestForSearch(Map<String, String> sParaTemp,String Url)
			throws Exception {
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp);
		test(sPara);
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset(ClientConfig.input_charset);

		request.setParameters(generatNameValuePair(sPara));
		request.setUrl(Url);
		System.out.println(request.getUrl());

		HttpResponse response = httpProtocolHandler.execute(request, "", "");
		if (response == null) {
			return null;
		}

		String strResult = response.getStringResult();
		System.out.println("strResult=>"+strResult);
		return strResult;
	}
	
	/**
	 * @param sParaTemp
		map.put("keyword","显瘦简约百"); 搜索关键字
	 * @param Url http://localhost:8080/111_index_search/sltRouter?
	 * return 返回品牌集合
	 */
	public static String buildRequestForSearchFilter(Map<String, String> sParaTemp,String Url)
			throws Exception {
		sParaTemp.put("parnterid", "1001");
		sParaTemp.put("method", "search");
		
		sParaTemp.put("from", "1");
		sParaTemp.put("sort", "1");
		sParaTemp.put("order", "2");
		sParaTemp.put("pricea", "0");
		sParaTemp.put("priceb", "90000");
		sParaTemp.put("typeid", "-1");
		sParaTemp.put("brandid", "-1");
		sParaTemp.put("filtertype","2");
		sParaTemp.put("page", "1");
		sParaTemp.put("size", "100");
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp);
		test(sPara);
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset(ClientConfig.input_charset);

		request.setParameters(generatNameValuePair(sPara));
		request.setUrl(Url);
		System.out.println(request.getUrl());

		HttpResponse response = httpProtocolHandler.execute(request, "", "");
		if (response == null) {
			return null;
		}

		String strResult = response.getStringResult();
		System.out.println("strResult=>"+strResult);
		return strResult;
	}
	
	/**
	 * 生成签名结果
	 * 
	 * @param sPara
	 *            要签名的数组
	 * @return 签名结果字符串
	 */
	public static String buildRequestMysign(Map<String, String> sPara) {
		String prestr = ClientCore.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String mysign = "";
		if (ClientConfig.sign_type.equals("MD5")) {
			mysign = MD5.sign(prestr, ClientConfig.key, ClientConfig.input_charset);
		}
		return mysign;
	}

	/**
	 * 生成要请求给的参数数组
	 * 
	 * @param sParaTemp
	 *            请求前的参数数组
	 * @return 要请求的参数数组
	 */
	private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
		// 除去数组中的空值和签名参数
		Map<String, String> sPara = ClientCore.paraFilter(sParaTemp);
		// 生成签名结果
		String mysign = buildRequestMysign(sPara);

		// 签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);
		sPara.put("sign_type", ClientConfig.sign_type);

		return sPara;
	}

	/**
	 * 建立请求，以表单HTML形式构造（默认）
	 * 
	 * @param sParaTemp
	 *            请求参数数组
	 * @param strMethod
	 *            提交方式。两个值可选：post、get
	 * @param strButtonName
	 *            确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName,String Url) {
//		String YAO_GATEWAY_NEW = Config.getConfigString("lucene.search.url");

		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp);
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + Url
				+ "_input_charset=" + ClientConfig.input_charset + "\" method=\"" + strMethod + "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);

			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

		return sbHtml.toString();
	}

	


	/**
	 * MAP类型数组转换成NameValuePair类型
	 * 
	 * @param properties
	 *            MAP类型数组
	 * @return NameValuePair类型数组
	 */
	private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
		NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
		}

		return nameValuePair;
	}

	/**
	 * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
	 * 
	 * @return 时间戳字符串
	 * @throws IOException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	public static String query_timestamp(String Url) throws MalformedURLException, DocumentException, IOException {
//		String YAO_GATEWAY_NEW = Config.getConfigString("lucene.search.url");

		// 构造访问query_timestamp接口的URL串
		String strUrl = Url + "service=query_timestamp&partner=" + ClientConfig.partner
				+ "&_input_charset" + ClientConfig.input_charset;
		StringBuffer result = new StringBuffer();

		SAXReader reader = new SAXReader();
		Document doc = reader.read(new URL(strUrl).openStream());

		List<Node> nodeList = doc.selectNodes("//alipay/*");

		for (Node node : nodeList) {
			// 截取部分不需要解析的信息
			if (node.getName().equals("is_success") && node.getText().equals("T")) {
				// 判断是否有成功标示
				List<Node> nodeList1 = doc.selectNodes("//response/timestamp/*");
				for (Node node1 : nodeList1) {
					result.append(node1.getText());
				}
			}
		}

		return result.toString();
	}

	public static void  test(Map<String,String> paraMap) {
		String url="";
        for (String key : paraMap.keySet()) {
            url=url+key+"="+paraMap.get(key)+"&";
        } 
        System.out.println(url);

	}


}
