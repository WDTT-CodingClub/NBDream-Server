package nbdream.bulletin.service;

import lombok.RequiredArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.bulletin.dto.request.SearchBulletinCondDto;
import nbdream.bulletin.dto.response.BulletinResDto;
import nbdream.bulletin.dto.response.BulletinsResDto;
import nbdream.bulletin.exception.BulletinNotFoundException;
import nbdream.bulletin.repository.BulletinRepository;
import nbdream.bulletin.repository.SearchingBulletinRepository;
import nbdream.image.domain.Image;
import nbdream.image.repository.ImageRepository;
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

        final List<Long> bulletinIds = bulletins.stream()
                .map(bulletin -> bulletin.getId())
                .collect(Collectors.toList());

        HashMap<Long, List<String>> imageMap = new HashMap<>();
        mappingImagesByTargetId(imageMap, imageRepository.findAllByTargetIds(bulletinIds));

        final List<BulletinResDto> bulletinResDtos = bulletins.stream()
                .map(bulletin -> new BulletinResDto(bulletin, bulletin.getAuthor(), imageMap.get(bulletin.getId()), bulletin.getComments()))
                .collect(Collectors.toList());

        return new BulletinsResDto(bulletinResDtos, hasNext(bulletins));
    }

    private boolean hasNext(List<Bulletin> bulletins) {
        if (bulletins.size() > PAGE_SIZE) {
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
