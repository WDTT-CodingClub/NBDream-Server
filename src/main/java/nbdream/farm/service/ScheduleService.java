package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.exception.CategoryNotFoundException;
import nbdream.common.advice.response.ApiResponse;
import nbdream.farm.domain.Crop;
import nbdream.farm.domain.Farm;
import nbdream.farm.domain.FarmWorkSchedule;
import nbdream.farm.domain.Schedule;
import nbdream.farm.exception.*;
import nbdream.farm.repository.*;
import nbdream.farm.service.dto.schedule.request.*;
import nbdream.farm.service.dto.schedule.response.FarmWorkListResDto;
import nbdream.farm.service.dto.schedule.response.ScheduleListResDto;
import nbdream.farm.service.dto.schedule.response.ScheduleResDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final FarmWorkScheduleCustomRepository farmWorkScheduleCustomRepository;
    private final FarmRepository farmRepository;
    private final ScheduleRepository scheduleRepository;
    private final CropRepository cropRepository;
    private final SearchScheduleRepository searchScheduleRepository;

    // (작물, 월) 요청받아서 농작업일정을 반환
    public FarmWorkListResDto getFarmWorkSchedule(FarmWorkListReqDto request, Long memberId) {
        List<String> cropNames = farmWorkScheduleCustomRepository.findAllCropNames();
        if(cropNames == null || cropNames.isEmpty() || !cropNames.contains(request.getCrop())){
            throw new CropFromWorkScheduleNotFoundException();
        }

        validateMonth(request.getMonth());

        List<FarmWorkSchedule> workScheduleList = farmWorkScheduleCustomRepository.findSchedulesByCropAndMonth(request.getCrop(), request.getMonth());
        return new FarmWorkListResDto().createFarmWorkListResDto(workScheduleList);
    }

    //일정 등록
    public ApiResponse<Void> registerSchedule(PostScheduleReqDto request, Long memberId) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        List<Crop> crops = cropRepository.findAll();
        ValidCheckCategory(request.getCategory(), crops);

        Schedule schedule = new Schedule(farm, request.getTitle(), request.parseStartDate(), request.parseEndDate(), request.getMemo(), request.getCategory());
        scheduleRepository.save(schedule);
        return ApiResponse.ok();
    }
    //일정 수정
    public ApiResponse<Void> updateSchedule(PutScheduleReqDto request, Long scheduleId, Long memberId) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        if(farm.getId() != schedule.getFarm().getId()){
            throw new UnEditableScheduleException();
        }
        List<Crop> crops = cropRepository.findAll();
        ValidCheckCategory(request.getCategory(), crops);

        schedule.update(request.getTitle(), request.parseStartDate(), request.parseEndDate(), request.getMemo(), request.getCategory());
        scheduleRepository.save(schedule);
        return ApiResponse.ok();
    }
    //일정 삭제
    public ApiResponse<Void> deleteSchedule(Long scheduleId, Long memberId) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        if(farm.getId() != schedule.getFarm().getId()){
            throw new UnEditableScheduleException();
        }
        scheduleRepository.delete(schedule);
        return ApiResponse.ok();
    }

    public ScheduleResDto getScheduleDetail(Long scheduleId, Long memberId) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        if(farm.getId() != schedule.getFarm().getId()){
            throw new ScheduleNotFoundException();
        }
        return new ScheduleResDto().updateResponse(schedule);
    }


    //주간 일정 조회
    public ScheduleListResDto getWeeklySchedule(WeekScheduleListReqDto request, Long memberId) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        List<Crop> crops = cropRepository.findAll();
        ValidCheckCategory(request.getCategory(), crops);
        List<Schedule> schedules = searchScheduleRepository.
                searchSchedule(farm.getId(), request.getCategory(), request.parseStartDate(), request.parseStartDate().plusDays(7));
        return new ScheduleListResDto().createResponse(schedules);
    }

    //월간 일정 조회
    public ScheduleListResDto getMonthlySchedule(ScheduleListReqDto request, Long memberId) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        List<Crop> crops = cropRepository.findAll();
        ValidCheckCategory(request.getCategory(), crops);
        List<Schedule> schedules = searchScheduleRepository.
                searchSchedule(farm.getId(), request.getCategory(), request.createStartDate(), request.createEndDate());
        return new ScheduleListResDto().createResponse(schedules);
    }

    private void ValidCheckCategory(String category, List<Crop> crops) {
        for(Crop crop : crops){
            if( category.equals(crop.getName()) || category.equals("전체")){
                return;
            }
        }
        throw new CategoryNotFoundException();
    }

    private void validateMonth(int month) {
        if(month < 1 || month > 12) {
            throw new InvalidDateFormatException();
        }
    }

}
