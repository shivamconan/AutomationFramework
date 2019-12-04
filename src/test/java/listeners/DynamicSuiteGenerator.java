package listeners;

import base.BaseClass;
import org.testng.Assert;
import org.testng.IAlterSuiteListener;
import org.testng.xml.*;
import utility.EnvironmentParameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shivam mishra
 */

public class DynamicSuiteGenerator extends BaseClass implements IAlterSuiteListener {

    private int noOfCars = EnvironmentParameters.getNoOfCars();

    @Override
    public void alter(List<XmlSuite> suites) {
        suites.forEach((suite) -> {
            List<String> testSuites = EnvironmentParameters.getTestSuites();
            if(testSuites.contains("CarCreation")) {
                int threadCountForCarCreation = Math.min(noOfCars, 5);
                suite.setThreadCount(threadCountForCarCreation);
                for(int i = 1; i<noOfCars; i++) {
                    testSuites.add("CarCreation");
                }
            }
            else {
                int noofDevices = initializeSessionManager().getExecutionDevicesList().size();
                Assert.assertTrue(noofDevices>0, "No device is connected");
                suite.setThreadCount(noofDevices);
            }

            /*if not provided from xml file, it will fetch tests from command
              line. If not from command line, it will fetch from defaultEnvironment.properties
            */

            if (suite.getTests().size() == 0) {
                List<XmlTest> xmlTests = new ArrayList<>();

                for(String testSuite: testSuites) {
                    int suiteIndex = 0;
                    String testClass = "testClasses." + testSuite + "Test";
                    List<XmlInclude> testMethodsIncludes = getTestMethods(testClass);
                    for(XmlInclude testMethodsInclude: testMethodsIncludes) {
                        XmlTest xmlTest = new XmlTest(suite);
                        xmlTest.setName(testSuite + (++suiteIndex));
                        List<XmlClass> xmlClasses = new ArrayList<XmlClass>();
                        XmlClass xmlClass = new XmlClass(testClass);
                        List<XmlInclude> xmlIncludes = new ArrayList<>();
                        xmlIncludes.add(testMethodsInclude);
                        xmlClass.setIncludedMethods(xmlIncludes);
                        xmlClasses.add(xmlClass);
                        xmlTest.setClasses(xmlClasses);
                        xmlTests.add(xmlTest);
                    }
                }
                suite.setTests(xmlTests);
            }
            suite.setParallel(XmlSuite.ParallelMode.TESTS);
        });
    }

    public List<XmlInclude> getTestMethods(String testClassName) {
        Class testClass = null;
        List<XmlInclude> xmlIncludes = new ArrayList<>();
        try {
            testClass = Class.forName(testClassName);
            Method[] methodsInClass = testClass.getMethods();
            for(Method method: methodsInClass) {
                Annotation[] annotations = method.getAnnotations();
                for(Annotation annotation: annotations) {
                    if(annotation.toString().contains("org.testng.annotations.Test")){
                        xmlIncludes.add(new XmlInclude(method.getName()));
                        break;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return xmlIncludes;
    }
}
