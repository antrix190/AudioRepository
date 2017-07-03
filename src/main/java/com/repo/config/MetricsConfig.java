/**
 * 
 */
package com.repo.config;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteSender;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.netflix.hystrix.contrib.codahalemetricspublisher.HystrixCodaHaleMetricsPublisher;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;

/**
 * @author antariksh.singh
 *
 * Apr 6, 2017
 */
//@Configuration
public class MetricsConfig {


	@Value("${graphite.host}")
	private String graphiteHost;

	@Value("${graphite.port}")
	private int graphitePort;

	@Bean
	HystrixMetricsPublisher hystrixMetricsPublisher(MetricRegistry metricRegistry) {
		HystrixCodaHaleMetricsPublisher publisher = new HystrixCodaHaleMetricsPublisher(metricRegistry);
		HystrixPlugins.getInstance().registerMetricsPublisher(publisher);
		return publisher;
	}

	@Bean
	public GraphiteReporter graphiteReporter(MetricRegistry metricRegistry) {
		metricRegistry.register("gc", new GarbageCollectorMetricSet());
		metricRegistry.register("memory", new MemoryUsageGaugeSet());
		metricRegistry.register("threads", new ThreadStatesGaugeSet());
		final GraphiteReporter reporter = GraphiteReporter
				.forRegistry(metricRegistry)
				.prefixedWith("file-uploader")
				.build(graphite());
		reporter.start(30, TimeUnit.SECONDS);
		return reporter;
	}

	@Bean
	GraphiteSender graphite() {
		return new Graphite(new InetSocketAddress(graphiteHost,graphitePort));
	}

	@Bean
	@Primary
	public DropwizardMetricServices dropwizardMetricServices(MetricRegistry metricRegistry){
		return new DropwizardMetricServices(metricRegistry);
	}
}
