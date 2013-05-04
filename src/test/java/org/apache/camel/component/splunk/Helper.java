/**
 * Copyright (C) 2010 Osinka <http://osinka.ru>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.splunk;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import com.splunk.Args;
import com.splunk.Index;
import com.splunk.IndexCollection;
import com.splunk.Input;
import com.splunk.InputCollection;
import com.splunk.Service;

public final class Helper {

    public static void mockComponent(final Service service, final Socket socket) {
        SplunkComponent.setConnFactory(new ConfigurationFactory() {
            @Override
            public SplunkConfiguration parseMap(Map<String, Object> parameters) {
                return new MockConnectionSettings(service, socket);
            }
        });
    }
}

class MockConnectionSettings extends SplunkConfiguration {
    private Service service;
    private Socket socket;

    public MockConnectionSettings(Service service, Socket socket) {
        super("foo", "bar");
        this.service = service;
        this.socket = socket;
        mockSplunkWriterApi();
    }

    private void mockSplunkWriterApi() {
        try {
            Index index = mock(Index.class);
            IndexCollection indexColl = mock(IndexCollection.class);
            when(service.getIndexes()).thenReturn(indexColl);
            InputCollection inputCollection = mock(InputCollection.class);
            when(service.getInputs()).thenReturn(inputCollection);
            Input input = mock(Input.class);
            when(service.open(anyInt())).thenReturn(socket);
            when(inputCollection.get(anyString())).thenReturn(input);
            when(indexColl.get(anyString())).thenReturn(index);
            when(index.attach(isA(Args.class))).thenReturn(socket);
            when(socket.getOutputStream()).thenReturn(System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Service createService() {
        return service;
    }

}
