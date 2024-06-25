package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.FarmingDiary;
import nbdream.farm.domain.Work;
import nbdream.farm.domain.WorkCategory;
import nbdream.farm.repository.WorkRepository;
import nbdream.farm.service.dto.work.WorkDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;

    public void createWorks(FarmingDiary farmingDiaryId, List<WorkDto> requests) {
        List<Work> works = requests.stream()
                .map(request -> {
                    Work work = Work.builder()
                            .farmingDiary(farmingDiaryId)
                            .workCategory(WorkCategory.valueOf(request.getWorkCategory()))
                            .content(request.getContent())
                            .build();
                    return work;
                })
                .collect(Collectors.toList());

        workRepository.saveAll(works);

    }

    public void updateWorks(FarmingDiary farmingDiary, List<WorkDto> requests) {
        deleteWorks(farmingDiary.getId());
        createWorks(farmingDiary, requests);
    }

    public void deleteWorks(Long farmingDiaryId) {
        List<Work> works = workRepository.findAllByFarmingDiaryId(farmingDiaryId);
        for(Work work : works) {
            workRepository.delete(work);
        }
    }

    public List<Work> findAllByFarmingDiaryId(Long farmingDiaryId) {
        return workRepository.findAllByFarmingDiaryId(farmingDiaryId);
    }

}
