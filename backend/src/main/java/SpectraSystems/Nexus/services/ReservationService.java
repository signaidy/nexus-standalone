package SpectraSystems.Nexus.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import SpectraSystems.Nexus.exceptions.ResourceNotFoundException;
import SpectraSystems.Nexus.models.Flight;
import SpectraSystems.Nexus.models.Reservation;
import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.repositories.FlightRepository;
import SpectraSystems.Nexus.repositories.ReservationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;
    private final UserService userService;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, FlightRepository flightRepository, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
        this.userService = userService;
    }

    
    /** 
     * @return 'List<Reservation>'
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    
    /** 
     * @param id
     * @return 'Optional<Reservation>'
     */
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    
    /** 
     * @param id
     * @return Optional<Reservation>
     */
    public Optional<Reservation> getReservationByReservationNumber(String id){
        return reservationRepository.findByReservationNumber(id);
    }

    
    /** 
     * @param userId
     * @return 'List<Reservation>'
     */
    public List<Reservation> getAllReservationsByUserId(Long userId) {
        return reservationRepository.findByUserid(userId);
    }

    
    /** 
     * @param reservation
     * @return 'Reservation'
     */
    public Reservation createReservation(Reservation reservation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getId();
        // Set the userid in the reservation object
        reservation.setUser(userId);
        reservation.setState("active");
        // Save the reservation
        return reservationRepository.save(reservation);
    }

    
    /** 
     * @param hotelId
     * @return List<Reservation>
     */
    public List<Reservation> getReservationsByHotelId(String hotelId) {
        return reservationRepository.findAllByHotelId(hotelId);
    }

    
    /** 
     * @param hotelId
     */
    // Method to cancel reservations by hotelId
    public void cancelReservationsByHotelId(String hotelId) {
        List<Reservation> reservations = getReservationsByHotelId(hotelId);
        for (Reservation reservation : reservations) {
            String bundle = reservation.getBundle();
            reservation.setState("cancelled");
            reservationRepository.save(reservation);
            List<Flight> flightsWithSameBundle = flightRepository.findByBundle(bundle);
            for (Flight flight : flightsWithSameBundle) {
                flight.setState("cancelled");
                flightRepository.save(flight);
            }
        }
    }

    
    /** 
     * @param id
     */
    public void cancelReservationsById(String id) {
        Optional<Reservation> optionalReservation  = getReservationByReservationNumber(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setState("cancelled");
            reservationRepository.save(reservation);
            sendCancellationEmail(reservation.getUser(), "reservation");
            List<Flight> flightsWithSameBundle = flightRepository.findByBundle(reservation.getBundle());
            for (Flight flight : flightsWithSameBundle) {
                flight.setState("cancelled");
                flightRepository.save(flight);
                sendCancellationEmail(reservation.getUser(), "flight");
            }
        } else {
            throw new RuntimeException("Reservation was not found");
        }
    }

    
    /** 
     * @param id
     * @param reservationDetails
     * @return 'Reservation'
     */
    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        // Update reservation details
        reservation.setUser(reservationDetails.getUser());
        reservation.setHotel(reservationDetails.getHotel());
        reservation.setDateStart(reservationDetails.getDateStart());
        reservation.setDateEnd(reservationDetails.getDateEnd());
        reservation.setRoomType(reservationDetails.getRoomType());
        reservation.setReservationNumber(reservationDetails.getReservationNumber());
        reservation.setLocation(reservationDetails.getLocation());

        return reservationRepository.save(reservation);
    }

    
    /** 
     * @param id
     */
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    
    /** 
     * @param userId
     * @param Type
     */
    private void sendCancellationEmail(Long userId, String Type) {
        try {
            // Retrieve user by userId
            Optional<User> optionalUser = userService.getUserById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String userEmail = user.getEmail();    
                // Create MimeMessage
                MimeMessage message = emailSender.createMimeMessage();
                if (Type == "flight") {
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(userEmail);
                    helper.setSubject("Flight Reservation Cancellation");
                    helper.setText("Your flight reservation has been cancelled.");
                } else {
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(userEmail);
                    helper.setSubject("Hotel Reservation Cancellation");
                    helper.setText("Your hotel reservation has been cancelled.");
                }        
                // Send email
                emailSender.send(message);
            } else {
                throw new RuntimeException("User with id " + userId + " not found.");
            }
            
        } catch (MessagingException ex) {
            // Handle exceptions
            ex.printStackTrace();
        }
    }
}

