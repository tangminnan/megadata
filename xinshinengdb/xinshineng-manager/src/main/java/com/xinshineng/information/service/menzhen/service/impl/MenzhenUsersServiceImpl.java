package com.xinshineng.information.service.menzhen.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.common.config.BootdoConfig;
import com.xinshineng.information.dao.menzhen.MenzhenUsersDao;
import com.xinshineng.information.domain.menzhen.MenzhenUsersDO;
import com.xinshineng.information.service.menzhen.service.MenzhenUsersService;




@Service
public class MenzhenUsersServiceImpl implements MenzhenUsersService {
	@Autowired
	private MenzhenUsersDao userDao;
	@Autowired
	private BootdoConfig bootdoConfig;
	

	@Override
	public MenzhenUsersDO get(Long uId){
		return userDao.get(uId);
	}
	
	@Override
	public List<MenzhenUsersDO> list(Map<String, Object> map){
		return userDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return userDao.count(map);
	}
	
	@Override
	public int save(MenzhenUsersDO user){
		return userDao.save(user);
	}
	
	@Override
	public int update(MenzhenUsersDO user){
		return userDao.update(user);
	}
	
	@Override
	public int remove(Long uId){
		return userDao.remove(uId);
	}
	
	@Override
	public int batchRemove(Long[] uIds){
		return userDao.batchRemove(uIds);
	}

	@Override
	public List<MenzhenUsersDO> lists() {
		return userDao.lists();
	}

	@Override
	public List<MenzhenUsersDO> getFileByid(Long uid) {
		return userDao.getFileByid(uid);
	}
	
	@Override
	public List<MenzhenUsersDO> getfileByname(Map<String, Object> map) {
		return userDao.getfileByname(map);
	}

	@Override
	public MenzhenUsersDO getNameByimg(String uimg) {
		return userDao.getNameByimg(uimg);
	}

	@Override
	public int removeByimg(String uimg) {
		return userDao.removeByimg(uimg);
	}

	@Override
	public List<Map<String, Object>> exeList(Map<String, Object> map) {
		return userDao.exeList(map);
	}

	@Override
	public List<MenzhenUsersDO> selectlist(Map<String, Object> map) {
		return userDao.selectlist(map);
	}

	@Override
	public int updateUser(Long uid) {
		return userDao.updateUser(uid);
	}
	
	/**
	 * 二维码下载
	 */
	@Override
	public void downloadErweima(Long[] ids, HttpServletResponse response) {
		try{
			for(int i=0;i<ids.length;i++){
				Map<String, Object> params = new HashMap<String, Object>();  
				MenzhenUsersDO usersDO=userDao.get(ids[i]);
			
				File file = new File(bootdoConfig.getUploadPath()+usersDO.getQRCode().substring(usersDO.getQRCode().lastIndexOf("/")+1));
				if(file==null ||  !file.exists())
					continue;
				params.put("${uname}",usersDO.getUname());  
				params.put("${uidcard}",usersDO.getUidcard());  
				params.put("${ugender}", usersDO.getUgender()==null?"":usersDO.getUgender()==1? "女":"男");
				params.put("${uorganization}",usersDO.getUorganization()==null?"":usersDO.getUorganization());  
				params.put("${ugrand}",usersDO.getUgrand()==null?"":usersDO.getUgrand());  
			
				if(usersDO.getQRCode()==null) continue;
				params.put("${QRCode}", new FileInputStream(new File(bootdoConfig.getUploadPath()+usersDO.getQRCode().substring(usersDO.getQRCode().lastIndexOf("/")+1))));
				String fileNameInResource = "erweima.docx";  
				InputStream is = getClass().getClassLoader().getResourceAsStream(fileNameInResource);       
				XWPFDocument doc = new XWPFDocument(is);
				this.replaceInPara(doc, params);  
				doc.write(new FileOutputStream(bootdoConfig.getPoiword()+new File(new String(usersDO.getUidcard().getBytes(),"iso-8859-1")+".docx")));
			}
			craeteZipPath(bootdoConfig.getPoiword(),response);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			File file=new File(bootdoConfig.getPoiword());
	        if(file.exists()) {
	           File[] files = file.listFiles();
	           for(File f :files)
	              f.delete();
	        }
	     }  
	}

	public static void craeteZipPath(String path,HttpServletResponse response) throws IOException{  

        ZipOutputStream zipOutputStream = null;
        OutputStream output=response.getOutputStream();  
//        response.reset();
        response.setHeader("Content-disposition", "attachment; filename="+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".zip");  
        response.setContentType("application/zip");  
        zipOutputStream = new ZipOutputStream(output,Charset.forName("GBK"));  
        File[] files = new File(path).listFiles();  
        FileInputStream fileInputStream = null;  
        byte[] buf = new byte[1024];  
        int len = 0;  
        if(files!=null && files.length > 0){  
            for(File wordFile:files){  
                String fileName = wordFile.getName();  
                fileInputStream = new FileInputStream(wordFile);  
                //放入压缩zip包中;  
                zipOutputStream.putNextEntry(new ZipEntry(fileName));  

                //读取文件;  
                while((len=fileInputStream.read(buf)) >0){  
                    zipOutputStream.write(buf, 0, len);  
                }  
                //关闭;  
                zipOutputStream.closeEntry();  
                if(fileInputStream != null){  
                    fileInputStream.close();  
                }  
            }  
        }  

        if(zipOutputStream !=null){  
            zipOutputStream.close();  
        }  
    } 

	
	/** 
     * 替换段落里面的变量 
     * 
     * @param doc    要替换的文档 
     * @param params 参数 
     */  
    public void replaceInPara(XWPFDocument doc, Map<String, Object> params) {  
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();  
        XWPFParagraph para;  
        while (iterator.hasNext()) {  
            para = iterator.next();  
            this.replaceInPara(para, params);  
        }  
    }  
    XWPFDocument  temp = new XWPFDocument();
    /** 
     * 替换段落里面的变量 
     * 
     * @param para   要替换的段落 
     * @param params 参数 
     */  
    public void replaceInPara(XWPFParagraph para, Map<String, Object> params) {  
        List<XWPFRun> runs;  
        if (this.matcher(para.getParagraphText()).find()) {  
            runs = para.getRuns();  
            int start = -1;  
            int end = -1;  
            String str = "";  
            for (int i = 0; i < runs.size(); i++) {  
                XWPFRun run = runs.get(i);  
                String runText = run.toString().trim();  
               if ('$' == runText.charAt(0)&&'{' == runText.charAt(1)) {  
                    start = i;  
                }  
                if ((start != -1)) {  
                    str += runText;  
               }  
                if ('}' == runText.charAt(runText.length() - 1)) {  
                    if (start != -1) {  
                        end = i;  
                        break;  
                    }  
                }
            } 
           for (int i = start; i <= end; i++) {  
               para.removeRun(i);  
               i--;  
                end--;  
            }  
           String s="";
            for (String key : params.keySet()) {  
                if (str.trim().equals(key) && !(params.get(key) instanceof InputStream)) {  
                    para.createRun().setText((String) params.get(key));  
                    s=key;
                    break; 
                }
                else if(str.equals(key) && params.get(key) instanceof InputStream){//插入图片
                	try {
						XWPFRun imageCellRunn = para.createRun();
						imageCellRunn.addPicture((InputStream)params.get(key), getPicFormat("browser.png"), "browser.png", Units.toEMU(200), Units.toEMU(200));
						s=key;
						System.out.println(key+  "  dssssssssssssssssssssssss");
						break;
                	} catch (InvalidFormatException e) {
						e.printStackTrace();
					}catch (IOException e) {
						e.printStackTrace();
					} 
                }
            } 
            if(!"".equals(s))
            	params.remove(s);
       }  
    }  
    
    public int getPicFormat(String imgFile){
    	int format = 0;
    	 if(imgFile.endsWith(".emf")) format = XWPFDocument.PICTURE_TYPE_EMF;
         else if(imgFile.endsWith(".wmf")) format = XWPFDocument.PICTURE_TYPE_WMF;
         else if(imgFile.endsWith(".pict")) format = XWPFDocument.PICTURE_TYPE_PICT;
         else if(imgFile.endsWith(".jpeg") || imgFile.endsWith(".jpg")) format = XWPFDocument.PICTURE_TYPE_JPEG;
         else if(imgFile.endsWith(".png")) format = XWPFDocument.PICTURE_TYPE_PNG;
         else if(imgFile.endsWith(".dib")) format = XWPFDocument.PICTURE_TYPE_DIB;
         else if(imgFile.endsWith(".gif")) format = XWPFDocument.PICTURE_TYPE_GIF;
         else if(imgFile.endsWith(".tiff")) format = XWPFDocument.PICTURE_TYPE_TIFF;
         else if(imgFile.endsWith(".eps")) format = XWPFDocument.PICTURE_TYPE_EPS;
         else if(imgFile.endsWith(".bmp")) format = XWPFDocument.PICTURE_TYPE_BMP;
         else if(imgFile.endsWith(".wpg")) format = XWPFDocument.PICTURE_TYPE_WPG;
        return format;
    }
    
    /** 
     * 正则匹配字符串 
     * 
     * @param str 
     * @return 
     */  
    private Matcher matcher(String str) {  
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);  
        Matcher matcher = pattern.matcher(str);  
        return matcher;  
    }
	
}
