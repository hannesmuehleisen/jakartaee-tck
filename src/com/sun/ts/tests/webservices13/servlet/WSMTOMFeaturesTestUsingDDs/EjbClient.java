/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.webservices13.servlet.WSMTOMFeaturesTestUsingDDs;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.net.*;
import java.util.*;
import java.awt.Image;
import javax.xml.ws.*;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.transform.stream.StreamSource;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.SessionContext;
import javax.annotation.PostConstruct;
import javax.naming.InitialContext;

import com.sun.ts.tests.jaxws.common.AttachmentHelper;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;

@Stateless(name = "WSMTOMFeaturesTestUsingDDsClntBean")
@Remote({ EjbClientIF.class })

public class EjbClient implements EjbClientIF {

  private static final boolean debug = true;

  private URL docURL = null;

  private String SDOC = null;

  // MTOM(true) on client/MTOM(true) on endpoint
  MTOMTest1 port1_1 = null;

  // MTOM() on client/MTOM(true) on endpoint
  MTOMTest1 port1_2 = null;

  // MTOM(true) on client/MTOM(false) on endpoint
  MTOMTest2 port2 = null;

  // MTOM(true, 2000) on client/MTOM(true, 2000) on endpoint
  MTOMTest3 port3_1 = null;

  // MTOM(false, 2000) on client/MTOM(true, 2000) on endpoint
  MTOMTest3 port3_2 = null;

  // MTOM() on client/MTOM(true, 2000) on endpoint
  MTOMTest3 port3_3 = null;

  // MTOM(true, 2000) on client/MTOM(false, 2000) on endpoint
  MTOMTest4 port4 = null;

  @PostConstruct
  public void postConstruct() {
    try {
      System.out.println("EjbClient:postConstruct()");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsmtomfeaturestestusingddsport1_1");
      port1_1 = (MTOMTest1) ctx
          .lookup("java:comp/env/service/wsmtomfeaturestestusingddsport1_1");
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsmtomfeaturestestusingddsport1_2");
      port1_2 = (MTOMTest1) ctx
          .lookup("java:comp/env/service/wsmtomfeaturestestusingddsport1_2");
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsmtomfeaturestestusingddsport2");
      port2 = (MTOMTest2) ctx
          .lookup("java:comp/env/service/wsmtomfeaturestestusingddsport2");
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsmtomfeaturestestusingddsport3_1");
      port3_1 = (MTOMTest3) ctx
          .lookup("java:comp/env/service/wsmtomfeaturestestusingddsport3_1");
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsmtomfeaturestestusingddsport3_2");
      port3_2 = (MTOMTest3) ctx
          .lookup("java:comp/env/service/wsmtomfeaturestestusingddsport3_2");
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsmtomfeaturestestusingddsport3_3");
      port3_3 = (MTOMTest3) ctx
          .lookup("java:comp/env/service/wsmtomfeaturestestusingddsport3_3");
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsmtomfeaturestestusingddsport4");
      port4 = (MTOMTest4) ctx
          .lookup("java:comp/env/service/wsmtomfeaturestestusingddsport4");
      System.out.println("EjbClient DEBUG: port1_1=" + port1_1);
      System.out.println("EjbClient DEBUG: port1_2=" + port1_2);
      System.out.println("EjbClient DEBUG: port2=" + port2);
      System.out.println("EjbClient DEBUG: port3_1=" + port3_1);
      System.out.println("EjbClient DEBUG: port3_2=" + port3_2);
      System.out.println("EjbClient DEBUG: port3_3=" + port3_3);
      System.out.println("EjbClient DEBUG: port4=" + port4);
    } catch (Exception e) {
      System.err.println("EjbClient:postConstruct() Exception: " + e);
      e.printStackTrace();
    }
  }

  public Properties execute(Properties p) {

    boolean pass = true;
    try {
      TestUtil.init(p);
      if (debug) {
        System.out.println("Remote logging intialized for Ejb");
        System.out.println("Here are the harness props");
        p.list(System.out);
      }
    } catch (Exception e) {
      System.out.println("execute: Exception: " + e);
      e.printStackTrace();
      throw new EJBException("unable to initialize remote logging");
    }

    try {
      String test = p.getProperty("TEST");
      System.out.println("EjbClient:execute: test to execute is: " + test);

      if (test.equals("ClientEnabledServerEnabledMTOMInTest")) {
        p.setProperty("TESTRESULT", ClientEnabledServerEnabledMTOMInTest(p));
      } else if (test.equals("ClientEnabledServerDisabledMTOMInTest")) {
        p.setProperty("TESTRESULT", ClientEnabledServerDisabledMTOMInTest(p));
      } else if (test.equals("ClientEnabledServerEnabledMTOMInDefaultTest")) {
        p.setProperty("TESTRESULT",
            ClientEnabledServerEnabledMTOMInDefaultTest(p));
      } else if (test.equals("ClientEnabledServerEnabledGT2000Test")) {
        p.setProperty("TESTRESULT", ClientEnabledServerEnabledGT2000Test(p));
      } else if (test.equals("ClientEnabledServerEnabledGT2000DefaultTest")) {
        p.setProperty("TESTRESULT",
            ClientEnabledServerEnabledGT2000DefaultTest(p));
      } else if (test.equals("ClientDisabledServerEnabledGT2000Test")) {
        p.setProperty("TESTRESULT", ClientDisabledServerEnabledGT2000Test(p));
      } else if (test.equals("ClientEnabledServerDisabledGT2000Test")) {
        p.setProperty("TESTRESULT", ClientEnabledServerDisabledGT2000Test(p));
      } else if (test.equals("ClientEnabledServerEnabledLT2000Test")) {
        p.setProperty("TESTRESULT", ClientEnabledServerEnabledLT2000Test(p));
      } else {
        p.setProperty("TESTRESULT", "TESTNAME NOT FOUND");
      }
    } catch (Exception e) {
      TestUtil.logErr("execute: Exception: " + e);
      System.out.println("execute: Exception: " + e);
      e.printStackTrace();
      p.setProperty("TESTRESULT", e.toString());
    }
    return (p);
  }

  private String ClientEnabledServerEnabledMTOMInTest(Properties p) {
    TestUtil.logMsg("EjbClient:ClientEnabledServerEnabledMTOMInTest");
    String result = "";

    try {
      SDOC = p.getProperty("SDOC");
      TestUtil.logMsg("SDOC =" + SDOC);
      docURL = new URL(p.getProperty("sdocURL"));
      TestUtil.logMsg("docURL =" + docURL);

      DataType data = new DataType();
      data.setDocName(SDOC);
      data.setDocUrl(docURL.toString());
      StreamSource doc = AttachmentHelper.getSourceDoc(docURL);
      data.setDoc(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC + "]");
      result = port1_1.mtomIn(data);
      if (!result.equals("")) {
        TestUtil.logErr("EjbClient:An error occurred with the attachment");
        TestUtil.logErr("result=" + result);
      }
    } catch (Exception e) {
      TestUtil.logErr("EjbClient:Exception occurred");
      TestUtil.printStackTrace(e);
      result = e.toString();
    }
    return result;
  }

  private String ClientEnabledServerDisabledMTOMInTest(Properties p) {
    TestUtil.logMsg("EjbClient:ClientEnabledServerDisabledMTOMInTest");
    String result = "";

    try {
      SDOC = p.getProperty("SDOC");
      TestUtil.logMsg("SDOC =" + SDOC);
      docURL = new URL(p.getProperty("sdocURL"));
      TestUtil.logMsg("docURL =" + docURL);

      DataType data = new DataType();
      data.setDocName(SDOC);
      data.setDocUrl(docURL.toString());
      StreamSource doc = AttachmentHelper.getSourceDoc(docURL);
      data.setDoc(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC + "]");
      result = port2.mtomIn(data);
      if (!result.equals("")) {
        TestUtil.logErr("EjbClient:An error occurred with the attachment");
        TestUtil.logErr("result=" + result);
      }
    } catch (Exception e) {
      TestUtil.logErr("EjbClient:Exception occurred");
      TestUtil.printStackTrace(e);
      result = e.toString();
    }
    return result;
  }

  private String ClientEnabledServerEnabledMTOMInDefaultTest(Properties p) {
    TestUtil.logMsg("EjbClient:ClientEnabledServerEnabledMTOMInDefaultTest");
    String result = "";

    try {
      SDOC = p.getProperty("SDOC");
      TestUtil.logMsg("SDOC =" + SDOC);
      docURL = new URL(p.getProperty("sdocURL"));
      TestUtil.logMsg("docURL =" + docURL);

      DataType data = new DataType();
      data.setDocName(SDOC);
      data.setDocUrl(docURL.toString());
      StreamSource doc = AttachmentHelper.getSourceDoc(docURL);
      data.setDoc(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC + "]");
      result = port1_2.mtomIn(data);
      if (!result.equals("")) {
        TestUtil.logErr("EjbClient:An error occurred with the attachment");
        TestUtil.logErr("result=" + result);
      }
    } catch (Exception e) {
      TestUtil.logErr("EjbClient:Exception occurred");
      TestUtil.printStackTrace(e);
      result = e.toString();
    }
    return result;
  }

  private String ClientEnabledServerEnabledGT2000Test(Properties p) {
    TestUtil.logMsg("EjbClient:ClientEnabledServerEnabledGT2000Test");
    String result = "";

    try {
      SDOC = p.getProperty("SDOC");
      TestUtil.logMsg("SDOC =" + SDOC);
      docURL = new URL(p.getProperty("sdocURL"));
      TestUtil.logMsg("docURL =" + docURL);

      DataType data = new DataType();
      data.setDocName(SDOC);
      data.setDocUrl(docURL.toString());
      Image doc = AttachmentHelper.getImageDoc(docURL);
      data.setDoc2(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC + "]");
      result = port3_1.mtomIn2000(data);
      if (!result.equals("")) {
        TestUtil.logErr("EjbClient:An error occurred with the attachment");
        TestUtil.logErr("result=" + result);
      }
    } catch (Exception e) {
      TestUtil.logErr("EjbClient:Exception occurred");
      TestUtil.printStackTrace(e);
      result = e.toString();
    }
    return result;
  }

  private String ClientEnabledServerEnabledGT2000DefaultTest(Properties p) {
    TestUtil.logMsg("EjbClient:ClientEnabledServerEnabledGT2000DefaultTest");
    String result = "";

    try {
      SDOC = p.getProperty("SDOC");
      TestUtil.logMsg("SDOC =" + SDOC);
      docURL = new URL(p.getProperty("sdocURL"));
      TestUtil.logMsg("docURL =" + docURL);

      DataType data = new DataType();
      data.setDocName(SDOC);
      data.setDocUrl(docURL.toString());
      Image doc = AttachmentHelper.getImageDoc(docURL);
      data.setDoc2(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC + "]");
      result = port3_3.mtomIn2000(data);
      if (!result.equals("")) {
        TestUtil.logErr("EjbClient:An error occurred with the attachment");
        TestUtil.logErr("result=" + result);
      }
    } catch (Exception e) {
      TestUtil.logErr("EjbClient:Exception occurred");
      TestUtil.printStackTrace(e);
      result = e.toString();
    }
    return result;
  }

  private String ClientDisabledServerEnabledGT2000Test(Properties p) {
    TestUtil.logMsg("EjbClient:ClientDisabledServerEnabledGT2000Test");
    String result = "";

    try {
      SDOC = p.getProperty("SDOC");
      TestUtil.logMsg("SDOC =" + SDOC);
      docURL = new URL(p.getProperty("sdocURL"));
      TestUtil.logMsg("docURL =" + docURL);

      DataType data = new DataType();
      data.setDocName(SDOC);
      data.setDocUrl(docURL.toString());
      Image doc = AttachmentHelper.getImageDoc(docURL);
      data.setDoc2(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC + "]");
      result = port3_2.mtomIn2000(data);
      if (!result.equals("")) {
        TestUtil.logErr("EjbClient:An error occurred with the attachment");
        TestUtil.logErr("result=" + result);
      }
    } catch (Exception e) {
      TestUtil.logErr("EjbClient:Exception occurred");
      TestUtil.printStackTrace(e);
      result = e.toString();
    }
    return result;
  }

  private String ClientEnabledServerDisabledGT2000Test(Properties p) {
    TestUtil.logMsg("EjbClient:ClientEnabledServerDisabledGT2000Test");
    String result = "";

    try {
      SDOC = p.getProperty("SDOC");
      TestUtil.logMsg("SDOC =" + SDOC);
      docURL = new URL(p.getProperty("sdocURL"));
      TestUtil.logMsg("docURL =" + docURL);

      DataType data = new DataType();
      data.setDocName(SDOC);
      data.setDocUrl(docURL.toString());
      Image doc = AttachmentHelper.getImageDoc(docURL);
      data.setDoc2(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC + "]");
      result = port4.mtomIn2000(data);
      if (!result.equals("")) {
        TestUtil.logErr("EjbClient:An error occurred with the attachment");
        TestUtil.logErr("result=" + result);
      }
    } catch (Exception e) {
      TestUtil.logErr("EjbClient:Exception occurred");
      TestUtil.printStackTrace(e);
      result = e.toString();
    }
    return result;
  }

  private String ClientEnabledServerEnabledLT2000Test(Properties p) {
    TestUtil.logMsg("EjbClient:ClientEnabledServerEnabledLT2000Test");
    String result = "";

    try {
      SDOC = p.getProperty("SDOC");
      TestUtil.logMsg("SDOC =" + SDOC);
      docURL = new URL(p.getProperty("sdocURL"));
      TestUtil.logMsg("docURL =" + docURL);

      DataType data = new DataType();
      data.setDocName(SDOC);
      data.setDocUrl(docURL.toString());
      Image doc = AttachmentHelper.getImageDoc(docURL);
      data.setDoc2(doc);
      TestUtil
          .logMsg("Send 1 document using MTOM via webservice method mtomIn()");
      TestUtil.logMsg("Document to send: [" + SDOC + "]");
      result = port3_1.mtomIn2000(data);
      if (!result.equals("")) {
        TestUtil.logErr("EjbClient:An error occurred with the attachment");
        TestUtil.logErr("result=" + result);
      }
    } catch (Exception e) {
      TestUtil.logErr("EjbClient:Exception occurred");
      TestUtil.printStackTrace(e);
      result = e.toString();
    }
    return result;
  }

}
