//package project.investmentservice.api;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//import project.investmentservice.api.InvestmentApiController.StockRequest;
//import project.investmentservice.domain.Channel;
//import project.investmentservice.service.ChannelService;
//import project.investmentservice.service.InvestmentService;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Transactional
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//public class InvestmentApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private InvestmentService investmentService;
//
//    @Autowired
//    private ChannelService channelService;
//
//    @Autowired
//    private WebApplicationContext ctx;
//
//    @Before
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
//                .addFilters(new CharacterEncodingFilter("UTF-8", true))
//                .build();
//    }
//
//    @Test
//    public void purchaseStock_성공() throws Exception{
//        //given
//        Channel channel = channelService.createChannel("springboot", 100, 10L, 112L, "man");
//        channelService.enterChannel(channel.getId(), 31L);
//
//        StockRequest request = new StockRequest();
//        request.setCompanyId(123L);
//        request.setUserId(31L);
//        request.setPrice(9.0);
//        request.setQuantity(10L);
//
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/investment/purchase/" + channel.getId())
//                .contentType(APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)));
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("type").value("SUCCESS"));
//        String result = resultActions.andReturn().getResponse().getContentAsString();
//        System.out.println("구매 성공 = " + result);
//    }
//
//    @Test
//    public void purchaseStock_실패() throws Exception{
//        //given
//        Channel channel = channelService.createChannel("springboot", 100, 10L, 112L);
//        channelService.enterChannel(channel.getId(), 31L);
//
//        StockRequest request = new StockRequest();
//        request.setCompanyId(123L);
//        request.setUserId(31L);
//        request.setPrice(9.0);
//        request.setQuantity(1000L);
//
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/investment/purchase/" + channel.getId())
//                .contentType(APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)));
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("type").value("FAIL"));
//        String result = resultActions.andReturn().getResponse().getContentAsString();
//        System.out.println("구매 실패 = " + result);
//    }
//
//
//    @Test
//    public void sellStock_성공() throws Exception {
//        //given
//        Channel channel = channelService.createChannel("springboot", 100, 10L, 112L);
//        channelService.enterChannel(channel.getId(), 31L);
//
//        // 구매 request
//        StockRequest purchaseRequest = new StockRequest();
//        purchaseRequest.setCompanyId(123L);
//        purchaseRequest.setUserId(31L);
//        purchaseRequest.setPrice(9.0);
//        purchaseRequest.setQuantity(10L);
//        mockMvc.perform(post("/api/v1/investment/purchase/" + channel.getId())
//                .contentType(APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(purchaseRequest)));
//
//        //판매 request
//        StockRequest sellRequest = new StockRequest();
//        sellRequest.setCompanyId(123L);
//        sellRequest.setUserId(31L);
//        sellRequest.setPrice(20.0);
//        sellRequest.setQuantity(10L);
//
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/investment/sell/"+channel.getId())
//                .contentType(APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(sellRequest)));
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("type").value("SUCCESS"));
//        String result = resultActions.andReturn().getResponse().getContentAsString();
//        System.out.println("result = " + result);
//    }
//
//    @Test
//    public void sellStock_실패() throws Exception {
//        //given
//        Channel channel = channelService.createChannel("springboot", 100, 10L, 112L);
//        channelService.enterChannel(channel.getId(), 31L);
//
//        // 구매 request
//        StockRequest purchaseRequest = new StockRequest();
//        purchaseRequest.setCompanyId(123L);
//        purchaseRequest.setUserId(31L);
//        purchaseRequest.setPrice(9.0);
//        purchaseRequest.setQuantity(10L);
//        mockMvc.perform(post("/api/v1/investment/purchase/" + channel.getId())
//                .contentType(APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(purchaseRequest)));
//
//        //판매 request
//        StockRequest sellRequest = new StockRequest();
//        sellRequest.setCompanyId(123L);
//        sellRequest.setUserId(31L);
//        sellRequest.setPrice(20.0);
//        sellRequest.setQuantity(11L);
//
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/investment/sell/"+channel.getId())
//                .contentType(APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(sellRequest)));
//
//        //then
//        resultActions.andExpect(status().isOk())
//                .andExpect(jsonPath("type").value("FAIL"));
//        String result = resultActions.andReturn().getResponse().getContentAsString();
//        System.out.println("result = " + result);
//    }
//}
