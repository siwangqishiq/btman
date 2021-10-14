package xyz.panyi.btman.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;
import xyz.panyi.btman.model.Film;

import java.util.List;


/**
 *
 * CREATE TABLE IF NOT EXISTS films (
 *     id INTEGER PRIMARY KEY AUTOINCREMENT,
 *     name VARCHAR(2000),
 *     href VARCHAR(2000),
 *     detail VARCHAR(5000),
 *     magnet VARCHAR(2000),
 *     magnetWeburl VARCHAR(2000),
 *     imageList VARCHAR(5000),
 *     updateTime LONG,
 *     extra VARCHAR(100)
 * );
 *
 */
@Mapper
@Repository
public interface FilmDao {
    @Insert("insert into films (name, href, detail, magnet, magnetWeburl,imageList,updateTime ,extra) " +
            "values(#{name},#{href},#{detail}," +
            "#{magnet},#{magnetWeburl},#{imageList},#{updateTime},#{extra})")
    @SelectKey(statement = "select seq as id from sqlite_sequence where (name='films')",
            before = false, keyProperty = "id", resultType = int.class)
    int insertFilm(Film film);


    @Select("select name, href, detail, magnet, magnetWeburl,imageList,updateTime ,extra from films order by updateTime desc limit 1")
    Film findLastFilm();

    @Select("select name, href, detail, magnet, magnetWeburl,imageList,updateTime ,extra from films " +
            " order by updateTime desc limit #{pageSize}")
    List<Film> queryFilmListRecent(int pageSize);

    @Select("select name, href, detail, magnet, magnetWeburl,imageList,updateTime ,extra from films " +
            "where updateTime < #{updateTime} order by updateTime desc limit #{pageSize}")
    List<Film> queryFilmList(int pageSize , long updateTime);
}
