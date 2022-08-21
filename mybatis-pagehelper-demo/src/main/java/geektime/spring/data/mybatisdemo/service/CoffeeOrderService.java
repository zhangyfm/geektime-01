package geektime.spring.data.mybatisdemo.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import geektime.spring.data.mybatisdemo.mapper.CoffeeOrderMapper;
import geektime.spring.data.mybatisdemo.model.Coffee;
import geektime.spring.data.mybatisdemo.model.CoffeeOrder;
import geektime.spring.data.mybatisdemo.model.OrderState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: zhangyufang
 * @Description:
 * @Date: Create in 15:28 2022/8/21
 **/
@Slf4j
@Service
public class CoffeeOrderService {

    @Autowired
    private CoffeeOrderMapper coffeeOrderMapper;

    public CoffeeOrder createOrder(String customer, Coffee... coffee) {
        return CoffeeOrder.builder()
                .customer(customer)
                .items(Arrays.asList(coffee))
                .state(OrderState.INIT)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
    }

    public List<CoffeeOrder> findAll(){
        List<CoffeeOrder> list =coffeeOrderMapper.findAll();
        log.info("Find List CoffeeOrder: {}", list);
        return list;
    }

    public CoffeeOrder findOneById(Long id){
        CoffeeOrder coffeeOrder =coffeeOrderMapper.findById(id);
        log.info("Find one CoffeeOrder: {}", coffeeOrder);
        return coffeeOrder;
    }

    @Transactional
    public int save(CoffeeOrder order){
        int count=coffeeOrderMapper.save(order);
        log.info("Save {} Coffee_order: {}", count, order);
        for (Coffee c:order.getItems()) {
           count= coffeeOrderMapper.saveOrder(order.getId(),c.getId());
            log.info("Save {} Coffee_order_items: {}", count, c);
        }
        return count;
    }
}
