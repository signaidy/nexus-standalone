package SpectraSystems.Nexus.controllers;

import SpectraSystems.Nexus.filters.JwtAuthenticationFilter;
import SpectraSystems.Nexus.models.Comment;
import SpectraSystems.Nexus.services.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CommentController.class,
        excludeAutoConfiguration = { SecurityAutoConfiguration.class }
)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerWebTest {

    @Autowired MockMvc mvc;

    @MockBean CommentService commentService;
    @MockBean JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getAllComments_ok() throws Exception {
        when(commentService.getAllComments()).thenReturn(List.of(new Comment(), new Comment()));

        mvc.perform(get("/nexus/comments").accept(APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

        verify(commentService).getAllComments();
    }

    @Test
    void getCommentById_found_and_notFound() throws Exception {
        when(commentService.getCommentById(1L)).thenReturn(Optional.of(new Comment()));
        when(commentService.getCommentById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/nexus/comments/{id}", 1L).accept(APPLICATION_JSON))
           .andExpect(status().isOk());

        mvc.perform(get("/nexus/comments/{id}", 99L).accept(APPLICATION_JSON))
           .andExpect(status().isNotFound());
    }

    @Test
    void createComment_created() throws Exception {
        when(commentService.createComment(any(Comment.class))).thenReturn(new Comment());

        mvc.perform(post("/nexus/comments")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content("{}"))
           .andExpect(status().isCreated());

        verify(commentService).createComment(any(Comment.class));
    }

    @Test
    void updateComment_ok() throws Exception {
        when(commentService.updateComment(eq(7L), any(Comment.class))).thenReturn(new Comment());

        mvc.perform(put("/nexus/comments/{id}", 7L)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content("{\"text\":\"hello\"}"))
           .andExpect(status().isOk());

        verify(commentService).updateComment(eq(7L), any(Comment.class));
    }

    @Test
    void deleteComment_noContent() throws Exception {
        doNothing().when(commentService).deleteComment(5L);

        mvc.perform(delete("/nexus/comments/{id}", 5L))
           .andExpect(status().isNoContent());

        verify(commentService).deleteComment(5L);
    }
}