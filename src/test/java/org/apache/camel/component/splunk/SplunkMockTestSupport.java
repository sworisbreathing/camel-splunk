/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.splunk;

import static org.mockito.Mockito.reset;

import java.net.Socket;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.splunk.Service;

public abstract class SplunkMockTestSupport extends CamelTestSupport {
    @Mock
    Service service;

    @Mock
    Socket socket;

    @Before
    @Override
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reset(service);
        reset(socket);
        Helper.mockComponent(service, socket);
        super.setUp();
    }

}
