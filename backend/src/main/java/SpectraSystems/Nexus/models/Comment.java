package SpectraSystems.Nexus.models;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "COMMENTS")
public class Comment {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "CONTENT", nullable = false, length = 1000)
    private String content;

    @Column(name = "CREATION_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(name = "PATH", nullable = false, length = 500)
    private String path;

    @JoinColumn(name = "PARENT_COMMENT_ID")
    private Long parentComment;

    @Column(name = "FLIGHT_ID", nullable = false)
    private Long flightId;

    @Column(name = "USER_NAME")
    private String userName;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> children;

    // Constructors, getters, setters, and other methods...
    
    public Comment() {
        // Default constructor
        this.creationDate = new Date();
        this.path = "/";
    }

    public Comment(Long userId, String content, Long flightId, String userName, Long parentComment) {
        this.userId = userId;
        this.content = content;
        this.parentComment = parentComment;
        this.creationDate = new Date();
        this.path = "/";
        this.flightId = flightId;
        this.userName = userName;
        this.children =  new ArrayList<>();
    }

    
    /** 
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    
    /** 
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    
    /** 
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    
    /** 
     * @param creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    
    /** 
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }
    
    
    /** 
     * @param parentComment
     */
    public void setParentComment(Long parentComment) {
        this.parentComment = parentComment;
    }
    
    
    /** 
     * @param flightId
     */
    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    
    /** 
     * @return Long
     */
    public Long getId() {
        return id;
    }
    
    
    /** 
     * @return Long
     */
    public Long getUserId() {
        return userId;
    }
    
    
    /** 
     * @return String
     */
    public String getContent() {
        return content;
    }
    
    
    /** 
     * @return Date
     */
    public Date getCreationDate() {
        return creationDate;
    }
    
    
    /** 
     * @return String
     */
    public String getPath() {
        return path;
    }
    
    
    /** 
     * @return Long
     */
    public Long getParentComment() {
        return parentComment;
    }
    
    
    /** 
     * @return Long
     */
    public Long getFlightId() {
        return flightId;
    }

    
    /** 
     * @return String
     */
    public String getUserName() {
        return userName;
    }

    
    /** 
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    
    /** 
     * @return List<Comment>
     */
    public List<Comment> getChildren() {
        return children;
    }

    
    /** 
     * @param children
     */
    public void setChildren(List<Comment> children) {
        this.children = children;
    }
}
