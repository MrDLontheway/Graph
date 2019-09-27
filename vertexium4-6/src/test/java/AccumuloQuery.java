import com.wxscistor.concurrent.MGraphDBManager;
import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.util.AuthUtils;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.elasticsearch.threadpool.ThreadPool;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.elasticsearch5.Elasticsearch5SearchIndex;
import org.vertexium.elasticsearch5.ElasticsearchSearchIndexConfiguration;
import org.vertexium.query.Query;

import java.util.ArrayList;

import static com.wxscistor.util.AuthUtils.*;

public class AccumuloQuery {
    @Test
    public void proQuery() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph createii = VertexiumConfig.createAccumuloGraph("createii123");
        if(createii.getSearchIndex() instanceof Elasticsearch5SearchIndex){
            ((Elasticsearch5SearchIndex) createii.getSearchIndex()).ensureIndexCreatedAndInitialized("createii123");
            ThreadPool threadPool = ((Elasticsearch5SearchIndex) createii.getSearchIndex()).getClient().threadPool();
            System.out.println(threadPool);
        }
        System.exit(1);
        Vertex save = createii.prepareVertex(Visibility.EMPTY)
                .setProperty("age",12,Visibility.EMPTY)
                .setProperty("name","lisi",Visibility.EMPTY)

                .save(new AccumuloAuthorizations());
        createii.flush();
//                vertices.add(save);
//        for (int i = 0; i < 10; i++) {
//            ArrayList<Vertex> vertices = new ArrayList<>();
//            for (int j = 0; j < 100000; j++) {
//                Vertex save = createii.prepareVertex(Visibility.EMPTY).save(new AccumuloAuthorizations());
//                vertices.add(save);
//            }
//            createii.getSearchIndex().addElements(createii,vertices,new AccumuloAuthorizations());
//        }
//        createii.flush();
//        AccumuloAuthorizations rootAuth = getRootAuth(none5000bulk);
//        none5000bulk.getVertices(FetchHints.ALL_INCLUDING_HIDDEN,rootAuth);
    }

    @Test
    public void testPool() throws Exception {
        MGraphDBManager.init(20,"createii");

        ArrayList<AccumuloGraph> accumuloGraphs = new ArrayList<>();
        while (true){
            try {
                AccumuloGraph connection = MGraphDBManager.getConnection();
                System.out.println(connection);
                accumuloGraphs.add(connection);
                Thread.sleep(1000);
                MGraphDBManager.close(connection);
                System.out.println(MGraphDBManager.connPool.size());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void query() throws Exception {
//        MGraphDBManager.init(20,"diy1");
        AccumuloGraph createii = VertexiumConfig.createAccumuloGraph("diy1");
        AccumuloAuthorizations rootAuth = getRootAuth(createii);
        Query limit = createii.query(rootAuth).has("com.scistor.property.yuanxi_name",
                "龙华街136号-17-1").skip(0).limit(10);
        limit.vertices().forEach(v->{
            System.out.println(v);
        });
    }

    @Test
    public void co() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            MGraphDBManager.executeBulk.submit(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    while (true){
                        i++;
                        System.out.println(Thread.currentThread().getName()+"running ............");
                        try {
                            Thread.sleep(3000);
                            if(i>=15){
                                break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        MGraphDBManager.executeBulk.shutdown();

//        while (true){
//            if(MGraphDBManager.executeBulk.isTerminated()){
//                break;
//            }
//            Thread.sleep(2000);
//        }
    }

    @Test
    public void addPro() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph sinanspeed = VertexiumConfig.createAccumuloGraph("sinanspeed");
        AccumuloAuthorizations rootAuth = getRootAuth(sinanspeed);

        sinanspeed.savePropertyDefinition(new PropertyDefinition("dl123",String.class, TextIndexHint.NONE));
        sinanspeed.savePropertyDefinition(new PropertyDefinition("dl456",String.class, TextIndexHint.ALL));
        VertexBuilder dl123 = sinanspeed.prepareVertex("dl123", Visibility.EMPTY);
        dl123.setProperty("dl123","dl123",Visibility.EMPTY);
        dl123.setProperty("dl456","dl456",Visibility.EMPTY);
        dl123.save(rootAuth);
        sinanspeed.flush();
        System.out.println(rootAuth);
    }
}
