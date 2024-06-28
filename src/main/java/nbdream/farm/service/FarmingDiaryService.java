package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Farm;
import nbdream.farm.domain.FarmingDiary;
import nbdream.farm.exception.FarmNotFoundException;
import nbdream.farm.exception.FarmingDiaryNotFoundException;
import nbdream.farm.repository.FarmRepository;
import nbdream.farm.repository.FarmingDiaryRepository;
import nbdream.farm.repository.SearchFarmingDiaryRepository;
import nbdream.farm.service.dto.farmingdiary.FarmingDiaryListRequest;
import nbdream.farm.service.dto.farmingdiary.FarmingDiaryRequest;
import nbdream.farm.service.dto.farmingdiary.SearchFarmingDiaryCond;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmingDiaryService {

    private final FarmingDiaryRepository farmingDiaryRepository;
    private final FarmRepository farmRepository;
    private final SearchFarmingDiaryRepository searchFarmingDiaryRepository;

    public FarmingDiary createFarmingDiary(final Long memberId, FarmingDiaryRequest request) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        FarmingDiary farmingDiary = FarmingDiary.builder()
                .date(request.getDate())
                .crop(request.getCrop())
                .weatherForecast(request.getWeatherForecast())
                .workingPeopleNumber(request.getWorkingPeopleNumber())
                .workingTime(request.getWorkingTime())
                .workingArea(request.getWorkingArea())
                .memo(request.getMemo())
                .farm(farm)
                .build();

        farmingDiary = farmingDiaryRepository.save(farmingDiary);

        return farmingDiary;
    }

    public FarmingDiary updateFarmingDiary(Long farmingDiaryId, FarmingDiaryRequest request) {
        FarmingDiary farmingDiary = findByFarmingDiaryId(farmingDiaryId);

        farmingDiary.update(request);

        farmingDiaryRepository.save(farmingDiary);

        return farmingDiary;
    }


    public FarmingDiary findByFarmingDiaryId(Long farmingDiaryId){
        FarmingDiary farmingDiary = farmingDiaryRepository
                .findById(farmingDiaryId)
                .orElseThrow(FarmingDiaryNotFoundException::new);
        return farmingDiary;
    }

    public List<FarmingDiary> findMonthlyFarmingDiary(final FarmingDiaryListRequest request, final Long memberId) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        return farmingDiaryRepository.findAllByFarmIdWithCond(farm.getId(), request.getMonth(), request.getYear(), request.getCrop());
    }

    public List<FarmingDiary> searchFarmingDiary(final SearchFarmingDiaryCond cond, final Long memberId) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        return searchFarmingDiaryRepository.getFarmingDiary(farm.getId(), cond);
    }

    public void deleteByFarmingDiaryId(Long farmingDiaryId) {
        farmingDiaryRepository.deleteById(farmingDiaryId);
    }

}
