package nbdream.calendar.service;

import com.google.gson.Gson;
import jakarta.persistence.SecondaryTable;
import lombok.RequiredArgsConstructor;
import nbdream.calendar.domain.Holiday;
import nbdream.calendar.dto.HolidayResponse;
import nbdream.calendar.repository.HolidayRepository;
import nbdream.farm.domain.FarmingDiary;
import nbdream.farm.exception.FetchApiInternalServerErrorException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayExplorer holidayExplorer;
    private final HolidayRepository holidayRepository;

    List<String> operationUris = List.of(
            "getHoliDeInfo",
            "getRestDeInfo",
            "getAnniversaryInfo",
            "get24DivisionsInfo",
            "getSundryDayInfo"
    );

    public List<HolidayResponse> getAllHolidays(String solYear, String solMonth) {
        Set<HolidayResponse> allHolidays = new HashSet<>();

        for (String uri : operationUris) {
            try {
                allHolidays.addAll(fetchHolidays(uri, solYear, solMonth));
            } catch (IOException e) {
                throw new FetchApiInternalServerErrorException();
            }
        }
        return new ArrayList<>(allHolidays);
    }


    private List<HolidayResponse> fetchHolidays(String operationUri, String solYear, String solMonth) throws IOException {
        JSONObject jsonObject = holidayExplorer.getHolidayExplorer(operationUri, solYear, solMonth);
        JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");

        List<HolidayResponse> holidayResponses = new ArrayList<>();

        if (body.getInt("totalCount") != 0) {
            if (body.getInt("totalCount") == 1) {
                JSONObject item = body.getJSONObject("items").getJSONObject("item");
                HolidayResponse holidayResponse = HolidayResponse.builder()
                        .dateKind(item.getString("dateKind"))
                        .localDate(String.valueOf(item.getInt("locdate")))
                        .dateName(item.getString("dateName"))
                        .isHoliday(item.getString("isHoliday"))
                        .build();
                holidayResponses.add(holidayResponse);
            } else {
                JSONArray items = body.getJSONObject("items").getJSONArray("item");
                for (Object item : items) {
                    JSONObject map = new JSONObject(new Gson().toJson(item)).getJSONObject("map");
                    HolidayResponse holidayResponse = HolidayResponse.builder()
                            .dateKind((map.getString("dateKind")))
                            .localDate(String.valueOf(map.getInt("locdate")))
                            .dateName(map.getString("dateName"))
                            .isHoliday(map.getString("isHoliday"))
                            .build();
                    holidayResponses.add(holidayResponse);
                }
            }
        }
        return holidayResponses;
    }


    public void createHolidays(FarmingDiary farmingDiaryId, List<HolidayResponse> responses) {

        List<Holiday> holidays = responses.stream()
                .map(response -> {
                    Holiday holiday = Holiday.builder()
                            .farmingDiary(farmingDiaryId)
                            .localDate(response.getLocalDate())
                            .dateKind(response.getDateKind())
                            .dateName(response.getDateName())
                            .isHoliday(response.getIsHoliday())
                            .build();
                    return holiday;
                })
                .collect(Collectors.toList());

        holidayRepository.saveAll(holidays);
    }




}