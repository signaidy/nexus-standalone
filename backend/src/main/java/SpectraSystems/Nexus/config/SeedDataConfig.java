package SpectraSystems.Nexus.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import SpectraSystems.Nexus.models.Aboutus;
import SpectraSystems.Nexus.models.Role;
import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.repositories.AboutusRespository;
import SpectraSystems.Nexus.repositories.UserRepository;
import SpectraSystems.Nexus.services.AboutUsService;
import SpectraSystems.Nexus.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AboutusRespository aboutusRespository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AboutUsService aboutUsService;

    
    /** 
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        
      if (userRepository.count() == 0) {

        User admin = User
                      .builder()
                      .first_Name("admin")
                      .last_Name("admin")
                      .email("admin@admin.com")
                      .password(passwordEncoder.encode("password"))
                      .role(Role.ROLE_ADMIN)
                      .age(99)
                      .country("USA")
                      .passport("123456789")
                      .build();

        userService.save(admin);
        log.debug("created ADMIN user - {}", admin);
      }
      if (aboutusRespository.count() == 0) {
        Aboutus admin = Aboutus
                      .builder()
                      .slogan("UwU")
                      .gif("https://i.pinimg.com/originals/31/a7/2a/31a72afda250825d993400c3ef28c55c.gif")
                      .yt("https://www.youtube.com/embed/a5plqZLfxRE?si=pPvS-7aP8J0ek8HY")
                      .cardsAmount(2)
                      .titleOne("ez")
                      .textOne("xdxdxd")
                      .imgOne("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_qnBaqCekVMgJ6kDf68fu_dv8znl_3czptk3F-NWnqw&s")
                      .titleTwo("gateto")
                      .textTwo("xdxdxd")
                      .imgTwo("https://i.imgflip.com/2/4mol3v.jpg")
                      .titleThree(null)
                      .textThree(null)
                      .imgThree(null)
                      .titleFour(null)
                      .textFour(null)
                      .imgFour(null)
                      .build();

        aboutUsService.saveOrUpdate(admin);
        log.debug("created AboutUs info - {}", admin);
      }
    }

}