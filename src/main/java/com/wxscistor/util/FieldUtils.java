package com.wxscistor.util;

import org.vertexium.Graph;
import org.vertexium.PropertyDefinition;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.elasticsearch5.Elasticsearch5SearchIndex;
import org.vertexium.type.GeoPoint;

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
}
