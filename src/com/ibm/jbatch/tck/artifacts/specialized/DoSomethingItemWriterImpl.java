/*
 * Copyright 2012, 2020 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.ibm.jbatch.tck.artifacts.specialized;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import jakarta.batch.api.chunk.AbstractItemWriter;

import com.ibm.jbatch.tck.artifacts.chunktypes.CheckpointData;
import com.ibm.jbatch.tck.artifacts.chunktypes.ReadRecord;


@javax.inject.Named("doSomethingItemWriterImpl")
public class DoSomethingItemWriterImpl extends AbstractItemWriter {

	private final static Logger logger = Logger.getLogger(DoSomethingItemWriterImpl.class.getName());
	
	@Override
	public void open(Serializable checkpointData) throws Exception {
		logger.fine("openWriter");
	}
	
	@Override
	public void close() throws Exception {
		logger.fine("closeWriter");
	}
	
	@Override
	public void writeItems(List<Object> myData) throws Exception {
		logger.fine("writeMyData receives chunk size=" + myData.size());
		for  (int i = 0; i< myData.size(); i++) {
			logger.fine("myData=" + ((ReadRecord)myData.get(i)).getCount());
		}
	}
	
	@Override
	public CheckpointData checkpointInfo() throws Exception {
		return new CheckpointData();
	}
}
