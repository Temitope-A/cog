package org.apache.flink.datalog.parser.tree.predicate;

import java.util.List;

public class SimplePredicateData extends PredicateData {
	private String predicateName;
	private List<TermData> predicateParameters;

	public SimplePredicateData(String predicateName, List<TermData> predicateParameters) {
		this.predicateName = predicateName;
		this.predicateParameters = predicateParameters;
	}

	@Override
	public String getPredicateName() {
		return predicateName;
	}

	@Override
	public List<TermData> getPredicateParameters() {
		return predicateParameters;
	}

	@Override
	public String toString() {
		return "SimplePredicateData{" +
			"predicateName='" + predicateName + '\'' +
			", predicateParameters=" + predicateParameters +
			'}';
	}
}
