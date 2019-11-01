package org.apache.flink.datalog.parser.tree;

import org.apache.flink.datalog.parser.tree.predicate.PredicateData;

import java.util.ArrayList;
import java.util.List;

public class OrNode extends Node {
	private PredicateData predicateData;
	private List<AndNode> children = new ArrayList<>();

	public OrNode(PredicateData predicateData) {
		this.predicateData = predicateData;
	}

	@Override
	public List<AndNode> getChildren() {
		return children;
	}

	public void setChildren(List<AndNode> children) {
		this.children = children;
	}

	@Override
	public int getChildrenCount() {
		return this.children.size();
	}

	@Override
	public PredicateData getPredicateData() {
		return predicateData;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrNode orNode = (OrNode) o;
		return predicateData.equals(orNode.predicateData);
	}

	@Override
	public String toString() {
		return "OrNode{" +
			"predicateData=" + predicateData +
			", children=" + children +
			'}';
	}
}
