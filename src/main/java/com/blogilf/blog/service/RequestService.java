package com.blogilf.blog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.blogilf.blog.model.entity.Request;
import com.blogilf.blog.model.repository.RequestRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestService {

    private RequestRepository requestRepository;

    RequestService(RequestRepository requestRepository){
        this.requestRepository = requestRepository;
    }

    @Scheduled(fixedRate = 10,timeUnit = TimeUnit.SECONDS)
    public void calculate10SecondsAverage() {

        LocalDateTime now = LocalDateTime.now();
        List<Request> responseTimes = requestRepository.findByTimeBetween(now.minusSeconds(10), now);
        calculateAndLogAverage(responseTimes, "10 seconds");
    }

    @Scheduled(fixedRate = 10,timeUnit = TimeUnit.MINUTES)
    public void calculate10MinAverage() {

        LocalDateTime now = LocalDateTime.now();
        List<Request> responseTimes = requestRepository.findByTimeBetween(now.minusMinutes(10), now);
        calculateAndLogAverage(responseTimes, "10 minutes");
    }


    @Scheduled(cron = "0 0 11 * * ?") // every 11 a.m
    public void calculateDailyAverage() {
        
        LocalDateTime now = LocalDateTime.now();
        List<Request> responseTimes = requestRepository.findByTimeBetween(now.minusDays(1), now);
        calculateAndLogAverage(responseTimes, "day");

    }

    private void calculateAndLogAverage(List<Request> requests, String period) {
        
        if (requests.isEmpty()) {
            log.info("There is no requests in the last {}",period);
            return;
        }

        double average = requests.stream().mapToLong(Request::getDuration).average().orElse(0);

        log.info("Requests: {} | Avg: {} | for last {}",requests.size(), average, period);
    }
}
