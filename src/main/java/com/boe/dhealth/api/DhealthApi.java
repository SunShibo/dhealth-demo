package com.boe.dhealth.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.druid.util.StringUtils;
import com.boe.dhealth.domain.JsonResponse;
import com.boe.dhealth.service.util.FileUtil;
import com.boe.dhealth.service.util.ImageUtil;
import com.boe.dhealth.service.util.OSSClientUtil;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.boe.common.sdk.dto.JsonResponse;
import com.boe.dhealth.service.DhealthService;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class DhealthApi {
	
	@Autowired
	private DhealthService dhealthService;

	@Resource
	private FastFileStorageClient fc;

	/**
	 * 上传图片
	 * @return
	 */
	@PostMapping("/body/upload")
	public  JsonResponse upload(MultipartFile file) throws Exception {
		System.out.println("开始上传图片");
		System.out.println("开始上传图片");
		System.out.println("开始上传图片");
		// 文件不能超过6M
//		boolean file0Size = FileUtil.checkFileSize(file.getSize(),10,"M");
//		if (!file0Size){
//			return  JsonResponse.fail("文件太大");
//		}
		//上传图片压缩start
		String filePath = ImageUtil.getPCpicturePath(file, 2);
		//上传图片压缩end
		System.err.println(filePath);
		OSSClientUtil oss = new OSSClientUtil();
		String analysis = oss.uploadImg2Oss(filePath);//解析图
		Map<String, Object> front=new HashMap<>();
		front.put("path",analysis);
		System.out.println("上传图片结束");
		System.out.println("上传图片结束");
		System.out.println("上传图片结束");


		// ceshi=========元数据
		Set<MetaData> metaDataSet = new HashSet<MetaData>();
		metaDataSet.add(new MetaData("Author", "yimingge"));
		metaDataSet.add(new MetaData("CreateDate", "2016-01-05"));


		try {
			StorePath uploadFile = null;

			uploadFile = fc.uploadFile(file.getInputStream(), file.getSize(), getFileExtName(file.getOriginalFilename()), metaDataSet);
			//打印地址
			System.out.println(uploadFile.getPath());
			System.out.println(uploadFile.getPath());
			System.out.println(uploadFile.getPath());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return  new JsonResponse(front);
	}
	private String getFileExtName(String name) {
		// TODO Auto-generated method stub
		return (name.substring(name.lastIndexOf(".")+1));
	}
	/**
	 *  正面关键点
	 * @param frontPath 正面  //https://oopsstatic.oss-cn-beijing.aliyuncs.com/1580801594814.jpg
	 * @param sidePath 侧面	//https://oopsstatic.oss-cn-beijing.aliyuncs.com/1580801491858.jpg
	 * @param code  用户编码
	 * @param name 用户名
	 * @return
	 */
	@PostMapping("/body/front")
	public  JsonResponse front(String frontPath,String sidePath,String code,String name){
		// 入参验证
		System.err.println("开始评估code:"+code);
		System.err.println("开始评估code:"+code);
		System.err.println("开始评估code:"+code);
		System.err.println("开始评估code:"+code);
		System.err.println("开始评估code:"+code);
		if(StringUtils.isEmpty(code)||code.length()<=3 || StringUtils.isEmpty(name)||StringUtils.isEmpty(frontPath)||StringUtils.isEmpty(sidePath)){
			return  JsonResponse.fail("参数异常");
		}

		//Map<String, Object> front=new HashMap<>();
		try {
			System.err.println(code);
			System.err.println(code);
			System.err.println(code);
			dhealthService.front(frontPath,sidePath, code,name);
			//dhealthService.front("C:\\Users\\wy\\Desktop\\timg (2).jpg","C:\\Users\\wy\\Desktop\\timg.jpg", code,name);

		} catch (Exception e) {
			e.printStackTrace();
			return  JsonResponse.fail("图片不合法");
		}
		return  new JsonResponse(null);
	}

	/**
	 *  评估记录
	 * @param code 用户编码
	 * @return
	 */
	@GetMapping("/body/list")
	public  JsonResponse list(String code){
		// 入参验证
		System.err.println("评估记录:"+code);
		System.err.println("评估记录:"+code);
		System.err.println("评估记录:"+code);
		System.err.println("评估记录:"+code);
		System.err.println("评估记录:"+code);
		if(StringUtils.isEmpty(code)||code.length()<=3){
			return  JsonResponse.fail("参数异常");
		}
		return  new JsonResponse(dhealthService.getList(code));
	}

	/**
	 *  评估详情
	 * @param id 评估记录id
	 * @return
	 */
	@GetMapping("/body/info")
	public  JsonResponse list(Integer id){
		// 入参验证
		if(id==null){
			return  JsonResponse.fail("参数异常");
		}
		Map<String, Object> front=new HashMap<>();

		return  new JsonResponse(dhealthService.getInfo(id));
	}



	/**
	 *  侧面关键点
	 * @param side  上传的图片
	 * @return
	 */
	@PostMapping("/body/side")
	public  JsonResponse side(MultipartFile side){
		// 入参验证
		if(side==null){
			return  JsonResponse.fail("参数异常");
		}
		Map<String, Object> front=null;
//		try {
//			front = dhealthService.side(side);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return  new JsonResponse(front);
	}

    public static void main(String[] args) {
        System.out.println("linux".toLowerCase().contains("linux"));
    }

    @PostMapping("/test/test")
    public  JsonResponse test(MultipartFile side){


        String front = System.getProperty("os.name").toLowerCase();
        System.out.println(front);
        return  new JsonResponse(front);
    }

}
