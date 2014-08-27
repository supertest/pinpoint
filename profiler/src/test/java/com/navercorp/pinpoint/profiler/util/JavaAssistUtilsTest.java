package com.nhn.pinpoint.profiler.util;

import javassist.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author emeroad
 */
public class JavaAssistUtilsTest {
    private final Logger logger = LoggerFactory.getLogger(JavaAssistUtilsTest.class.getName());
    private ClassPool pool;

    @Before
    public void setUp() throws Exception {
        pool = new ClassPool();
        pool.appendSystemPath();
    }

    @Test
    public void testGetParameterDescription() throws Exception {
        CtClass ctClass = pool.get("java.lang.String");
        CtMethod substring = ctClass.getDeclaredMethod("substring", new CtClass[]{CtClass.intType});

        String ctDescription = JavaAssistUtils.getParameterDescription(substring.getParameterTypes());
        logger.info(ctDescription);

        String clsDescription = JavaAssistUtils.getParameterDescription(new Class[]{int.class});
        logger.info(clsDescription);
        Assert.assertEquals(ctDescription, clsDescription);


    }

    @Test
    public void testGetLineNumber() throws Exception {
//        pool.appendClassPath(new ClassClassPath(AbstractHttpClient.class));
        CtClass ctClass = pool.get("org.apache.http.impl.client.AbstractHttpClient");
        CtClass params = pool.get("org.apache.http.params.HttpParams");
        // non-javadoc, see interface HttpClient
//        public synchronized final HttpParams getParams() {
//            if (defaultParams == null) {
//                defaultParams = createHttpParams();
//            }
//            return defaultParams;
//        }

        CtMethod setParams = ctClass.getDeclaredMethod("setParams", new CtClass[]{params});
        int lineNumber = JavaAssistUtils.getLineNumber(setParams);
        logger.info("line:" + lineNumber);

        logger.info(setParams.getName());
        logger.info(setParams.getLongName());

        String[] paramName = JavaAssistUtils.getParameterVariableName(setParams);
        logger.info(Arrays.toString(paramName));
        Assert.assertEquals(paramName.length, 1);
        Assert.assertEquals(paramName[0], "params");

        String[] parameterType = JavaAssistUtils.getParameterType(setParams.getParameterTypes());
        logger.info(Arrays.toString(parameterType));

        String s = ApiUtils.mergeParameterVariableNameDescription(parameterType, paramName);
        logger.info(s);
    }

    @Test
    public void testVariableNameError1() throws Exception {
        CtClass ctClass = pool.get("com.mysql.jdbc.ConnectionImpl");
        CtClass params = pool.get("org.apache.http.params.HttpParams");
        CtMethod setParams = ctClass.getDeclaredMethod("setAutoCommit", new CtClass[]{CtClass.booleanType});
        int lineNumber = JavaAssistUtils.getLineNumber(setParams);
        logger.info("line:" + lineNumber);

        logger.info(setParams.getName());
        logger.info(setParams.getLongName());

        String[] paramName = JavaAssistUtils.getParameterVariableName(setParams);
        logger.info(Arrays.toString(paramName));
        Assert.assertEquals(paramName.length, 1);
        Assert.assertEquals(paramName[0], "autoCommitFlag");

        String[] parameterType = JavaAssistUtils.getParameterType(setParams.getParameterTypes());
        logger.info(Arrays.toString(parameterType));

        String s = ApiUtils.mergeParameterVariableNameDescription(parameterType, paramName);
        logger.info(s);
    }

    @Test
    public void testVariableNameError2() throws Exception {
        CtClass ctClass = pool.get("com.mysql.jdbc.StatementImpl");
        CtClass params = pool.get("java.lang.String");
        CtMethod setParams = ctClass.getDeclaredMethod("executeQuery", new CtClass[]{params});
        int lineNumber = JavaAssistUtils.getLineNumber(setParams);

        logger.info(setParams.getName());
        logger.info(setParams.getLongName());

        String[] paramName = JavaAssistUtils.getParameterVariableName(setParams);
        logger.info(Arrays.toString(paramName));
        Assert.assertEquals(paramName.length, 1);
        Assert.assertEquals(paramName[0], "sql");

        String[] parameterType = JavaAssistUtils.getParameterType(setParams.getParameterTypes());
        logger.info(Arrays.toString(parameterType));

        String s = ApiUtils.mergeParameterVariableNameDescription(parameterType, paramName);
        logger.info(s);
    }


    @Test
    public void testGetParameterDescription2() throws Exception {
        String clsDescription = JavaAssistUtils.getParameterDescription(new Class[]{String.class, Integer.class});
        logger.info(clsDescription);
        Assert.assertEquals("(java.lang.String, java.lang.Integer)", clsDescription);
    }

}