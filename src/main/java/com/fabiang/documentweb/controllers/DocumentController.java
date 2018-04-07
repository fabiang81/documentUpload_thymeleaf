package com.fabiang.documentweb.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fabiang.documentweb.entities.Document;
import com.fabiang.documentweb.repos.DocumentRepository;

@Controller
public class DocumentController {
	
	@Autowired
	DocumentRepository repository;
	
	@RequestMapping("/displayUpload")
	public String displayUpload(ModelMap modelMap) {
		List<Document> documents = repository.findAll();
		modelMap.addAttribute("documents", documents);
		return "documentUpload";
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public String uploadDocument(@RequestParam("document") MultipartFile multiPartFile, @RequestParam("id") Long id, ModelMap modelMap) {
		Document document = new Document();
		document.setId(id);
		document.setName(multiPartFile.getOriginalFilename());
		try {
			document.setData(multiPartFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		repository.save(document);
		List<Document> documents = repository.findAll();
		modelMap.addAttribute("documents", documents);
		modelMap.addAttribute("msg", "The document was successfully uploaded");
		return "documentUpload";
	}
	
	@RequestMapping("/download")
	public StreamingResponseBody download(@RequestParam("id") long id, HttpServletResponse response) {
		Document document = repository.findOne(id);
		byte[] data = document.getData();
		String docName = document.getName();
		response.setHeader("Content-Disposition", "attachment;filename="+docName);
		return outputStream -> {
			outputStream.write(data);
		};
	}

}
