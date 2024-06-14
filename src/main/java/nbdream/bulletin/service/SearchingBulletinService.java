package nbdream.bulletin.service;

import lombok.RequiredArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.bulletin.dto.response.BulletinDetailResDto;
import nbdream.bulletin.exception.BulletinNotFoundException;
import nbdream.bulletin.repository.BulletinRepository;
import nbdream.image.repository.ImageRepository;
import nbdream.member.domain.Member;
import nbdream.member.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Transactional(readOnly = true)
public class SearchingBulletinService {
    private final MemberRepository memberRepository;
    private final BulletinRepository bulletinRepository;
    private final ImageRepository imageRepository;

    public BulletinDetailResDto getBulletinDetails(final Long bulletinId) {
        final Bulletin bulletin = bulletinRepository.findById(bulletinId).orElseThrow(() -> new BulletinNotFoundException());
        final Member author = bulletin.getAuthor();
        final List<String> imageUrls = imageRepository.findAllByTargetId(bulletinId)
                .stream()
                .map(image -> image.getImageUrl())
                .collect(Collectors.toList());

        return new BulletinDetailResDto(bulletin, author, imageUrls);
    }
}
