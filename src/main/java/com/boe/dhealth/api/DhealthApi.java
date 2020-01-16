package com.boe.dhealth.api;

import java.io.IOException;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.boe.dhealth.domain.JsonResponse;
import com.boe.dhealth.service.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.boe.common.sdk.dto.JsonResponse;
import com.boe.dhealth.service.DhealthService;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DhealthApi {
	
	@Autowired
	private DhealthService dhealthService;


	/**
	 *  正面关键点
	 * @param file  上传的图片
	 * @param code   用户编码
	 * @return
	 */
	@PostMapping("/body/front")
	public  JsonResponse front(MultipartFile file,String code){
		// 入参验证
		if(StringUtils.isEmpty(code) || file==null){
			return  JsonResponse.fail("参数异常");
		}
		// 文件不能超过4M
		boolean file0Size = FileUtil.checkFileSize(file.getSize(),4,"M");
		if (!file0Size){
			return  JsonResponse.fail("文件太大");
		}

		Map<String, Object> front=null;
		try {
			front = dhealthService.front(file, code);
		} catch (Exception e) {
			e.printStackTrace();
			return  JsonResponse.fail("图片不合法");
		}
		return  new JsonResponse(front);
	}



	/**
	 *  正面关键点
	 * @param file  上传的图片
	 * @return
	 */
	@PostMapping("/body/side")
	public  JsonResponse side(MultipartFile file){
		// 入参验证
		if(file==null){
			return  JsonResponse.fail("参数异常");
		}

		Map<String, Object> front=null;
		try {
			front = dhealthService.side(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  new JsonResponse(front);
	}

}
