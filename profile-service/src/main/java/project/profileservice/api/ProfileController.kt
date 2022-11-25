package project.profileservice.api

import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.profileservice.domain.Profile
import project.profileservice.service.ProfileService

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
class ProfileController
    (
    private val profileService : ProfileService
){

    @GetMapping("/{user_id}")
    fun getProfileByUserId(@PathVariable("user_id)") id : Long) : {
        val profile : Profile = profileService.findOneById(id)

    }
}