package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.exception.CategoryNotFoundException;
import nbdream.common.advice.response.ApiResponse;
import nbdream.common.exception.UnauthorizedException;
import nbdream.farm.domain.Crop;
import nbdream.farm.domain.Farm;
import nbdream.farm.domain.FarmWorkSchedule;
import nbdream.farm.domain.Schedule;
import nbdream.farm.exception.*;
import nbdream.farm.repository.CropRepository;
import nbdream.farm.repository.ScheduleRepository;
import nbdream.farm.repository.FarmWorkScheduleCustomRepository;
import nbdream.farm.repository.SearchScheduleRepository;
import nbdream.farm.service.dto.schedule.request.*;
import nbdream.farm.service.dto.schedule.response.FarmWorkListResDto;
import nbdream.farm.service.dto.schedule.response.FarmWorkResDto;
import nbdream.farm.service.dto.schedule.response.ScheduleListResDto;
import nbdream.farm.service.dto.schedule.response.ScheduleResDto;
import nbdream.member.domain.Member;
import nbdream.member.exception.MemberNotFoundException;
import nbdream.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final FarmWorkScheduleCustomRepository farmWorkScheduleCustomRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final CropRepository cropRepository;
    private final SearchScheduleRepository searchScheduleRepository;

    // (작물, 월) 요청받아서 농작업일정을 반환
    public FarmWorkListResDto getFarmWorkSchedule(FarmWorkListReqDto request, Long memberId) {
        List<String> cropNames = farmWorkScheduleCustomRepository.findAllCropNames();
        if(cropNames == null || cropNames.isEmpty() || !cropNames.contains(request.getCrop())){
            throw new CropFromWorkScheduleNotFoundException();
        }

        List<FarmWorkSchedule> workScheduleList = farmWorkScheduleCustomRepository.findSchedulesByCropAndMonth(request.getCrop(), request.getMonth());
        if(workScheduleList == null || workScheduleList.isEmpty()){
            throw new FarmWorkScheduleNotFoundException();
        }

        return new FarmWorkListResDto().createFarmWorkListResDto(workScheduleList);
    }

    //일정 등록
    public ApiResponse<Void> registerSchedule(PostScheduleReqDto request, Long memberId) {
        Member member = memberRepository.findByIdFetchFarm(memberId).orElseThrow(MemberNotFoundException::new);
        Farm farm = member.getFarm();
        List<Crop> crops = cropRepository.findAll();
        ValidCheckCategory(request.getCategory(), crops);

        Schedule schedule = new Schedule(farm, request.getTitle(), request.parseStartDate(), request.parseEndDate(), request.getMemo(), request.getCategory());
        scheduleRepository.save(schedule);
        return ApiResponse.ok();
    }
    //일정 수정
    public ApiResponse<Void> updateSchedule(PutScheduleReqDto request, Long scheduleId, Long memberId) {
        Member member = memberRepository.findByIdFetchFarm(memberId).orElseThrow(MemberNotFoundException::new);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        if(member.getFarm().getId() != schedule.getFarm().getId()){
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
        Member member = memberRepository.findByIdFetchFarm(memberId).orElseThrow(MemberNotFoundException::new);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        if(member.getFarm().getId() != schedule.getFarm().getId()){
            throw new UnEditableScheduleException();
        }
        scheduleRepository.delete(schedule);
        return ApiResponse.ok();
    }

    public ScheduleResDto getScheduleDetail(Long scheduleId, Long memberId) {
        Member member = memberRepository.findByIdFetchFarm(memberId).orElseThrow(MemberNotFoundException::new);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        return new ScheduleResDto().updateResponse(schedule);
    }


    //주간 일정 조회
    public ScheduleListResDto getWeeklySchedule(WeekScheduleListReqDto request, Long memberId) {
        Member member = memberRepository.findByIdFetchFarm(memberId).orElseThrow(MemberNotFoundException::new);
        List<Crop> crops = cropRepository.findAll();
        ValidCheckCategory(request.getCategory(), crops);
        List<Schedule> schedules = searchScheduleRepository.
                searchSchedule(member.getFarm().getId(), request.getCategory(), request.parseStartDate(), request.parseStartDate().plusDays(7));
        return new ScheduleListResDto().createResponse(schedules);
    }

    //월간 일정 조회
    public ScheduleListResDto getMonthlySchedule(ScheduleListReqDto request, Long memberId) {
        Member member = memberRepository.findByIdFetchFarm(memberId).orElseThrow(MemberNotFoundException::new);
        List<Crop> crops = cropRepository.findAll();
        ValidCheckCategory(request.getCategory(), crops);
        List<Schedule> schedules = searchScheduleRepository.
                searchSchedule(member.getFarm().getId(), request.getCategory(), request.createStartDate(), request.createEndDate());
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

}
