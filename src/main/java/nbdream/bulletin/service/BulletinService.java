package nbdream.bulletin.service;

import lombok.RequiredArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.bulletin.domain.BulletinCategory;
import nbdream.bulletin.dto.request.BulletinReqDto;
import nbdream.bulletin.exception.BulletinNotFoundException;
import nbdream.bulletin.repository.BulletinRepository;
import nbdream.image.domain.Image;
import nbdream.image.repository.ImageRepository;
import nbdream.member.domain.Member;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class BulletinService {
    private final MemberRepository memberRepository;
    private final BulletinRepository bulletinRepository;
    private final ImageRepository imageRepository;

    public Long createBulletin(final Long memberId, final BulletinReqDto request) {
        final Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());
        final Bulletin bulletin = new Bulletin(member, request.getDreamCrop(), BulletinCategory.of(request.getBulletinCategory()), request.getContent());

        final Long bulletinId = bulletinRepository.save(bulletin).getId();

        request.getImageUrls()
                .stream()
                .map(url -> imageRepository.save(new Image(bulletinId, url)));

        return bulletinId;
    }

    public Long updateBulletin(final Long memberId, final Long bulletinId, final BulletinReqDto request) {
        final Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());
        final Bulletin bulletin = bulletinRepository.findById(bulletinId).orElseThrow(() -> new BulletinNotFoundException());

        bulletin.update(member, request.getDreamCrop(), BulletinCategory.of(request.getBulletinCategory()), request.getContent());

        request.getImageUrls()
                .stream()
                .map(url -> imageRepository.save(new Image(bulletinId, url)));

        return bulletinId;
    }

    public void deleteBulletin(final Long memberId, final Long bulletinId) {
        final Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());
        final Bulletin bulletin = bulletinRepository.findById(bulletinId).orElseThrow(() -> new BulletinNotFoundException());

        imageRepository.findAllByTargetId(bulletinId)
                .stream()
                .forEach(image -> image.delete());

        bulletin.delete(member);
    }

}
