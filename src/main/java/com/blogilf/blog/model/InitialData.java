package com.blogilf.blog.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.blogilf.blog.config.SecurityConfig;
import com.blogilf.blog.model.entity.Article;
import com.blogilf.blog.model.entity.Role;
import com.blogilf.blog.model.entity.User;
import com.blogilf.blog.model.repository.ArticleRepository;
import com.blogilf.blog.model.repository.CountryRepository;
import com.blogilf.blog.model.repository.UserRepository;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;

import java.util.Arrays;
import java.util.Random;

@Component
@Order(2)
public class InitialData implements CommandLineRunner {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final Random random = new Random();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(SecurityConfig.encoderStrength);

    InitialData(ArticleRepository articleRepository, UserRepository userRepository, CountryRepository countryRepository){
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User admin1 = createUser("Admin1", "admin1", "000", Role.ADMIN);
        
        User user1 = createUser("Amir", "amir", "111", Role.USER);
        User user2 = createUser("Ali", "ali", "222", Role.USER);
        User user3 = createUser("Reza", "reza", "333", Role.USER);

        userRepository.save(admin1);
        userRepository.saveAll(Arrays.asList(user1, user2, user3));

        createArticle(user1, 1, 9);
        createArticle(user2, 21, 23);
        createArticle(user3, 31, 33);

        for (int i = 4; i <= 15; i++) {
            String name = "Name" + i;
            String username = "user" + i;
            String password = "123";
            User user = createUser(name, username, password, Role.USER);
            userRepository.save(user);
        }
        System.out.println("\nData added!\n");
    }

    private User createUser(String name, String username, String password, Role role) {
        Point randomLocation = createLocation();
        return User.builder()
                .name(name)
                .username(username)
                .password(encoder.encode(password))
                .role(role)
                .location(randomLocation)
                .country(countryRepository.findCountryByLocation(randomLocation).orElse(null))
                .build();
    }

    private Point createLocation() {
        double lat = -90 + 180 * random.nextDouble();  // lat between -90 and 90
        double lon = -180 + 360 * random.nextDouble(); // lon between -180 and 180
        Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
        point.setSRID(4326); // to work with country polygons
        return point;
    }

    private void createArticle(User user, int start, int end) {
        for (int i = start; i <= end; i++) {
            Article article = Article.builder()
                    .title("Article " + i)
                    .content("Hey, " + i)
                    .slug("slug" + i)
                    .author(user)
                    .view(random.nextLong(1000, 10000))
                    .readTime(random.nextInt(1, 60))
                    .build();
            articleRepository.save(article);
        }
    }
}
