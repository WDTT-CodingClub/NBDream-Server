package nbdream.bulletin.service;

import lombok.RequiredArgsConstructor;
import nbdream.bulletin.domain.Bookmark;
import nbdream.bulletin.domain.BookmarkStatus;
import nbdream.bulletin.domain.Bulletin;
import nbdream.bulletin.domain.BulletinCategory;
import nbdream.bulletin.dto.request.BulletinReqDto;
import nbdream.bulletin.dto.request.UpdateBulletinReqDto;
import nbdream.bulletin.exception.BulletinNotFoundException;
import nbdream.bulletin.repository.BookmarkRepository;
import nbdream.bulletin.repository.BulletinRepository;
import nbdream.comment.repository.CommentRepository;
import nbdream.image.domain.Image;
import nbdream.image.repository.ImageRepository;
import nbdream.image.service.ImageService;
import nbdream.member.domain.Member;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class BulletinService {
    private final MemberRepository memberRepository;
    private final BulletinRepository bulletinRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ImageService imageService;

    public Long createBulletin(final Long memberId, final BulletinReqDto request) {
        final Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());
        final Bulletin bulletin = new Bulletin(member, request.getDreamCrop(), BulletinCategory.of(request.getBulletinCategory()), request.getContent());

        final Long bulletinId = bulletinRepository.save(bulletin).getId();

        request.getImageUrls().stream()
                .forEach(imageUrl -> imageRepository.save(new Image(bulletinId, imageUrl)));

        return bulletinId;
    }

    public Long updateBulletin(final Long memberId, final Long bulletinId, final BulletinReqDto request) {
        final Bulletin bulletin = bulletinRepository.findById(bulletinId).orElseThrow(() -> new BulletinNotFoundException());

        bulletin.update(memberId, request.getDreamCrop(), BulletinCategory.of(request.getBulletinCategory()), request.getContent());

        imageService.updateTargetImages(bulletinId, request.getImageUrls());

        return bulletinId;
    }

    public void deleteBulletin(final Long memberId, final Long bulletinId) {
        final Bulletin bulletin = bulletinRepository.findById(bulletinId).orElseThrow(() -> new BulletinNotFoundException());

        List<String> imageUrls = imageRepository.findAllByTargetId(bulletinId).stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
        imageService.deleteImageUrlsWithImage(imageUrls);

        commentRepository.findByBulletinId(bulletinId).stream()
                        .forEach(comment -> comment.delete(memberId));

        bulletin.delete(memberId);
    }

    public int bookmark(final Long memberId, final Long bulletinId) {
        final Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Bulletin bulletin = bulletinRepository.findById(bulletinId).orElseThrow(() -> new BulletinNotFoundException());

        Optional<Bookmark> bookmark = bookmarkRepository.findByMemberAndBulletin(member, bulletin);

        if (bookmark.isEmpty()) {
            bookmarkRepository.save(new Bookmark(member, bulletin));
            bulletin.plusBookmarkedCount();
            return BookmarkStatus.ON.getCode();
        }

        bookmarkRepository.delete(bookmark.get());
        bulletin.minusBookmarkedCount();
        return BookmarkStatus.OFF.getCode();
    }

}
