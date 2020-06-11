package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Р—Р°РІС‚СЂР°Рє", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "РћР±РµРґ", 1000),
                new UserMeal(LocalDateTime.of(2019, Month.JANUARY, 30, 20, 0), "РЈР¶РёРЅ", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Р•РґР° РЅР° РіСЂР°РЅРёС‡РЅРѕРµ Р·РЅР°С‡РµРЅРёРµ", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Р—Р°РІС‚СЂР°Рє", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "РћР±РµРґ", 500),
                new UserMeal(LocalDateTime.of(2021, Month.JANUARY, 31, 20, 0), "РЈР¶РёРЅ", 410),
                new UserMeal(LocalDateTime.of(2020, Month.MAY, 31, 10, 0), "Р—Р°РІС‚СЂР°Рє", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.MAY, 31, 13, 0), "РћР±РµРґ", 500),
                new UserMeal(LocalDateTime.of(2020, Month.MAY, 31, 22, 0), "РЈР¶РёРЅ", 620)
        );


//        System.out.println(filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
//       filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> lsTo = new ArrayList<>();
        Map<LocalDateTime,UserMeal> mapMeals = new HashMap<>();
        Map<LocalDate,Integer> mapCalPerDay = new HashMap<>();

        for(UserMeal meal: meals){
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mapMeals.put(meal.getDateTime(), meal);
            }

            mapCalPerDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);

        }


        Iterator<LocalDate> it = mapCalPerDay.keySet().iterator();
        while (it.hasNext()) {
            LocalDate key = it.next();
            Integer value = mapCalPerDay.get(key);
            if (value <= caloriesPerDay) {
                it.remove();
            }
        }

        for (Map.Entry<LocalDateTime,UserMeal> entry : mapMeals.entrySet()) {
            boolean excess =  mapCalPerDay.containsKey(entry.getKey().toLocalDate());

            UserMealWithExcess mealWithExcess = new UserMealWithExcess(entry.getValue().getDateTime()
                    , entry.getValue().getDescription()
                    , entry.getValue().getCalories(), excess);
            lsTo.add(mealWithExcess);
        }
        return lsTo;
    }


    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate,Integer> summaryMap = meals.parallelStream()
                .collect(Collectors.groupingBy(UserMeal::getDate,
                        Collectors.summingInt((UserMeal::getCalories)))); // summing the amount of Calories per Day.

        return  meals.stream()
                .filter( meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(e -> new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(),
                        summaryMap.containsKey(e.getDate())
                                && summaryMap.get(e.getDate()) > caloriesPerDay ))
                .collect(Collectors.toList());
    }
}
