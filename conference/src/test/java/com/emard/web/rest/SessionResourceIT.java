package com.emard.web.rest;

import com.emard.ConferenceApp;
import com.emard.domain.Session;
import com.emard.repository.SessionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.emard.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SessionResource} REST controller.
 */
@SpringBootTest(classes = ConferenceApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SessionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DESCRIPTION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DESCRIPTION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DESCRIPTION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DESCRIPTION_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ENDT_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENDT_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionMockMvc;

    private Session session;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Session createEntity(EntityManager em) {
        Session session = new Session()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .descriptionContentType(DEFAULT_DESCRIPTION_CONTENT_TYPE)
            .startDateTime(DEFAULT_START_DATE_TIME)
            .endtDateTime(DEFAULT_ENDT_DATE_TIME);
        return session;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Session createUpdatedEntity(EntityManager em) {
        Session session = new Session()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .descriptionContentType(UPDATED_DESCRIPTION_CONTENT_TYPE)
            .startDateTime(UPDATED_START_DATE_TIME)
            .endtDateTime(UPDATED_ENDT_DATE_TIME);
        return session;
    }

    @BeforeEach
    public void initTest() {
        session = createEntity(em);
    }

    @Test
    @Transactional
    public void createSession() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // Create the Session
        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isCreated());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate + 1);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSession.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSession.getDescriptionContentType()).isEqualTo(DEFAULT_DESCRIPTION_CONTENT_TYPE);
        assertThat(testSession.getStartDateTime()).isEqualTo(DEFAULT_START_DATE_TIME);
        assertThat(testSession.getEndtDateTime()).isEqualTo(DEFAULT_ENDT_DATE_TIME);
    }

    @Test
    @Transactional
    public void createSessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // Create the Session with an existing ID
        session.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setTitle(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setStartDateTime(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndtDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setEndtDateTime(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSessions() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList
        restSessionMockMvc.perform(get("/api/sessions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].descriptionContentType").value(hasItem(DEFAULT_DESCRIPTION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(Base64Utils.encodeToString(DEFAULT_DESCRIPTION))))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))))
            .andExpect(jsonPath("$.[*].endtDateTime").value(hasItem(sameInstant(DEFAULT_ENDT_DATE_TIME))));
    }
    
    @Test
    @Transactional
    public void getSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get the session
        restSessionMockMvc.perform(get("/api/sessions/{id}", session.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(session.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.descriptionContentType").value(DEFAULT_DESCRIPTION_CONTENT_TYPE))
            .andExpect(jsonPath("$.description").value(Base64Utils.encodeToString(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.startDateTime").value(sameInstant(DEFAULT_START_DATE_TIME)))
            .andExpect(jsonPath("$.endtDateTime").value(sameInstant(DEFAULT_ENDT_DATE_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingSession() throws Exception {
        // Get the session
        restSessionMockMvc.perform(get("/api/sessions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session
        Session updatedSession = sessionRepository.findById(session.getId()).get();
        // Disconnect from session so that the updates on updatedSession are not directly saved in db
        em.detach(updatedSession);
        updatedSession
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .descriptionContentType(UPDATED_DESCRIPTION_CONTENT_TYPE)
            .startDateTime(UPDATED_START_DATE_TIME)
            .endtDateTime(UPDATED_ENDT_DATE_TIME);

        restSessionMockMvc.perform(put("/api/sessions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSession)))
            .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSession.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSession.getDescriptionContentType()).isEqualTo(UPDATED_DESCRIPTION_CONTENT_TYPE);
        assertThat(testSession.getStartDateTime()).isEqualTo(UPDATED_START_DATE_TIME);
        assertThat(testSession.getEndtDateTime()).isEqualTo(UPDATED_ENDT_DATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Create the Session

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionMockMvc.perform(put("/api/sessions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        int databaseSizeBeforeDelete = sessionRepository.findAll().size();

        // Delete the session
        restSessionMockMvc.perform(delete("/api/sessions/{id}", session.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
