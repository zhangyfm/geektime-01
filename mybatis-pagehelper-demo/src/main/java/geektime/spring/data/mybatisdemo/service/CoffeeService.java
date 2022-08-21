package geektime.spring.data.mybatisdemo.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import geektime.spring.data.mybatisdemo.mapper.CoffeeMapper;
import geektime.spring.data.mybatisdemo.model.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

/**
 * @Author: zhangyufang
 * @Description:
 * @Date: Create in 14:50 2022/8/21
 **/
@Slf4j
@Service
public class CoffeeService {

    private static final String CACHE = "springbucks-coffee";
    @Autowired
    private RedisTemplate<String, Coffee> redisTemplate;

    @Autowired
    private CoffeeMapper coffeeMapper;

    public Coffee findOneById(Long id){
        Coffee c = coffeeMapper.findById(id);
        log.info("Find One Coffee: {}", c);
        return c;
    }

    public void findAll(){
        coffeeMapper.findAllWithRowBounds(new RowBounds(1, 0))
                .forEach(c -> log.info("Page(1) Coffee {}", c));
    }

    /**
     * h缓存，有些问题，Optional类型没转换成功
     * @param name
     * @return
     */
    public Optional<Coffee> findOneCoffee(String name) {
        HashOperations<String, String, Coffee> hashOperations = redisTemplate.opsForHash();
        if (redisTemplate.hasKey(CACHE) && hashOperations.hasKey(CACHE, name)) {
            log.info("Get coffee {} from Redis.", name);
            return Optional.of(hashOperations.get(CACHE, name));
        }

      return null;
    }

    public void saveCache(){
        HashOperations<String, String, Coffee> hashOperations = redisTemplate.opsForHash();
        coffeeMapper.findAllWithRowBounds(new RowBounds(1, 0))
                .forEach(c -> {
                    log.info("Put coffee {} to Redis.", c.getName());
                    hashOperations.put(CACHE, c.getName(), c);
                    redisTemplate.expire(CACHE, 1, TimeUnit.MINUTES);
                });
    }


    public void deleteById(Long id){
        int count =coffeeMapper.deleteById(id);
        log.info("delete One Coffee: {}", count);
    }

    @Transactional
    public int saveOneCoffee(Coffee c) {
        /*Coffee c = Coffee.builder().name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0)).build();*/
        int count = coffeeMapper.save(c);
        log.info("Save {} Coffee: {}", count, c);
        return count;
    }

    /**
     *
     * @param c
     * @return
     */
    @Transactional
    public int updatePriceByName(Coffee c){
        int count=coffeeMapper.update(c);
        log.info("update {} Coffee: {}", count, c);
        return count;
    }

}
