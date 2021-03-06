/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002, 2020 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.deploy.portcomplink.ejb;

import jakarta.ejb.SessionBean;
import jakarta.ejb.SessionContext;
import java.rmi.RemoteException;
import javax.xml.rpc.Service;
import javax.naming.InitialContext;

public class InterModuleEjbBean implements SessionBean {

  public InterModuleEjbBean() {
  }

  public void ejbCreate() {
  }

  public void ejbRemove() {
  }

  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }

  public void setSessionContext(SessionContext sc) {
  }

  public String interModCall(String param) throws RemoteException {
    /* call look up intra-module SEI */
    InitialContext ctx;
    try {
      ctx = new InitialContext();
      javax.xml.rpc.Service svc = (Service) ctx
          .lookup("java:comp/env/service/portcomplink/intra");
      IntraModuleSei intraSei = (IntraModuleSei) svc
          .getPort(IntraModuleSei.class);
      return "inter " + intraSei.intraModCall(param);
    } catch (Throwable t) {
      return t.toString();
    }
  }
};
