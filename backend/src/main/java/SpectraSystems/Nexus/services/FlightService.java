package SpectraSystems.Nexus.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import SpectraSystems.Nexus.exceptions.ResourceNotFoundException;
import SpectraSystems.Nexus.models.Flight;
import SpectraSystems.Nexus.models.FlightPurchaseRequest;
import SpectraSystems.Nexus.models.Provider;
import SpectraSystems.Nexus.models.TicketPurchase;
import SpectraSystems.Nexus.models.Type;
import SpectraSystems.Nexus.models.externalFlight;
import SpectraSystems.Nexus.repositories.FlightRepository;
import SpectraSystems.Nexus.repositories.ProviderRepository;
import SpectraSystems.Nexus.repositories.TicketPurchaseRepository;
import SpectraSystems.Nexus.models.City;
import SpectraSystems.Nexus.models.Comment;

import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final RestTemplate restTemplate;
    private final TicketPurchaseRepository ticketPurchaseRepository;
    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private CommentService commentService; 

    @Autowired
    public FlightService(FlightRepository flightRepository, RestTemplate restTemplate, TicketPurchaseRepository ticketPurchaseRepository) {
        this.flightRepository = flightRepository;
        this.restTemplate = restTemplate;
        this.ticketPurchaseRepository = ticketPurchaseRepository;
    }

    
    /** 
     * @return 'List<Flight>'
     */
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    
    /** 
     * @param id
     * @return Optional<Flight>
     */
    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }
    
    
    /** 
     * @param userId
     * @return 'List<Flight>'
     */
    public List<Flight> getAllFlightsByUserId(Long userId) {
        return flightRepository.findByUserid(userId);
    }

    
    /** 
     * @param flight
     * @return 'Flight'
     */
    public Flight createFlight(Flight flight) {
        if (flight != null) {
            return flightRepository.save(flight);
        } else {
            return null;
        }
    }
    
    
    /** 
     * @param flightNumber
     * @return 'List<Flight>'
     */
    public List<Flight> getFlightsByFlightNumber(String flightNumber) {
        return flightRepository.findAllByFlightNumber(flightNumber);
    }

    
    /** 
     * @return 'List<externalFlight>'
     */
    public List<externalFlight> getAllFlightsFromOtherBackend() {
        List<externalFlight> allFlights = new ArrayList<>();
    
        // Find all providers with type FLIGHT
        // List<Provider> flightProviders = providerRepository.findByType(Type.AEROLINEA);
    
        // for (Provider provider : flightProviders) {
        //     String providerUrl = provider.getProviderUrl();
    
        //     // Construct the URL with query parameters (assuming no additional parameters needed)
        //     String apiUrl = providerUrl + "/get-all-flights";
    
        //     try {
        //         ResponseEntity<externalFlight[]> responseEntity = restTemplate.exchange(
        //                 apiUrl,
        //                 HttpMethod.GET,
        //                 null,
        //                 externalFlight[].class
        //         );
    
        //         externalFlight[] providerFlights = responseEntity.getBody();
    
        //         // Add provider ID to each flight
        //         for (externalFlight flight : providerFlights) {
        //             flight.setProviderId(provider.getId());
        //         }
    
        //         // Add flights from this provider to the list
        //         allFlights.addAll(Arrays.asList(providerFlights));
        //     } catch (RestClientResponseException e) {
        //         // Handle potential exceptions during the request (optional)
        //         System.out.println("Error fetching flights from provider: " + provider.getProviderName() + ", URL: " + apiUrl);
        //     }
        // }
    
        return allFlights;
    }
    

    
    /** 
     * @param originCity
     * @param destinationCity
     * @param departureDay
     * @param passengers
     * @return 'List<externalFlight>'
     */
    public List<externalFlight> getOneWayFlightsFromOtherBackend(
            Long originCity,
            Long destinationCity,
            String departureDay,
            int passengers
    ) {
        List<externalFlight> allFlights = new ArrayList<>(); // List to store all flights

        // Find all providers with type FLIGHT
        // List<Provider> flightProviders = providerRepository.findByType(Type.AEROLINEA);

        // for (Provider provider : flightProviders) {
        //     String providerUrl = provider.getProviderUrl();
        //     Long providerId = provider.getId();

        //     // Construct URL with query parameters for one-way flights
        //     UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(providerUrl + "/get-one-way-flights")
        //             .queryParam("originCity", originCity)
        //             .queryParam("destinationCity", destinationCity)
        //             .queryParam("departureDay", departureDay)
        //             .queryParam("passengers", passengers);

        //     ResponseEntity<externalFlight[]> responseEntity = restTemplate.exchange(
        //             builder.toUriString(),
        //             HttpMethod.GET,
        //             null,
        //             externalFlight[].class
        //     );

        //     externalFlight[] providerFlights = responseEntity.getBody();
        //     for (externalFlight flight : providerFlights) {
        //         flight.setProviderId(providerId);
        //     }
        //     allFlights.addAll(Arrays.asList(providerFlights)); // Add one-way flights

        //     // Construct URL with query parameters for scale flights
        //     builder = UriComponentsBuilder.fromUriString(providerUrl + "/scale-flights")
        //             .queryParam("originCity", originCity)
        //             .queryParam("destinationCity", destinationCity)
        //             .queryParam("departureDay", departureDay);

        //     responseEntity = restTemplate.exchange(
        //             builder.toUriString(),
        //             HttpMethod.GET,
        //             null,
        //             externalFlight[].class
        //     );

        //     externalFlight[] scaleFlights = responseEntity.getBody();
        //     for (externalFlight flight : scaleFlights) {
        //         flight.setProviderId(providerId);
        //     }
        //     for (externalFlight scaleFlight : scaleFlights) {
        //         // Format arrival date for secondary flight search
        //         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //         dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        //         String formattedDepartureDay = dateFormat.format(scaleFlight.getArrivalDate());

        //         // 3. Connecting Flights for Scale Flights (from same provider)
        //         builder = UriComponentsBuilder.fromUriString(providerUrl + "/get-one-way-flights")
        //                 .queryParam("originCity", scaleFlight.getDestinationCityId())
        //                 .queryParam("destinationCity", destinationCity)
        //                 .queryParam("departureDay", formattedDepartureDay)
        //                 .queryParam("passengers", passengers);

        //         ResponseEntity<externalFlight[]> secondaryResponseEntity = restTemplate.exchange(
        //                 builder.toUriString(),
        //                 HttpMethod.GET,
        //                 null,
        //                 externalFlight[].class
        //         );

        //         externalFlight[] secondaryFlights = secondaryResponseEntity.getBody();
        //         for (externalFlight flight : secondaryFlights) {
        //             flight.setProviderId(providerId);
        //         }
        //         if (secondaryFlights != null && secondaryFlights.length > 0) {
        //             // Set connecting flight for the scale flight (assuming a setter exists)
        //             scaleFlight.setScale(secondaryFlights[0]);

        //             // Add processed scale flight to the list
        //             allFlights.add(scaleFlight);
        //         }
        //     }
        // }

        return allFlights;
    }


    
    /** 
     * @return 'List<City>'
     */
    public List<City> getAllCitiesFromOtherBackend() {
        List<City> allCities = new ArrayList<>();
    
        // Find all providers with type AEROLINEA
        // List<Provider> aerolíneaProviders = providerRepository.findByType(Type.AEROLINEA);
        // for (Provider provider : aerolíneaProviders) {
        //     String providerUrl = provider.getProviderUrl();
            
        //     // Make a request to the provider's URL to get cities
        //     try {
        //         ResponseEntity<List<City>> responseEntity = restTemplate.exchange(
        //             providerUrl + "/get-cities", 
        //             HttpMethod.GET,
        //             null,
        //             new ParameterizedTypeReference<List<City>>() {}
        //         );
        //         List<City> providerCities = responseEntity.getBody();
        //         for (City city : providerCities) {
        //             if (!allCities.contains(city)) {
        //                 allCities.add(city);
        //             }
        //         }
        //     } catch (RestClientResponseException e) {
        //         // Handle potential exceptions during the request
        //         System.out.println("Error fetching cities from provider: " + provider.getProviderName() + ", URL: " + providerUrl);
        //     }
        // }
        
        return allCities;
    }

    
    /** 
     * @param amount
     * @param method
     * @param providerId
     * @param purchaseRequest
     * @throws HttpServerErrorException
     * @throws JsonProcessingException
     */
    public void purchaseFlight(int amount, String method, Long providerId, FlightPurchaseRequest purchaseRequest) throws HttpServerErrorException, JsonProcessingException  {
        // Set discount and user_id
        long userIdPurchase = 1;

        // if (providerId == null) {
        //     throw new RuntimeException("Missing provider ID for flight purchase"); // Or provide a more specific error message
        // }
    
        // // Find the provider by ID
        // Optional<Provider> providerOptional = providerRepository.findById(providerId);
        // if (!providerOptional.isPresent()) {
        //     throw new RuntimeException("Provider not found for flight purchase with ID: " + providerId); // Handle provider not found
        // }
        // Provider provider = providerOptional.get();
        // int discount = (int) (provider.getPercentageDiscount() * 100);
        // // Construct the URL for purchasing a flight based on provider URL format (assuming a POST request to a specific endpoint)
        // String apiUrl = provider.getProviderUrl() + "/purchase/" + amount + '/' + method + '/' + discount;

        // // Create request body
        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);

        // // Construct the URL to retrieve available tickets
        // String ticketsUrl = provider.getProviderUrl() + "/availabletickets/" + purchaseRequest.getFlightId() + "/" + purchaseRequest.getType();

        // // Make a GET request to retrieve available tickets
        // ResponseEntity<String> ticketsResponse = restTemplate.getForEntity(ticketsUrl, String.class);

        // // Parse the response to extract the ticket_id
        // ObjectMapper TobjectMapper = new ObjectMapper();
        // JsonNode ticketsResponseBody = TobjectMapper.readTree(ticketsResponse.getBody());
        // // Extract ticket_id from the first ticket

        // List<Long> ticketIds = new ArrayList<>();
        // if (ticketsResponseBody.isArray()) {
        //     for (JsonNode ticketNode : ticketsResponseBody) {
        //         Long ticketId = ticketNode.get("ticket_id").asLong();
        //         ticketIds.add(ticketId);
        //     }
        // } else {
        //     throw new RuntimeException("Unexpected response format: Not an array");
        // }
        // Long firstTicketId = ticketIds.get(0);
        
        // logger.info("TicketId: {}", firstTicketId);

        // // Create the body as JSON
        // String requestBody = "{"
        //         + "\"user_id\": \"" + userIdPurchase + "\","
        //         + "\"flight_id\": \"" + purchaseRequest.getFlightId() + "\","
        //         + "\"state\": \"" + "active" + "\","
        //         + "\"type\": \"" + purchaseRequest.getType() + "\","
        //         + "\"ticket_id\": \"" + firstTicketId + "\""
        //         + "}";

        // logger.info("Sending HTTP request to: {}", apiUrl);
        // logger.info("Request body: {}", requestBody);

        // HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

        // logger.info("Received HTTP response with status code: {}", response.getStatusCode());
        // logger.debug("Response body: {}", response.getBody());
        
        // // Check if the purchase was successful
        // if (response.getStatusCode() == HttpStatus.OK) {
        //     // Check if the response body contains an error message
        //     ObjectMapper EobjectMapper = new ObjectMapper();
        //     JsonNode EresponseBody = EobjectMapper.readTree(response.getBody());
            
        //     if (EresponseBody.has("error")) {
        //         String errorMessage = EresponseBody.get("error").asText();
        //         // Handle the error case appropriately
        //         logger.error("Error received from the external service: {}", errorMessage);
        //         // You can throw an exception or handle it based on your application's requirements
        //         // For example:
        //         throw new RuntimeException("Error received from the external service: " + errorMessage);
        //     }
        //     // Create and save Flight object
        //     for (int i = 0; i < amount; i++) {
        //         Flight flight = new Flight(firstTicketId ,purchaseRequest.getUserId(), purchaseRequest.getFlightId().toString(), purchaseRequest.getDepartureDate(), purchaseRequest.getDepartureLocation(), purchaseRequest.getArrivalLocation(), purchaseRequest.getReturnDate(), purchaseRequest.getType(), purchaseRequest.getPrice(), purchaseRequest.getBundle());
        //         flight.setProviderId(providerId);
        //         logger.info("Sending HTTP request to: {}", flight.getId());
        //         flightRepository.save(flight);
        //     }

        //     // Convert the response body to a JSON array
        //     ObjectMapper objectMapper = new ObjectMapper();
        //     JsonNode responseBody = objectMapper.readTree(response.getBody());
            
        //     int size = responseBody.size();
        //     int startIndex = size - amount; // Index to start extracting tickets
            
        //     // Extract the last 'amount' tickets
        //     for (int i = startIndex; i < size - 1; i++) {
        //         JsonNode ticketNode = responseBody.get(i);
        //         // Check if ticketNode is null before accessing its properties
        //         if (ticketNode != null) {
        //             Long ticketId = ticketNode.get("ticket_id").asLong();
        //             Long ticketUserId = ticketNode.get("user_id").asLong();
        //             // Create TicketPurchase object
        //             TicketPurchase ticketPurchase = new TicketPurchase();
        //             ticketPurchase.setTicketId(ticketId.intValue());
        //             ticketPurchase.setUserId(ticketUserId.intValue());
        //             ticketPurchase.setFlightId(purchaseRequest.getFlightId().intValue()); // Assuming flightId is available here
        //             // Set type and state if needed
                    
        //             // Save TicketPurchase object to database
        //             ticketPurchaseRepository.save(ticketPurchase);
        //         } else {
        //             break;
        //         }
        //     }

            
        // } else {
        //     throw new RuntimeException("Purchase was unsuccessful");
        // }
    }

    
    /** 
     * @param id
     * @param flightDetails
     * @return 'Flight'
     */
    public Flight updateFlight(Long id, Flight flightDetails) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        // Update flight details
        flight.setFlightNumber(flightDetails.getFlightNumber());
        flight.setDepartureLocation(flightDetails.getDepartureLocation());
        flight.setArrivalLocation(flightDetails.getArrivalLocation());
        flight.setReturnDate(flightDetails.getReturnDate());

        return flightRepository.save(flight);
    }

    
    /** 
     * @param id
     */
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }
}

