package com.example.kafgenerator.Services;
import com.example.kafgenerator.Entities.User;
import com.example.kafgenerator.Entities.UserPrincipal;
import com.example.kafgenerator.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {


@Autowired
private UserRepository userRepository;
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user == null) {

            throw new UsernameNotFoundException(username + " not found");

        }

            return new UserPrincipal(user);

    }
}
