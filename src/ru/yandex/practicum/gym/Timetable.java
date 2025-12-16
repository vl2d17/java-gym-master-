package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        daySchedule.computeIfAbsent(time, k -> new ArrayList<>())
                .add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {

        List<TrainingSession> result = new ArrayList<>();
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        for (List<TrainingSession> sessions : daySchedule.values()) {
            result.addAll(sessions);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek,
                                                                            TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        List<TrainingSession> sessions = daySchedule.get(timeOfDay);

        return sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    public Map<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> coachCount = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCount.put(coach, coachCount.getOrDefault(coach, 0) + 1);
                }
            }
        }
        List<Map.Entry<Coach, Integer>> sortedEntries = new ArrayList<>(coachCount.entrySet());
        sortedEntries.sort((a, b) ->
                b.getValue().compareTo(a.getValue()));

        Map<Coach, Integer> sortedResult = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> entry : sortedEntries) {
            sortedResult.put(entry.getKey(), entry.getValue());
        }
        return sortedResult;
    }
}
