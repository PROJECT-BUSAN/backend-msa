package project.investmentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.investmentservice.domain.Channel;
import project.investmentservice.repository.ChannelRepository;
import project.investmentservice.service.ChannelService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/game")
public class ChannelController {

    private ChannelRepository channelRepository;
    private ChannelService channelService;

    //모든 채널 반환
    @GetMapping("/channels")
    @ResponseBody
    public List<Channel> channels() {
        return channelRepository.findAllChannel();
    }

    //채널 생성
    @PostMapping("/channel")
    @ResponseBody
    public Channel createChannel(@RequestParam String name, @RequestParam int LimitOfParticipants, Long entryFee, Long hostId) {
        return channelService.createChannel(name, LimitOfParticipants, entryFee, hostId);
    }

    //채팅방 삭제
    @DeleteMapping("/channel/delete{channelId}")
    public String deleteChannel(@PathVariable String channelId) {
        channelService.deleteChannel(channelId);
        return "game/channel";
    }

    // 채널 입장
    @GetMapping("/channel/enter/{channelId}")
    public String channelDetail(Model model, @PathVariable String channelId) {
        model.addAttribute("channelId", channelId);
        return "/game/channeldetail";
    }

    // 특정 채널 조회
    @GetMapping("/channel/{channelId}")
    public Channel channelInfo(@PathVariable String channelId) {
        return channelService.findOneChannel(channelId);
    }
}
