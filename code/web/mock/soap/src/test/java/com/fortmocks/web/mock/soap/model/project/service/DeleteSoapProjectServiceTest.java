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
import com.fortmocks.core.mock.soap.model.project.domain.SoapPort;
import com.fortmocks.core.mock.soap.model.project.domain.SoapProject;
import com.fortmocks.core.mock.soap.model.project.service.message.input.DeleteSoapPortInput;
import com.fortmocks.core.mock.soap.model.project.service.message.input.DeleteSoapProjectInput;
import com.fortmocks.core.mock.soap.model.project.service.message.output.DeleteSoapPortOutput;
import com.fortmocks.core.mock.soap.model.project.service.message.output.DeleteSoapProjectOutput;
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
public class DeleteSoapProjectServiceTest {

    @Spy
    private DozerBeanMapper mapper;

    @Mock
    private Repository repository;

    @InjectMocks
    private DeleteSoapProjectService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess(){
        final SoapProject soapProject = SoapProjectDtoGenerator.generateSoapProject();

        Mockito.when(repository.findOne(soapProject.getId())).thenReturn(soapProject);

        final DeleteSoapProjectInput input = new DeleteSoapProjectInput(soapProject.getId());
        final ServiceTask<DeleteSoapProjectInput> serviceTask = new ServiceTask<DeleteSoapProjectInput>(input);
        final ServiceResult<DeleteSoapProjectOutput> serviceResult = service.process(serviceTask);
        serviceResult.getOutput();

        Mockito.verify(repository, Mockito.times(1)).delete(soapProject.getId());
    }
}
