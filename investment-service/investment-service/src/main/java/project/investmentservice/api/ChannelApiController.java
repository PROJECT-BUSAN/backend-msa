package project.investmentservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class ChannelApiController {

//    @Autowired
//    private Channel channel;
//    private Long channelNum = 1L;
//    private Long userId = 1L;
//
//    @GetMapping("/create/channel")
//    public Long channelTest(HttpServletRequest request, HttpServletResponse response) {
////        String[] Participants = request.getParameterValues("users");
//        String channelName = request.getParameter("name");
//        int participantNum = Integer.parseInt(request.getParameter("participantNum"));
//
////        for (String participant : Participants) {
////            //userName으로 user_id 검색하는 로직 필요 -> 검색 결과가 userId라고 가정
////            channel.createChannel(channelNum, response, userId);
////            userId++;
////        }
//        channel.createChannel(channelNum, response, channelName, participantNum);
//        channelNum++;
//
//        return channel.getChannel(request);
//    }


}
