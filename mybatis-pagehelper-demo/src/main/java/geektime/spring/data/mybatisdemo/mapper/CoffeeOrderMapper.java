package geektime.spring.data.mybatisdemo.mapper;

import java.util.List;

import geektime.spring.data.mybatisdemo.model.CoffeeOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CoffeeOrderMapper {


    @Insert("insert into t_order (create_time, update_time, customer, state)"
            + "values (now(), now(), #{customer}, #{state})")
    @Options(useGeneratedKeys = true)
    int save(CoffeeOrder coffeeOrder);

    @Insert("insert into t_order_coffee (coffee_order_id, items_id) "
            + "values (#{orderId}, #{coffeeId})")
    int saveOrder(@Param("orderId") Long orderId,@Param("coffeeId") Long coffeeId);



    @Select("select * from t_order where id = #{id}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "create_time", property = "createTime"),
            // map-underscore-to-camel-case = true 可以实现一样的效果
            // @Result(column = "update_time", property = "updateTime"),
    })
    CoffeeOrder findById(@Param("id") Long id);

    @Select("select * from t_order ")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "create_time", property = "createTime"),
    })
    List<CoffeeOrder> findAll();

    @Insert("delete from #{customer}, #{state})")
    @Options(useGeneratedKeys = true)
    int deleteById(Long id);


}
