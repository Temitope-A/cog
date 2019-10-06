package org.apache.flink.datalog.executor;

import org.apache.flink.api.dag.Transformation;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.apache.flink.table.api.TableConfig;
import org.apache.flink.table.delegation.Executor;
import org.apache.flink.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class ExecutorBase implements Executor {

	private static final String DEFAULT_JOB_NAME = "Flink Exec Datalog Job";

	private final StreamExecutionEnvironment executionEnvironment;
	protected List<Transformation<?>> transformations = new ArrayList<>();
	protected TableConfig tableConfig;

	public ExecutorBase(StreamExecutionEnvironment executionEnvironment) {
		this.executionEnvironment = executionEnvironment;
	}

	public void setTableConfig(TableConfig tableConfig) {
		this.tableConfig = tableConfig;
	}

	@Override
	public void apply(List<Transformation<?>> transformations) {
		this.transformations.addAll(transformations);
	}

	public StreamExecutionEnvironment getExecutionEnvironment() {
		return executionEnvironment;
	}

	public StreamGraph generateStreamGraph(String jobName) {
		return generateStreamGraph(transformations, jobName);
	}

	public abstract StreamGraph generateStreamGraph(List<Transformation<?>> transformations, String jobName);

	protected String getNonEmptyJobName(String jobName) {
		if (StringUtils.isNullOrWhitespaceOnly(jobName)) {
			return DEFAULT_JOB_NAME;
		} else {
			return jobName;
		}
	}
}
