package com.chen.weather.service.impl;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.chen.util.CommonUtil;
import com.chen.util.HttpUtil;
import com.chen.util.StringsUtil;
import com.chen.weather.model.WeatherData;
import com.chen.weather.service.WeatherService;

@Service
public class WeatherServiceImpl implements WeatherService {

    /**
     * 日志
     */
    private final static Logger LOGGER = Logger.getLogger(WeatherServiceImpl.class);


    //初始化ascClient需要的几个参数
    @Value("#{sms.product}")
    private String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）

    @Value("#{sms.domain}")
    private String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）

    //替换成你的AK
    @Value("#{sms.accessKeyId}")
    private String accessKeyId;//你的accessKeyId,参考本文档步骤2

    @Value("#{sms.phoneNum}")
    private String phoneNum;//电话号码

    @Value("#{sms.accessKeySecret}")
    private String accessKeySecret;//你的accessKeySecret，参考本文档步骤2

    @Value("#{sms.singnalName}")
    private String signalName;//短信签名

    @Value("#{sms.templateCode}")
    private String templateCode;//短信模板

    @Autowired
    private CommonUtil commonUtil;

    @Value("#{pro.toMail}")
    private String toMail;

    @Value("#{pro.urlApi}")
    private String urlApi;

    @Value("#{sms.flag}")
    private String SMSFlag;

    @Scheduled(cron = "0 20 22 * * ?")
    public int sendMail() {
        WeatherData data = null;
        try {
            data = getWeatherData(urlApi, "utf-8");
        } catch (Exception e1) {
            LOGGER.error(e1.getMessage());
            LOGGER.info("获取天气数据异常");
            return -2;
        }
        if (data == null) {
            LOGGER.info("获取天气数据异常");
            return -2;
        }
        String text = "您好" + toMail.substring(toMail.indexOf(",") + 1) + "。" + data.getLocation() + "明天[" + data.getDate() + "]天气预报,白天" +
                data.getCond_txt_d() + ",晚间" + data.getCond_txt_n() + ",最高温度" + data.getTmp_max() + ",最低温度" + data.getTmp_min() +
                ",降水概率" + data.getPop() + ",请注意天气变化！";
        commonUtil.sendMail("天气预报", text);
        LOGGER.info("发送邮件成功");
        return 0;

    }

    @Scheduled(cron = "0 0 22 * * ?")
    public int sendSMS() {
        if ("on".equalsIgnoreCase(SMSFlag)) {
            //获取天气数据
            WeatherData data = null;
            try {
                data = getWeatherData(urlApi, "utf-8");
            } catch (Exception e1) {
                LOGGER.error(e1.getMessage());
                LOGGER.info("获取天气数据异常");
                return -2;
            }
            if (data == null) {
                LOGGER.info("获取天气数据异常");
                return -2;
            }
            int code = Integer.valueOf(data.getCond_code_d());
            if (code >= 205 && code <= 901) {
                //设置超时时间-可自行调整
                System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
                System.setProperty("sun.net.client.defaultReadTimeout", "10000");
                try {
                    //初始化ascClient,暂时不支持多region（请勿修改）
                    IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
                    DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
                    IAcsClient acsClient = new DefaultAcsClient(profile);
                    //组装请求对象
                    SendSmsRequest request = new SendSmsRequest();
                    //使用post提交
                    request.setMethod(MethodType.POST);

                    //必填:短信签名-可在短信控制台中找到
                    request.setSignName(signalName);
                    //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
                    request.setTemplateCode(templateCode);

                    //短信模板
                    JSONObject jo = new JSONObject();

                    jo.put("location", data.getLocation());
                    jo.put("date", data.getDate());
                    jo.put("cond_txt_d", data.getCond_txt_d());
                    jo.put("cond_txt_n", data.getCond_txt_n());
                    jo.put("tmp_max", data.getTmp_max());
                    jo.put("tmp_min", data.getTmp_min());
                    jo.put("pop", data.getPop());
                    //要发送的手机号和姓名
                    String[] phoneNums = phoneNum.split(";");
                    for (int i = 0; i < phoneNums.length; i++) {
                        String[] pn = phoneNums[i].split(",");
                        jo.put("name", pn[1]);
                        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
                        request.setPhoneNumbers(pn[0]);
                        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
                        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
                        request.setTemplateParam(jo.toJSONString());
                        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
                        //request.setSmsUpExtendCode("90997");
                        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
                        // request.setOutId("yourOutId");
                        //请求失败这里会抛ClientException异常
                        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
                        LOGGER.info("阿里大鱼短信服务返回参数 [ code=" + sendSmsResponse.getCode() + ", message=" + sendSmsResponse.getMessage() + " ]");
                        if (!(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK"))) {
                            //请求成功
                            LOGGER.error("发送短信异常");
                            return -1;
                        }
                    }
                    LOGGER.error("发送短信成功");
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    LOGGER.error("发送短信异常");
                    return -1;
                }
            }
            return 0;
        } else {
            LOGGER.info("发送短信开关关闭");
            return 10;
        }
    }

    public WeatherData getWeatherData(String urlApi, String encode) throws ClientProtocolException, IOException {
        LOGGER.info("获取天气数据");
        HttpUtil http = new HttpUtil();
        WeatherData data = new WeatherData();
        String result = http.httpGet(urlApi, "utf-8");
        JSONObject jo = JSON.parseObject(result);
        JSONArray ja = jo.getJSONArray("HeWeather6");
        if ("ok".equalsIgnoreCase(ja.getJSONObject(0).getString("status"))) {
            data.setLocation(ja.getJSONObject(0).getJSONObject("basic").getString("location"));
            jo = ja.getJSONObject(0).getJSONArray("daily_forecast").getJSONObject(1);
            data.setCond_code_d(jo.getString("cond_code_d"));
            data.setCond_code_n(jo.getString("cond_code_n"));
            data.setCond_txt_d(jo.getString("cond_txt_d"));
            data.setCond_txt_n(jo.getString("cond_txt_n"));
            data.setDate(jo.getString("date"));
            data.setPop(jo.getString("pop"));
            data.setHum(jo.getString("hum"));
            data.setTmp_max(jo.getString("tmp_max"));
            data.setTmp_min(jo.getString("tmp_min"));
            data.setUv_index(jo.getString("uv_index"));
            data.setVis(jo.getString("vis"));
            data.setWind_deg(jo.getString("wind_deg"));
            data.setWind_dir(jo.getString("wind_dir"));
            data.setWind_sc(jo.getString("wind_sc"));
            data.setWind_spd(jo.getString("wind_spd"));
        }
        return data;
    }

}
