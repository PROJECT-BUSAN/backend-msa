package project.profileservice.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Attendance;
import project.profileservice.domain.Badge;
import project.profileservice.domain.Profile;
import project.profileservice.domain.ProfileBadge;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@Transactional(readOnly = true)
@SpringBootTest
public class ProfileRepositoryTest {

    @Autowired private ProfileRepository profileRepository;
    @Autowired private BadgeRepository badgeRepository;
    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private ProfileBadgeRepository profileBadgeRepository;
    
    @Test
    @Transactional
    public void save_Test() {
        //given
        Profile profile = new Profile();
        profile.setUser_id(1L);
        profile.setNowStrick(10);
        profile.setPoint(1000000L);

        //when
        profileRepository.save(profile);
        Profile findProfile = profileRepository.findOne(1L);

        //then
        Assertions.assertEquals(profile.getId(), findProfile.getId());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Transactional
    public void deleteById_Test() throws Exception {
        //given
        Profile profile = createProfileObject();
        profileRepository.save(profile);
        Long user_id = profile.getUser_id();

        //when
        profileRepository.deleteById(profile.getId());
        Profile findProfile = profileRepository.findOne(user_id);
        
        //then
        Assert.fail("유저를 찾을 수 없습니다.");
    }

    @Test
    @Transactional
    public void findOneAllInfo_Test() {
        // given
        Profile profile = createProfileAllInfo();

        // when
        Profile findProfile = profileRepository.findOneAllInfo(profile.getUser_id());
        
        // then
        Assertions.assertEquals(profile.getId(), findProfile.getId());
    }

    @Test
    @Transactional
    public void findOneAttendanceAll_Test() {
        //given
        Profile profile = createProfileAllInfo();
        LocalDateTime createAt = profile.getAttendances().get(0).getCreateAt();

        //when
        Profile findProfile = profileRepository.findOneAttendanceAll(profile.getUser_id());

        //then
        Assertions.assertEquals(findProfile.getAttendances().get(0).getCreateAt(), createAt);

    }

    @Test
    @Transactional
    public void findAll_Test() {
        //given
        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        profileRepository.save(profile1);
        profileRepository.save(profile2);

        //when
        List<Profile> findProfiles = profileRepository.findAll();

        //then
        Assertions.assertEquals(findProfiles.size(), 2);
    }
    
    private Profile createProfileObject() {
        Profile profile = new Profile();
        profile.setUser_id(1L);
        profile.setNowStrick(10);
        profile.setPoint(1000000L);
        return profile;
    }

    private Badge createBadgeObject() {
        Badge badge = new Badge();
        badge.setImage_url("http://..");
        badge.setName("출석!");
        return badge;
    }
    
    private Profile createProfileAllInfo() {
        Profile profile = createProfileObject();
        Badge badge = createBadgeObject();

        ProfileBadge profileBadge = new ProfileBadge();
        profileBadge.CreateProfileBadge(profile, badge);
        profile.addProfileBadge(profileBadge);
        badge.addProfileBadge(profileBadge);

        Attendance attendance = new Attendance();
        attendance.setProfile(profile);
        attendance.setCreateAt(LocalDateTime.now());

        profile.addAttendance(attendance);

        badgeRepository.save(badge);
        profileRepository.save(profile);
        attendanceRepository.save(attendance);
        profileBadgeRepository.save(profileBadge);

        return profile;
    }
}
