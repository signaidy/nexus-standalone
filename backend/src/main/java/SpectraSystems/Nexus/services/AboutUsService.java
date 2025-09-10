package SpectraSystems.Nexus.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import SpectraSystems.Nexus.exceptions.ResourceNotFoundException;
import SpectraSystems.Nexus.models.Aboutus;
import SpectraSystems.Nexus.repositories.AboutusRespository;

@Service
public class AboutUsService {
    private final AboutusRespository aboutUsRepository;

    @Autowired
    public AboutUsService(AboutusRespository aboutUsRepository) {
        this.aboutUsRepository = aboutUsRepository;
    }

    
    /** 
     * @param aboutUsEntity
     * @return 'Aboutus'
     */
    public Aboutus saveOrUpdate(Aboutus aboutUsEntity) {
        return aboutUsRepository.save(aboutUsEntity);
    }

    
    /** 
     * @param id
     * @return 'Optional<Aboutus>'
     */
    public Optional<Aboutus> findById(Long id) {
        return aboutUsRepository.findById(id);
    }

    
    /** 
     * @return 'List<Aboutus>'
     */
    public List<Aboutus> findAll() {
        return aboutUsRepository.findAll();
    }

    
    /** 
     * @param id
     */
    public void deleteById(Long id) {
        aboutUsRepository.deleteById(id);
    }
    
    
    /** 
     * @param id
     * @param aboutUsDetails
     * @return 'Aboutus'
     */
    public Aboutus updateAboutUs(Long id, Aboutus aboutUsDetails) {
        Aboutus aboutUs = aboutUsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AboutUs not found with id: " + id));

        // Update AboutUs details
        aboutUs.setSlogan(aboutUsDetails.getSlogan());
        aboutUs.setGif(aboutUsDetails.getGif());
        aboutUs.setYt(aboutUsDetails.getYt());
        aboutUs.setCardsAmount(aboutUsDetails.getCardsAmount());
        aboutUs.setTitleOne(aboutUsDetails.getTitleOne());
        aboutUs.setTextOne(aboutUsDetails.getTextOne());
        aboutUs.setImgOne(aboutUsDetails.getImgOne());
        aboutUs.setTitleTwo(aboutUsDetails.getTitleTwo());
        aboutUs.setTextTwo(aboutUsDetails.getTextTwo());
        aboutUs.setImgTwo(aboutUsDetails.getImgTwo());
        aboutUs.setTitleThree(aboutUsDetails.getTitleThree());
        aboutUs.setTextThree(aboutUsDetails.getTextThree());
        aboutUs.setImgThree(aboutUsDetails.getImgThree());
        aboutUs.setTitleFour(aboutUsDetails.getTitleFour());
        aboutUs.setTextFour(aboutUsDetails.getTextFour());
        aboutUs.setImgFour(aboutUsDetails.getImgFour());

        return aboutUsRepository.save(aboutUs);
    }
}