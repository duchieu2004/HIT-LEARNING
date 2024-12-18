package com.example.hit_learning;

import com.example.hit_learning.entity.ERole;
import com.example.hit_learning.entity.Role;
import com.example.hit_learning.entity.User;
import com.example.hit_learning.repository.RedisRepository;
import com.example.hit_learning.repository.RoleRepository;
import com.example.hit_learning.repository.UserRepository;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
@OpenAPIDefinition(
        info =  @Info(
                title = "HIT_LEARNING API" ,
                version = "2.0" ,
                description = "RestApi for front-end developer" ,
                contact = @Contact(
                        name = "Roãn Văn Quyền"
                        , email = "presidentquyen@gmail.com"
                )
        ),
        servers =@Server(
                url = "localhost:8080/hit-learning/",
                description = "HIT_LEARNING API url"
        )
)
@RequiredArgsConstructor
public class HitLearningApplication{

    final RedisRepository redisRepository;
    final MinioClient minioClient ;
    final RoleRepository roleRepository;
    final PasswordEncoder passwordEncoder;
    final UserRepository userRepository;


    public static void main(String[] args) {
        SpringApplication.run(HitLearningApplication.class, args);
    }
    @Bean
    ApplicationRunner applicationRunner(){
        return args -> {

            try {
                redisRepository.deleteAll("comment");
                roleRepository.save(new Role(ERole.ADMIN));
                roleRepository.save(new Role(ERole.LEADER));
                roleRepository.save(new Role(ERole.USER));

                User userLeader = new User();
                userLeader.setName("ADMIN HIT LEARNING");
                userLeader.setEmail("roan.dev.back-end@gmail.com");
                userLeader.setUsername("hit-learning-leader");
                Set<Role> rolesLe = new HashSet<>();
                rolesLe.add(new Role(ERole.USER));
                rolesLe.add(new Role(ERole.LEADER));
                userLeader.setRoles(rolesLe);
                userLeader.setPassword(passwordEncoder.encode("leader"));

                if (userRepository.findByUsername(userLeader.getUsername()) == null)
                    userRepository.save(userLeader);


                User user = new User();
                user.setName("ADMIN HIT LEARNING");
                user.setEmail("roan.dev.backend@gmail.com");
                user.setUsername("hit-learning-admin");
                Set<Role> roles = new HashSet<>();
                roles.add(new Role(ERole.USER));
                roles.add(new Role(ERole.LEADER));
                roles.add(new Role(ERole.ADMIN));
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode("admin"));

                if (userRepository.findByUsername(user.getUsername()) == null)
                    userRepository.save(user);
            }catch (Exception e){
                System.out.println("Lỗi tạo tài khoản: ");
            }

            if(!minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket("hit-learning").build())){
                minioClient.makeBucket(MakeBucketArgs.builder()
                                .bucket("hit-learning")
                        .build());
            }
        } ;
    }

}
