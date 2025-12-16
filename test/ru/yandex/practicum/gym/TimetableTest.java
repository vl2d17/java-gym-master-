package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);


        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(
                DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(singleTrainingSession, mondaySessions.get(0));
        Assertions.assertEquals(new TimeOfDay(13, 0),
                mondaySessions.get(0).getTimeOfDay());

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(
                DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(
                DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(
                DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());
        Assertions.assertEquals(thursdayChildTrainingSession, thursdaySessions.get(0));
        Assertions.assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1));

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(
                DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertEquals(1, sessions.size());
        Assertions.assertEquals(singleTrainingSession, sessions.get(0));

        sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        Assertions.assertTrue(sessions.isEmpty());
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Козлов", "Виталий", "Петрович");
        Coach coach2 = new Coach("Маликова", "Валентина",
                "Михайловна");
        Group group1 = new Group("Детская группа", Age.CHILD, 60);
        Group group2 = new Group("Взрослая группа", Age.ADULT, 90);

        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));
        TrainingSession session3 = new TrainingSession(group1, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(9, 30));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);

        // сортировка по времени для дня
        List<TrainingSession> wednesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY);
        Assertions.assertEquals(3, wednesdaySessions.size());

        // порядок по времени (использовал compareTo)
        Assertions.assertTrue(wednesdaySessions.get(0).getTimeOfDay().compareTo(
                wednesdaySessions.get(1).getTimeOfDay()) <= 0);
        Assertions.assertTrue(wednesdaySessions.get(1).getTimeOfDay().compareTo(
                wednesdaySessions.get(2).getTimeOfDay()) <= 0);

        // несколько тренировок в одно время
        List<TrainingSession> sessionsAt10 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));
        Assertions.assertEquals(2, sessionsAt10.size());

        // содержимое по разным критериям
        boolean foundSession1 = false;
        boolean foundSession2 = false;
        for (TrainingSession session : sessionsAt10) {
            if (session.getCoach().equals(coach1) && session.getGroup().equals(group1)) {
                foundSession1 = true;
            }
            if (session.getCoach().equals(coach2) && session.getGroup().equals(group2)) {
                foundSession2 = true;
            }
        }
        Assertions.assertTrue(foundSession1, "Должна быть найдена session1");
        Assertions.assertTrue(foundSession2, "Должна быть найдена session2");
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        //Для всех дней недели возвращаются пустые списки
        for (DayOfWeek day : DayOfWeek.values()) {
            List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(
                    day);
            Assertions.assertTrue(sessions.isEmpty());
            List<TrainingSession> sessionsAtTime = timetable.getTrainingSessionsForDayAndTime(
                    day, new TimeOfDay(10, 0));
            Assertions.assertTrue(sessionsAtTime.isEmpty());
        }
    }

    @Test
    void testSessionsOrderWithDifferentTimes() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Страшная", "Анна", "Сергеевна");
        Group group = new Group("Test group", Age.ADULT, 55);
        // Создаем тренировки
        TrainingSession session1 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(18, 30));
        TrainingSession session2 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(9, 0));
        TrainingSession session3 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(14, 15));
        TrainingSession session4 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(11, 45));

        // Добавляем в разном порядке
        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);
        timetable.addNewTrainingSession(session4);

        List<TrainingSession> fridaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY);

        // Проверить правильный порядок сортировки
        Assertions.assertEquals(4, fridaySessions.size());

        // Проверим порядок, сравнивая время
        TimeOfDay prevTime = null;
        for (TrainingSession session : fridaySessions) {
            if (prevTime != null) {
                Assertions.assertTrue(prevTime.compareTo(session.getTimeOfDay()) <= 0,
                        "Времена должны быть отсортированы по возрастанию");
            }
            prevTime = session.getTimeOfDay();
        }

        // Конкретные проверки времени
        Assertions.assertEquals(9 * 60 + 0, fridaySessions.get(0).getTimeOfDay().getHours() * 60 +
                fridaySessions.get(0).getTimeOfDay().getMinutes());
        Assertions.assertEquals(11 * 60 + 45, fridaySessions.get(1).getTimeOfDay().getHours() * 60 +
                fridaySessions.get(1).getTimeOfDay().getMinutes());
        Assertions.assertEquals(14 * 60 + 15, fridaySessions.get(2).getTimeOfDay().getHours() * 60 +
                fridaySessions.get(2).getTimeOfDay().getMinutes());
        Assertions.assertEquals(18 * 60 + 30, fridaySessions.get(3).getTimeOfDay().getHours() * 60 +
                fridaySessions.get(3).getTimeOfDay().getMinutes());
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Виктор", "Николаевич");
        Group group = new Group("Группа", Age.ADULT, 70);

        //три тренировки для одного тренера
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 15)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(19, 45)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(3, result.get(coach));
    }

    @Test
    void testGetCountByCoachesMultipleCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Козлов", "Виталий", "Петрович");  // 4 тренировки
        Coach coach2 = new Coach("Маликова", "Валентина", "Михайловна"); // 2 тренировки
        Coach coach3 = new Coach("Страшная", "Анна", "Сергеевна"); // 1 тренировка

        Group group = new Group("Группа", Age.ADULT, 90);

        // Добавляем тренировки в разном порядке
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0))); // Маликова
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0))); // Козлов
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0))); // Козлов
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.FRIDAY, new TimeOfDay(16, 0))); // Страшная
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.FRIDAY, new TimeOfDay(18, 0))); // Козлов
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(11, 0))); // Маликова
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0))); // Козлов

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        // Проверить размер
        Assertions.assertEquals(3, result.size());

        // Преобразуем в список для проверки порядка
        List<Map.Entry<Coach, Integer>> entries = new ArrayList<>(result.entrySet());

        // Проверить порядок по убыванию количества тренировок
        Assertions.assertEquals(coach1, entries.get(0).getKey());
        Assertions.assertEquals(4, entries.get(0).getValue());

        Assertions.assertEquals(coach2, entries.get(1).getKey());
        Assertions.assertEquals(2, entries.get(1).getValue());

        Assertions.assertEquals(coach3, entries.get(2).getKey());
        Assertions.assertEquals(1, entries.get(2).getValue());
    }

    @Test
    void testGetCountByCoachesWithEqualCounts() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Бабакян", "Сордор", "Ирушевич");
        Coach coach2 = new Coach("Борисов", "Михаил", "Олегович");
        Group group = new Group("Группа", Age.CHILD, 60);

        // Оба тренера имеют по 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(10, 0)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.containsKey(coach1));
        Assertions.assertTrue(result.containsKey(coach2));
        Assertions.assertEquals(2, result.get(coach1));
        Assertions.assertEquals(2, result.get(coach2));
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();
        Map<Coach, Integer> result = timetable.getCountByCoaches();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testAddAndGetPerformance() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Тестов", "Тимофей", "Талгатович");
        Group group = new Group("Тест", Age.ADULT, 60);

        // для проверки производительности
        int count = 100;
        for (int i = 0; i < count; i++) {
            int hour = i % 24;
            int minute = (i * 7) % 60;
            DayOfWeek day = DayOfWeek.values()[i % 7];

            timetable.addNewTrainingSession(new TrainingSession(group, coach,
                    day, new TimeOfDay(hour, minute)));
        }

        //  Проверка все ли добавились
        int totalSessions = 0;
        for (DayOfWeek day : DayOfWeek.values()) {
            totalSessions += timetable.getTrainingSessionsForDay(day).size();
        }
        Assertions.assertEquals(count, totalSessions);
    }

    @Test
    void testTrainingSessionEqualsAndHashCode() {
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петрова", "Мария", "Сергеевна");
        Group group1 = new Group("Группа 1", Age.CHILD, 60);
        Group group2 = new Group("Группа 2", Age.ADULT, 90);

        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)); // Такая же как session1
        TrainingSession session3 = new TrainingSession(group2, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)); // Другая группа
        TrainingSession session4 = new TrainingSession(group1, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)); // Другой тренер
        TrainingSession session5 = new TrainingSession(group1, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0)); // Другой день

        // Проверка equals
        Assertions.assertEquals(session1, session2, "Одинаковые тренировки должны быть равны");
        Assertions.assertNotEquals(session1, session3, "Разные группы - разные тренировки");
        Assertions.assertNotEquals(session1, session4, "Разные тренеры - разные тренировки");
        Assertions.assertNotEquals(session1, session5, "Разные дни - разные тренировки");

        // Проверка hashCode
        Assertions.assertEquals(session1.hashCode(), session2.hashCode(),
                "Равные объекты должны иметь одинаковый hashCode");
    }
}



