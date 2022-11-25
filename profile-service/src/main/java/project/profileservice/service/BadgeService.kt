package project.profileservice.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import project.profileservice.domain.Badge
import project.profileservice.domain.dto.UpdateBadgeDto
import project.profileservice.repository.t.BadgeRepository

@Service
class BadgeService
    (
    private val badgeRepository: BadgeRepository

){

    @Transactional(readOnly = true)
    fun findOneById(id : Long) : Badge {
        return badgeRepository.findById(id).orElseThrow()
    }

    @Transactional(readOnly = true)
    fun findAll() : List<Badge> {
        return badgeRepository.findAll()
    }

    @Transactional
    fun create(name : String, image_url : String) : Long {
        val badge = Badge(name, image_url)
        badgeRepository.save(badge)
        return badge.id
    }

    @Transactional
    fun update(id : Long, badgeDto : UpdateBadgeDto) {
        val badge : Badge = badgeRepository.findById(id).orElseThrow()
        badge.updateByDto(badgeDto)
    }

    @Transactional
    fun delete(id : Long) {
        val badge : Badge = badgeRepository.findById(id).orElseThrow()
        badgeRepository.delete(badge)
    }

}