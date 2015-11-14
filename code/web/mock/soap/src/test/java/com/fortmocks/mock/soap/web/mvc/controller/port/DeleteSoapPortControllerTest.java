/*
 * Copyright 2015 Karl Dahlgren
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

package com.fortmocks.mock.soap.web.mvc.controller.port;

import com.fortmocks.core.basis.model.Input;
import com.fortmocks.core.mock.soap.model.project.dto.SoapPortDto;
import com.fortmocks.core.mock.soap.model.project.dto.SoapProjectDto;
import com.fortmocks.mock.soap.config.TestApplication;
import com.fortmocks.core.mock.soap.model.project.dto.SoapPortDtoGenerator;
import com.fortmocks.core.mock.soap.model.project.dto.SoapProjectDtoGenerator;
import com.fortmocks.core.mock.soap.model.project.service.message.output.ReadSoapPortOutput;
import com.fortmocks.mock.soap.web.mvc.controller.AbstractSoapControllerTest;
import com.fortmocks.web.core.service.ServiceProcessor;
import com.fortmocks.web.core.web.mvc.controller.AbstractController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@WebAppConfiguration
public class DeleteSoapPortControllerTest extends AbstractSoapControllerTest {

    private static final String SERVICE_URL = "/web/soap/";
    private static final String PAGE = "partial/mock/soap/port/deleteSoapPort.jsp";
    private static final String DELETE = "delete";
    private static final String CONFIRM = "confirm";

    @InjectMocks
    private DeleteSoapPortController deleteSoapPortController;

    @Mock
    private ServiceProcessor serviceProcessor;

    @Override
    protected AbstractController getController() {
        return deleteSoapPortController;
    }

    @Test
    public void testDeleteApplicationWithValidId() throws Exception {
        final SoapProjectDto soapProjectDto = SoapProjectDtoGenerator.generateSoapProjectDto();
        final SoapPortDto soapPortDto = SoapPortDtoGenerator.generateSoapPortDto();
        when(serviceProcessor.process(any(Input.class))).thenReturn(new ReadSoapPortOutput(soapPortDto));
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.get(SERVICE_URL + PROJECT + SLASH + soapProjectDto.getId() + SLASH + PORT + SLASH + soapPortDto.getId() + SLASH + DELETE);
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(5))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PORT, soapPortDto));
    }


    @Test
    public void testDeleteConfirmApplicationWithValidId() throws Exception {
        final SoapProjectDto soapProjectDto = SoapProjectDtoGenerator.generateSoapProjectDto();
        final SoapPortDto soapPortDto = SoapPortDtoGenerator.generateSoapPortDto();
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.get(SERVICE_URL + PROJECT + SLASH + soapProjectDto.getId() + SLASH + PORT + SLASH + soapPortDto.getId() + SLASH + DELETE + SLASH + CONFIRM);
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0));
    }

}
