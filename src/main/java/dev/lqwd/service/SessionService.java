package dev.lqwd.service;

import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Transactional
    public void test(){


    }

}
