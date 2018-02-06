package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterSampleApplicationApp;

import io.github.jhipster.application.domain.Ansprechpartner;
import io.github.jhipster.application.repository.AnsprechpartnerRepository;
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
 * Test class for the AnsprechpartnerResource REST controller.
 *
 * @see AnsprechpartnerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class AnsprechpartnerResourceIntTest {

    private static final String DEFAULT_VORNAME = "AAAAAAAAAA";
    private static final String UPDATED_VORNAME = "BBBBBBBBBB";

    private static final String DEFAULT_NACHNAME = "AAAAAAAAAA";
    private static final String UPDATED_NACHNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON_NR = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON_NR = "BBBBBBBBBB";

    @Autowired
    private AnsprechpartnerRepository ansprechpartnerRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAnsprechpartnerMockMvc;

    private Ansprechpartner ansprechpartner;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnsprechpartnerResource ansprechpartnerResource = new AnsprechpartnerResource(ansprechpartnerRepository);
        this.restAnsprechpartnerMockMvc = MockMvcBuilders.standaloneSetup(ansprechpartnerResource)
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
    public static Ansprechpartner createEntity(EntityManager em) {
        Ansprechpartner ansprechpartner = new Ansprechpartner()
            .vorname(DEFAULT_VORNAME)
            .nachname(DEFAULT_NACHNAME)
            .email(DEFAULT_EMAIL)
            .telefonNr(DEFAULT_TELEFON_NR);
        return ansprechpartner;
    }

    @Before
    public void initTest() {
        ansprechpartner = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnsprechpartner() throws Exception {
        int databaseSizeBeforeCreate = ansprechpartnerRepository.findAll().size();

        // Create the Ansprechpartner
        restAnsprechpartnerMockMvc.perform(post("/api/ansprechpartners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ansprechpartner)))
            .andExpect(status().isCreated());

        // Validate the Ansprechpartner in the database
        List<Ansprechpartner> ansprechpartnerList = ansprechpartnerRepository.findAll();
        assertThat(ansprechpartnerList).hasSize(databaseSizeBeforeCreate + 1);
        Ansprechpartner testAnsprechpartner = ansprechpartnerList.get(ansprechpartnerList.size() - 1);
        assertThat(testAnsprechpartner.getVorname()).isEqualTo(DEFAULT_VORNAME);
        assertThat(testAnsprechpartner.getNachname()).isEqualTo(DEFAULT_NACHNAME);
        assertThat(testAnsprechpartner.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAnsprechpartner.getTelefonNr()).isEqualTo(DEFAULT_TELEFON_NR);
    }

    @Test
    @Transactional
    public void createAnsprechpartnerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ansprechpartnerRepository.findAll().size();

        // Create the Ansprechpartner with an existing ID
        ansprechpartner.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnsprechpartnerMockMvc.perform(post("/api/ansprechpartners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ansprechpartner)))
            .andExpect(status().isBadRequest());

        // Validate the Ansprechpartner in the database
        List<Ansprechpartner> ansprechpartnerList = ansprechpartnerRepository.findAll();
        assertThat(ansprechpartnerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAnsprechpartners() throws Exception {
        // Initialize the database
        ansprechpartnerRepository.saveAndFlush(ansprechpartner);

        // Get all the ansprechpartnerList
        restAnsprechpartnerMockMvc.perform(get("/api/ansprechpartners?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ansprechpartner.getId().intValue())))
            .andExpect(jsonPath("$.[*].vorname").value(hasItem(DEFAULT_VORNAME.toString())))
            .andExpect(jsonPath("$.[*].nachname").value(hasItem(DEFAULT_NACHNAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].telefonNr").value(hasItem(DEFAULT_TELEFON_NR.toString())));
    }

    @Test
    @Transactional
    public void getAnsprechpartner() throws Exception {
        // Initialize the database
        ansprechpartnerRepository.saveAndFlush(ansprechpartner);

        // Get the ansprechpartner
        restAnsprechpartnerMockMvc.perform(get("/api/ansprechpartners/{id}", ansprechpartner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ansprechpartner.getId().intValue()))
            .andExpect(jsonPath("$.vorname").value(DEFAULT_VORNAME.toString()))
            .andExpect(jsonPath("$.nachname").value(DEFAULT_NACHNAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.telefonNr").value(DEFAULT_TELEFON_NR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnsprechpartner() throws Exception {
        // Get the ansprechpartner
        restAnsprechpartnerMockMvc.perform(get("/api/ansprechpartners/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnsprechpartner() throws Exception {
        // Initialize the database
        ansprechpartnerRepository.saveAndFlush(ansprechpartner);
        int databaseSizeBeforeUpdate = ansprechpartnerRepository.findAll().size();

        // Update the ansprechpartner
        Ansprechpartner updatedAnsprechpartner = ansprechpartnerRepository.findOne(ansprechpartner.getId());
        // Disconnect from session so that the updates on updatedAnsprechpartner are not directly saved in db
        em.detach(updatedAnsprechpartner);
        updatedAnsprechpartner
            .vorname(UPDATED_VORNAME)
            .nachname(UPDATED_NACHNAME)
            .email(UPDATED_EMAIL)
            .telefonNr(UPDATED_TELEFON_NR);

        restAnsprechpartnerMockMvc.perform(put("/api/ansprechpartners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnsprechpartner)))
            .andExpect(status().isOk());

        // Validate the Ansprechpartner in the database
        List<Ansprechpartner> ansprechpartnerList = ansprechpartnerRepository.findAll();
        assertThat(ansprechpartnerList).hasSize(databaseSizeBeforeUpdate);
        Ansprechpartner testAnsprechpartner = ansprechpartnerList.get(ansprechpartnerList.size() - 1);
        assertThat(testAnsprechpartner.getVorname()).isEqualTo(UPDATED_VORNAME);
        assertThat(testAnsprechpartner.getNachname()).isEqualTo(UPDATED_NACHNAME);
        assertThat(testAnsprechpartner.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAnsprechpartner.getTelefonNr()).isEqualTo(UPDATED_TELEFON_NR);
    }

    @Test
    @Transactional
    public void updateNonExistingAnsprechpartner() throws Exception {
        int databaseSizeBeforeUpdate = ansprechpartnerRepository.findAll().size();

        // Create the Ansprechpartner

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAnsprechpartnerMockMvc.perform(put("/api/ansprechpartners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ansprechpartner)))
            .andExpect(status().isCreated());

        // Validate the Ansprechpartner in the database
        List<Ansprechpartner> ansprechpartnerList = ansprechpartnerRepository.findAll();
        assertThat(ansprechpartnerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAnsprechpartner() throws Exception {
        // Initialize the database
        ansprechpartnerRepository.saveAndFlush(ansprechpartner);
        int databaseSizeBeforeDelete = ansprechpartnerRepository.findAll().size();

        // Get the ansprechpartner
        restAnsprechpartnerMockMvc.perform(delete("/api/ansprechpartners/{id}", ansprechpartner.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ansprechpartner> ansprechpartnerList = ansprechpartnerRepository.findAll();
        assertThat(ansprechpartnerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ansprechpartner.class);
        Ansprechpartner ansprechpartner1 = new Ansprechpartner();
        ansprechpartner1.setId(1L);
        Ansprechpartner ansprechpartner2 = new Ansprechpartner();
        ansprechpartner2.setId(ansprechpartner1.getId());
        assertThat(ansprechpartner1).isEqualTo(ansprechpartner2);
        ansprechpartner2.setId(2L);
        assertThat(ansprechpartner1).isNotEqualTo(ansprechpartner2);
        ansprechpartner1.setId(null);
        assertThat(ansprechpartner1).isNotEqualTo(ansprechpartner2);
    }
}
