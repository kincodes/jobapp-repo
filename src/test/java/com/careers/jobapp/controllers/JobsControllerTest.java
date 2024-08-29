package com.careers.jobapp.controllers;

import com.careers.jobapp.domain.Jobs;
import com.careers.jobapp.repositories.JobsRepository;
import com.careers.jobapp.services.JobsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //addFilters disables the spring security
public class JobsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder; //assist with date parsing

    @MockBean
    private JobsRepository jobsRepository;

    @InjectMocks
    private JobsController jobsController;

    @InjectMocks
    private JobsServiceImpl jobsService;

    Jobs RECORD_1 = new Jobs(1,"Programmer", "IBM", LocalDate.parse("2024-06-28"), LocalDate.parse("2024-06-28"), "Applied");
    Jobs RECORD_2 = new Jobs(2,"Software Engineer", "TD", LocalDate.parse("2024-07-05"), LocalDate.parse("2024-07-05"), "Applied");
    Jobs RECORD_3 = new Jobs(3,"", "EA Games", LocalDate.parse("2024-07-15"), LocalDate.parse("2024-07-15"), "Applied");

    @BeforeEach
    public void setUp() {

        List<Jobs> records = new ArrayList<>(List.of(RECORD_1, RECORD_2, RECORD_3));
    }

    @Test
    public void testAddJob400BadRequest() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/job/add")
                        .content(newMapper.writeValueAsString(RECORD_3))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("is mandatory"));

    }

    @Test
    public void testAddJob200OK() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/job/add")
                        .content(newMapper.writeValueAsString(RECORD_1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void testGetAllRecords200OK() throws Exception {
        List<Jobs> recordsLocal = new ArrayList<>(List.of(RECORD_1, RECORD_2));

        when(jobsRepository.findAll()).thenReturn(recordsLocal);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/job/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].company", is("TD")));

    }

    @Test
    public void testUpdateJob400BadRequest() throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 8889;
        Jobs updatedJob = new Jobs(503,"Software Developer", "Google", LocalDate.parse("2024-06-28"), LocalDate.parse("2024-06-28"), "Applied");


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/job/update/{jobId}", id)
                        .content(newMapper.writeValueAsString(updatedJob))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("does not exists"));

    }


    @Test
    public void testUpdateJob200OK() throws Exception {
        ObjectMapper newMapper = mapperBuilder.build();

        int id = 115;

        Jobs job1 = new Jobs(id,"Software Developer", "Google", LocalDate.parse("2024-08-04"), LocalDate.parse("2024-08-25"), "Interview");

        Jobs updatedJob = new Jobs(id,"Software Developer", "Google", LocalDate.parse("2024-08-04"), LocalDate.parse("2024-08-25"), "Rejected");

        when(jobsRepository.findById(id)).thenReturn(Optional.of(job1));
        when(jobsRepository.save(any(Jobs.class))).thenReturn(updatedJob);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/job/update/{id}", id)
                        .content(newMapper.writeValueAsString(updatedJob))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "job updated successfully!");

    }

    @Test
    public void testDeleteJob400BadRequest()throws Exception {

        ObjectMapper newMapper = mapperBuilder.build();

        int id = 121;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/job/delete/{jobId}", id)
                        .content(newMapper.writeValueAsString(RECORD_1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("does not exists"));
    }

    @AfterEach
    public void cleanUp() {
        reset(jobsRepository);

    }


}
