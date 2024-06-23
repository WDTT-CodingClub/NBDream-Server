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
import nbdream.comment.domain.Comment;
import nbdream.comment.repository.CommentRepository;
import nbdream.farm.domain.Farm;
import nbdream.farm.exception.FarmNotFoundException;
import nbdream.farm.repository.CropRepository;
import nbdream.farm.repository.FarmCropRepository;
import nbdream.farm.repository.FarmRepository;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FarmCropRepository farmCropRepository;
    private final CropRepository cropRepository;
    private final BulletinRepository bulletinRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final FarmRepository farmRepository;
    private final AccountBookRepository accountBookRepository;
    private final WithdrawalRepository withdrawalRepository;

    public TokenResponse signup(String nickname) {
        Member member = Member.builder()
                .socialId(UUID.randomUUID().toString())
                .authority(Authority.USER)
                .nickname(nickname)
                .build();

        Long id = memberRepository.save(member).getId();
        return new TokenResponse(jwtTokenProvider.createAccessToken(id), jwtTokenProvider.createRefreshToken(id));
    }

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
            deleteBulletinComments(bulletin.getId());
            deleteBulletinBookmarks(bulletin.getId());
            deleteBulletinImages(bulletin.getId());
        }
    }

    public void deleteBulletinComments(final Long bulletinId) {
        List<Comment> bulletinComments = commentRepository.findByBulletinId(bulletinId);

        for (Comment comment : bulletinComments) {
            comment.delete();
        }
    }

    public void deleteMyComments(final Long memberId) {
        List<Comment> myComments = commentRepository.findByAuthorId(memberId);

        for (Comment comment : myComments) {
            comment.delete();
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
            imageService.deleteImage(new ImageDto(image.getImageUrl()));
        }

        imageRepository.deleteAllByTargetId(bulletinId);
    }

    public void deleteFarm(final Long memberId) {
        Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        deleteFarmCrops(farm.getId());
        farmRepository.delete(farm);
    }

    public void deleteFarmCrops(final Long farmId) {
        farmCropRepository.deleteAllByFarmId(farmId);
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
