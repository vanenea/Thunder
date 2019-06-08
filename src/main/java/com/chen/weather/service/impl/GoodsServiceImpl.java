package com.chen.weather.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chen.util.CommonUtil;
import com.chen.util.HttpUtil;
import com.chen.weather.model.Goods;
import com.chen.weather.service.GoodsService;

@Service
public class GoodsServiceImpl implements GoodsService {

	/**
	 * 日志
	 */
	private static final Logger LOGGER = Logger.getLogger(GoodsServiceImpl.class);

	public Goods getGoodsInfor() {
		HttpUtil httpUtil = new HttpUtil();
		Goods goods = new Goods();
		try {
			String url = "https://mdskip.taobao.com/core/initItemDetail.htm?itemId=592698955534";
			LOGGER.info("###### 商品查询  URL " + url + "######");
			httpUtil.setHeader("cookie", "miid=1135350365352516307; cna=jhUUFNwhZwQCAXPWFQIexuVT; enc=3MY7DMahcKNWSDVtcnYbgAN744f3XaiOwj2SDnZbsBaGLFiRvF5u5EBP%2BR4p4SPBePTTfyp5dqlG5MPrb1cvRg%3D%3D; hng=CN%7Czh-CN%7CCNY%7C156; thw=cn; ali_ab=122.246.68.183.1551522533141.9; UM_distinctid=169d78d6aeb5df-0265362dbe4484-6b1b1279-1fa400-169d78d6aec91c; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; v=0; _tb_token_=e7e47be05487e; unb=1068320397; uc1=cookie16=Vq8l%2BKCLySLZMFWHxqs8fwqnEw%3D%3D&cookie21=U%2BGCWk%2F7pY%2FF&cookie15=WqG3DMC9VAQiUQ%3D%3D&existShop=false&pas=0&cookie14=UoTZ7YMWFHOfiQ%3D%3D&tag=8&lng=zh_CN; sg=v7c; t=9f93df81dde8dcaa192be594e6e11f50; _l_g_=Ug%3D%3D; skt=7d689027c19c4f6e; cookie2=14a5931cfcecfc18b1a1244e92b441e8; cookie1=Aia6TYhZF0JF3ysBxAWV3KoRNtodiLHR%2FeC7Mr%2FDhlM%3D; csg=dfd0b773; uc3=vt3=F8dBy3jfCo0C%2BtRRbP0%3D&id2=UoH5bwtirSTExA%3D%3D&nk2=j2SkCtI%3D&lg2=Vq8l%2BKCLz3%2F65A%3D%3D; existShop=MTU1OTk4NTE3MA%3D%3D; tracknick=%5Cu70AB%5Cu99A5v; lgc=%5Cu70AB%5Cu99A5v; _cc_=UIHiLt3xSw%3D%3D; dnk=%5Cu70AB%5Cu99A5v; _nk_=%5Cu70AB%5Cu99A5v; cookie17=UoH5bwtirSTExA%3D%3D; tg=0; mt=np=; isg=BKqqAcHigp7WvQ4yVdk13kcr-xCMsywY-Q1F2DRjS_2IZ0ohHK_7hXaW89Nel6YN; l=bBEZ4QCHvI73eU4GBOCi5uI8UN7O_IRAguPRwdfXi_5dN6T1MzQOlF4I-F96Vj5RscLB4g3r6KJ9-etkw; x5sec=7b226d616c6c64657461696c736b69703b32223a226230623764616639346665376562373466353265613831636232653931393033434e796237756346454a546f364b32497870474d7a674561444445774e6a677a4d6a417a4f5463374e673d3d227d; ucn=unszyun");
			httpUtil.setHeader("referer", "https://detail.tmall.com/item.htm?spm=a1z10.4-b-s.w4004-17006249888.4.5b0f7b9fHeiZ4A&pvid=bedff20b-dc5d-45d2-a905-92be0c1ea890&pos=2&acm=03131.1003.1.702582&id=592698955534&scm=1007.12940.25805.100200300000000&skuId=4248070790759");
			String httpGet = httpUtil.httpGet("url", "UTF-8");
			LOGGER.info("###### 商品查询 RESPONSE " + httpGet +" ######");
			JSONObject jo = JSON.parseObject(httpGet);
			jo = JSON.parseObject(jo.getString("defaultModel"));
			
			//价格
			JSONObject priceInfo = JSON.parseObject(JSON.parseObject(jo.getString("itemPriceResultDO")).getString("priceInfo"));
			//4248070790759
			priceInfo = JSON.parseObject(priceInfo.getString("4248070790759"));
			
			goods.setName("SUPIMA COTTON 170/92A/M 蓝色");
			goods.setPrice(priceInfo.getString("price"));
			if(priceInfo.getBoolean("areaSold")) {
				goods.setIsSold("1");
			} else {
				goods.setIsSold("0");
			}
			JSONObject skuQuantity = JSON.parseObject(JSON.parseObject(jo.getString("inventoryDO")).getString("skuQuantity"));
			skuQuantity = JSON.parseObject(priceInfo.getString("4248070790759"));
			goods.setSkuQuantity(skuQuantity.getInteger("quantity"));
			
			//库存大于零且在售
			if(goods.getSkuQuantity() > 0 && "1".equals(goods.getIsSold()) && Double.parseDouble(goods.getPrice())<=59.00 ) {
				CommonUtil cu = new CommonUtil();
				String text = "您好呀！。 您想买的"+ goods.getName() +"已经达到您的预期价格"+goods.getPrice() + ", 可以考虑购买!!!";
				cu.sendMail("商品通知", text);
			}
		} catch (Exception e) {
			LOGGER.info("#### 查询商品失败 ######", e );
			CommonUtil cu = new CommonUtil();
			String text = "您查询商品程序出错啦,请去日志文件中查看原因 !!! </br> <code>"+ e.getMessage() +"</code>";
			cu.sendMail("程序错误通知", text);
			throw new RuntimeException("查询商品失败", e);
		} 
		LOGGER.info("#### 查询商品成功 "+goods);
		return goods;
	} 
	
	public static void main(String[] args) {
		GoodsServiceImpl g = new GoodsServiceImpl();
		g.getGoodsInfor();
	}
}
