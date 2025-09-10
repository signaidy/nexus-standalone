package SpectraSystems.Nexus.controllers;

import org.hibernate.service.internal.ProvidedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import SpectraSystems.Nexus.models.Provider;
import SpectraSystems.Nexus.models.Type;
import SpectraSystems.Nexus.services.ProviderServices;

import java.util.List;

@RestController
@RequestMapping("/nexus/providers")
public class ProviderController {

    private final ProviderServices providerService;

    @Autowired
    public ProviderController(ProviderServices providerService) {
        this.providerService = providerService;
    }

    
    /** 
     * @return a 'ResponseEntity<List<Provider>>'
     */
    @GetMapping
    public ResponseEntity<List<Provider>> getAllProviders() {
        List<Provider> providers = providerService.getAllProviders();
        return new ResponseEntity<>(providers, HttpStatus.OK);
    }

    
    /** 
     * @param id
     * @return a 'ResponseEntity<Provider>'
     */
    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long id) {
        return providerService.getProviderById(id)
                .map(provider -> new ResponseEntity<>(provider, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    
    /** 
     * @param type
     * @return a 'ResponseEntity<List<Provider>>'
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Provider>> getProviderByType(@PathVariable Type type) {
        List<Provider> providers = providerService.getProviderByType(type);
        return new ResponseEntity<>(providers, HttpStatus.OK);
    }

    
    /** 
     * @param provider
     * @return a 'ResponseEntity<Provider>'
     */
    @PostMapping
    public ResponseEntity<Provider> createProvider(@RequestBody Provider provider) {
        Provider createdProvider = providerService.createProvider(provider);
        return new ResponseEntity<>(createdProvider, HttpStatus.CREATED);
    }

    /** 
     * Update an existing provider by ID
     *
     * @param id       The ID of the provider to update
     * @param provider The updated provider data
     * @return         ResponseEntity containing the updated provider data or NOT_FOUND if the provider with the given ID is not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Provider> updateProvider(@PathVariable Long id, @RequestBody Provider provider) {
        // Set the ID of the provider to update
        provider.setId(id);
        
        // Update the provider data in the database
        Provider updatedProvider = providerService.updateProvider(provider);
        
        if (updatedProvider != null) {
            // If provider updated successfully, return it with OK status
            return new ResponseEntity<>(updatedProvider, HttpStatus.OK);
        } else {
            // If provider with the given ID is not found, return NOT_FOUND status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    /** 
     * @param id
     * @return a 'ResponseEntity<Void>'
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

