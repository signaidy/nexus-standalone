package SpectraSystems.Nexus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SpectraSystems.Nexus.models.Provider;
import SpectraSystems.Nexus.models.Type;
import SpectraSystems.Nexus.repositories.ProviderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderServices {

    private final ProviderRepository providerRepository;

    @Autowired
    public ProviderServices(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    
    /** 
     * @return 'List<Provider>'
     */
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    
    /** 
     * @param id
     * @return 'Optional<Provider>'
     */
    public Optional<Provider> getProviderById(Long id) {
        return providerRepository.findById(id);
    }

    
    /** 
     * @param type
     * @return 'List<Provider>'
     */
    public List<Provider> getProviderByType(Type type) {
        return providerRepository.findByType(type);
    }

    
    /** 
     * @param provider
     * @return 'Provider'
     */
    public Provider createProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    /** 
     * Update an existing provider
     *
     * @param provider The updated provider data
     * @return         The updated provider
     */
    public Provider updateProvider(Provider provider) {
        // Check if the provider with the given ID exists
        Long id = provider.getId();
        Optional<Provider> existingProviderOptional = providerRepository.findById(id);

        if (existingProviderOptional.isPresent()) {
            // If the provider exists, update its data
            Provider existingProvider = existingProviderOptional.get();
            // Update the fields of the existing provider with the new data
            existingProvider.setProviderName(provider.getProviderName());
            existingProvider.setProviderUrl(provider.getProviderUrl());
            existingProvider.setType(provider.getType());
            existingProvider.setPercentageDiscount(provider.getPercentageDiscount());
            existingProvider.setGainsFlights(provider.getGainsFlights());
            existingProvider.setGainsHotel(provider.getGainsHotel());

            // Save the updated provider in the database
            return providerRepository.save(existingProvider);
        } else {
            // If the provider with the given ID is not found, return null
            return null;
        }
    }
    
    /** 
     * @param id
     */
    public void deleteProvider(Long id) {
        providerRepository.deleteById(id);
    }
}

