package SpectraSystems.Nexus.services;

import SpectraSystems.Nexus.exceptions.ResourceNotFoundException;
import SpectraSystems.Nexus.models.Aboutus;
import SpectraSystems.Nexus.repositories.AboutusRespository;
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
class AboutUsServiceUnitTest {

    @Mock AboutusRespository repo;
    @InjectMocks AboutUsService service;

    @Test
    void saveOrUpdate_delegates_to_repository() {
        Aboutus in = new Aboutus();
        in.setSlogan("Hello");
        Aboutus saved = new Aboutus();
        saved.setSlogan("Hello");

        when(repo.save(in)).thenReturn(saved);

        Aboutus out = service.saveOrUpdate(in);

        assertSame(saved, out);
        verify(repo).save(in);
    }

    @Test
    void findById_present_and_empty() {
        Aboutus a = new Aboutus();
        when(repo.findById(1L)).thenReturn(Optional.of(a));
        when(repo.findById(9L)).thenReturn(Optional.empty());

        assertTrue(service.findById(1L).isPresent());
        assertTrue(service.findById(9L).isEmpty());
    }

    @Test
    void findAll_returns_list_from_repo() {
        when(repo.findAll()).thenReturn(List.of(new Aboutus(), new Aboutus()));
        assertEquals(2, service.findAll().size());
        verify(repo).findAll();
    }

    @Test
    void deleteById_calls_repo() {
        doNothing().when(repo).deleteById(7L);
        service.deleteById(7L);
        verify(repo).deleteById(7L);
    }

    @Test
    void updateAboutUs_happyPath_updates_fields_and_saves() {
        // Existing entity in DB
        Aboutus existing = new Aboutus();
        existing.setId(5L);
        existing.setSlogan("old");
        existing.setGif("old.gif");
        existing.setYt("oldYt");
        existing.setCardsAmount(1);
        existing.setTitleOne("t1-old");
        existing.setTextOne("x1-old");
        existing.setImgOne("img1-old");
        existing.setTitleTwo("t2-old");
        existing.setTextTwo("x2-old");
        existing.setImgTwo("img2-old");
        existing.setTitleThree("t3-old");
        existing.setTextThree("x3-old");
        existing.setImgThree("img3-old");
        existing.setTitleFour("t4-old");
        existing.setTextFour("x4-old");
        existing.setImgFour("img4-old");

        // Incoming details
        Aboutus details = new Aboutus();
        details.setSlogan("new");
        details.setGif("new.gif");
        details.setYt("newYt");
        details.setCardsAmount(4);
        details.setTitleOne("t1");
        details.setTextOne("x1");
        details.setImgOne("img1");
        details.setTitleTwo("t2");
        details.setTextTwo("x2");
        details.setImgTwo("img2");
        details.setTitleThree("t3");
        details.setTextThree("x3");
        details.setImgThree("img3");
        details.setTitleFour("t4");
        details.setTextFour("x4");
        details.setImgFour("img4");

        when(repo.findById(5L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Aboutus.class))).thenAnswer(inv -> inv.getArgument(0));

        Aboutus out = service.updateAboutUs(5L, details);

        // All fields should reflect 'details'
        assertEquals(5L, out.getId());
        assertEquals("new", out.getSlogan());
        assertEquals("new.gif", out.getGif());
        assertEquals("newYt", out.getYt());
        assertEquals(4, out.getCardsAmount());
        assertEquals("t1", out.getTitleOne());
        assertEquals("x1", out.getTextOne());
        assertEquals("img1", out.getImgOne());
        assertEquals("t2", out.getTitleTwo());
        assertEquals("x2", out.getTextTwo());
        assertEquals("img2", out.getImgTwo());
        assertEquals("t3", out.getTitleThree());
        assertEquals("x3", out.getTextThree());
        assertEquals("img3", out.getImgThree());
        assertEquals("t4", out.getTitleFour());
        assertEquals("x4", out.getTextFour());
        assertEquals("img4", out.getImgFour());

        // Verify we saved the modified existing entity
        ArgumentCaptor<Aboutus> cap = ArgumentCaptor.forClass(Aboutus.class);
        verify(repo).save(cap.capture());
        assertEquals(5L, cap.getValue().getId());
        assertEquals("new", cap.getValue().getSlogan());
    }

    @Test
    void updateAboutUs_notFound_throws() {
        when(repo.findById(42L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.updateAboutUs(42L, new Aboutus()));
        verify(repo, never()).save(any());
    }
}