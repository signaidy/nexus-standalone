package SpectraSystems.Nexus.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SpectraSystems.Nexus.models.Comment;
import SpectraSystems.Nexus.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    
    /** 
     * @return 'List<Comment>'
     */
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    
    
    /** 
     * @param flightId
     * @return 'List<Comment>'
     */
    public List<Comment> getCommentsByFlightId(Long flightId) {
        return commentRepository.findByFlightId(flightId);
    }

    
    
    /** 
     * @param id
     * @return 'Optional<Comment>'
     */
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    
    
    /** 
     * @param comment
     * @return 'Comment'
     */
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    
    /** 
     * @param id
     * @param commentDetails
     * @return 'Comment'
     */
    public Comment updateComment(Long id, Comment commentDetails) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment existingComment = optionalComment.get();
            existingComment.setContent(commentDetails.getContent());
            return commentRepository.save(existingComment);
        } else {
            // Handle if the comment with the given id is not found
            return null;
        }
    }
    
    
    /** 
     * @param id
     */
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}

