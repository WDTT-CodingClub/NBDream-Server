package nbdream.bulletin.service;

import lombok.RequiredArgsConstructor;
import nbdream.bulletin.domain.Bookmark;
import nbdream.bulletin.domain.Bulletin;
import nbdream.bulletin.dto.request.SearchBulletinCondDto;
import nbdream.bulletin.dto.response.BulletinResDto;
import nbdream.bulletin.dto.response.BulletinsResDto;
import nbdream.bulletin.exception.BulletinNotFoundException;
import nbdream.bulletin.repository.BookmarkRepository;
import nbdream.bulletin.repository.BulletinRepository;
import nbdream.bulletin.repository.SearchingBulletinRepository;
import nbdream.comment.dto.CommentResDto;
import nbdream.image.domain.Image;
import nbdream.image.repository.ImageRepository;
import nbdream.member.domain.Member;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Transactional(readOnly = true)
public class SearchingBulletinService {
    private final BulletinRepository bulletinRepository;
    private final ImageRepository imageRepository;
    private final SearchingBulletinRepository searchingBulletinRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;

    private static final int PAGE_SIZE = 10;

    public BulletinResDto getBulletinDetails(final Long memberId, final Long bulletinId) {
        final Bulletin bulletin = bulletinRepository.findByIdFetchComments(bulletinId).orElseThrow(() -> new BulletinNotFoundException());

        final List<String> imageUrls = imageRepository.findAllByTargetId(bulletinId).stream()
                .map(image -> image.getImageUrl())
                .collect(Collectors.toList());

        final List<CommentResDto> comments = bulletin.getComments().stream().map(comment -> new CommentResDto(comment, memberId)).toList();
        return BulletinResDto.builder()
                .bulletin(bulletin)
                .author(bulletin.getAuthor())
                .imageUrls(imageUrls)
                .comments(comments)
                .isAuthor(bulletin.isAuthor(memberId))
                .isBookmarked(isBookmarked(memberId, bulletinId))
                .build();
    }

    public BulletinsResDto getBulletins(final Long memberId, SearchBulletinCondDto cond) {
        final List<Bulletin> bulletins = searchingBulletinRepository.searchBulletins(cond, PAGE_SIZE + 1);

        return bulletinMappingImagesAndComments(memberId, bulletins);
    }

    public BulletinsResDto getBookmarkBulletins(final Long memberId, final Long lastBulletinId) {
        final Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());
        final List<Bookmark> bookmarks = bookmarkRepository.findByMember(member);
        final List<Long> bookmarkBulletinIds = bookmarks.stream()
                .map(bookmark -> bookmark.getBulletin().getId())
                .collect(Collectors.toList());

        final List<Bulletin> bulletins = bulletinRepository.findByIdsWithPaging(bookmarkBulletinIds, lastBulletinId);

        return bulletinMappingImagesAndComments(memberId, bulletins);
    }

    public BulletinsResDto getMyBulletins(final Long memberId, final Long bulletinId) {
        final Member author = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());

        final List<Bulletin> bulletins = bulletinRepository.findByAuthorWithPaging(author.getId(), bulletinId);

        return bulletinMappingImagesAndComments(memberId, bulletins);
    }

    public BulletinsResDto bulletinMappingImagesAndComments(final Long memberId, final List<Bulletin> bulletins) {
        final List<Long> bulletinIds = bulletins.stream()
                .map(bulletin -> bulletin.getId())
                .collect(Collectors.toList());

        HashMap<Long, List<String>> imageMap = new HashMap<>();
        mappingImagesByTargetId(imageMap, imageRepository.findAllByTargetIds(bulletinIds));

        boolean hasNext = hasNext(bulletins);

        final List<BulletinResDto> bulletinResDtos = bulletins.stream()
                .map(bulletin -> BulletinResDto.builder()
                        .bulletin(bulletin)
                        .author(bulletin.getAuthor())
                        .imageUrls(imageMap.get(bulletin.getId()))
                        .comments(bulletin.getComments().stream().map(comment -> new CommentResDto(comment, memberId)).toList())
                        .isAuthor(bulletin.isAuthor(memberId))
                        .isBookmarked(isBookmarked(memberId, bulletin.getId()))
                        .build())
                .collect(Collectors.toList());

        return new BulletinsResDto(bulletinResDtos, hasNext);
    }

    private boolean hasNext(List<Bulletin> bulletins) {
        if (bulletins.size() > PAGE_SIZE) {
            System.out.println(bulletins);
            bulletins.remove(PAGE_SIZE);
            return true;
        }
        return false;
    }

    private void mappingImagesByTargetId(HashMap<Long, List<String>> imageMap, List<Image> images) {
        for (Image image : images) {
            if (!imageMap.containsKey(image.getTargetId())) {
                imageMap.put(image.getTargetId(), new ArrayList<>());
            }
            imageMap.get(image.getTargetId()).add(image.getImageUrl());
        }
    }

    private boolean isBookmarked(final Long memberId, final Long bulletinId) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByMemberIdAndBulletinId(memberId, bulletinId);
        return (bookmark.isPresent()) ? true : false;
    }

}
