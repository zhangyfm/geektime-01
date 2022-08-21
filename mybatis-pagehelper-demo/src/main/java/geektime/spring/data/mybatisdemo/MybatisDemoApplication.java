package geektime.spring.data.mybatisdemo;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import geektime.spring.data.mybatisdemo.model.Coffee;
import geektime.spring.data.mybatisdemo.model.CoffeeOrder;
import geektime.spring.data.mybatisdemo.model.OrderState;
import geektime.spring.data.mybatisdemo.service.CoffeeOrderService;
import geektime.spring.data.mybatisdemo.service.CoffeeService;
import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
@Slf4j
@MapperScan("geektime.spring.data.mybatisdemo.mapper")
public class MybatisDemoApplication implements ApplicationRunner {
    /*@Autowired
    private CoffeeMapper coffeeMappe*/;

    @Autowired
    private CoffeeService coffeeService;
    @Autowired
    private CoffeeOrderService coffeeOrderService;

    public static void main(String[] args) {
        SpringApplication.run(MybatisDemoApplication.class, args);
    }

    @Bean
    public RedisTemplate<String, Coffee> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Coffee> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer customizer() {
        return builder -> builder.readFrom(ReadFrom.MASTER_PREFERRED);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //新增
        Coffee c = Coffee.builder().name("espresso01")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0)).build();
        coffeeService.saveOneCoffee(c);
        //查找
        c = coffeeService.findOneById(c.getId());
        log.info("Find Coffee: {}", c);

        c.setPrice(Money.of(CurrencyUnit.of("CNY"),100.0));
        Thread.sleep(1000);
        //修改
        coffeeService.updatePriceByName(c);
        //查找 所有
        coffeeService.findAll();
        //删除
        //coffeeService.deleteById(c.getId());
        //查找
         coffeeService.findOneById(c.getId());

         //创建订单
        CoffeeOrder order =CoffeeOrder.builder()
                .customer("zhangsan")
                .items(Arrays.asList(c))
                .state(OrderState.INIT)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        coffeeOrderService.save(order);

        //查询放入缓存
        coffeeService.saveCache();


        //从缓存中获取数据
        Optional<Coffee> ch = coffeeService.findOneCoffee("espresso");
        log.info("Coffee {}", ch);

        for (int i = 0; i < 5; i++) {
            ch = coffeeService.findOneCoffee("espresso");
        }

        log.info("Value from Redis: {}", ch);

       /* coffeeMapper.findAllWithRowBounds(new RowBounds(1, 3))
                .forEach(c -> log.info("Page(1) Coffee {}", c));
        coffeeMapper.findAllWithRowBounds(new RowBounds(2, 3))
                .forEach(c -> log.info("Page(2) Coffee {}", c));

        log.info("===================");

        coffeeMapper.findAllWithRowBounds(new RowBounds(1, 0))
                .forEach(c -> log.info("Page(1) Coffee {}", c));

        log.info("===================");

        coffeeMapper.findAllWithParam(1, 3)
                .forEach(c -> log.info("Page(1) Coffee {}", c));
        List<Coffee> list = coffeeMapper.findAllWithParam(2, 3);
        PageInfo page = new PageInfo(list);
        log.info("PageInfo: {}", page);*/
    }
}

