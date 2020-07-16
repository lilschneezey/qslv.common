package qslv.common;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import qslv.util.ElapsedTimeSLILogger;


@ExtendWith(MockitoExtension.class)
public class Unit_ResourceTimer {
	@Mock
	Logger log;
	ElapsedTimeSLILogger timer;
	
	@BeforeEach
	void init() {
		timer = new ElapsedTimeSLILogger(log, "YYY", "XXX");
	}
	
	@Test
	void test_measureTime() {
		//--Prepare----
		doNothing().when(log).info(anyString(), eq("YYY"), eq("XXX"), anyLong());
		//--Execute----
		int rr = timer.logElapsedTime(() -> {return 0;});
		assertEquals(0, rr);
		verify(log).info(anyString(), eq("YYY"), eq("XXX"), anyLong());
	}
	
	@Test
	void test_measureTime_throws() {
		//--Prepare----
		doNothing().when(log).info(anyString(), eq("YYY"), eq("XXX"), anyLong());
		//--Execute----
		assertThrows(RuntimeException.class, ()-> {
			timer.logElapsedTime(() -> {throw new RuntimeException("msg");});
		});
		verify(log).info(anyString(), eq("YYY"), eq("XXX"), anyLong());
	}
}
