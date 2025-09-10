package SpectraSystems.Nexus.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import SpectraSystems.Nexus.models.Provider;
import SpectraSystems.Nexus.models.Reservation;
import SpectraSystems.Nexus.models.Type;
import SpectraSystems.Nexus.models.User;
import SpectraSystems.Nexus.repositories.ProviderRepository;
import SpectraSystems.Nexus.services.ReservationService;
import SpectraSystems.Nexus.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.http.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/nexus/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final RestTemplate restTemplate;
    private static final String HOTEL_USER_ID = "65f9310acfb50244b4e886b0";
    private static final SimpleDateFormat API_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class); // use for logging stuff if needed

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    public ReservationController(ReservationService reservationService, RestTemplate restTemplate, UserService userService) {
        this.reservationService = reservationService;
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    
    /** 
     * @return a 'ResponseEntity<List<Reservation>>'
     */
    // Endpoint to retrieve all reservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    
    /** 
     * @param userId
     * @return a 'ResponseEntity<List<Reservation>>'
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getAllReservationsByUserId(@PathVariable Long userId) {
        List<Reservation> Reservations = reservationService.getAllReservationsByUserId(userId);
        return new ResponseEntity<>(Reservations, HttpStatus.OK);
    }

    
    /** 
     * @param id
     * @return a 'ResponseEntity<Reservation>'
     */
    // Endpoint to retrieve a reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") Long id) {
        return reservationService.getReservationById(id)
                .map(reservation -> new ResponseEntity<>(reservation, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    
    /** 
     * @param "providerId"
     * @param providerId
     * @param reservation
     * @return a 'ResponseEntity<Reservation>'
     */
    // Endpoint to create a new reservation
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestParam(value = "providerId", required = true) Long providerId, @RequestBody Reservation reservation) {
        if (providerId == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.badRequest().body(null);
        // // Find the provider by ID
        // Optional<Provider> providerOptional = providerRepository.findById(providerId);
        // if (!providerOptional.isPresent()) {
        //     return ResponseEntity.notFound().build(); // Provider with ID not found
        // }
        // Provider provider = providerOptional.get();

        // // Construct the URL for creating reservation based on provider URL format
        // String apiUrl = provider.getProviderUrl() + "/create-reservation";

        // RestTemplate restTemplate = new RestTemplate();

        // // Prepare the request body
        // Map<String, Object> requestBody = new HashMap<>();
        // requestBody.put("hotelId", reservation.getHotelId());
        // requestBody.put("userId", HOTEL_USER_ID);
        // String formattedCheckin = API_DATE_FORMAT.format(reservation.getDateStart());
        // requestBody.put("checkin", formattedCheckin);
        // String formattedCheckout = API_DATE_FORMAT.format(reservation.getDateEnd());
        // requestBody.put("checkout", formattedCheckout);
        // requestBody.put("roomType", reservation.getRoomType());
        // requestBody.put("roomPrice", reservation.getPrice());
        // requestBody.put("guests", reservation.getGuests());
        // requestBody.put("stayDays", reservation.getTotalDays());
        // requestBody.put("totalPrice", reservation.getTotalPrice());

        // try {
        //     // Make a POST request to the provider's URL
        //     ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestBody, String.class);

        //     if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.CREATED) {
        //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        //     }

        //     // Parse the response to extract reservation ID
        //     ObjectMapper objectMapper = new ObjectMapper();
        //     JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
        //     String reservationId = rootNode.get("_id").asText();

        //     reservation.setReservationNumber(reservationId);

        //     Reservation createdReservation = reservationService.createReservation(reservation);
        //     sendPurchaseConfirmationEmail(reservation.getUser());
        //     return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
        // } catch (RestClientResponseException e) {
        //     System.out.println("Error creating reservation with provider: " + provider.getProviderName() + ", URL: " + apiUrl);
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        // }
    }

    
    /** 
     * @param id
     * @param reservationDetails
     * @return a 'ResponseEntity<Reservation>'
     */
    // Endpoint to update an existing reservation
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Reservation> updateReservation(@PathVariable("id") Long id, @RequestBody Reservation reservationDetails) {
        Reservation updatedReservation = reservationService.updateReservation(id, reservationDetails);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }

    
    /** 
     * @param id
     * @return a 'ResponseEntity<Void>'
     */
    // Endpoint to delete a reservation by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
    /** 
     * @param hotelId
     * @return a 'ResponseEntity<Void>'
     */
    @PutMapping("/cancelHotel/{hotelId}")
    public ResponseEntity<Void> cancelReservationsByHotelId(@PathVariable String hotelId) {
        reservationService.cancelReservationsByHotelId(hotelId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    /** 
     * @param id
     * @return a 'ResponseEntity<Void>'
     */
    @PutMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelReservationById(@PathVariable String id) {
        reservationService.cancelReservationsById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    /** 
     * @param getHotels(
     * @return a 'ResponseEntity<List<Map<String, Object>>>'
     */
    @GetMapping(value = "/hotelsearch", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> getHotels(
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "check-in", required = false) String checkIn,
            @RequestParam(value = "check-out", required = false) String checkOut,
            @RequestParam(value = "guests", required = false) Integer guests) {

        List<Map<String, Object>> allHotels = new ArrayList<>();
        // Find all providers with type HOTEL
        // List<Provider> hotelProviders = providerRepository.findByType(Type.HOTEL);

        // for (Provider provider : hotelProviders) {
        //     String providerUrl = provider.getProviderUrl();
        //     // Construct the URL with query parameters
        //     String apiUrl = providerUrl + "/get-filtered-hotels?city=" + city;
        //     if (checkIn != null) {
        //         apiUrl += "&check-in=" + checkIn;
        //     }
        //     if (checkOut != null) {
        //         apiUrl += "&check-out=" + checkOut;
        //     }
        //     if (guests != null) {
        //         apiUrl += "&guests=" + guests;
        //     }

        //     // Make a request to the provider's URL
        //     try {
        //         ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(
        //                 apiUrl,
        //                 HttpMethod.GET,
        //                 null,
        //                 new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        //         );
        //         List<Map<String, Object>> providerHotels = responseEntity.getBody();
        //         // Add provider ID to each hotel
        //         for (Map<String, Object> hotel : providerHotels) {
        //             hotel.put("providerId", provider.getId());
        //         }
        //         allHotels.addAll(providerHotels);
        //     } catch (RestClientResponseException e) {
        //         // Handle potential exceptions during the request (optional)
        //         System.out.println("Error fetching hotels from provider: " + provider.getProviderName() + ", URL: " + apiUrl);
        //     }
        // }
        return ResponseEntity.ok().body(allHotels);
    }

    
    /** 
     * @param getHotelRoomById(
     * @return a 'ResponseEntity<Map<String, Object>>'
     */
    @GetMapping(value = "/roomsearch", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getHotelRoomById(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "providerId", required = true) Long providerId) {

        // Validate and handle missing providerId (optional)
        if (providerId == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.badRequest().body(null);
        // Find the provider by ID
        // Optional<Provider> providerOptional = providerRepository.findById(providerId);
        // if (!providerOptional.isPresent()) {
        //     return ResponseEntity.notFound().build(); // Provider with ID not found
        // }

        // Provider provider = providerOptional.get();
        // String apiUrl = provider.getProviderUrl() + "/get-hotel-by-id/" + id;
        // RestTemplate restTemplate = new RestTemplate();

        // try {
        //     ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
        //     Map<String, Object> hotel = response.getBody();
        //     // Check if the response contains hotel data
        //     if (hotel == null) {
        //         return ResponseEntity.notFound().build();
        //     }
        
        //     // Create a new mutable map to construct the response body
        //     Map<String, Object> responseBody = new HashMap<>(constructResponse(hotel));
            
        //     // Add the providerId field to the response body
        //     responseBody.put("providerId", providerId);
        
        //     return ResponseEntity.ok().body(responseBody);
        // } catch (RestClientResponseException e) {
        //     System.out.println("Error fetching hotel from provider: " + provider.getProviderName() + ", URL: " + apiUrl);
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        // }
        
        
    }

    
    /** 
     * @param hotel
     * @return a 'Map<String, Object>'
     */
    private Map<String, Object> constructResponse(Map<String, Object> hotel) {
        // Construct the JSON response with hotel id and rooms
        Map<String, Object> response = Map.of(
                "hotelId", hotel.get("_id"),                
                "name", hotel.get("name"),
                "rooms", hotel.get("rooms")
        );
        return response;
    }

    
    /** 
     * @return a 'List<String>'
     */
    @GetMapping("/cities")
    public List<String> getAllHotelCitiesFromOtherBackend() {
    List<String> allHotelCities = new ArrayList<>();
    
    // Find all providers with type HOTEL
    // // List<Provider> hotelProviders = providerRepository.findByType(Type.HOTEL);
    
    // // for (Provider provider : hotelProviders) {
    // //     String providerUrl = provider.getProviderUrl();
        
    // //     // Make a request to the provider's URL to get cities
    // //     try {
    // //         ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
    // //             providerUrl + "/get-cities",
    // //             HttpMethod.GET,
    // //             null,
    // //             new ParameterizedTypeReference<List<String>>() {}
    // //         );
    // //         List<String> providerHotelCities = responseEntity.getBody();
            
    // //         // Filter new hotel cities by comparing names with allHotelCities
    // //         for (String cityName : providerHotelCities) {
    // //             if (!allHotelCities.contains(cityName)) {
    // //                 allHotelCities.add(cityName);
    // //             }
    // //         }
    // //     } catch (RestClientResponseException e) {
    // //         System.out.println("Error fetching cities from provider: " + provider.getProviderName() + ", URL: " + providerUrl);
    // //     }
    //      }        
        return allHotelCities;
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
                helper.setSubject("Hotel Reservation Confirmation");
                helper.setText("Thank you for purchasing a hotel reservation with us. Your booking has been confirmed.");
        
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

