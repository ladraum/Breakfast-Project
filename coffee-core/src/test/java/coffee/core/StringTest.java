package coffee.core;

import coffee.core.util.StringUtil;


public class StringTest{


	public static void main( String[] args ){
    int i = 0;
    long prev_time = System.currentTimeMillis();
    long time;

    for( i = 0; i< 100000; i++){
        String s = "Blah" + i + "Blah";
        s.toString();
    }
    time = System.currentTimeMillis() - prev_time;

    System.out.println("Time String concat: " + time);

    prev_time = System.currentTimeMillis();
    for( i = 0; i<100000; i++){
        String s = String.format("Blah %d Blah", i);
        s.toString();
    }
    time = System.currentTimeMillis() - prev_time;
    System.out.println("Time StringFormat: " + time);

    prev_time = System.currentTimeMillis();
    StringBuffer buffer = new StringBuffer();
    for( i = 0; i<100000; i++){
        buffer.append("Blah ").append(i).append(" Blah");
    }
    time = System.currentTimeMillis() - prev_time;
    System.out.println("Time StringBuffer " + time);

    prev_time = System.currentTimeMillis();
    buffer = new StringBuffer();
    for( i = 0; i<100000; i++){
        buffer.append("Blah ").append(i).append(" Blah");
    }
    buffer.toString();
    time = System.currentTimeMillis() - prev_time;
    System.out.println("Time StringBuffer#toString() " + time);

    prev_time = System.currentTimeMillis();
    
    for( i = 0; i<100000; i++){
    	new StringUtil().str("Blah ").str(i).str(" Blah");
    }
    time = System.currentTimeMillis() - prev_time;
    System.out.println("Time sbConcat " + time);

    }
    
}