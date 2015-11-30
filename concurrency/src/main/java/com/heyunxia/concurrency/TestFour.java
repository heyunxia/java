package com.heyunxia.concurrency;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-30 上午11:27
 */
public class TestFour {
    public static void main(String[] args) {
        testInsertVsIntern();
    }

    private static void testInsertVsIntern()
    {
        //in order to compile these methods
        //testMapInsertion( 100 * 1000 );
        //testMapInsertionIntern( 100 * 1000 );
        //System.gc();

        System.out.println( "Now real run" );

        //testMapInsertion( 50 * 1000 * 1000 + 100 );
        System.gc();
        testMapInsertionIntern( 50 * 1000 * 1000 + 100 );
    }

    private static void testMapInsertion( final int cnt )
    {
        final Map<Integer, String> map = new HashMap<Integer, String>( cnt );
        long start = System.currentTimeMillis();
        for ( int i = 0; i < cnt; ++i )
        {
            final String str = Integer.toString( i );
            map.put( i, str );
            if ( i % 1000000 == 0 ) //1M
            {
                System.out.println( i + "; time (insert) = " + ( System.currentTimeMillis() - start ) / 1000.0 + " sec" );
                start = System.currentTimeMillis();
            }
        }
        System.out.println( "Total length = " + map.size() );
    }

    private static void testMapInsertionIntern( final int cnt )
    {
        final Map<Integer, String> map = new HashMap<Integer, String>( cnt );
        long start = System.currentTimeMillis();
        for ( int i = 0; i < cnt; ++i )
        {
            final String str = Integer.toString( i );
            map.put( i, str.intern() ); //here is the difference!
            if ( i % 1000000 == 0 ) //1M
            {
                System.out.println( i + "; time (intern) = " + ( System.currentTimeMillis() - start ) / 1000.0 + " sec" );
                start = System.currentTimeMillis();
            }
        }
        System.out.println( "Total length = " + map.size() );
    }
}
