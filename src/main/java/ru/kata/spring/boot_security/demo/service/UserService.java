package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void addUsers() {
        Role user = new Role("ROLE_USER");
        user.setId(1L);
        Role admin = new Role("ROLE_ADMIN");
        admin.setId(2L);
        roleRepository.saveAll(List.of(user,admin));
        User user1 = new User();
        user1.setName("Zaur");
        user1.setUsername("Zaur");
        user1.setSurname("Tregulov");
        user1.setPassword(bCryptPasswordEncoder.encode("Zaur"));
        User admin1 = new User();
        admin1.setName("Ivan");
        admin1.setUsername("Ivan");
        admin1.setSurname("Ivanov");
        admin1.setPassword(bCryptPasswordEncoder.encode("Ivan"));
        Set<Role> userRole = new HashSet<>();
        userRole.add(roleRepository.getById(1L));
        user1.setRoles(userRole);
        Set<Role> adminRole = new HashSet<>();
        adminRole.add(roleRepository.getById(2L));
        admin1.setRoles(adminRole);
        userRepository.save(user1);
        userRepository.save(admin1);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = userRepository.findByUsername(username);
        if (myUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return myUser;
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
