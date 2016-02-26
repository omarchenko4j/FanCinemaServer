package mos.edu.server.fancinema.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mos.edu.server.fancinema.Constants;
import mos.edu.server.fancinema.entity.Favorite;
import mos.edu.server.fancinema.entity.RatingFilm;
import mos.edu.server.fancinema.entity.Review;
import mos.edu.server.fancinema.entity.User;
import mos.edu.server.fancinema.entity.represent.UserFavorite;
import mos.edu.server.fancinema.entity.represent.UserReview;
import mos.edu.server.fancinema.service.UserService;

@RestController
@RequestMapping(value = Constants.URI_USERS)
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET,
					value = Constants.URI_USER_BY_ID,
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Resource<User>> getUser(@PathVariable(value = "id_user") int id) {
		User user = userService.getUserById(id);
		if (user != null) {
			Resource<User> userResource = new Resource<>(user);
			userResource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
			return new ResponseEntity<>(userResource, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.POST,
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Resource<User>> addUser(@RequestBody User user) {
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET,
					value = Constants.URI_USER_RATING_FOR_FILM,
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Resource<RatingFilm>> getUserRatingForFilm(@PathVariable(value = "id_user") int idUser,
																 @PathVariable(value = "id_film") int idFilm) {
		RatingFilm filmRating = userService.getUserRatingForFilm(idUser, idFilm);
		if (filmRating != null) {
			Resource<RatingFilm> filmRatingResource = new Resource<>(filmRating);
			filmRatingResource.add(linkTo(methodOn(UserController.class).getUserRatingForFilm(idUser, idFilm)).withSelfRel());
			return new ResponseEntity<>(filmRatingResource, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.POST,
					value = Constants.URI_USER_RATING_FOR_FILM,
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Resource<RatingFilm>> addUserRatingForFilm(@PathVariable(value = "id_user") int idUser,
												  	  			 @PathVariable(value = "id_film") int idFilm,
												  	  			 @RequestBody RatingFilm filmRating) {
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET,
					value = Constants.URI_USER_REVIEWS,
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<PagedResources<UserReview>> getUserReviews(@PathVariable(value = "id_user") int id,
														  	     @RequestParam(value = "page", required = false, defaultValue = "0") int page, 
														  	     @RequestParam(value = "size", required = false, defaultValue = "30") int size) {
		if (page < 0 || size <= 0) 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Page<UserReview> reviews = userService.getUserReviews(id, page, size);
		if (reviews != null && reviews.hasContent()) {
			PageMetadata metadata = new PageMetadata(reviews.getSize(), reviews.getNumber(), reviews.getTotalElements(), reviews.getTotalPages());
			PagedResources<UserReview> reviewResources = new PagedResources<>(reviews.getContent(), metadata);
			
			int prev_page = page - 1;
			if (prev_page >= 0)
				reviewResources.add(linkTo(methodOn(UserController.class).getUserReviews(id, prev_page, size)).withRel("prev"));
			reviewResources.add(linkTo(methodOn(UserController.class).getUserReviews(id, page, size)).withSelfRel());
			int next_page = page + 1;
			long last_page = reviews.getTotalPages();
			if (next_page != last_page)
				reviewResources.add(linkTo(methodOn(UserController.class).getUserReviews(id, next_page, size)).withRel("next"));
			
			return new ResponseEntity<>(reviewResources, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.POST,
					value = Constants.URI_USER_FILM_REVIEW,
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Resource<Review>> addUserReviewForFilm(@PathVariable(value = "id_user") int idUser,
													  		 @PathVariable(value = "id_film") int idFilm,
													  		 @RequestBody Review review) {
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET,
					value = Constants.URI_USER_FAVORITE,
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<PagedResources<UserFavorite>> getUserFavorite(@PathVariable(value = "id_user") int id,
													  	        	@RequestParam(value = "page", required = false, defaultValue = "0") int page, 
													  	        	@RequestParam(value = "size", required = false, defaultValue = "30") int size) {
		if (page < 0 || size <= 0) 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Page<UserFavorite> favorite = userService.getUserFavorite(id, page, size);
		if (favorite != null && favorite.hasContent()) {
			PageMetadata metadata = new PageMetadata(favorite.getSize(), favorite.getNumber(), favorite.getTotalElements(), favorite.getTotalPages());
			PagedResources<UserFavorite> favoriteResources = new PagedResources<>(favorite.getContent(), metadata);
			
			int prev_page = page - 1;
			if (prev_page >= 0)
				favoriteResources.add(linkTo(methodOn(UserController.class).getUserFavorite(id, prev_page, size)).withRel("prev"));
			favoriteResources.add(linkTo(methodOn(UserController.class).getUserFavorite(id, page, size)).withSelfRel());
			int next_page = page + 1;
			long last_page = favorite.getTotalPages();
			if (next_page != last_page)
				favoriteResources.add(linkTo(methodOn(UserController.class).getUserFavorite(id, next_page, size)).withRel("next"));
			
			return new ResponseEntity<>(favoriteResources, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.GET,
					value = Constants.URI_USER_FAVORITE,
					params = {"looked"},
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<PagedResources<UserFavorite>> getUserFavoriteIsLooked(@PathVariable(value = "id_user") int id,
																			@RequestParam(value = "looked", required = false, defaultValue = "0") boolean looked,
																  	        @RequestParam(value = "page", required = false, defaultValue = "0") int page, 
																  	        @RequestParam(value = "size", required = false, defaultValue = "30") int size) {
		if (page < 0 || size <= 0) 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Page<UserFavorite> favorite = userService.getUserFavoriteIsLooked(id, looked, page, size);
		if (favorite != null && favorite.hasContent()) {
			PageMetadata metadata = new PageMetadata(favorite.getSize(), favorite.getNumber(), favorite.getTotalElements(), favorite.getTotalPages());
			PagedResources<UserFavorite> favoriteResources = new PagedResources<>(favorite.getContent(), metadata);
			
			int prev_page = page - 1;
			if (prev_page >= 0)
				favoriteResources.add(linkTo(methodOn(UserController.class).getUserFavoriteIsLooked(id, looked, prev_page, size)).withRel("prev"));
			favoriteResources.add(linkTo(methodOn(UserController.class).getUserFavoriteIsLooked(id, looked, page, size)).withSelfRel());
			int next_page = page + 1;
			long last_page = favorite.getTotalPages();
			if (next_page != last_page)
				favoriteResources.add(linkTo(methodOn(UserController.class).getUserFavoriteIsLooked(id, looked, next_page, size)).withRel("next"));
			
			return new ResponseEntity<>(favoriteResources, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.POST,
					value = Constants.URI_USER_FILM_FAVORITE,
					consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public HttpEntity<Resource<Favorite>> addUserFavorite(@PathVariable(value = "id_user") int idUser,
													      @PathVariable(value = "id_film") int idFilm,
													      @RequestBody Favorite favorite) {
		return null;
	}
	
}