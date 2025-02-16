package com.myproject.springbootcdcdebezium.repository;

import com.myproject.springbootcdcdebezium.entity.DBCDCLog;
import com.myproject.springbootcdcdebezium.entity.LogState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author nguyenle
 * @since 3:28 PM Fri 2/14/2025
 */
@Repository
public interface CDCLogRepository extends MongoRepository<DBCDCLog, String> {

    List<DBCDCLog> findDBCDCLogsByStateAndIgnoredOrderByCreatedDateAsc(LogState logState, boolean ignored);

}
