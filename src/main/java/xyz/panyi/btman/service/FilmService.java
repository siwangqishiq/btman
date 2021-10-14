package xyz.panyi.btman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.panyi.btman.dao.FilmDao;

@Service
public class FilmService {
    @Autowired
    private FilmDao mFileDao;


}
