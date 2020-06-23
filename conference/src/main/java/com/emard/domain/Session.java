package com.emard.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Session.
 */
@Entity
@Table(name = "session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    
    @Lob
    @Column(name = "description", nullable = false)
    private byte[] description;

    @Column(name = "description_content_type", nullable = false)
    private String descriptionContentType;

    @NotNull
    @Column(name = "start_date_time", nullable = false)
    private ZonedDateTime startDateTime;

    @NotNull
    @Column(name = "endt_date_time", nullable = false)
    private ZonedDateTime endtDateTime;

    @ManyToMany(mappedBy = "sessions")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Speaker> speakers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Session title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getDescription() {
        return description;
    }

    public Session description(byte[] description) {
        this.description = description;
        return this;
    }

    public void setDescription(byte[] description) {
        this.description = description;
    }

    public String getDescriptionContentType() {
        return descriptionContentType;
    }

    public Session descriptionContentType(String descriptionContentType) {
        this.descriptionContentType = descriptionContentType;
        return this;
    }

    public void setDescriptionContentType(String descriptionContentType) {
        this.descriptionContentType = descriptionContentType;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public Session startDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ZonedDateTime getEndtDateTime() {
        return endtDateTime;
    }

    public Session endtDateTime(ZonedDateTime endtDateTime) {
        this.endtDateTime = endtDateTime;
        return this;
    }

    public void setEndtDateTime(ZonedDateTime endtDateTime) {
        this.endtDateTime = endtDateTime;
    }

    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public Session speakers(Set<Speaker> speakers) {
        this.speakers = speakers;
        return this;
    }

    public Session addSpeakers(Speaker speaker) {
        this.speakers.add(speaker);
        speaker.getSessions().add(this);
        return this;
    }

    public Session removeSpeakers(Speaker speaker) {
        this.speakers.remove(speaker);
        speaker.getSessions().remove(this);
        return this;
    }

    public void setSpeakers(Set<Speaker> speakers) {
        this.speakers = speakers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        return id != null && id.equals(((Session) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Session{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", descriptionContentType='" + getDescriptionContentType() + "'" +
            ", startDateTime='" + getStartDateTime() + "'" +
            ", endtDateTime='" + getEndtDateTime() + "'" +
            "}";
    }
}
