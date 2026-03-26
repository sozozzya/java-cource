package ru.senla.hotel.application.util.sorting;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SortingService {

    public <T> List<T> sort(List<T> data, Comparator<T> comparator) {

        if (data == null || comparator == null) {
            return data;
        }

        return data.stream()
                .sorted(comparator)
                .toList();
    }
}
