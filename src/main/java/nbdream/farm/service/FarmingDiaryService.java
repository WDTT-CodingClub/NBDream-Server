package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.FarmingDiary;
import nbdream.farm.exception.FarmingDiaryNotFoundException;
import nbdream.farm.repository.FarmingDiaryRepository;
import nbdream.farm.service.dto.farmingdiary.FarmingDiaryRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FarmingDiaryService {

    private final FarmingDiaryRepository farmingDiaryRepository;


    public FarmingDiary createFarmingDiary(FarmingDiaryRequest request) {

        FarmingDiary farmingDiary = FarmingDiary.builder()
                .date(request.getDate())
                .crop(request.getCrop())
                .weatherForecast(request.getWeatherForecast())
                .workingPeopleNumber(request.getWorkingPeopleNumber())
                .workingTime(request.getWorkingTime())
                .workingArea(request.getWorkingArea())
                .memo(request.getMemo())
                .build();

        farmingDiary = farmingDiaryRepository.save(farmingDiary);

        return farmingDiary;
    }


    public FarmingDiary updateFarmingDiary(Long farmingDiaryId, FarmingDiaryRequest request) {

        FarmingDiary farmingDiary = findByFarmingDiaryId(farmingDiaryId);

        farmingDiary = FarmingDiary.builder()
                .crop(request.getCrop())
                .date(request.getDate())
                .weatherForecast(request.getWeatherForecast())
                .workingTime(request.getWorkingTime())
                .workingArea(request.getWorkingArea())
                .workingPeopleNumber(request.getWorkingPeopleNumber())
                .memo(request.getMemo())
                .build();

        farmingDiaryRepository.save(farmingDiary);

        return farmingDiary;
    }


    public FarmingDiary findByFarmingDiaryId(Long farmingDiaryId){
        FarmingDiary farmingDiary = farmingDiaryRepository
                .findById(farmingDiaryId)
                .orElseThrow(FarmingDiaryNotFoundException::new);
        return farmingDiary;
    }

    public void deleteByFarmingDiaryId(Long farmingDiaryId) {
        farmingDiaryRepository.deleteById(farmingDiaryId);
    }

}
