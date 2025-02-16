package com.myproject.springbootcdcdebezium.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author nguyenle
 * @since 3:05 PM Fri 2/14/2025
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cdc_log")
@CompoundIndexes({
	@CompoundIndex(name = "index_1", def = "{'createdDate' : -1, 'state' : -1}")
})
public class DBCDCLog {

	@Id
	private String id;

	@Indexed(background = true, expireAfterSeconds = 30 * 24 * 60 * 60) // index expire after 30 days
	private Date createdDate = new Date();

	@Indexed(background = true, partialFilter = "{ 'state': 'NO_PUBLISHED' }")
	private LogState state = LogState.NO_PUBLISHED;

	private String log;

	@Indexed(background = true)
	private int numberSentFailed = 0;

	@Indexed(background = true, partialFilter = "{ 'ignored' : 'false' }")
	private boolean ignored = false;

	public DBCDCLog(String content) {
		this.log = content;
	}

}
