package com.example.urlsorting.sorting.entities;

import java.util.Date;
import java.util.List;

import com.example.urlsorting.user.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Sort {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sortId;

	@Column()
	private String destination;

	@Column()
	private String sort;

	@Column()
	private Date createAt;

	@Column()
	private int clickCnt = 0;

	@Column()
	private Date lastClickAt;

	@JsonIgnore
	@OneToMany(mappedBy = "sort", cascade = CascadeType.ALL)
	private List<Log> logs;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_Id")
	private User user;

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public void lastClickAt(Date lastClickAt) {
		this.lastClickAt = lastClickAt;
	}

	public void clickCnt(int clickCnt) {
		this.clickCnt = clickCnt + 1;
	}
}
