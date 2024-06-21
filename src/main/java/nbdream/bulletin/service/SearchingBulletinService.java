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

    public BulletinResDto getBulletinDetails(final Long bulletinId) {
        final Bulletin bulletin = bulletinRepository.findByIdFetchComments(bulletinId).orElseThrow(() -> new BulletinNotFoundException());

        final List<String> imageUrls = imageRepository.findAllByTargetId(bulletinId).stream()
                .map(image -> image.getImageUrl())
                .collect(Collectors.toList());

        return new BulletinResDto(bulletin, bulletin.getAuthor(), imageUrls, bulletin.getComments());
    }

    public BulletinsResDto getBulletins(SearchBulletinCondDto cond) {
        final List<Bulletin> bulletins = searchingBulletinRepository.searchBulletins(cond, PAGE_SIZE + 1);

        return bulletinMappingImagesAndComments(bulletins);
    }

    public BulletinsResDto getBookmarkBulletins(final Long memberId, final Long lastBulletinId) {
        final Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());
        final List<Bookmark> bookmarks = bookmarkRepository.findByMember(member);
        final List<Long> bookmarkBulletinIds = bookmarks.stream()
                .map(bookmark -> bookmark.getBulletin().getId())
                .collect(Collectors.toList());

        final List<Bulletin> bulletins = bulletinRepository.findByIdsWithPaging(bookmarkBulletinIds, lastBulletinId);

        return bulletinMappingImagesAndComments(bulletins);
    }

    public BulletinsResDto getMyBulletins(final Long memberId, final Long bulletinId) {
        final Member author = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());

        final List<Bulletin> bulletins = bulletinRepository.findByAuthorWithPaging(author.getId(), bulletinId);

        return bulletinMappingImagesAndComments(bulletins);
    }

    public BulletinsResDto bulletinMappingImagesAndComments(final List<Bulletin> bulletins) {
        final List<Long> bulletinIds = bulletins.stream()
                .map(bulletin -> bulletin.getId())
                .collect(Collectors.toList());

        HashMap<Long, List<String>> imageMap = new HashMap<>();
        mappingImagesByTargetId(imageMap, imageRepository.findAllByTargetIds(bulletinIds));

        boolean hasNext = hasNext(bulletins);

        final List<BulletinResDto> bulletinResDtos = bulletins.stream()
                .map(bulletin -> new BulletinResDto(bulletin, bulletin.getAuthor(), imageMap.get(bulletin.getId()), bulletin.getComments()))
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
}
