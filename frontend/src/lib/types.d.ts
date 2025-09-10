interface User {
  userId: string;
  email: string;
  firstName: string;
  iat: number;
  lastName: string;
  originCountry: string;
  passportNumber: string;
  role: "user" | "admin" | "enterprise";
  age: string;
  percentage: string;
  entryDate: string;
}

interface City {
  cityId: string;
  name: string;
}

interface Flight {
  flightId: number;
  originCityId: number;
  originCityName: string;
  destinationCityId: number;
  destinationCityName: string;
  departureDate: string;
  arrivalDate: string;
  touristPrice: number;
  businessPrice: number;
  detail: string;
  touristQuantity: number;
  businessQuantity: number;
  commentaries: Commentary[];
  rating: Rating;
  scale: Flight;
  returnFlight: Flight;
}

interface CompleteFlight {
  flightId: number;
  originCityId: number;
  originCityName: string;
  destinationCityId: number;
  destinationCityName: string;
  departureDate: string;
  arrivalDate: string;
  touristPrice: number;
  businessPrice: number;
  detail: string;
  touristQuantity: number;
  businessQuantity: number;
  touristCapacity: number;
  businessCapacity: number;
  state: number;
}

interface Ticket {
  ticketId: number;
  price: number;
  flightId: number;
  type: string;
  state: string;
  userId: number;
  userName: string;
}

interface Commentary {
  commentId: number;
  parentCommentId: number;
  userId: number;
  content: string;
  creationDate: string;
  path: string;
  flightId: number;
  userName: string;
  children: Commentary[];
}

interface Rating {
  average: number;
  count: number;
}
