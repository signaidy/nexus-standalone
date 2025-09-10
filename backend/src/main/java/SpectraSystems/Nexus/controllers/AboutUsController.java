package SpectraSystems.Nexus.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import SpectraSystems.Nexus.models.Aboutus;
import SpectraSystems.Nexus.services.AboutUsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/nexus/aboutus")
@RequiredArgsConstructor
public class AboutUsController {
    private final AboutUsService aboutUsService;

    
    /** 
     * @param aboutUsEntity
     * @return returns a 'ResponseEntity<Aboutus>'
     */
    @PostMapping
    public ResponseEntity<Aboutus> createOrUpdateAboutUs(@RequestBody Aboutus aboutUsEntity) {
        Aboutus savedAboutUs = aboutUsService.saveOrUpdate(aboutUsEntity);
        return new ResponseEntity<>(savedAboutUs, HttpStatus.CREATED);
    }

    
    /** 
     * @param id
     * @return returns a 'ResponseEntity<Aboutus>'
     */
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Aboutus> getAboutUsById(@PathVariable Long id) {
        Optional<Aboutus> aboutUs = aboutUsService.findById(id);
        return aboutUs.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    
    /** 
     * @return returns a 'ResponseEntity<List<Aboutus>>'
     */
    @GetMapping
    public ResponseEntity<List<Aboutus>> getAllAboutUs() {
        List<Aboutus> allAboutUs = aboutUsService.findAll();
        return new ResponseEntity<>(allAboutUs, HttpStatus.OK);
    }

    
    /** 
     * @param id
     * @param aboutUs
     * @return returns a 'ResponseEntity<Aboutus>'
     */
    @PutMapping("/{id}")
    public ResponseEntity<Aboutus> updateAboutUs(@PathVariable Long id, @RequestBody Aboutus aboutUs) {
        if (!aboutUsService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        aboutUs.setId(id);
        Aboutus updatedAboutUs = aboutUsService.saveOrUpdate(aboutUs);
        return ResponseEntity.ok(updatedAboutUs);
    }

    
    /** 
     * @param id
     * @return returns a 'ResponseEntity<Void>'
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAboutUs(@PathVariable Long id) {
        aboutUsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
