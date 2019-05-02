package org.onlperations.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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

import opennlp.tools.util.Span;

import org.onlperations.entity.ConversationInput;
import org.onlperations.services.NLPServiceUtilities;
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
	
	@PostMapping(value = "/getNamedEntities")
	public ResponseEntity<String[]> getNamedEntities(@RequestBody ConversationInput conversationInput) {
		String[] namedEntities = {""};
		
		Span nameSpans[] = nlpServices.findNamedEntity(conversationInput.getSentence(), "NAM");
		String[] sentenceArr = conversationInput.getSentence().split(" ");
		
		LOGGER.info("nameSpans: " + Arrays.toString(nameSpans));
		
		for(int nameSpansCnt = 0; nameSpansCnt < nameSpans.length; nameSpansCnt++) {
			List<String> nameSpan = (ArrayList<String>) NLPServiceUtilities.getValueAtSpan(nameSpans[nameSpansCnt], sentenceArr);
			LOGGER.info("nameSpan: " + nameSpan);
		}
		
		
		Span orgSpans[] = nlpServices.findNamedEntity(conversationInput.getSentence(), "ORG");
		
		LOGGER.info("orgSpans: " + Arrays.toString(orgSpans));
		
		for(int orgSpansCnt = 0; orgSpansCnt < orgSpans.length; orgSpansCnt++) {
			List<String> orgSpan = (ArrayList<String>) NLPServiceUtilities.getValueAtSpan(orgSpans[orgSpansCnt], sentenceArr);
			LOGGER.info("orgSpan: " + orgSpan);
		}
				
		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<String[]>(namedEntities, responseHeaders, HttpStatus.OK);
	}

}
