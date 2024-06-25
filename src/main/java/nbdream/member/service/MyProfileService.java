package nbdream.member.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Crop;
import nbdream.farm.domain.Farm;
import nbdream.farm.domain.FarmCrop;
import nbdream.farm.domain.Location;
import nbdream.farm.exception.CropNotFoundException;
import nbdream.farm.exception.FarmNotFoundException;
import nbdream.farm.repository.CropRepository;
import nbdream.farm.repository.FarmCropRepository;
import nbdream.farm.repository.FarmRepository;
import nbdream.farm.service.LandElementsService;
import nbdream.farm.util.Coordinates;
import nbdream.member.domain.Member;
import nbdream.member.dto.request.UpdateProfileReqDto;
import nbdream.member.dto.response.MyPageResDto;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import nbdream.weather.service.WeatherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MyProfileService {
    private final MemberRepository memberRepository;
    private final FarmRepository farmRepository;
    private final FarmCropRepository farmCropRepository;
    private final CropRepository cropRepository;
    private final LandElementsService landElementsService;
    private final WeatherService weatherService;

    @Transactional(readOnly = true)
    public MyPageResDto getMyPage(final Long memberId) {
        final Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);

        final Location location = farm.getLocation();
        final List<String> crops = farmCropRepository.findByFarmId(farm.getId()).stream()
                .map(farmCrop -> farmCrop.getCrop().getName())
                .collect(Collectors.toList());

        return MyPageResDto.builder()
                .memberId(memberId)
                .nickname(member.getNickname())
                .address(location.getAddress())
                .profileImageUrl(member.getProfileImageUrl())
                .crops(crops)
                .bjdCode(location.getBjdCode())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    @Transactional
    public void updateProfile(final Long memberId, final UpdateProfileReqDto request) {
        final Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);

        //farm 객체 업데이트 되기 전 실행
        landElementsService.saveOrUpdateLandElements(farm, request.getBjdCode(), new Coordinates(request.getLatitude(), request.getLongitude()));
        member.update(request.getNickname(), request.getProfileImageUrl());

        farm.updateLocation(request.getAddress(),request.getBjdCode(), request.getLatitude(), request.getLongitude());

        //farm의 위치가 바뀌었다면 기상청 API를 통해 다시 받아오고, 아니라면 DB에서 가져옴
        weatherService.getWeathers(memberId);
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
