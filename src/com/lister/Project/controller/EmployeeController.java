package com.lister.Project.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ComparableComparator;
import org.hibernate.util.CalendarComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
//import com.lister.Project.dao.Comparator;
import com.lister.Project.domain.Employee;
import com.lister.Project.service.EmployeeService;

/**
 * @author souvik.p
 *
 */
@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService es;
	
	/**
	 * @param emp
	 * @param model
	 * @return
	 */
	@RequestMapping("/save")
	public String show(@ModelAttribute Employee emp,Map<String,Object> model){
		es.addemployee(emp);
		List<Employee> le=es.getEmployeeList();
		model.put("Employees", le);
		return "employeedtls";
	}
	
	/**
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/delete")
	public String remove(@RequestParam  int id,Map<String,Object> model){
		es.removeEmployeeByID(id);
		System.out.println("Deleted");
		List<Employee> le=es.getEmployeeList();
		model.put("Employees", le);
		return "employeedtls";
	}
	
	/**
	 * @param model
	 * @return
	 * @throws ReportSDKException
	 * @throws IOException
	 */
	@RequestMapping("/generate")
	public String generate(Model model) throws ReportSDKException, IOException{
		//EmployeeService es=new EmployeeService();
		if(es.generate()){
			model.addAttribute("message", "Report published succesfully");
		}
		else{
			model.addAttribute("message", "The output report file must be open in some other application.");
			List<Employee> le=es.getEmployeeList();
			model.addAttribute("Employees", le);
			return "employeedtls";
		}
		File directory=new File("C://GeneratedReports");
		System.out.println("File output at C drive");
		File lf[]=directory.listFiles();
		Arrays.sort(lf, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));
	    List<String> fname=new ArrayList<String>();
		for(File oListItem:lf){
			if(!oListItem.isDirectory()){
				fname.add(oListItem.getName());
				System.out.println(oListItem.getName());
			}
		}
		model.addAttribute("reports",fname);
		return "reportlist";
	}
}
