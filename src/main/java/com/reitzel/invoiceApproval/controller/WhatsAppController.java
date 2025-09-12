package com.reitzel.invoiceApproval.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.reitzel.invoiceApproval.whatsappservice.WhatsAppService;




@RestController
@RequestMapping("api/whatsapp")
public class WhatsAppController extends BaseController {
	
	@Value("${whatsapp.service.url}")
    private String whatsappServiceUrl;
	
	
	@Autowired
    private WhatsAppService whatsAppService;

//    @PostMapping("/whatsapp")
//    public ResponseEntity<?> testWhatsApp(
//            @RequestParam String number,
//            @RequestParam(required = false) String name) {
//        try {
//            String response = whatsAppService.sendBirthdayMessage(number, name);
//            return ResponseEntity.ok(Map.of(
//                "status", "success",
//                "response", response
//            ));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
//                "error", "WhatsApp message failed",
//                "details", e.getMessage()
//            ));
//        }
//    }
//    
//    @PostMapping(value="/send-document")
//    public ResponseEntity<String> sendDocument(
//        @RequestParam("number") String number,
//        @RequestParam("caption") String caption,
//        @RequestPart("file") MultipartFile file) {
//
//        try {
//            String url = whatsappServiceUrl + "/send-document";
//
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("number", number);
//            body.add("caption", caption);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
//                @Override
//                public String getFilename() {
//                    return file.getOriginalFilename();
//                }
//            };
//
//            body.add("file", new HttpEntity<>(resource, headers));
//
//            RestTemplate restTemplate = new RestTemplate();
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//            );
//
//            return response;
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Document send failed: " + e.getMessage());
//        }
//    }
    
//    @Autowired
//    private WhatsAppService whatsAppService;
//
    // Endpoint to send a text message
    @PostMapping("/send-text")
    public String sendText(
            @RequestParam String toNumber,
            @RequestParam String message) {
        return whatsAppService.sendTextMessage(toNumber, message);
    }

    @PostMapping("/send-media")
    public String sendMedia(
            @RequestParam String toNumber,
            @RequestParam String caption,
            @RequestParam String driveFileId) {

        return whatsAppService.sendMediaMessage(toNumber, caption, driveFileId);
    }
    
    
    


}