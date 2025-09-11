package SpectraSystems.Nexus.services;

import SpectraSystems.Nexus.models.Comment;
import SpectraSystems.Nexus.repositories.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceUnitTest {

    @Mock CommentRepository repo;
    @InjectMocks CommentService service;

    @Test
    void getAllComments_returns_list_from_repo() {
        when(repo.findAll()).thenReturn(List.of(new Comment(), new Comment()));
        assertEquals(2, service.getAllComments().size());
        verify(repo).findAll();
    }

    @Test
    void getCommentsByFlightId_delegates_to_repo() {
        when(repo.findByFlightId(5L)).thenReturn(List.of(new Comment()));
        assertEquals(1, service.getCommentsByFlightId(5L).size());
        verify(repo).findByFlightId(5L);
    }

    @Test
    void getCommentById_present_and_empty() {
        Comment c = new Comment();
        c.setId(7L);
        when(repo.findById(7L)).thenReturn(Optional.of(c));
        when(repo.findById(9L)).thenReturn(Optional.empty());

        assertTrue(service.getCommentById(7L).isPresent());
        assertTrue(service.getCommentById(9L).isEmpty());
    }

    @Test
    void createComment_saves_and_returns_entity() {
        Comment in = new Comment();
        in.setContent("hello");

        when(repo.save(in)).thenReturn(in);

        Comment out = service.createComment(in);
        assertSame(in, out);
        verify(repo).save(in);
    }

    @Test
    void updateComment_happyPath_updates_content_and_saves() {
        Comment existing = new Comment();
        existing.setId(11L);
        existing.setContent("old");

        Comment details = new Comment();
        details.setContent("new");

        when(repo.findById(11L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        Comment out = service.updateComment(11L, details);

        assertNotNull(out);
        assertEquals("new", out.getContent());

        ArgumentCaptor<Comment> cap = ArgumentCaptor.forClass(Comment.class);
        verify(repo).save(cap.capture());
        assertEquals(11L, cap.getValue().getId());
        assertEquals("new", cap.getValue().getContent());
    }

    @Test
    void updateComment_notFound_returns_null_and_does_not_save() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        Comment out = service.updateComment(999L, new Comment());
        assertNull(out);
        verify(repo, never()).save(any());
    }

    @Test
    void deleteComment_calls_repo_deleteById() {
        doNothing().when(repo).deleteById(3L);
        service.deleteComment(3L);
        verify(repo).deleteById(3L);
    }
}