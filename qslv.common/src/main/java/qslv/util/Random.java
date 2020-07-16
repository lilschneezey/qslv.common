package qslv.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public final class Random {
 
       private static java.util.Random random = new java.util.Random();
      
       public final static int randomInt(int low, int high) {
              return random.nextInt(high-low+1)+low;
       }
       public final static int randomInt() { return random.nextInt(); }
       public final static int randomInt(int high) { return random.nextInt(high); }
 
       public final static long randomLong(long low, long high) {
              return (long)((random.nextDouble() * (high - low + 1) + low));
       }
       public final static long randomLong() { return random.nextLong(); }
       public final static long randomLong(long high) { return randomLong(Long.MIN_VALUE,high); }
      
       public final static double randomDouble(double low, double high) {
              return (random.nextDouble() * (high - low + 1) + low);
       }
       public final static double randomDouble() { return randomDouble(Double.MIN_VALUE,Double.MAX_VALUE); }
       public final static double randomDouble(double high) { return randomDouble(Double.MIN_VALUE,high); }
      
       public final static float randomFloat(float low, float high) {
              return (random.nextFloat() * (high - low + 1) + low);
       }
       public final static float randomFloat() { return randomFloat(Float.MIN_VALUE,Float.MAX_VALUE); }
       public final static float randomFloat(float high) { return randomFloat(Float.MIN_VALUE,high); }
      
       public final static boolean randomBoolean() { return random.nextBoolean(); }
 
       public final static byte[] randomByte( int len ) {
              byte[] bytes = new byte[len];
              random.nextBytes(bytes);
              return bytes;
       }
      
    static final String WORD_UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String ALPHANUM_UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    static final String DIGITS = "1234567890";
 
    static final String WORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static final String ALPHANUM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    static final String EXTENDED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890`-=;',.[]\\~!@#$%^&*() {}|:\"<>?";
 
    public final static String randomString( String librarychars, int len ) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
             builder.append(librarychars.charAt(randomInt(librarychars.length())));
              }
        return builder.toString();
    }
    public final static String randomWord(int len) { return randomString(WORD_CHARS,len); }
    public final static String randomUpperWord(int len) { return randomString(ALPHANUM_CHARS,len); }
    public final static String randomAlphaNum(int len) { return randomString(WORD_CHARS,len); }
    public final static String randomUpperAlphaNum(int len) { return randomString(ALPHANUM_UPPER_CHARS,len); }
    public final static String randomString(int len) { return randomString(EXTENDED_CHARS,len); }
    public final static String randomDigits(int len) { return randomString(DIGITS,len); }
    
    public final static LocalDate randomLocalDate() {
    	return LocalDate.of( LocalDate.now().getYear() - randomInt(1,100), randomInt(1,12), randomInt(1,28));
    }
    public final static LocalDate randomLocalDateYearsAgo(int yearsAgo) {
    	return LocalDate.of( LocalDate.now().getYear() - yearsAgo, randomInt(1,12), randomInt(1,28));
    }
    public final static LocalDate randomLocalDateMonthsAgo(int monthsAgo) {
    	return LocalDate.of( LocalDate.now().getYear(), LocalDate.now().getMonthValue() - monthsAgo, randomInt(1,28));
    }
    public final static LocalDateTime randomLocalDateTime() {
    	return LocalDateTime.of(randomLocalDate(), randomLocalTime());
    }
    public final static LocalTime randomLocalTime() {
    	return LocalTime.of(randomInt(0,23), randomInt(0,59), randomInt(0,59), randomInt(0,999999));
    }
    public final static Date randomDate() {
    	return Date.from(randomLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}