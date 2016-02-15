/*
 * Copyright 2016 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortmocks.web.mock.soap.model.event.service;

import com.fortmocks.core.basis.model.Repository;
import com.fortmocks.core.basis.model.ServiceResult;
import com.fortmocks.core.basis.model.ServiceTask;
import com.fortmocks.core.mock.soap.model.event.domain.SoapEvent;
import com.fortmocks.core.mock.soap.model.event.dto.SoapEventDto;
import com.fortmocks.core.mock.soap.model.event.service.message.input.ReadSoapEventsByOperationIdInput;
import com.fortmocks.core.mock.soap.model.event.service.message.output.ReadSoapEventsByOperationIdOutput;
import com.fortmocks.web.mock.soap.model.event.SoapEventDtoGenerator;
import org.dozer.DozerBeanMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karl Dahlgren
 * @since 1.4
 */
public class ReadSoapEventsByOperationIdServiceTest {


    @Spy
    private DozerBeanMapper mapper;

    @Mock
    private Repository repository;

    @InjectMocks
    private ReadSoapEventsByOperationIdService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess(){
        final List<SoapEvent> soapEvents = new ArrayList<SoapEvent>();
        for(int index = 0; index < 3; index++){
            final SoapEvent soapEvent = SoapEventDtoGenerator.generateSoapEvent();
            soapEvents.add(soapEvent);
        }

        soapEvents.get(0).setOperationId("OperationId");
        soapEvents.get(1).setOperationId("OperationId");
        soapEvents.get(2).setOperationId("InvalidOperationId");

        Mockito.when(repository.findAll()).thenReturn(soapEvents);

        final ReadSoapEventsByOperationIdInput input = new ReadSoapEventsByOperationIdInput("OperationId");
        final ServiceTask<ReadSoapEventsByOperationIdInput> serviceTask = new ServiceTask<ReadSoapEventsByOperationIdInput>(input);
        final ServiceResult<ReadSoapEventsByOperationIdOutput> serviceResult = service.process(serviceTask);
        final ReadSoapEventsByOperationIdOutput output = serviceResult.getOutput();


        Assert.assertEquals(2, output.getSoapEvents().size());

        for(int index = 0; index < 2; index++){
            final SoapEvent soapEvent = soapEvents.get(index);
            final SoapEventDto returnedSoapEvent = output.getSoapEvents().get(index);

            Assert.assertEquals(soapEvent.getId(), returnedSoapEvent.getId());
            Assert.assertEquals(soapEvent.getOperationId(), returnedSoapEvent.getOperationId());
            Assert.assertEquals(soapEvent.getPortId(), returnedSoapEvent.getPortId());
            Assert.assertEquals(soapEvent.getProjectId(), returnedSoapEvent.getProjectId());
        }
    }
}
