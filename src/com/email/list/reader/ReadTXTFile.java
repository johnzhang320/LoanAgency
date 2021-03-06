package com.email.list.reader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loan.agent.mvc.utils.Utility;

public class ReadTXTFile implements EmailListService {
	public static Logger LOG  =Logger.getLogger(ReadTXTFile.class); 

	/**
	 *  Implement abstract function
	 */
	public List<EmailDynamicVO> getDynamicEmailInfo(InputStream fis) {
		List<EmailDynamicVO> dyList = new ArrayList<EmailDynamicVO>();
		 
    	BufferedReader br = null;
		 DataInputStream in = null;
		try
        {
			  
			 in = new DataInputStream(fis);
			  
			 br = new BufferedReader(new InputStreamReader(in));
	 
		 
		    int rowCount=0;
		    String line=null;
		    int totalCols=0;
		    while ((line=br.readLine())!=null) {	
		    	 
		    	SimpleEmailLineVO vo = ParserEmailListOnPlainText.parserEmailLine(line);
		    	
		    	
		    	if (vo!=null && vo.isValid()) {
		    		vo.setLineId(new Integer(rowCount+1).toString());
		    		String oldFirstName = SimpleEmailLineVO.rltrim(vo.getFirstName());	
		    		int spos = oldFirstName.indexOf(" ");
		    		
		    	
		    	   if (spos!=-1) {
		    			String name[] = oldFirstName.split(" ");
		    			vo.setFirstName(SimpleEmailLineVO.rltrim(name[0]));
		    			if (name.length>0) {
		    				vo.setLastName(SimpleEmailLineVO.rltrim(name[1]));
		    			}
		    		}
		    	  
		    		EmailDynamicVO dvo = new EmailDynamicVO();
		    		totalCols=0;
			    	if (!Utility.isEmpty(vo.getFirstName())) {
			    			 dvo.addCol(vo.getFirstName());
			    			 totalCols++;
			    	}
			    	if (!Utility.isEmpty(vo.getLastName())) {
			    		 dvo.addCol(vo.getLastName());
			    		 totalCols++;
			    	}
			    	if (!Utility.isEmpty(vo.getEmailAddress())) {
			    		 dvo.addCol(vo.getEmailAddress());
			    		 totalCols++;
			    	}
			    	dvo.setTotalCols(totalCols);
			    	dyList.add(dvo);
			    		 
		    	}
		    	rowCount++;
		    	
			}
		    
          }  catch (Exception e)
		  {
		       LOG.error("Read Excel file failure, message is "+e.getMessage());
		        return null;
		  } finally {
		   	try {        		
		       		br.close(); 
		       		in.close();
		        		
		       	} catch (Exception e) {
		       		 LOG.error("Read Excel File, normall Close failure, message is "+e.getMessage());
		       		 return null;
		       	}
		  }
 
		return dyList;
	}
	
	 
}
