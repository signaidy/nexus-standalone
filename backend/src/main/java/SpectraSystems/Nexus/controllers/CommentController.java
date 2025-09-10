package SpectraSystems.Nexus.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import SpectraSystems.Nexus.models.Comment;
import SpectraSystems.Nexus.services.CommentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/nexus/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    
    /** 
     * @return the 'ResponseEntity<List<Comment>>'
     */
    // Endpoint to retrieve all comments
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    
    /** 
     * @param id
     * @return the 'ResponseEntity<Comment>'
     */
    // Endpoint to retrieve a comment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable("id") Long id) {
        Optional<Comment> commentOptional = commentService.getCommentById(id);
        if (commentOptional.isPresent()) {
            return new ResponseEntity<>(commentOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    /** 
     * @param comment
     * @return the 'ResponseEntity<Comment>'
     */
    // Endpoint to create a new comment
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    
    /** 
     * @param id
     * @param commentDetails
     * @return the 'ResponseEntity<Comment>'
     */
    // Endpoint to update an existing comment
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") Long id, @RequestBody Comment commentDetails) {
        Comment updatedComment = commentService.updateComment(id, commentDetails);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    
    /** 
     * @param id
     * @return the 'ResponseEntity<Void>'
     */
    // Endpoint to delete a comment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

