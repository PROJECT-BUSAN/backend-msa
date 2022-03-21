package project.investmentservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/investment/test")
public class TestApiController {
    
    private final HttpApiController httpApiController;
    
    /**
     * GET 테스트 API
     * @return
     */
    @GetMapping("")
    public ResponseEntity gettestv1() {
        List<CompanyApiController.TestResult> responseList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            CompanyApiController.TestResult testResult = new CompanyApiController.TestResult("message" + i, "data");
            responseList.add(testResult);
        }

        return new ResponseEntity(responseList, HttpStatus.OK);
    }

    /**
     * POST 테스트 API
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseEntity posttestv1(@RequestBody TestRequest request) {
        List<CompanyApiController.TestResult> responseList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            CompanyApiController.TestResult testResult = new CompanyApiController.TestResult("message" + i, request.data);
            responseList.add(testResult);
        }

        return new ResponseEntity(responseList, HttpStatus.OK);
    }

    /**
     * profile-service 컨테이너와의 통신 테스트 api
     * @return
     */
    @PostMapping("/test")
    public ResponseEntity test() {
        String profileServiceUrl = "http://172.30.1.11:8081/api/v1/profile/";
        double userPoint = -1.0;
        /**
         * 입장할 때 유저 프로필의 point를 차감한다.
         * 만약 입장료보다 point를 적게 가지고 있을 시 참여 불가능.
         */
        profileServiceUrl += ("2" + "/point");

        ResponseEntity<String> response = httpApiController.getRequest(profileServiceUrl);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(response.getBody());
            System.out.println("node = " + node);
            userPoint = Double.parseDouble(node.get("userPoint").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (userPoint < 500) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * httpApiController 테스트
     * @return
     */
    @PostMapping("/123")
    public ResponseEntity<String> get() {
        TestRequest test = new TestRequest();
        return httpApiController.postRequest("http://localhost:8080/api/v1/investment/test", test);
    }
    
    @Data
    static class TestRequest {
        private String data;
    }
}
