package com.coursework.jobseeker.service;

import com.coursework.jobseeker.model.User;
import com.coursework.jobseeker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user =  userRepository.findByUsername(username);
        if (user == null) {
            return userRepository.findByEmail(username);
        }
        return user;
    }

    // создать нового пользователя
    public User createUser(String username,
                                 String email,
                                 String password)  {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }


    // редактировать профиль
    public void editProfile( String name,
                           String surname,
                           String patronymic,
                           String city,
                           String phone,
                           String about,
                           User user) {
        user.setName(name);
        user.setSurname(surname);
        user.setPatronymic(patronymic);
        user.setCity(city);
        user.setPhoneNumber(phone);
        user.setAbout(about);
        userRepository.save(user);
    }

    // получить инициалы пользователя
    public String getInitials(User user) {
        if (user.getSurname() == null) {
            return "";
        } else {
            String initials = user.getSurname() + " " + user.getName().charAt(0) + ". " + user.getPatronymic().charAt(0) + ".";
            return initials;
        }
    }

    public User getUserById(long id){
        User user = userRepository.findById(id);
        return user;
    }

    public User getUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        return user;
    }

    public User getUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        return user;
    }
}
