package geektime.spring.data.mybatisdemo.mapper;

import java.util.List;
import java.util.Optional;

import geektime.spring.data.mybatisdemo.model.Coffee;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface CoffeeMapper {
    @Select("select * from t_coffee order by id")
    List<Coffee> findAllWithRowBounds(RowBounds rowBounds);

    @Select("select * from t_coffee order by id")
    List<Coffee> findAllWithParam(@Param("pageNum") int pageNum,
                                  @Param("pageSize") int pageSize);

    @Insert("insert into t_coffee (name, price, create_time, update_time)"
            + "values (#{name}, #{price}, now(), now())")
    @Options(useGeneratedKeys = true)
    int save(Coffee coffee);

    @Select("select * from t_coffee where id = #{id}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "create_time", property = "createTime"),
            // map-underscore-to-camel-case = true 可以实现一样的效果
            // @Result(column = "update_time", property = "updateTime"),
    })
    Coffee findById(@Param("id") Long id);


    @Select("select * from t_coffee where name =#{name}")
    /*@Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "create_time", property = "createTime"),
    })*/
    Optional<Coffee> findByName(@Param("name") String name);


    @Update("update t_coffee set price=#{price} ,update_time=now() where name=#{name} ")
    //@Options(useGeneratedKeys = true)
    int update(Coffee coffee);


    @Insert("delete from t_coffee where id=#{id}")
    @Options(useGeneratedKeys = true)
    int deleteById(Long id);
}
