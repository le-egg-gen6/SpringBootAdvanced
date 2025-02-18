package com.myproject.springbootmasterslavedbsynchronization.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author nguyenle
 * @since 11:23 AM Tue 2/18/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cdc_log")
@CompoundIndexes({})
public class DBCDCLog {

	@Id
	private String id;

	@Indexed(background = true, expireAfterSeconds = 30 * 24 * 60 * 60)
	private Date receiveDate = new Date();

	@Indexed(background = true, partialFilter = "{ 'state' : 'NO_PROCESSED'}")
	private LogState state = LogState.NO_PROCESSED;

	private String log;

	@Indexed(background = true)
	private int numberProcessFailed = 0;

	@Indexed(background = true, partialFilter = "{ 'ignored' : 'false'}")
	private boolean ignored = false;

	public DBCDCLog(String logStr) {
		this.log = logStr;
	}

}
