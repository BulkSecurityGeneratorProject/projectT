package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ProjectTApp;

import com.mycompany.myapp.domain.Mark;
import com.mycompany.myapp.repository.MarkRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MarkResource REST controller.
 *
 * @see MarkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectTApp.class)
public class MarkResourceIntTest {

    private static final Integer DEFAULT_X = 1;
    private static final Integer UPDATED_X = 2;

    private static final Integer DEFAULT_Y = 1;
    private static final Integer UPDATED_Y = 2;

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMarkMockMvc;

    private Mark mark;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MarkResource markResource = new MarkResource(markRepository);
        this.restMarkMockMvc = MockMvcBuilders.standaloneSetup(markResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mark createEntity(EntityManager em) {
        Mark mark = new Mark()
            .x(DEFAULT_X)
            .y(DEFAULT_Y);
        return mark;
    }

    @Before
    public void initTest() {
        mark = createEntity(em);
    }

    @Test
    @Transactional
    public void createMark() throws Exception {
        int databaseSizeBeforeCreate = markRepository.findAll().size();

        // Create the Mark
        restMarkMockMvc.perform(post("/api/marks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mark)))
            .andExpect(status().isCreated());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeCreate + 1);
        Mark testMark = markList.get(markList.size() - 1);
        assertThat(testMark.getX()).isEqualTo(DEFAULT_X);
        assertThat(testMark.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    public void createMarkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = markRepository.findAll().size();

        // Create the Mark with an existing ID
        mark.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarkMockMvc.perform(post("/api/marks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mark)))
            .andExpect(status().isBadRequest());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMarks() throws Exception {
        // Initialize the database
        markRepository.saveAndFlush(mark);

        // Get all the markList
        restMarkMockMvc.perform(get("/api/marks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mark.getId().intValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)));
    }
    
    @Test
    @Transactional
    public void getMark() throws Exception {
        // Initialize the database
        markRepository.saveAndFlush(mark);

        // Get the mark
        restMarkMockMvc.perform(get("/api/marks/{id}", mark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mark.getId().intValue()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y));
    }

    @Test
    @Transactional
    public void getNonExistingMark() throws Exception {
        // Get the mark
        restMarkMockMvc.perform(get("/api/marks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMark() throws Exception {
        // Initialize the database
        markRepository.saveAndFlush(mark);

        int databaseSizeBeforeUpdate = markRepository.findAll().size();

        // Update the mark
        Mark updatedMark = markRepository.findById(mark.getId()).get();
        // Disconnect from session so that the updates on updatedMark are not directly saved in db
        em.detach(updatedMark);
        updatedMark
            .x(UPDATED_X)
            .y(UPDATED_Y);

        restMarkMockMvc.perform(put("/api/marks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMark)))
            .andExpect(status().isOk());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
        Mark testMark = markList.get(markList.size() - 1);
        assertThat(testMark.getX()).isEqualTo(UPDATED_X);
        assertThat(testMark.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    public void updateNonExistingMark() throws Exception {
        int databaseSizeBeforeUpdate = markRepository.findAll().size();

        // Create the Mark

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarkMockMvc.perform(put("/api/marks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mark)))
            .andExpect(status().isBadRequest());

        // Validate the Mark in the database
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMark() throws Exception {
        // Initialize the database
        markRepository.saveAndFlush(mark);

        int databaseSizeBeforeDelete = markRepository.findAll().size();

        // Delete the mark
        restMarkMockMvc.perform(delete("/api/marks/{id}", mark.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Mark> markList = markRepository.findAll();
        assertThat(markList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mark.class);
        Mark mark1 = new Mark();
        mark1.setId(1L);
        Mark mark2 = new Mark();
        mark2.setId(mark1.getId());
        assertThat(mark1).isEqualTo(mark2);
        mark2.setId(2L);
        assertThat(mark1).isNotEqualTo(mark2);
        mark1.setId(null);
        assertThat(mark1).isNotEqualTo(mark2);
    }
}
