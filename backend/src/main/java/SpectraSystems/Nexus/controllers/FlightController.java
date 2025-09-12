package SpectraSystems.Nexus.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import SpectraSystems.Nexus.models.City;
import SpectraSystems.Nexus.models.Flight;
import SpectraSystems.Nexus.models.FlightPurchaseRequest;
import SpectraSystems.Nexus.models.Reservation;
import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.models.externalFlight;
import SpectraSystems.Nexus.repositories.FlightRepository;
import SpectraSystems.Nexus.repositories.ReservationRepository;
import SpectraSystems.Nexus.services.FlightService;
import SpectraSystems.Nexus.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/nexus/flights")
public class FlightController {

    private final FlightService flightService;
    private final FlightRepository flightRepository;
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private static final String Cancelled = "cancelled";


    // private static final Logger logger = LoggerFactory.getLogger(FlightController.class); Use for loggin errors lmao

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    public FlightController(FlightRepository flightRepository, FlightService flightService, ReservationRepository reservationRepository, UserService userService) {
        this.flightRepository = flightRepository;
        this.flightService = flightService;
        this.reservationRepository = reservationRepository;
        this.userService = userService;
    }

    
    /** 
     * @return a 'ResponseEntity<List<Flight>>'
     */
    // Endpoint to retrieve all flights
    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    
    /** 
     * @param id
     * @return a 'ResponseEntity<Flight>'
     */
    // Endpoint to retrieve a flight by ID
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable("id") Long id) {
        return flightService.getFlightById(id)
                .map(flight -> new ResponseEntity<>(flight, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    
    /** 
     * @param userId
     * @return a 'ResponseEntity<List<Flight>>'
     */
    // Endpoint to retrieve all flights by userID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Flight>> getAllFlightsByUserId(@PathVariable Long userId) {
        List<Flight> flights = flightService.getAllFlightsByUserId(userId);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    
    /** 
     * @return a 'ResponseEntity<List<externalFlight>>'
     */
    @GetMapping("/avianca/flights")
    public ResponseEntity<List<externalFlight>> getAllFlightsFromOtherBackend() {
        List<externalFlight> flights = flightService.getAllFlightsFromOtherBackend();
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    
    /** 
     * @return a 'ResponseEntity<List<externalFlight>>'
     */
    @GetMapping("/avianca/one-way-flights")
    public ResponseEntity<List<externalFlight>> getOneWayFlightsFromOtherBackend(
        @RequestParam(value = "originCity") Long originCityId,
        @RequestParam(value = "destinationCity") Long destinationCityId,
        @RequestParam(value = "departureDay") String departureDay,
        @RequestParam(value = "passengers") int passengers
    ) {
        List<externalFlight> flights = flightService.getOneWayFlightsFromOtherBackend(originCityId, destinationCityId, departureDay, passengers);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    
    /** 
     * @return a 'ResponseEntity<List<externalFlight>>' 
     */
    @GetMapping("/avianca/round-trip-flights")
    public ResponseEntity<List<externalFlight>> getRoundTripFlights(
            @RequestParam(value = "originCity") Long originCityId,
            @RequestParam(value = "destinationCity") Long destinationCityId,
            @RequestParam(value = "departureDay") String departureDay,
            @RequestParam(value = "returnDay") String returnDay,
            @RequestParam(value = "passengers") int passengers
    ) {
        // Get outbound flights
        List<externalFlight> outboundFlights = flightService.getOneWayFlightsFromOtherBackend(
                originCityId, destinationCityId, departureDay, passengers);

        // Get return flights (reverse origin and destination)
        List<externalFlight> returnFlights = flightService.getOneWayFlightsFromOtherBackend(
                destinationCityId, originCityId, returnDay, passengers);

        

        // Add return flights to each outbound flight
        for (externalFlight outboundFlight : outboundFlights) {
            for (externalFlight returnFlight : returnFlights) {
                if (matchFlights(outboundFlight, returnFlight)) {
                    outboundFlight.setReturnFlight(returnFlight);
                    break;
                }
            }
        }
        return new ResponseEntity<>(outboundFlights, HttpStatus.OK);
    }

    
    /** 
     * @param outboundFlight
     * @param returnFlight
     * @return a 'boolean' if the flights match
     */
    // Method to check if flights match based on origin, destination cities, and scales
    private boolean matchFlights(externalFlight outboundFlight, externalFlight returnFlight) {
        if (outboundFlight.getScale() != null && returnFlight.getScale() != null) {
            return outboundFlight.getScale().getDestinationCityId().equals(returnFlight.getOriginCityId()) &&
                    outboundFlight.getOriginCityId().equals(returnFlight.getScale().getDestinationCityId());
        } else if (outboundFlight.getScale() != null) {
            return outboundFlight.getScale().getDestinationCityId().equals(returnFlight.getOriginCityId()) &&
                    outboundFlight.getOriginCityId().equals(returnFlight.getDestinationCityId());
        } else {
            return outboundFlight.getDestinationCityId().equals(returnFlight.getOriginCityId()) &&
                    outboundFlight.getOriginCityId().equals(returnFlight.getDestinationCityId());
        }
    }

    
    /** 
     * @return the 'ResponseEntity<List<City>>'
     */
    @GetMapping("/avianca/cities")
    public ResponseEntity<List<City>> getAllCitiesFromOtherBackend() {
        
        List<City> cities = flightService.getAllCitiesFromOtherBackend();
        if(true){
            cities = flightService.getAllCitiesFromOtherBackend();
        }
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    
    /** 
     * @param flight
     * @return the 'ResponseEntity<Flight>'
     */
    // Endpoint to create a new flight
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        Flight createdFlight = flightService.createFlight(flight);
        return new ResponseEntity<>(createdFlight, HttpStatus.CREATED);
        if (true) {
            Flight createdFlight = flightService.createFlight(flight);            
        }
    }

    
    /** 
     * @return the 'ResponseEntity<Map<String, String>>'
     * @throws JsonProcessingException
     */
    // Endpoint for purchasing a flight
    @PostMapping("/purchase/{amount}/{method}/{providerId}")
    public ResponseEntity<Map<String, String>> purchaseFlight(
            @PathVariable int amount,
            @PathVariable String method,
            @PathVariable Long providerId,
            @RequestBody FlightPurchaseRequest purchaseRequest
    ) throws JsonProcessingException {
        flightService.purchaseFlight(amount, method, providerId, purchaseRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Flight purchased successfully.");
        // Send email notification to the user
        sendPurchaseConfirmationEmail(purchaseRequest.getUser_id());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    
    /** 
     * @param id
     * @param flightDetails
     * @return the 'ResponseEntity<Flight>'
     */
    // Endpoint to update an existing flight
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Flight> updateFlight(@PathVariable("id") Long id, @RequestBody Flight flightDetails) {
        Flight updatedFlight = flightService.updateFlight(id, flightDetails);
        return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
    }

    
    /** 
     * @param flightNumber
     * @return the 'ResponseEntity<List<Flight>>'
     */
    @PutMapping("/deactivate/{flightNumber}")
    public ResponseEntity<List<Flight>> deactivateFlightsByFlightNumber(@PathVariable String flightNumber) {
        List<Flight> flights = flightService.getFlightsByFlightNumber(flightNumber);
        if (!flights.isEmpty()) {
            for (Flight flight : flights) {
                flight.setState(Cancelled);
                flightService.updateFlight(flight.getId(), flight);
                String bundle = flight.getBundle();
            
                List<Flight> flightsWithSameBundle = flightRepository.findByBundle(bundle);
                
                for (Flight flightBundle : flightsWithSameBundle) {
                    flightBundle.setState(Cancelled);
                    flightRepository.save(flightBundle);
                    sendCancellationEmail(flightBundle.getUser(), "flight");
                }
                List<Reservation> reservationsWithSameBundle = reservationRepository.findByBundle(bundle);
                for (Reservation reservation : reservationsWithSameBundle) {
                    reservation.setState(Cancelled);
                    reservationRepository.save(reservation);
                    sendCancellationEmail(reservation.getUser(), "reservation");
                }
            }
            return new ResponseEntity<>(flights, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    /** 
     * @param id
     * @return the 'ResponseEntity<List<Flight>>'
     */
    @PutMapping("/deactivateTicket/{id}")
    public ResponseEntity<List<Flight>> deactivateFlightsById(@PathVariable Long id) {
        Optional<Flight> optionalFlight = flightService.getFlightById(id);
        if(optionalFlight.isPresent()){
            Flight flight = optionalFlight.get();
            flight.setState(Cancelled);
            flightRepository.save(flight);
            String bundle = flight.getBundle();
            
            List<Flight> flightsWithSameBundle = flightRepository.findByBundle(bundle);
            
            for (Flight flights : flightsWithSameBundle) {
                flights.setState(Cancelled);
                flightRepository.save(flights);
                sendCancellationEmail(flights.getUser(), "flight");
            }
            List<Reservation> reservationsWithSameBundle = reservationRepository.findByBundle(bundle);
            for (Reservation reservation : reservationsWithSameBundle) {
                reservation.setState(Cancelled);
                reservationRepository.save(reservation);
                sendCancellationEmail(reservation.getUser(), "reservation");
            }
            
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    /** 
     * @param id
     * @return the 'ResponseEntity<Void>'
     */
    // Endpoint to delete a flight by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteFlight(@PathVariable("id") Long id) {
        flightService.deleteFlight(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    
    /** 
     * @param userId
     */
    private void sendPurchaseConfirmationEmail(Long userId) {
        try {
            // Retrieve user by userId
            Optional<User> optionalUser = userService.getUserById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String userEmail = user.getEmail();
                // Create MimeMessage
                MimeMessage message = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(userEmail);
                helper.setSubject("Flight Purchase Confirmation");
                helper.setText("Thank you for purchasing a flight with us. Your booking has been confirmed.");
        
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
