package com.sydehealth.sydehealth.model.portfolio;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InstaPhotoResponse {

    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("data")
    @Expose
    private ArrayList<Data> data = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }


    public class Caption {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("created_time")
        @Expose
        private String createdTime;
        @SerializedName("from")
        @Expose
        private From from;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public From getFrom() {
            return from;
        }

        public void setFrom(From from) {
            this.from = from;
        }

    }


    public class CarouselMedium {

        @SerializedName("images")
        @Expose
        private Images_ images;
        @SerializedName("users_in_photo")
        @Expose
        private ArrayList<UsersInPhoto_> usersInPhoto = null;
        @SerializedName("type")
        @Expose
        private String type;

        public Images_ getImages() {
            return images;
        }

        public void setImages(Images_ images) {
            this.images = images;
        }

        public ArrayList<UsersInPhoto_> getUsersInPhoto() {
            return usersInPhoto;
        }

        public void setUsersInPhoto(ArrayList<UsersInPhoto_> usersInPhoto) {
            this.usersInPhoto = usersInPhoto;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }


    public class Comments {

        @SerializedName("count")
        @Expose
        private Integer count;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

    }


    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("images")
        @Expose
        private Images images;
        @SerializedName("created_time")
        @Expose
        private String createdTime;
        @SerializedName("caption")
        @Expose
        private Caption caption;
        @SerializedName("user_has_liked")
        @Expose
        private Boolean userHasLiked;
        @SerializedName("likes")
        @Expose
        private Likes likes;
        @SerializedName("tags")
        @Expose
        private ArrayList<String> tags = null;
        @SerializedName("filter")
        @Expose
        private String filter;
        @SerializedName("comments")
        @Expose
        private Comments comments;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("location")
        @Expose
        private Location location;
        @SerializedName("attribution")
        @Expose
        private Object attribution;
        @SerializedName("users_in_photo")
        @Expose
        private ArrayList<UsersInPhoto> usersInPhoto = null;
        @SerializedName("carousel_media")
        @Expose
        private ArrayList<CarouselMedium> carouselMedia = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public Caption getCaption() {
            return caption;
        }

        public void setCaption(Caption caption) {
            this.caption = caption;
        }

        public Boolean getUserHasLiked() {
            return userHasLiked;
        }

        public void setUserHasLiked(Boolean userHasLiked) {
            this.userHasLiked = userHasLiked;
        }

        public Likes getLikes() {
            return likes;
        }

        public void setLikes(Likes likes) {
            this.likes = likes;
        }

        public ArrayList<String> getTags() {
            return tags;
        }

        public void setTags(ArrayList<String> tags) {
            this.tags = tags;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public Comments getComments() {
            return comments;
        }

        public void setComments(Comments comments) {
            this.comments = comments;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Object getAttribution() {
            return attribution;
        }

        public void setAttribution(Object attribution) {
            this.attribution = attribution;
        }

        public ArrayList<UsersInPhoto> getUsersInPhoto() {
            return usersInPhoto;
        }

        public void setUsersInPhoto(ArrayList<UsersInPhoto> usersInPhoto) {
            this.usersInPhoto = usersInPhoto;
        }

        public ArrayList<CarouselMedium> getCarouselMedia() {
            return carouselMedia;
        }

        public void setCarouselMedia(ArrayList<CarouselMedium> carouselMedia) {
            this.carouselMedia = carouselMedia;
        }

    }


    public class From {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("full_name")
        @Expose
        private String fullName;
        @SerializedName("profile_picture")
        @Expose
        private String profilePicture;
        @SerializedName("username")
        @Expose
        private String username;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }


    public class Images {

        @SerializedName("thumbnail")
        @Expose
        private Thumbnail thumbnail;
        @SerializedName("low_resolution")
        @Expose
        private LowResolution lowResolution;
        @SerializedName("standard_resolution")
        @Expose
        private StandardResolution standardResolution;

        public Thumbnail getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(Thumbnail thumbnail) {
            this.thumbnail = thumbnail;
        }

        public LowResolution getLowResolution() {
            return lowResolution;
        }

        public void setLowResolution(LowResolution lowResolution) {
            this.lowResolution = lowResolution;
        }

        public StandardResolution getStandardResolution() {
            return standardResolution;
        }

        public void setStandardResolution(StandardResolution standardResolution) {
            this.standardResolution = standardResolution;
        }

    }


    public class Images_ {

        @SerializedName("thumbnail")
        @Expose
        private Thumbnail_ thumbnail;
        @SerializedName("low_resolution")
        @Expose
        private LowResolution_ lowResolution;
        @SerializedName("standard_resolution")
        @Expose
        private StandardResolution_ standardResolution;

        public Thumbnail_ getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(Thumbnail_ thumbnail) {
            this.thumbnail = thumbnail;
        }

        public LowResolution_ getLowResolution() {
            return lowResolution;
        }

        public void setLowResolution(LowResolution_ lowResolution) {
            this.lowResolution = lowResolution;
        }

        public StandardResolution_ getStandardResolution() {
            return standardResolution;
        }

        public void setStandardResolution(StandardResolution_ standardResolution) {
            this.standardResolution = standardResolution;
        }

    }


    public class Likes {

        @SerializedName("count")
        @Expose
        private Integer count;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

    }


    public class Location {

        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("longitude")
        @Expose
        private Double longitude;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("id")
        @Expose
        private Long id;

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

    }


    public class LowResolution {

        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("url")
        @Expose
        private String url;

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    public class LowResolution_ {

        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("url")
        @Expose
        private String url;

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    public class Meta {

        @SerializedName("error_type")
        @Expose
        private String errorType;
        @SerializedName("code")
        @Expose
        private Integer code;
        @SerializedName("error_message")
        @Expose
        private String errorMessage;

        public String getErrorType() {
            return errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }


    public class Pagination {

        @SerializedName("next_url")
        @Expose
        private String nextUrl;
        @SerializedName("next_max_id")
        @Expose
        private String nextMaxId;

        public String getNextUrl() {
            return nextUrl;
        }

        public void setNextUrl(String nextUrl) {
            this.nextUrl = nextUrl;
        }

        public String getNextMaxId() {
            return nextMaxId ==null?"":nextMaxId;
        }

        public void setNextMaxId(String nextMaxId) {
            this.nextMaxId = nextMaxId;
        }

    }


    public class Position {

        @SerializedName("x")
        @Expose
        private Double x;
        @SerializedName("y")
        @Expose
        private Double y;

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

    }


    public class Position_ {

        @SerializedName("x")
        @Expose
        private Double x;
        @SerializedName("y")
        @Expose
        private Double y;

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

    }


    public class StandardResolution {

        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("url")
        @Expose
        private String url;

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    public class StandardResolution_ {

        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("url")
        @Expose
        private String url;

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    public class Thumbnail {

        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("url")
        @Expose
        private String url;

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    public class Thumbnail_ {

        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("url")
        @Expose
        private String url;

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }


    public class User {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("full_name")
        @Expose
        private String fullName;
        @SerializedName("profile_picture")
        @Expose
        private String profilePicture;
        @SerializedName("username")
        @Expose
        private String username;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }


    public class User_ {

        @SerializedName("username")
        @Expose
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }


    public class User__ {

        @SerializedName("username")
        @Expose
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }


    public class UsersInPhoto {

        @SerializedName("user")
        @Expose
        private User_ user;
        @SerializedName("position")
        @Expose
        private Position position;

        public User_ getUser() {
            return user;
        }

        public void setUser(User_ user) {
            this.user = user;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

    }


    public class UsersInPhoto_ {

        @SerializedName("user")
        @Expose
        private User__ user;
        @SerializedName("position")
        @Expose
        private Position_ position;

        public User__ getUser() {
            return user;
        }

        public void setUser(User__ user) {
            this.user = user;
        }

        public Position_ getPosition() {
            return position;
        }

        public void setPosition(Position_ position) {
            this.position = position;
        }

    }

}