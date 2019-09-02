package com.alexandros.teleram.bot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "ideas")
@NamedQuery(name="Idea.fetchByRememberDate",
			query="SELECT i from Idea i WHERE i.rememberDate =:date ")
public class Idea implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name="message")
	private String message;
	@Column(name="user_id")
	private int userId;
	@Column(name="submit_date")
	@Temporal(TemporalType.DATE)
	private Date submitDate;
	@Temporal(TemporalType.DATE)
	@Column(name="remember_date")
	private Date rememberDate;
	@Column(name="chat_id")
	private Long chatId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public Date getRememberDate() {
		return rememberDate;
	}

	public void setRememberDate(Date rememberDate) {
		this.rememberDate = rememberDate;
	}

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}
}
