package org.vertexium.query;

public class DateHistogramAggregation extends Aggregation implements SupportsNestedAggregationsAggregation {
    private final String aggregationName;
    private final String fieldName;

    public DateHistogramAggregation(String aggregationName, String fieldName) {
        this.aggregationName = aggregationName;
        this.fieldName = fieldName;
    }

    @Override
    public String getAggregationName() {
        return null;
    }

    @Override
    public void addNestedAggregation(Aggregation nestedAggregation) {

    }

    @Override
    public Iterable<Aggregation> getNestedAggregations() {
        return null;
    }
}
