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
package org.apache.camel.component.splunk.support;

import java.io.IOException;
import java.net.Socket;

import org.apache.camel.component.splunk.SplunkEndpoint;

import com.splunk.Args;
import com.splunk.Input;
import com.splunk.Service;

public class TcpDataWriter extends SplunkDataWriter {
    private int port;

    public TcpDataWriter(SplunkEndpoint endpoint, Args args) {
        super(endpoint, args);
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    protected Socket createSocket(Service service) throws IOException {
        Input input = service.getInputs().get(String.valueOf(port));
        if (input == null) {
            throw new RuntimeException("no input defined for port " + port);
        }
        if (input.isDisabled()) {
            throw new RuntimeException(String.format("input on port %d is disabled", port));
        }
        Socket socket = service.open(port);
        return socket;
    }
}
