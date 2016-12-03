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

public class ClientSubmitES {

	/**
	 * 提供给商户的服务接入网关URL(新)
	 */
	// private static final String YAO_GATEWAY_NEW =
//	private static final String YAO_GATEWAY_NEW = "http://common.111yao.com/sltRouter?";


	
	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
//		map.put("parnterid", "1001");
//		map.put("method", "search");
//		
		map.put("isfilter", "2");
		map.put("from", "1");
		map.put("sort", "-1");
		map.put("order", "-1");
		map.put("pricea", "-1");
		map.put("priceb", "-1");
		map.put("typeid", "-1");
		map.put("brandid", "-1");
		map.put("keyword","营养");
		map.put("page", "1");
		map.put("size", "20");
//		map.put("goodsid", "1");
		String ES_GATEWAY_URL = "http://localhost:8080/111_index_es/URIService/searchgoods?";
//		String ES_GATEWAY_URL = "http://localhost:8080/111_index_es/URIService/updategoods?";
		
		ClientSubmitES.buildRequestForSearch(map,ES_GATEWAY_URL);
//		ClientSubmitES.buildForUpdateInsert(map,ES_GATEWAY_URL);
//		Map<String, String> map1 = new HashMap<String, String>();
//		map1.put("keyword","商品");
//		ClientSubmitES.buildRequestForSearchFilter(map1,YAO_GATEWAY_URL);
	}

	/**
	 * 更新商品信息，在商品有修改或者新增的时候调用此接口
	 * goodsid:不能为空
	 * @param 
	 */
	public static String buildForUpdateInsert(Map<String, String> sParaTemp,String Url)
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
	 * 商品搜索使用此接口
	 * isfilter:1-返回条件数据 2-返回商品搜索结果
	 * from:1-pc 2-wap 3-app
	 * typeid:1-otc 2-处方药A 3-处方药B 4-非药
	 * brandid:-1 全部 
	 * keyword:搜索关键字
	 * pricea:最低价，两个价格一个为-1，就所搜全部价格
	 * priceb:最高价，两个价格一个为-1，就所搜全部价格
	 * order:1-升序 2-降序
	 * sort:1-销量 2-评论 3-价格 4-折扣率 不传值默认相关度排序
	 * page:当前页码
	 * size:每页大小
	 * Url 服务器地址url
	 * @param 
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
