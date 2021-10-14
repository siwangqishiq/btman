package xyz.panyi.btman.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.panyi.btman.model.Film;
import xyz.panyi.btman.model.Resp;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {
    @GetMapping(value = "films")
    Resp<List<Film>> queryFilms(){
        List<Film> list = new ArrayList<Film>();
        Film film = new Film();
        film.setName("向西村上春树");
        list.add(film);
        return Resp.genResp(list);
    }
}
