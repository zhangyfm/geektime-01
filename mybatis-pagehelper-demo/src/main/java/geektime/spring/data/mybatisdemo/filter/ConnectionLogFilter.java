package geektime.spring.data.mybatisdemo.filter;

import java.util.Properties;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionLogFilter extends FilterEventAdapter {

    @Override
    public void connection_connectBefore(FilterChain chain, Properties info) {
        log.info("BEFORE CONNECTION!");
    }

    @Override
    public void connection_connectAfter(ConnectionProxy connection) {
        log.info("AFTER CONNECTION!");
    }

    @Override
    protected void statementExecuteUpdateBefore(StatementProxy statement, String sql) {
        log.info("UpdateBefore111 {} Sql {}",statement.getParameters(),sql);
    }

    @Override
    protected void resultSetOpenAfter(ResultSetProxy resultSet) {
        log.info("statementExecuteQueryAfter-zfq :{}",resultSet);
    }

    @Override
    protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
        log.info("statementExecuteQueryAfter-zfq :{}",sql);
    }
}
