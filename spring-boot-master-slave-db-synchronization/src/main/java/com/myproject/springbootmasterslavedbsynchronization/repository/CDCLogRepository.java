package com.myproject.springbootmasterslavedbsynchronization.repository;

import com.myproject.springbootmasterslavedbsynchronization.entity.DBCDCLog;
import com.myproject.springbootmasterslavedbsynchronization.entity.LogState;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author nguyenle
 * @since 11:35 AM Tue 2/18/2025
 */
public interface CDCLogRepository extends MongoRepository<DBCDCLog, String> {

	List<DBCDCLog> findDBCDCLogByStateAndIgnoredOrderByReceiveDateAsc(LogState state, boolean ignored);

}
