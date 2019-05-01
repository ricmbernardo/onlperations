package org.onlperations.controller;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.onlperations.entity.ConversationInput;
import org.onlperations.services.NLPServices;

@RestController
@RequestMapping("/nlpo")
public class NLPOperationsController {
	
	private static final Logger LOGGER = LogManager.getLogger(NLPOperationsController.class);

	@Resource
	NLPServices nlpServices;
	
	@PostMapping(value = "/getPOSTags")
	public ResponseEntity<String[]> getPOSTags(@RequestBody ConversationInput conversationInput) {
		String[] tags = nlpServices.tag(conversationInput.getSentence());
		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<String[]>(tags, responseHeaders, HttpStatus.OK);
	}

}
