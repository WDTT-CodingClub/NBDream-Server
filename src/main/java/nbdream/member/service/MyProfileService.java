package nbdream.member.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Crop;
import nbdream.farm.domain.Farm;
import nbdream.farm.domain.FarmCrop;
import nbdream.farm.domain.Location;
import nbdream.farm.exception.CropNotFoundException;
import nbdream.farm.repository.CropRepository;
import nbdream.farm.repository.FarmCropRepository;
import nbdream.member.domain.Member;
import nbdream.member.dto.request.UpdateProfileReqDto;
import nbdream.member.dto.response.MyPageResDto;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyProfileService {
    private final MemberRepository memberRepository;
    private final FarmCropRepository farmCropRepository;
    private final CropRepository cropRepository;

    @Transactional(readOnly = true)
    public MyPageResDto getMyPage(final Long memberId) {
        final Member member = memberRepository.findByIdFetchFarm(memberId).orElseThrow(() -> new MemberNotFoundException());
        final Farm farm = member.getFarm();

        final Location location = farm.getLocation();
        final List<String> crops = farmCropRepository.findByFarmId(farm.getId()).stream()
                .map(farmCrop -> farmCrop.getCrop().getName())
                .collect(Collectors.toList());

        return MyPageResDto.builder()
                .nickname(member.getNickname())
                .address(location.getAddress())
                .profileImageUrl(member.getProfileImageUrl())
                .crops(crops)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    @Transactional
    public void updateProfile(final Long memberId, final UpdateProfileReqDto request) {
        final Member member = memberRepository.findByIdFetchFarm(memberId).orElseThrow(() -> new MemberNotFoundException());
        final Farm farm = member.getFarm();

        member.update(request.getNickname(), request.getProfileImageUrl());
        farm.updateLocation(request.getAddress(), request.getLatitude(), request.getLongitude());
        updateFarmCrops(farm, request);
    }

    public void updateFarmCrops(final Farm farm, final UpdateProfileReqDto request) {
        List<FarmCrop> farmCrops = farmCropRepository.findByFarmId(farm.getId());
        List<String> cropNames = new ArrayList<>();

        for (FarmCrop farmCrop : farmCrops) {
            if (!request.getCrops().contains(farmCrop.getCrop().getName())) {
                farmCropRepository.delete(farmCrop);
            }
        }

        for (String cropName : request.getCrops()) {
            if (!cropNames.contains(cropName)) {
                Crop crop = cropRepository.findByName(cropName).orElseThrow(() -> new CropNotFoundException());
                farmCropRepository.save(new FarmCrop(farm, crop));
            }
        }
    }
}
