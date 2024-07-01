package nbdream.member.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.AccountBook;
import nbdream.accountBook.exception.AccountBookNotFoundException;
import nbdream.accountBook.repository.AccountBookRepository;
import nbdream.auth.dto.response.TokenResponse;
import nbdream.auth.infrastructure.JwtTokenProvider;
import nbdream.bulletin.domain.Bulletin;
import nbdream.bulletin.repository.BookmarkRepository;
import nbdream.bulletin.repository.BulletinRepository;
import nbdream.calendar.domain.Holiday;
import nbdream.calendar.repository.HolidayRepository;
import nbdream.comment.domain.Comment;
import nbdream.comment.repository.CommentRepository;
import nbdream.farm.domain.*;
import nbdream.farm.exception.FarmNotFoundException;
import nbdream.farm.repository.*;
import nbdream.image.domain.Image;
import nbdream.image.dto.ImageDto;
import nbdream.image.repository.ImageRepository;
import nbdream.image.service.ImageService;
import nbdream.member.domain.Authority;
import nbdream.member.domain.LoginType;
import nbdream.member.domain.Member;
import nbdream.member.domain.Withdrawal;
import nbdream.member.dto.request.WithdrawalReqDto;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import nbdream.member.repository.WithdrawalRepository;
import nbdream.weather.repository.SimpleWeatherRepository;
import nbdream.weather.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FarmCropRepository farmCropRepository;
    private final BulletinRepository bulletinRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final FarmRepository farmRepository;
    private final FarmingDiaryRepository farmingDiaryRepository;
    private final WorkRepository workRepository;
    private final HolidayRepository holidayRepository;
    private final LandElementsRepository landElementsRepository;
    private final SimpleWeatherRepository simpleWeatherRepository;
    private final WeatherRepository weatherRepository;
    private final AccountBookRepository accountBookRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final ScheduleRepository scheduleRepository;


    public TokenResponse signup(String nickname) {
        Member member = Member.builder()
                .socialId(UUID.randomUUID().toString())
                .authority(Authority.USER)
                .nickname(nickname)
                .build();

        Member savedMember = memberRepository.save(member);
        farmRepository.save(new Farm(savedMember));
        accountBookRepository.save(new AccountBook(savedMember));
        return new TokenResponse(jwtTokenProvider.createAccessToken(savedMember.getId()),
                jwtTokenProvider.createRefreshToken(savedMember.getId()));
    }

    @Transactional(readOnly = true)
    public LoginType getLoginType(final Long memberId) {
        final Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return member.getLoginType();
    }

    public void saveWithdrawalReason(final Long memberId, final WithdrawalReqDto request) {
        withdrawalRepository.save(new Withdrawal(memberId, request.getReason(), request.getOtherReasons()));
    }

    public void deleteMember(final Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        deleteMyBulletins(memberId);
        deleteMyComments(memberId);
        deleteMyBookmarks(memberId);
        deleteFarm(memberId);
        deleteAccountBook(memberId);
        member.delete();
    }

    public void deleteMyBulletins(final Long memberId) {
        List<Bulletin> bulletins = bulletinRepository.findByAuthorId(memberId);
        for (Bulletin bulletin : bulletins) {
            deleteBulletinComments(memberId, bulletin.getId());
            deleteBulletinBookmarks(bulletin.getId());
            deleteBulletinImages(bulletin.getId());
            bulletin.expire(memberId);
        }
    }

    public void deleteBulletinComments(final Long memberId, final Long bulletinId) {
        List<Comment> bulletinComments = commentRepository.findByBulletinId(bulletinId);

        for (Comment comment : bulletinComments) {
            comment.expire(memberId);
        }
    }

    public void deleteMyComments(final Long memberId) {
        List<Comment> myComments = commentRepository.findByAuthorId(memberId);

        for (Comment comment : myComments) {
            comment.expire(memberId);
        }
    }

    public void deleteBulletinBookmarks(final Long bulletinId) {
        bookmarkRepository.deleteAllByBulletinId(bulletinId);
    }

    public void deleteMyBookmarks(final Long memberId) {
        bookmarkRepository.deleteAllByMemberId(memberId);
    }

    public void deleteBulletinImages(final Long bulletinId) {
        List<Image> bulletinImages = imageRepository.findAllByTargetId(bulletinId);

        for (Image image : bulletinImages) {
            image.expire();
        }
    }

    public void deleteFarm(final Long memberId) {
        Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        deleteFarmCrops(farm.getId());
        deleteLandElements(farm.getId());
        deleteFarmingDiary(farm.getId());
        deleteSchedule(farm.getId());
        deleteWeathers(farm.getId());
        farmRepository.delete(farm);
    }

    public void deleteFarmCrops(final Long farmId) {
        farmCropRepository.deleteAllByFarmId(farmId);
    }

    public void deleteLandElements(final Long farmId) {
        Farm farm = farmRepository.findById(farmId).orElseThrow(FarmNotFoundException::new);
        LandElements landElements = farm.getLandElements();
        landElementsRepository.delete(landElements);
    }
    
    public void deleteFarmingDiary(final Long farmId) {
        List<FarmingDiary> farmingDiaries = farmingDiaryRepository.findAllByFarmId(farmId);

        for (FarmingDiary farmingDiary : farmingDiaries) {
            deleteFarmingDiaryImages(farmingDiary.getId());
            deleteFarmingDiaryWorks(farmingDiary.getId());
            deleteFarmingDiaryHolidays(farmingDiary.getId());
        }
        farmingDiaryRepository.deleteAll(farmingDiaries);
    }

    public void deleteFarmingDiaryImages(final Long farmingDiaryId) {
        List<Image> farmingDiaryImages = imageRepository.findAllByTargetId(farmingDiaryId);
        for (Image image : farmingDiaryImages) {
            imageService.deleteImage(new ImageDto(image.getImageUrl()));
        }
        imageRepository.deleteAllByTargetId(farmingDiaryId);
    }

    public void deleteFarmingDiaryWorks(final Long farmingDiaryId) {
        List<Work> works = workRepository.findAllByFarmingDiaryId(farmingDiaryId);
        workRepository.deleteAll(works);
    }

    public void deleteFarmingDiaryHolidays(final Long farmingDiaryId) {
        List<Holiday> holidays = holidayRepository.findAllByFarmingDiaryId(farmingDiaryId);
        holidayRepository.deleteAll(holidays);
    }

    public void deleteSchedule(final Long farmId) {
        List<Schedule> schedules = scheduleRepository.findByFarmId(farmId);
        for(Schedule schedule : schedules){
            scheduleRepository.delete(schedule);
        }
    }

    public void deleteWeathers(final Long farmId) {
        simpleWeatherRepository.deleteAllByFarmId(farmId);
        weatherRepository.deleteAllByFarmId(farmId);
    }

    public void deleteAccountBook(final Long memberId) {
        AccountBook accountBook = accountBookRepository.findByMemberId(memberId).orElseThrow(AccountBookNotFoundException::new);
        deleteBookAccountImages(accountBook.getId());
        accountBookRepository.delete(accountBook);
    }

    public void deleteBookAccountImages(final Long bookAccountId) {
        List<Image> bulletinImages = imageRepository.findAllByTargetId(bookAccountId);
        for (Image image : bulletinImages) {
            imageService.deleteImage(new ImageDto(image.getImageUrl()));
        }

        imageRepository.deleteAllByTargetId(bookAccountId);
    }
}
