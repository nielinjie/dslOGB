import com.google.classpath.ClassPath;
import com.google.classpath.ClassPathFactory;
import com.google.classpath.ResourceFilter;
import com.paic.feature.Context;
import com.paic.feature.Feature;
import com.paic.feature.FeatureManager;
import com.paic.feature.dsl.Dsl;
import com.paic.feature.dsl.DslSource;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nielinjie
 * Date: 13-8-28
 * Time: PM2:37
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    class Owner {
        private String propertyA;

        String getPropertyA() {
            return propertyA;
        }

        void setPropertyA(String propertyA) {
            this.propertyA = propertyA;
        }
    }

    public void test() {
        //Feature manager init..
        //TODO do this with spring
        FeatureManager fm = new FeatureManager();
        Dsl dsl = new Dsl();
        dsl.setSource(new DslSource() {
            private String[] setup(){
                ClassPathFactory factory = new ClassPathFactory();
                ClassPath classPath = factory.createFromJVM();
                String[] re = classPath.findResources("/com/paic/dsls", new ResourceFilter() {
                    @Override
                    public boolean match(String packageName, String resourceName) {
                        return resourceName.endsWith(".dsl");

                    }
                });
                return re;
            }
            String[] files = {"/com/paic/dsls/isStock.dsl"};//setup();

            @Override
            public List<String> getDsls() {
                List<String> re = new ArrayList<String>();
                try {
                    for (String file : files) {
                        re.add(IOUtils.toString(Main.class.getResourceAsStream(file)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return re;
            }
        });
        List<Class<?>> classes = new ArrayList<Class<?>>();
//        classes.add(SublistFeature.class);
        dsl.setRegisteredClasses(classes);
        dsl.setup();
        fm.setDsl(dsl);

        //

        //Use example

        Owner owner = new Owner();
        owner.setPropertyA("a");
        Context context = new Context() {

            @Override
            public Object get(String name) {
                if (name.equals("contextA")) return "a";
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        List<Feature> features = fm.getFeatures(owner, context);
        assert features.size() == 1;
    }

    public static void main(String[] args) {

        new Main().test();
    }
}
