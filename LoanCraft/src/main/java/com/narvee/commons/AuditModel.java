package com.narvee.commons;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@jakarta.persistence.MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "createddate", nullable = false, updatable = false)
	private LocalDateTime createddate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "updateddate", nullable = false, updatable = true)
	private LocalDateTime updateddate;

	@PrePersist
	public void setCreateddate() {
		ZoneId india = ZoneId.of("Asia/Kolkata");
		LocalDateTime now = LocalDateTime.now(india);
		this.createddate = now;
		this.updateddate = now;
	}

	@PreUpdate
	public void setUpdateddate() {
		ZoneId india = ZoneId.of("Asia/Kolkata");
		LocalDateTime now = LocalDateTime.now(india);
		this.updateddate = now;
	}
	
	
	
}
