package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.repository.FarmWorkScheduleRepository;
import nbdream.farm.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final FarmWorkScheduleRepository farmWorkScheduleRepository;
    private final ScheduleRepository scheduleRepository;


}
