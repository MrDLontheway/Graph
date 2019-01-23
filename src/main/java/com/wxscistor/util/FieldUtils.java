package com.wxscistor.util;

import org.vertexium.Graph;
import org.vertexium.PropertyDefinition;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.elasticsearch5.Elasticsearch5SearchIndex;
import org.vertexium.type.GeoPoint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: dl
 * @Date: 2018/12/28 14:37
 * @Version 1.0
 */
public class FieldUtils {
    public static String[] getEsFieldNames(AccumuloGraph graph, String fieldName, String... auths){
        //fieldName = fieldName.replace(".","-");
        PropertyDefinition propertyDefinition = graph.getPropertyDefinition(fieldName);
        String[] propertyNames = ((Elasticsearch5SearchIndex) graph.getSearchIndex()).getPropertyNames(graph,fieldName, new AccumuloAuthorizations(auths));
        if (propertyDefinition.getDataType() == GeoPoint.class) {
            for (int i = 0; i < propertyNames.length; i++) {
                propertyNames[i] = propertyNames[i] + "_gp";
            }
        }
        return propertyNames;
    }

    public static Set<String> getFieldNames(AccumuloGraph graph,AccumuloAuthorizations userAuth){
        Set<String> result = new HashSet<>();
        Set<String> allPropertyNames = graph.getSimilarPropertyNames();
        allPropertyNames.forEach(x->{
            String[] allMatchingPropertyNames = ((Elasticsearch5SearchIndex) graph.getSearchIndex()).getPropertyNames(graph,x, userAuth);
            result.addAll(Arrays.asList(allMatchingPropertyNames));
            }
        );
        return result;
    }
}
