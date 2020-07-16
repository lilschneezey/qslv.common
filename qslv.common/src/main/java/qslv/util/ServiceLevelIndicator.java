package qslv.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;

public class ServiceLevelIndicator {

	public static void logServiceTraceData(Logger log, String serviceName, String aitId, String clientAitId, String businessTaxonomyId, String CorrelationId) {
		log.info("TRACE SERVICE={}, AIT={}, CLIENT-AIT={}, BUSINESS-TAXONOMY={}, CORRELATION-ID={}",
			serviceName, aitId, clientAitId, businessTaxonomyId, CorrelationId);
	}
	
	public static void logServiceElapsedTime( Logger log, String serviceName, String aitId, long elapsedTime ) {
		log.info("SLI={}, SUCCESS, AIT={}, ELAPSED-TIME-NS={}", serviceName, aitId, Duration.ofNanos(elapsedTime).toString());
	}

	public static void logServiceElapsedTime( Logger log, String serviceName, String aitId, Duration elapsedTime ) {
		log.info("SLI={}, SUCCESS, AIT={}, ELAPSED-TIME={}", serviceName, aitId, elapsedTime.toString());
	}
	
	public static void logServiceFailureElapsedTime( Logger log, String serviceName, String aitId, long elapsedTime ) {
		log.info("SLI={}, FAILURE, AIT={}, ELAPSED-TIME-NS={}", serviceName, aitId, Duration.ofNanos(elapsedTime).toString());
	}

	public static void logServiceFailureElapsedTime( Logger log, String serviceName, String aitId, Duration elapsedTime ) {
		log.info("SLI={}, FAILURE, AIT={}, ELAPSED-TIME={}", serviceName, aitId, elapsedTime.toString());
	}	
	public static void logRemoteServiceElapsedTime( Logger log, String serviceName, String aitId, String remoteAit, long elapsedTime ) {
		log.info("SLI={}, SUCCESS, AIT={}, REMOTE-AIT={}, ELAPSED-TIME-NS={}", serviceName, aitId, Duration.ofNanos(elapsedTime).toString());
	}

	public static void logRemoteServiceElapsedTime( Logger log, String serviceName, String aitId, String remoteAit, Duration elapsedTime ) {
		log.info("SLI={}, SUCCESS, AIT={}, REMOTE-AIT={}, ELAPSED-TIME={}", serviceName, aitId, elapsedTime.toString());
	}
	
	public static void logRemoteServiceFailureElapsedTime( Logger log, String serviceName, String aitId, String remoteAit, long elapsedTime ) {
		log.info("SLI={}, FAILURE, AIT={}, REMOTE-AIT={}, ELAPSED-TIME-NS={}", serviceName, aitId, remoteAit, Duration.ofNanos(elapsedTime).toString());
	}

	public static void logRemoteServiceFailureElapsedTime( Logger log, String serviceName, String aitId, String remoteAit, Duration elapsedTime ) {
		log.info("SLI={}, FAILURE, AIT={}, REMOTE-AIT={}, ELAPSED-TIME={}", serviceName, aitId, remoteAit, elapsedTime.toString());
	}

	public static void logAsyncServiceElapsedTime( Logger log, String serviceName, String aitId, LocalDateTime start, LocalDateTime end, Duration elapsedTime ) {
		log.info("SLI={}, SUCCESS, AIT={}, START={}, END={}, ELAPSED-TIME={}", serviceName, aitId, start.toString(), end.toString(), elapsedTime.toString());
	}
	
	public static void logAsyncServiceElapsedTime( Logger log, String serviceName, String aitId, LocalDateTime start ) {
		LocalDateTime now = LocalDateTime.now();
		long elapsedSeconds = now.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC);
		log.info("SLI={}, SUCCESS, AIT={}, START={}, END={}, ELAPSED-TIME={}", serviceName, aitId, start.toString(), now.toString(), Duration.ofSeconds(elapsedSeconds) );
	}

	public static void logServiceVsServerElapsedTime(Logger log, String serviceName, String aitId, String remoteAit, Duration elapsedTime, Duration serverElapsedTime) {
		log.info("SLI={}, SUCCESS, AIT={}, REMOTE-AIT={}, ELAPSED-TIME={}, SERVER-ELAPSED-TIME={}", serviceName, aitId, remoteAit, elapsedTime.toString(), serverElapsedTime.toString());
	}
}
