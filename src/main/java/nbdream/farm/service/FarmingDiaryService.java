package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.FarmingDiary;
import nbdream.farm.repository.FarmingDiaryRepository;
import nbdream.farm.service.dto.farmingdiary.CreateDiaryRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Service
@RequiredArgsConstructor
public class FarmingDiaryService {

    private final FarmingDiaryRepository farmingDiaryRepository;


    public FarmingDiary createFarmingDiary(CreateDiaryRequest request) {

        FarmingDiary farmingDiaryEntity = FarmingDiary.builder()
                .date(request.getDate())
                .crop(request.getCrop())
                .workingPeopleNumber(request.getWorkingPeopleNumber())
                .workingTime(request.getWorkingTime())
                .workingArea(request.getWorkingArea())
                .memo(request.getMemo())
                .build();

        farmingDiaryEntity = farmingDiaryRepository.save(farmingDiaryEntity);

        return farmingDiaryEntity;
    }


    public void updateFarmingDiary(){}


    public void deleteFarmingDiary(Long farmingDiaryId){}


    public void getFarmingDiaries(){}
}
