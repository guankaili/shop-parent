package com.world.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;


/**
	* @author cuihuofei
	* @date   2020年1月7日 下午2:06:18
	* @version v1.0.0
	* @Description
	* GoodsNameSplitUtil.java 
	* Modification History:
	* Date                         Author          Version          Description
	---------------------------------------------------------------------------------*
	* 2020年1月7日 下午2:06:18       cuihuofei        v1.0.0           Created
*/
public class GoodsNameSplitUtil {
	public static Map<String,String> getSpec(String goodsName){
        Map<String,String> map=new HashMap<String,String>();
        map.put("size","");  //尺寸
        map.put("spec",""); //规格
        map.put("speed",""); //速级
        map.put("deco",""); //花纹
        map.put("goodsShortName","");
        if(StringUtils.isEmpty(goodsName)){
            return map;
        }
        // 示例：LH-165/65R13_LS188_77_H_FH
        String name = goodsName.replaceAll("LH-", "");
        String[] arr = name.split("_");
        if(arr.length<4){
            map.put("goodsShortName",goodsName);
            return map;
        }
        map.put("spec",arr[0]);
        map.put("deco",arr[1]);
        map.put("speed",arr[2]+arr[3]);

        if(arr.length>4){
            //如果截取的长度大于四，说明名称中携带环保类型
            map.put("goodsShortName",goodsName.substring(0,goodsName.lastIndexOf("_")));
        }else{
            map.put("goodsShortName",goodsName);
        }

        String s="0123456789";
        //尺寸取法：根据规格最后一位数字开始取，取两位
        char[] ch = arr[0].toCharArray();
        for(int i=ch.length-1;i>=0;i--){
            if(s.contains(ch[i]+"")){
                map.put("size",""+ch[i-1]+ch[i]);
                return map;
            }
        }
        return map;
    }

    public static void main(String[] args) {
        Map<String, String> map = GoodsNameSplitUtil.getSpec("路航 DL-195/65R15_DH1_91_V_FH");
        System.out.println(map);
    }
}
