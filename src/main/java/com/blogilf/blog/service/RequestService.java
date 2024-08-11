package com.blogilf.blog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.blogilf.blog.model.Request;
import com.blogilf.blog.repository.RequestRepository;

@Component
public class RequestService {

    // private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

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
            System.out.println("There is no requests in the last " + period);
            // logger.info("There is no requests in the last " + period + ".");
            return;
        }

        double average = requests.stream().mapToLong(Request::getDuration).average().orElse(0);

        System.out.println("Requests: " + requests.size() +" | AVG: " + average + " for last " + period);
        // logger.info("AVG response time for the last " + period + " is " + average + " ms");
    }
}
