package org.hibernate.bugs;

import java.io.IOException;
import javax.persistence.EntityManager;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
@State(Scope.Thread)
public class JPABenchmark2_1 extends JPABenchmark2Base {

	@Benchmark
	public void perf5() {
		final EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		queryEmployees( em, false );
		em.getTransaction().commit();
		em.close();
	}

	public static void main(String[] args) {
		JPABenchmark2_1 jpaBenchmark = new JPABenchmark2_1();
		jpaBenchmark.setup();

		for ( int i = 0; i < 10_000; i++ ) {
			jpaBenchmark.perf5();
		}

		jpaBenchmark.destroy();
	}

	public static void main1(String[] args) throws RunnerException, IOException {
		if ( args.length == 0 ) {
			final Options opt = new OptionsBuilder()
					.include( ".*" + JPABenchmark2_1.class.getSimpleName() + ".*" )
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
