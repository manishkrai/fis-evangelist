package com.fis.evangelist.subscription.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DynamicUpdate
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="SUBSCRIPTION")
public class Subscription {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="SUBSCRIBER_NAME")
	private String subscriberName;
	
	@Column(name="BOOK_ID")
	private String bookId;
	
	@Column(name="DATE_SUBSCRIBED")
	private Date dateSubscribed;
	
	@Column(name="DATE_RETURNED")
	private Date dateReturned;
}
