package com.narvee.commons;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;

@jakarta.persistence.MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditModel implements Serializable {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "createddate", nullable = false, updatable = false)
	private LocalDateTime createddate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "updateddate", nullable = false, updatable = true)
	private LocalDateTime updateddate;

	@PrePersist
	public void setCreateddate() {
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		this.createddate = now;
		this.updateddate = now;
	}

	@PreUpdate
	public void setUpdateddate() {
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		this.updateddate = now;
	}
}
