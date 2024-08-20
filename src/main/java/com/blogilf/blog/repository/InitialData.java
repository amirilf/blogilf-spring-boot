package com.blogilf.blog.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.blogilf.blog.config.SecurityConfig;
import com.blogilf.blog.model.Article;
import com.blogilf.blog.model.Role;
import com.blogilf.blog.model.User;

import io.jsonwebtoken.lang.Arrays;

@Component
public class InitialData implements CommandLineRunner {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(SecurityConfig.encoderStrength);

    InitialData(ArticleRepository articleRepository, UserRepository userRepository){
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // initial some starter data for testing

        User admin1 = User.builder().name("Admin1").username("admin1").password(encoder.encode("000")).role(Role.ADMIN).build();
        
        User user1 = User.builder().name("Amir").username("amir").password(encoder.encode("111")).role(Role.USER).build();
        User user2 = User.builder().name("Ali").username("ali").password(encoder.encode("222")).role(Role.USER).build();
        User user3 = User.builder().name("Reza").username("reza").password(encoder.encode("333")).role(Role.USER).build();

        Article a11 = Article.builder().title("Article 1").content("Hey, 11").slug("first1").author(user1).view(100l).readTime(2).build();
        Article a12 = Article.builder().title("Article 2").content("Hey, 12").slug("first2").author(user1).view(150l).readTime(3).build();
        Article a13 = Article.builder().title("Article 3").content("Hey, 13").slug("first3").author(user1).view(200l).readTime(5).build();
        Article a14 = Article.builder().title("Article 4").content("Hey, 14").slug("first4").author(user1).view(10000l).readTime(2).build();
        Article a15 = Article.builder().title("Article 5").content("Hey, 15").slug("first5").author(user1).view(1200l).readTime(15).build();
        Article a16 = Article.builder().title("Article 6").content("Hey, 16").slug("first6").author(user1).view(110l).readTime(10).build();
        Article a17 = Article.builder().title("Article 7").content("Hey, 17").slug("first7").author(user1).view(100l).readTime(21).build();
        Article a18 = Article.builder().title("Article 8").content("Hey, 18").slug("first8").author(user1).view(100l).readTime(56).build();
        Article a19 = Article.builder().title("Article 9").content("Hey, 19").slug("first9").author(user1).view(1110l).readTime(30).build();

        Article a21 = Article.builder().title("Article 21").content("Hey, 21").slug("second1").author(user2).view(5440l).readTime(5).build();
        Article a22 = Article.builder().title("Article 22").content("Hey, 22").slug("second2").author(user2).view(1230l).readTime(3).build();
        Article a23 = Article.builder().title("Article 23").content("Hey, 23").slug("second3").author(user2).readTime(11).build();

        Article a31 = Article.builder().title("Article 31").content("Hey, 31").slug("third1").author(user3).readTime(14).build();
        Article a32 = Article.builder().title("Article 32").content("Hey, 32").slug("third2").author(user3).view(234526l).readTime(41).build();
        Article a33 = Article.builder().title("Article 33").content("Hey, 33").slug("third3").author(user3).view(16l).readTime(4).build();

        userRepository.save(admin1);
        userRepository.saveAll(Arrays.asList(new User[]{user1,user2,user3}));
        articleRepository.saveAll(Arrays.asList(new Article[]{a11,a12,a13,a14,a15,a16,a17,a18,a19}));
        articleRepository.saveAll(Arrays.asList(new Article[]{a21,a22,a23}));
        articleRepository.saveAll(Arrays.asList(new Article[]{a31,a32,a33}));

        System.out.println("\nData added!\n");
    }
}
