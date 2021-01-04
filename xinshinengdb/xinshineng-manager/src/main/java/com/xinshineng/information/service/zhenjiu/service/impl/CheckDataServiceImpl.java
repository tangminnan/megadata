package com.xinshineng.information.service.zhenjiu.service.impl;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
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

import com.xinshineng.information.dao.zhenjiu.CheckDataDao;
import com.xinshineng.information.dao.zhenjiu.zhenjiuUserDao;
import com.xinshineng.information.domain.zhenjiu.CheckDataDO;
import com.xinshineng.information.domain.zhenjiu.zhenjiuUserDO;
import com.xinshineng.information.service.zhenjiu.service.CheckDataService;
import com.xinshineng.common.config.BootdoConfig;




@Service
public class CheckDataServiceImpl implements CheckDataService {
	@Autowired
	private CheckDataDao checkDataDao;
	@Autowired
	private zhenjiuUserDao userDao;
	@Autowired
	private BootdoConfig bootdoConfig;
	
	@Override
	public CheckDataDO get(Integer id){
		return checkDataDao.get(id);
	}
	
	@Override
	public List<CheckDataDO> list(Map<String, Object> map){
		return checkDataDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return checkDataDao.count(map);
	}
	
	@Override
	public int save(CheckDataDO checkData){
		return checkDataDao.save(checkData);
	}
	
	@Override
	public int update(CheckDataDO checkData){
		return checkDataDao.update(checkData);
	}
	
	@Override
	public int remove(Integer id){
		return checkDataDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return checkDataDao.batchRemove(ids);
	}

	@Override
	public void downloadExportExcel(Integer id, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		XWPFDocument doc;
		InputStream is;
		zhenjiuUserDO userDO = userDao.get(id);
		try{
			List<CheckDataDO> studentList = checkDataDao.getStudentId(id);
			for (int i = 0; i < studentList.size(); i++) {
				params.put("${userName}", userDO.getNickname());
				params.put("${phone}", userDO.getPhone());		
				params.put("${checkor}", studentList.get(i).getCheckor()==null?"":studentList.get(i).getCheckor());
				params.put("${checkorCompany}", studentList.get(i).getCheckorCompany()==null?"":studentList.get(i).getCheckorCompany());
				params.put("${checkDate}", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(studentList.get(i).getCheckDate()));
				params.put("${firstSecond}", studentList.get(i).getFirstSecond()==null?"":studentList.get(i).getFirstSecond());
				params.put("${refractionist}", studentList.get(i).getRefractionist()==null?"":studentList.get(i).getRefractionist());
				if(studentList.get(i).getCheckEyePositionNear()!=null){
					if(studentList.get(i).getCheckEyePositionNear() == 1){
						params.put("${checkEyePositionNear}", "正位");
					}else if(studentList.get(i).getCheckEyePositionNear() == 2){
						params.put("${checkEyePositionNear}", "内隐斜");
					}else if(studentList.get(i).getCheckEyePositionNear() == 3){
						params.put("${checkEyePositionNear}", "外隐斜");
					}
				}
				if(studentList.get(i).getCheckEyePositionFar()!=null){
					if(studentList.get(i).getCheckEyePositionFar() == 1){
						params.put("${checkEyePositionFar}", "正位");
					}else if(studentList.get(i).getCheckEyePositionFar() == 2){
						params.put("${checkEyePositionFar}", "内隐斜");
					}else if(studentList.get(i).getCheckEyePositionFar() == 3){
						params.put("${checkEyePositionFar}", "外隐斜");
					}
				}
				if(studentList.get(i).getWorthFour()!=null){
					if(studentList.get(i).getWorthFour() == 1){
						params.put("${worthFour}", "2个(红)");
					}else if(studentList.get(i).getWorthFour() == 2){
						params.put("${worthFour}", "3个(绿)");
					}else if(studentList.get(i).getWorthFour() == 3){
						params.put("${worthFour}", "4个");
					}else if(studentList.get(i).getWorthFour() == 4){
						params.put("${worthFour}", "5个(2红3绿)");
					}
				}
				params.put("${stereopsis}", studentList.get(i).getStereopsis()==null?"":studentList.get(i).getStereopsis());
				
				params.put("${nakedEyeOd}", studentList.get(i).getNakedEyeOd()==null?"":studentList.get(i).getNakedEyeOd());
				params.put("${nakedEyeOs}", studentList.get(i).getNakedEyeOs()==null?"":studentList.get(i).getNakedEyeOs());
				
				params.put("${eyePressureOd}", studentList.get(i).getEyePressureOd()==null?"":studentList.get(i).getEyePressureOd());
				params.put("${eyePressureOs}", studentList.get(i).getEyePressureOs()==null?"":studentList.get(i).getEyePressureOs());
				
				params.put("${formerEyeOd}", studentList.get(i).getFormerEyeOd()==null?"":studentList.get(i).getFormerEyeOd());
				params.put("${formerEyeOs}", studentList.get(i).getFormerEyeOs()==null?"":studentList.get(i).getFormerEyeOs());
				
				params.put("${correctSightOd}", studentList.get(i).getCorrectSightOd()==null?"":studentList.get(i).getCorrectSightOd());
				params.put("${correctSightOs}", studentList.get(i).getCorrectSightOs()==null?"":studentList.get(i).getCorrectSightOs());
				
				params.put("${sAutorefractionOd}", studentList.get(i).getsAutorefractionOd()==null?"":studentList.get(i).getsAutorefractionOd());
				params.put("${cAutorefractionOd}", studentList.get(i).getcAutorefractionOd()==null?"":studentList.get(i).getcAutorefractionOd());
				params.put("${aAutorefractionOd}", studentList.get(i).getaAutorefractionOd()==null?"":studentList.get(i).getaAutorefractionOd());
				
				params.put("${sAutorefractionOs}", studentList.get(i).getsAutorefractionOs()==null?"":studentList.get(i).getsAutorefractionOs());
				params.put("${cAutorefractionOs}", studentList.get(i).getcAutorefractionOs()==null?"":studentList.get(i).getcAutorefractionOs());
				params.put("${aAutorefractionOs}", studentList.get(i).getaAutorefractionOs()==null?"":studentList.get(i).getaAutorefractionOs());
			
				params.put("${sComprehensiveOptometryOd}", studentList.get(i).getsComprehensiveOptometryOd()==null?"":studentList.get(i).getsComprehensiveOptometryOd());
				params.put("${cComprehensiveOptometryOd}", studentList.get(i).getcComprehensiveOptometryOd()==null?"":studentList.get(i).getcComprehensiveOptometryOd());
				params.put("${aComprehensiveOptometryOd}", studentList.get(i).getaComprehensiveOptometryOd()==null?"":studentList.get(i).getaComprehensiveOptometryOd());
				
				params.put("${sComprehensiveOptometryOs}", studentList.get(i).getsComprehensiveOptometryOs()==null?"":studentList.get(i).getsComprehensiveOptometryOs());
				params.put("${cComprehensiveOptometryOs}", studentList.get(i).getcComprehensiveOptometryOs()==null?"":studentList.get(i).getcComprehensiveOptometryOs());
				params.put("${aComprehensiveOptometryOs}", studentList.get(i).getaComprehensiveOptometryOs()==null?"":studentList.get(i).getaComprehensiveOptometryOs());
				
				params.put("${eyeNra}", studentList.get(i).getEyeNra()==null?"":studentList.get(i).getEyeNra());
				params.put("${eyePra}", studentList.get(i).getEyePra()==null?"":studentList.get(i).getEyePra());
				params.put("${eyeBcc}", studentList.get(i).getEyeBcc()==null?"":studentList.get(i).getEyeBcc());
				params.put("${eyeAca}", studentList.get(i).getEyeAca()==null?"":studentList.get(i).getEyeAca());
				params.put("${eyeNpc}", studentList.get(i).getEyeNpc()==null?"":studentList.get(i).getEyeNpc());
				
				params.put("${ampOd}", studentList.get(i).getAmpOd()==null?"":studentList.get(i).getAmpOd());
				params.put("${ampOs}", studentList.get(i).getAmpOs()==null?"":studentList.get(i).getAmpOs());
				params.put("${ampOu}", studentList.get(i).getAmpOu()==null?"":studentList.get(i).getAmpOu());
				
				params.put("${afOd}", studentList.get(i).getAfOd()==null?"":studentList.get(i).getAfOd());
				params.put("${afOs}", studentList.get(i).getAfOs()==null?"":studentList.get(i).getAfOs());
				params.put("${afOu}", studentList.get(i).getAfOu()==null?"":studentList.get(i).getAfOu());
				
				params.put("${axialLengthOd}", studentList.get(i).getAxialLengthOd()==null?"":studentList.get(i).getAxialLengthOd());
				params.put("${kchmFirstOd}", studentList.get(i).getKchmFirstOd()==null?"":studentList.get(i).getKchmFirstOd());
				params.put("${kchmSecondOd}", studentList.get(i).getKchmSecondOd()==null?"":studentList.get(i).getKchmSecondOd());
				params.put("${cornealDiameterOd}", studentList.get(i).getCornealDiameterOd()==null?"":studentList.get(i).getCornealDiameterOd());
				params.put("${anteriorChamberDepthOd}", studentList.get(i).getAnteriorChamberDepthOd()==null?"":studentList.get(i).getAnteriorChamberDepthOd());
				params.put("${crystalThicknessOd}", studentList.get(i).getCrystalThicknessOd()==null?"":studentList.get(i).getCrystalThicknessOd());
				params.put("${vitreousCavityOd}", studentList.get(i).getVitreousCavityOd()==null?"":studentList.get(i).getVitreousCavityOd());
				
				params.put("${axialLengthOs}", studentList.get(i).getAxialLengthOs()==null?"":studentList.get(i).getAxialLengthOs());
				params.put("${kchmFirstOs}", studentList.get(i).getKchmFirstOs()==null?"":studentList.get(i).getKchmFirstOs());
				params.put("${kchmSecondOs}", studentList.get(i).getKchmSecondOs()==null?"":studentList.get(i).getKchmSecondOs());
				params.put("${cornealDiameterOs}", studentList.get(i).getCornealDiameterOs()==null?"":studentList.get(i).getCornealDiameterOs());
				params.put("${anteriorChamberDepthOs}", studentList.get(i).getAnteriorChamberDepthOs()==null?"":studentList.get(i).getAnteriorChamberDepthOs());
				params.put("${crystalThicknessOs}", studentList.get(i).getCrystalThicknessOs()==null?"":studentList.get(i).getCrystalThicknessOs());
				params.put("${vitreousCavityOs}", studentList.get(i).getVitreousCavityOs()==null?"":studentList.get(i).getVitreousCavityOs());
				
				String fileNameInResource = "exportTable.docx";  
				is = getClass().getClassLoader().getResourceAsStream(fileNameInResource);       
				doc = new XWPFDocument(is);
				this.replaceInPara(doc, params); 
				this.replaceInTable(doc, params);
				doc.write(new FileOutputStream(bootdoConfig.getPoiword()+new File(new String(new SimpleDateFormat("yyyyMMddhhmmss").format(studentList.get(i).getCheckDate()).getBytes(),"iso-8859-1")+".docx")));
			}
			craeteZipPath(bootdoConfig.getPoiword(),response);
			
		}catch (Exception e) {
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
    
    /** 
     * 替换表格里面的变量 
     * 
     * @param doc    要替换的文档 
     * @param params 参数 
     */  
    public void replaceInTable(XWPFDocument doc, Map<String, Object> params) {  
        Iterator<XWPFTable> iterator = doc.getTablesIterator();  
        XWPFTable table;  
        List<XWPFTableRow> rows;  
        List<XWPFTableCell> cells;  
        List<XWPFParagraph> paras;  
        while (iterator.hasNext()) {  
            table = iterator.next();  
            rows =  table.getRows();
            for (int i=0;i<rows.size();i++) {
            	if(i<38 || params.containsKey("${n}")){
            		cells = rows.get(i).getTableCells();  
	            	for (XWPFTableCell cell : cells) {  
	            		paras = cell.getParagraphs(); 
	            		for (XWPFParagraph para : paras) {  
	            			this.replaceInPara(para, params);  
	            		}  
	            	}
            	}
            }  
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
    
    /**
	 * 
	 * 复制图片到target
	 * @param target
	 * @param picture
	 * @throws IOException
	 * @throws InvalidFormatException
	 * 
	 */
	public void copyPicture(XWPFRun target, XWPFPicture picture)throws IOException, InvalidFormatException {

	    String filename = picture.getPictureData().getFileName();
	    InputStream pictureData = new ByteArrayInputStream(picture
	            .getPictureData().getData());
	    int pictureType = picture.getPictureData().getPictureType();
	    int width = (int) picture.getCTPicture().getSpPr().getXfrm().getExt()
	            .getCx();

	    int height = (int) picture.getCTPicture().getSpPr().getXfrm().getExt()
	            .getCy();

	    // target.addBreak();
	    target.addPicture(pictureData, pictureType, filename, width, height);
	    // target.addBreak(BreakType.PAGE);
	}
	
    
	/**
	 * 
	 * 复制RUN，从source到target
	 * @param target
	 * @param source
	 * 
	 */
	public void copyRun(XWPFRun target, XWPFRun source) {
	    // 设置run属性
	    target.getCTR().setRPr(source.getCTR().getRPr());
	    // 设置文本
	    target.setText(source.text());
	    // 处理图片
	    List<XWPFPicture> pictures = source.getEmbeddedPictures();

	    for (XWPFPicture picture : pictures) {
	        try {
	            copyPicture(target, picture);
	        } catch (InvalidFormatException e) {
//	            logger.error("copyRun", e);
	        } catch (IOException e) {
//	            logger.error("copyRun", e);
	        }
	    }
	}
	
    
    /**
	 * 复制段落，从source到target
	 * @param target
	 * @param source
	 * 
	 */
	public void copyParagraph(XWPFParagraph target, XWPFParagraph source) {

	    // 设置段落样式
	    target.getCTP().setPPr(source.getCTP().getPPr());

	    // 移除所有的run
	    for (int pos = target.getRuns().size() - 1; pos >= 0; pos--) {
	        target.removeRun(pos);
	    }

	    // copy 新的run
	    for (XWPFRun s : source.getRuns()) {
	        XWPFRun targetrun = target.createRun();
	        copyRun(targetrun, s);
	    }

	}
    
    
    /**
	 * 复制单元格，从source到target
	 * @param target
	 * @param source
	 * 
	 */
	public void copyTableCell(XWPFTableCell target, XWPFTableCell source) {
	    // 列属性
	    if (source.getCTTc() != null) {
	        target.getCTTc().setTcPr(source.getCTTc().getTcPr());
	    }
	    // 删除段落
	    for (int pos = 0; pos < target.getParagraphs().size(); pos++) {
	        target.removeParagraph(pos);
	    }
	    // 添加段落
	    for (XWPFParagraph sp : source.getParagraphs()) {
	        XWPFParagraph targetP = target.addParagraph();
	        copyParagraph(targetP, sp);
	    }
	}
    
    /**
	 * 
	 * 复制行，从source到target
	 * @param target
	 * @param source
	 * 
	 */
	public void copyTableRow(XWPFTableRow target, XWPFTableRow source) {
	    // 复制样式
	    if (source.getCtRow() != null) {
	        target.getCtRow().setTrPr(source.getCtRow().getTrPr());
	    }
	    // 复制单元格
	    for (int i = 0; i < source.getTableCells().size(); i++) {
	        XWPFTableCell cell1 = target.getCell(i);
	        XWPFTableCell cell2 = source.getCell(i);
	        if (cell1 == null) {
	            cell1 = target.addNewTableCell();
	        }
	        copyTableCell(cell1, cell2);
	    }
	}
    /**
	 * 行复制
	 */
	 private void copyTableRow(XWPFDocument doc) {
		XWPFTable table = doc.getTables().get(0);
		copyTableRow(table.createRow(),table.getRows().get(0));
	 }
	
		
		
    
    /** 
     * 关闭输入流 
     * 
     * @param is 
     */  
    public void close(InputStream is) {  
        if (is != null) {  
            try {  
                is.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    /** 
     * 关闭输出流 
     * 
     * @param os 
     */  
    public void close(OutputStream os) {  
        if (os != null) {  
            try {  
                os.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }
	
	
	
}
