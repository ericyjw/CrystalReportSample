package com.lister.Project.dao;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import com.businessobjects.samples.CRJavaHelper;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;

import javax.servlet.ServletContext;

/**
 * @author souvik.p
 *
 */
public class GenerateReport {
//	private static final String db_user="system";
//	private static final String db_pwd="kroger";
//	private static final String db_url="jdbc:oracle:thin:@localhost:1521:xe";
//	private static final String db_driver="oracle.jdbc.driver.OracleDriver";

	private static final String db_user="sa";
	private static final String db_pwd="";
	private static final String db_url="jdbc:h2:tcp://localhost/~/test";
	private static final String db_driver="org.h2.Driver";
	//private static final Logger logger=;

	private static final String sampleReportFilePath = "C://sample1.rpt";
	
	/**
	 * @return
	 * @throws ReportSDKException
	 * @throws IOException
	 */
	public boolean generate() throws ReportSDKException, IOException{
		ReportClientDocument rcd=new ReportClientDocument();


		String filePath= getClass().getClassLoader().getResource("sample1.rpt").getPath();


		System.out.println(filePath);
	    rcd.open(filePath, 0);
	    CRJavaHelper crj=new CRJavaHelper();
	    crj.changeDataSource(rcd, db_user, db_pwd, db_url, db_driver, "");
		crj.logonDataSource(rcd, db_user, db_pwd);
	    rcd.checkDatabaseAndUpdate();
	    rcd.refreshReportDocument();
	    System.out.println(rcd.path());
	    ByteArrayInputStream bais=(ByteArrayInputStream)rcd.getPrintOutputController().export(ReportExportFormat.PDF);
	    System.out.println("File loaded succesfully");
	    rcd.close();
	    try{
	    	Date d=new Date();
	    	//String currentDate=Integer.toString(d.getDate())+Integer.toString(d.getMonth())+Integer.toString(d.)+"_"+Integer.toString(d.getHours())+":"+Integer.toString(d.getMinutes())+":"+Integer.toString(d.getSeconds());
	    	String currentDate=new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_a'.pdf'").format(new Date());
	    	System.out.println(currentDate);
	    	String fname="Employee"+"_"+currentDate;
	    	String directory="C://reports";
	    	String path=directory+"/"+fname;
			File file = new File(path);
		    FileOutputStream fileOutputStream = new FileOutputStream(file);
		    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bais.available());
		    byte[] byteArray=new byte[bais.available()];
			int x = bais.read(byteArray, 0, bais.available());
		    byteArrayOutputStream.write(byteArray, 0, x);
		    byteArrayOutputStream.writeTo(fileOutputStream);
		    fileOutputStream.close();
		    System.out.println("File exported succesfully");
		    return true;
	    }
	    catch(FileNotFoundException fnfe){
	    	fnfe.printStackTrace();
	    	return false;
	    }
	}
}
