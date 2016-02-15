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

package com.fortmocks.web.mock.soap.model.project.service;

import com.fortmocks.core.basis.model.Repository;
import com.fortmocks.core.basis.model.ServiceResult;
import com.fortmocks.core.basis.model.ServiceTask;
import com.fortmocks.core.mock.soap.model.project.domain.SoapOperation;
import com.fortmocks.core.mock.soap.model.project.domain.SoapPort;
import com.fortmocks.core.mock.soap.model.project.domain.SoapProject;
import com.fortmocks.core.mock.soap.model.project.dto.SoapMockResponseDto;
import com.fortmocks.core.mock.soap.model.project.service.message.input.CreateSoapMockResponseInput;
import com.fortmocks.core.mock.soap.model.project.service.message.output.CreateSoapMockResponseOutput;
import com.fortmocks.web.mock.soap.model.project.SoapMockResponseDtoGenerator;
import com.fortmocks.web.mock.soap.model.project.SoapOperationDtoGenerator;
import com.fortmocks.web.mock.soap.model.project.SoapPortDtoGenerator;
import com.fortmocks.web.mock.soap.model.project.SoapProjectDtoGenerator;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class CreateSoapMockResponseServiceTest {

    @Spy
    private DozerBeanMapper mapper;

    @Mock
    private Repository repository;

    @InjectMocks
    private CreateSoapMockResponseService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess(){
        final SoapProject soapProject = SoapProjectDtoGenerator.generateSoapProject();
        final SoapPort soapPort = SoapPortDtoGenerator.generateSoapPort();
        final SoapOperation soapOperation = SoapOperationDtoGenerator.generateSoapOperation();
        final SoapMockResponseDto soapMockResponseDto = SoapMockResponseDtoGenerator.generateSoapMockResponseDto();

        soapProject.getPorts().add(soapPort);
        soapPort.getOperations().add(soapOperation);

        Mockito.when(repository.findOne(soapProject.getId())).thenReturn(soapProject);

        final CreateSoapMockResponseInput input = new CreateSoapMockResponseInput(soapProject.getId(), soapPort.getId(), soapOperation.getId(), soapMockResponseDto);
        final ServiceTask<CreateSoapMockResponseInput> serviceTask = new ServiceTask<>(input);
        final ServiceResult<CreateSoapMockResponseOutput> serviceResult = service.process(serviceTask);
        final CreateSoapMockResponseOutput output = serviceResult.getOutput();

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(SoapProject.class));
    }
}
