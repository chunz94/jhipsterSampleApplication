package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterSampleApplicationApp;

import io.github.jhipster.application.domain.Unternehmen;
import io.github.jhipster.application.repository.UnternehmenRepository;
import io.github.jhipster.application.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.List;

import static io.github.jhipster.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UnternehmenResource REST controller.
 *
 * @see UnternehmenResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class UnternehmenResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STEUER_ID = "AAAAAAAAAA";
    private static final String UPDATED_STEUER_ID = "BBBBBBBBBB";

    @Autowired
    private UnternehmenRepository unternehmenRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUnternehmenMockMvc;

    private Unternehmen unternehmen;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UnternehmenResource unternehmenResource = new UnternehmenResource(unternehmenRepository);
        this.restUnternehmenMockMvc = MockMvcBuilders.standaloneSetup(unternehmenResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unternehmen createEntity(EntityManager em) {
        Unternehmen unternehmen = new Unternehmen()
            .name(DEFAULT_NAME)
            .steuerID(DEFAULT_STEUER_ID);
        return unternehmen;
    }

    @Before
    public void initTest() {
        unternehmen = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnternehmen() throws Exception {
        int databaseSizeBeforeCreate = unternehmenRepository.findAll().size();

        // Create the Unternehmen
        restUnternehmenMockMvc.perform(post("/api/unternehmen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unternehmen)))
            .andExpect(status().isCreated());

        // Validate the Unternehmen in the database
        List<Unternehmen> unternehmenList = unternehmenRepository.findAll();
        assertThat(unternehmenList).hasSize(databaseSizeBeforeCreate + 1);
        Unternehmen testUnternehmen = unternehmenList.get(unternehmenList.size() - 1);
        assertThat(testUnternehmen.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnternehmen.getSteuerID()).isEqualTo(DEFAULT_STEUER_ID);
    }

    @Test
    @Transactional
    public void createUnternehmenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = unternehmenRepository.findAll().size();

        // Create the Unternehmen with an existing ID
        unternehmen.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnternehmenMockMvc.perform(post("/api/unternehmen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unternehmen)))
            .andExpect(status().isBadRequest());

        // Validate the Unternehmen in the database
        List<Unternehmen> unternehmenList = unternehmenRepository.findAll();
        assertThat(unternehmenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUnternehmen() throws Exception {
        // Initialize the database
        unternehmenRepository.saveAndFlush(unternehmen);

        // Get all the unternehmenList
        restUnternehmenMockMvc.perform(get("/api/unternehmen?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unternehmen.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].steuerID").value(hasItem(DEFAULT_STEUER_ID.toString())));
    }

    @Test
    @Transactional
    public void getUnternehmen() throws Exception {
        // Initialize the database
        unternehmenRepository.saveAndFlush(unternehmen);

        // Get the unternehmen
        restUnternehmenMockMvc.perform(get("/api/unternehmen/{id}", unternehmen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unternehmen.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.steuerID").value(DEFAULT_STEUER_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUnternehmen() throws Exception {
        // Get the unternehmen
        restUnternehmenMockMvc.perform(get("/api/unternehmen/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnternehmen() throws Exception {
        // Initialize the database
        unternehmenRepository.saveAndFlush(unternehmen);
        int databaseSizeBeforeUpdate = unternehmenRepository.findAll().size();

        // Update the unternehmen
        Unternehmen updatedUnternehmen = unternehmenRepository.findOne(unternehmen.getId());
        // Disconnect from session so that the updates on updatedUnternehmen are not directly saved in db
        em.detach(updatedUnternehmen);
        updatedUnternehmen
            .name(UPDATED_NAME)
            .steuerID(UPDATED_STEUER_ID);

        restUnternehmenMockMvc.perform(put("/api/unternehmen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUnternehmen)))
            .andExpect(status().isOk());

        // Validate the Unternehmen in the database
        List<Unternehmen> unternehmenList = unternehmenRepository.findAll();
        assertThat(unternehmenList).hasSize(databaseSizeBeforeUpdate);
        Unternehmen testUnternehmen = unternehmenList.get(unternehmenList.size() - 1);
        assertThat(testUnternehmen.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnternehmen.getSteuerID()).isEqualTo(UPDATED_STEUER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingUnternehmen() throws Exception {
        int databaseSizeBeforeUpdate = unternehmenRepository.findAll().size();

        // Create the Unternehmen

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUnternehmenMockMvc.perform(put("/api/unternehmen")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unternehmen)))
            .andExpect(status().isCreated());

        // Validate the Unternehmen in the database
        List<Unternehmen> unternehmenList = unternehmenRepository.findAll();
        assertThat(unternehmenList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUnternehmen() throws Exception {
        // Initialize the database
        unternehmenRepository.saveAndFlush(unternehmen);
        int databaseSizeBeforeDelete = unternehmenRepository.findAll().size();

        // Get the unternehmen
        restUnternehmenMockMvc.perform(delete("/api/unternehmen/{id}", unternehmen.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Unternehmen> unternehmenList = unternehmenRepository.findAll();
        assertThat(unternehmenList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Unternehmen.class);
        Unternehmen unternehmen1 = new Unternehmen();
        unternehmen1.setId(1L);
        Unternehmen unternehmen2 = new Unternehmen();
        unternehmen2.setId(unternehmen1.getId());
        assertThat(unternehmen1).isEqualTo(unternehmen2);
        unternehmen2.setId(2L);
        assertThat(unternehmen1).isNotEqualTo(unternehmen2);
        unternehmen1.setId(null);
        assertThat(unternehmen1).isNotEqualTo(unternehmen2);
    }
}
