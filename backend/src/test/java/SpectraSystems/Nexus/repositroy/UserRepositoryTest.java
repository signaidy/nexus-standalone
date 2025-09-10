package SpectraSystems.Nexus.repositroy;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import SpectraSystems.Nexus.models.Role;
import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.repositories.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void userRepository_SaveAll_RetrunSavedUser(){
        //Arrange
        User user = User.builder().first_Name("John").last_Name("Doe").email("john@doe.com").age(8).password("1234567890").country("Spain").passport("1234567890").role(Role.ROLE_USER).build();
        //Act
        User savedUser = userRepository.save(user);
        //Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test 
    public  void userRepository_GetAll_ReturnMoreThanOneUser(){
        //Arrange
        User user = User.builder().first_Name("John").last_Name("Doe").email("john@doe.com").age(8).password("1234567890").country("Spain").passport("1234567890").role(Role.ROLE_USER).build();
        User user2 = User.builder().first_Name("John").last_Name("Wick").email("john@wick.com").age(8).password("1234567890").country("Russia").passport("1234567890").role(Role.ROLE_USER).build();
       
        //Act
        userRepository.save(user);
        userRepository.save(user2);
        List<User> users= userRepository.findAll();
        //Assert
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);

    }

    @Test 
    public  void userRepository_FindById_ReturnsUser(){
        //Arrange
        User user = User.builder().first_Name("John").last_Name("Doe").email("john@doe.com").age(8).password("1234567890").country("Spain").passport("1234567890").role(Role.ROLE_USER).build();
        
        //Act
        userRepository.save(user);
        User users= userRepository.findById(user.getId()).get();
        //Assert
        Assertions.assertThat(users).isNotNull();

    }

    @Test 
    public  void userRepository_FindByEmail_ReturnsUser(){
        //Arrange
        User user = User.builder().first_Name("John").last_Name("Doe").email("john@doe.com").age(8).password("1234567890").country("Spain").passport("1234567890").role(Role.ROLE_USER).build();
        
        //Act
        userRepository.save(user);
        User users= userRepository.findByEmail(user.getEmail()).get();
        //Assert
        Assertions.assertThat(users).isNotNull();

    }

    @Test 
    public  void userRepository_UpdateUser_ReturnsUser(){
        //Arrange
        User user = User.builder().first_Name("John").last_Name("Doe").email("john@doe.com").age(8).password("1234567890").country("Spain").passport("1234567890").role(Role.ROLE_USER).build();
        
        //Act
        userRepository.save(user);
        User userSave= userRepository.findById(user.getId()).get();
        userSave.setCountry("Russia");
        userSave.setPercentage(20);

        User updatedUser = userRepository.save(userSave);
        //Assert
        Assertions.assertThat(updatedUser.getCountry()).isNotNull();
        Assertions.assertThat(updatedUser.getPercentage()).isNotNull();

    }

    @Test 
    public  void userRepository_Delete_ReturnsUser(){
        //Arrange
        User user = User.builder().first_Name("John").last_Name("Doe").email("john@doe.com").age(8).password("1234567890").country("Spain").passport("1234567890").role(Role.ROLE_USER).build();
        
        //Act
        userRepository.save(user);

        userRepository.deleteById(user.getId());
        Optional<User> userReturn = userRepository.findById(user.getId());
        //Assert
        Assertions.assertThat(userReturn).isEmpty();

    }
}
