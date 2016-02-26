package mos.edu.server.fancinema.entity.represent;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import mos.edu.server.fancinema.Constants;
import mos.edu.server.fancinema.entity.Review;
import mos.edu.server.fancinema.entity.User;
import mos.edu.server.fancinema.entity.composite_key.ReviewKey;

@Entity
@Table(name = Constants.TABLE_REVIEWS)
public class UserReview {

	@JsonIgnore
	@EmbeddedId
	private ReviewKey reviewKey;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "film_id", insertable = false, updatable = false)
	private ShortFilm film;
	
	@Column(name = Review.COLUMN_REVIEW, nullable = false, columnDefinition = "TEXT")
	private String review;
	
	protected UserReview() {}
	
	public ReviewKey getReviewKey() {
		return reviewKey;
	}

	public void setReviewKey(ReviewKey reviewKey) {
		this.reviewKey = reviewKey;
	}
	
	public ShortFilm getFilm() {
		return film;
	}

	public void setFilm(ShortFilm film) {
		this.film = film;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
	
}
