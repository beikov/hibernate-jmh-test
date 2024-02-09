package org.hibernate.bugs;

import jakarta.persistence.*;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
@State(Scope.Thread)
public class JPABenchmark1_1 extends JPABenchmark1Base {

	@Benchmark
	public void perf6() {
		final EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		final List<Author> authors = em.createQuery( "from Author", Author.class ).getResultList();
		authors.forEach( author -> assertFalse( author.books.isEmpty() ) );
		em.getTransaction().commit();
		em.close();
	}

	public static void main1(String[] args) {
		JPABenchmark1_1 jpaBenchmark = new JPABenchmark1_1();
		jpaBenchmark.setup();

		for ( int i = 0; i < 10_000; i++ ) {
			jpaBenchmark.perf6();
		}

		jpaBenchmark.destroy();
	}

	public static void main(String[] args) throws RunnerException, IOException {
		if ( args.length == 0 ) {
			final Options opt = new OptionsBuilder()
					.include( ".*" + JPABenchmark1_1.class.getSimpleName() + ".*" )
					.warmupIterations( 3 )
					.warmupTime( TimeValue.seconds( 3 ) )
					.measurementIterations( 3 )
					.measurementTime( TimeValue.seconds( 5 ) )
					.threads( 1 )
					.addProfiler( "gc" )
					.forks( 2 )
					.build();
			new Runner( opt ).run();
		}
		else {
			Main.main( args );
		}
	}
}
