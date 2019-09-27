package com.dl.speed;

import com.wxscistor.concurrent.MGraphDBManager;
import com.wxscistor.config.VertexiumConfig;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.ElementBuilder;
import org.vertexium.search.IndexHint;
import org.vertexium.type.GeoPoint;
import org.vertexium.accumulo.AccumuloGraph;

import java.util.*;


public class ReciveHandler implements Runnable{
    private AccumuloGraph graph = null;
    private int vnum = 0;
    private int bulkNumber = 10000;
    private boolean isIndex = false;
    String graphName = null;

    ReciveHandler(String graphName, int vnum, boolean isIndex){
        this.vnum = vnum;
        this.graphName = graphName;
        this.isIndex = isIndex;
        if(isIndex){
            graph = VertexiumConfig.createAccumuloGraph(graphName);
        }else{
            graph = VertexiumConfig.createAccumuloGraphWithNoSeach(graphName);
        }
    }

    ReciveHandler(AccumuloGraph graph, int vnum, boolean isIndex){
        this.vnum = vnum;
        this.graphName = graphName;
        this.isIndex = isIndex;
        if(isIndex){
            this.graph = graph;
        }else{
            this.graph = graph;
        }
    }

    private List<String> objectUris = new ArrayList<String>();
    private List<String> linkUris = new ArrayList<String>();
    private List<String> belonggroup = new ArrayList<String>();



    private Random r = new Random();

    public <T>T getRandomeValue(List<T> data){
        T datum = data.get(r.nextInt(data.size()));
        return datum;
    }

    public void inint(){
        objectUris.addAll(Arrays.asList(new String[]{
                "com.scistor.object.document.document_01",
                "com.scistor.object.entity.facebook_user",
                "com.scistor.object.entity.fjc",
                "com.scistor.object.entity.important_person",
                "com.scistor.object.entity.import_orgnization",
                "com.scistor.object.entity.linkedin_organization",
                "com.scistor.object.entity.linkedin_user",
                "com.scistor.object.entity.phonenumber",
                "com.scistor.object.entity.test1",
                "com.scistor.object.entity.twitter_user",
                "com.scistor.object.entity.weibo_user",
                "com.scistor.object.event.education_experience",
                "com.scistor.object.event.facebook_post",
                "com.scistor.object.event.facebook_post_like",
                "com.scistor.object.event.facebook_post_reply",
                "com.scistor.object.event.linkedin_post",
                "com.scistor.object.event.linkedin_post_reply",
                "com.scistor.object.event.living_experience",
                "com.scistor.object.event.phoneevent",
                "com.scistor.object.event.twitter_post",
                "com.scistor.object.event.twitter_post_forward",
                "com.scistor.object.event.twitter_post_reply",
                "com.scistor.object.event.weibo_post",
                "com.scistor.object.event.weibo_post_forward",
                "com.scistor.object.event.weibo_post_reply",
                "com.scistor.object.event.work_experience",
                "com.scistor.object.media.media_01"
        }));
        linkUris.addAll(Arrays.asList(new String[]{
                "com.scistor.link.123",
                "com.scistor.link.2",
                "com.scistor.link.231",
                "com.scistor.link.234",
                "com.scistor.link.5",
                "com.scistor.link.55",
                "com.scistor.link.66",
                "com.scistor.link.brother_sister",
                "com.scistor.link.classmates",
                "com.scistor.link.couple",
                "com.scistor.link.dad_daguhter",
                "com.scistor.link.dad_son",
                "com.scistor.link.education",
                "com.scistor.link.family",
                "com.scistor.link.femailfirends",
                "com.scistor.link.follower",
                "com.scistor.link.friend",
                "com.scistor.link.group_member",
                "com.scistor.link.iswho",
                "com.scistor.link.lianshurenwutwoway",
                "com.scistor.link.lianshutozheng",
                "com.scistor.link.living",
                "com.scistor.link.markmap",
                "com.scistor.link.mom_daughter",
                "com.scistor.link.mother_son",
                "com.scistor.link.objectdocumentmap",
                "com.scistor.link.phonerecord",
                "com.scistor.link.post",
                "com.scistor.link.post_like",
                "com.scistor.link.post_reply",
                "com.scistor.link.sourcecall",
                "com.scistor.link.take_part_in",
                "com.scistor.link.targetcall",
                "com.scistor.link.work",
                "com.scistor.link.wqw",
                "com.scistor.link.yu",
                "com.scistor.link.zhengtolianshu"
        }));

        belonggroup.addAll(Arrays.asList(new String[]{
                "Entity",
                "Event",
                "Document",
                "Media"
        }));
    }

    @Override
    public void run() {
        inint();
        ArrayList<org.vertexium.ElementBuilder<Element>> vertexBuilders = new ArrayList<>();
        Visibility fjjtest = new Visibility("admin");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("admin","vis1","vis22");
        for (int i = 0; i < vnum; i++) {
            if(i%bulkNumber==0 && i!=0){
                System.out.println(new Date().toLocaleString()+"create 10w data!!!!!!!!!!");
                ArrayList<ElementBuilder<Element>> edgeBuilders = new ArrayList<>();
                //生成随机边
                for (int j = 0; j < bulkNumber/3; j++) {
                    String out = getRandomeValue(vertexBuilders).getElementId();
                    String in = getRandomeValue(vertexBuilders).getElementId();
                    ElementBuilder builder = graph.prepareEdge(out, in, getRandomeValue(linkUris), Visibility.EMPTY);
                    builder.addPropertyValue("time_start","time_start",new Date(),fjjtest);
                    builder.addPropertyValue("time_end","time_end",new Date(),fjjtest);
                    builder.setIndexHint(IndexHint.DO_NOT_INDEX);
                    edgeBuilders.add(builder);
                }
                System.out.println(new Date().toLocaleString()+"start 2 thread   SaveDataHandler SaveDataHandler SaveDataHandler");
                MGraphDBManager.asyncBulkData(graph,auth,edgeBuilders);
                MGraphDBManager.asyncBulkData(graph,auth,vertexBuilders);
//                new Thread(new SaveDataHandler(graph,auth,edata)).run();
//                new Thread(new SaveDataHandler(graph,auth,vdata)).run();
//                if(isIndex){
//                    graph.addVerticesAsync(vertexBuilders,auth);
//                    graph.addEdgesAsync(edgeBuilders,auth);
//                }else {
//                    graph.addVertices(vertexBuilders,auth);
//                    edgeBuilders.forEach(builder->{
//                        builder.save(auth);
//                    });
//                }
//                graph.flush();
                edgeBuilders = new ArrayList<>();
                vertexBuilders = new ArrayList<>();
            }
            String uri = getRandomeValue(objectUris);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            ElementBuilder vertexBuilder = graph.prepareVertex(uuid, Visibility.EMPTY);
            int i1 = r.nextInt(100);
            int i2 = r.nextInt(100);
            vertexBuilder.addPropertyValue("rowkey","rowkey",uuid,fjjtest);
            vertexBuilder.addPropertyValue("object","object",uri,fjjtest);
            vertexBuilder.addPropertyValue("belong_group","belong_group","Entity",fjjtest);
            vertexBuilder.addPropertyValue("createtime","createtime",new Date(),fjjtest);
            vertexBuilder.addPropertyValue("label","label",uuid,fjjtest);
            vertexBuilder.addPropertyValue("category","category","category",fjjtest);

            vertexBuilder.addPropertyValue("com.scistor.property.share_user_name","com.scistor.property.share_user_name",RandomValue.getChineseName(),fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.yuanxi_name","com.scistor.property.yuanxi_name",RandomValue.getRoad(),fjjtest);
            vertexBuilder.addPropertyValue("home_address","home_address",RandomValue.getRoad(),fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.end_school","com.scistor.property.end_school",uri,fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.sd","com.scistor.property.sd",i1,fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.hhh2396","com.scistor.property.hhh2396",i1,fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.test5number","com.scistor.property.test5number",i2,fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.33","com.scistor.property.33",i2,fjjtest);

            vertexBuilder.addPropertyValue("com.scistor.property.location","com.scistor.property.location",new GeoPoint(15.33,17.90,RandomValue.getRoad()),fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.share_location","com.scistor.property.share_location",new GeoPoint(15.32,17,RandomValue.getRoad()),fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.weibo_counts","com.scistor.property.weibo_counts",i2,fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.weibo_counts1","com.scistor.property.weibo_counts1",i2,fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.weibo_counts2","com.scistor.property.weibo_counts2",i2,fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.weibo_counts3","com.scistor.property.weibo_counts3",i2,fjjtest);
            vertexBuilder.addPropertyValue("com.scistor.property.school_start_year","com.scistor.property.school_start_year",new Date(),fjjtest);
            vertexBuilder.setIndexHint(IndexHint.DO_NOT_INDEX);
            vertexBuilders.add(vertexBuilder);
        }
//        this.graph.flushGraph();
        System.out.println(new Date().toLocaleString()+Thread.currentThread().getName()+":finshed!!!!!!!!   save:"+vnum+"data");
    }
}
