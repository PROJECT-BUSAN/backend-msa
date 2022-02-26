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
@RequestMapping("game")
public class ChannelController {

    private ChannelService channelService;



}
