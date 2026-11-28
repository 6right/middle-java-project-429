import "@typespec/http";
using TypeSpec.Http;

@service(#{ title: "Flight Booking API" })
namespace FlightBookingApi;

model City {
  code: string;
  name: string;
  country?: string;
}

model Money {
  amount: integer;
  currency: "RUB";
}

model Airline {
  code: string;
  name: string;
}

model Flight {
  @pattern("^[^/\\s]+$")
  id: string;
  flightNumber: string;
  airline: Airline;
  origin: City;
  destination: City;
  departureAt: utcDateTime;
  arrivalAt: utcDateTime;
  durationMinutes: integer;
  price: Money;
  seatsAvailable: integer;
}

model Passenger {
  @minLength(1)
  firstName: string;
  @minLength(1)
  lastName: string;
  dateOfBirth: plainDate;
  @minLength(1)
  documentNumber: string;
}

model Contact {
  @minLength(1)
  email: string;
  @minLength(1)
  phone: string;
}

model Booking {
  @pattern("^[A-Z0-9]{6}$")
  code: string;
  status: "confirmed" | "cancelled";
  flight: Flight;
  @minItems(1)
  passengers: Passenger[];
  contact: Contact;
  totalPrice: Money;
  createdAt: utcDateTime;
}

model CreateBookingRequest {
  @minLength(1)
  flightId: string;
  contact: Contact;
  @minItems(1)
  passengers: Passenger[];
}

model CancelBookingRequest {
  lastName: string;
}

model CancelledBooking extends Booking {
  status: "cancelled";
}

model BookingCreated {
  @statusCode statusCode: 201;
  @body booking: Booking;
}

@route("/api/cities")
interface Cities {
  @get list(): City[];
}

@route("/api/health")
interface Health {
  @get check(): { status: "ok" };
}

@route("/api/flights")
interface Flights {
  @get search(
    @query origin: string,
    @query destination: string,
    @query date: plainDate,
    @query passengers?: integer = 1
  ): Flight[] | ValidationError;

  @route("/{id}")
  @get getById(@path id: string): Flight | NotFoundError;
}

@route("/api/bookings")
interface Bookings {
  @post create(@body request: CreateBookingRequest): BookingCreated | ValidationError;

  @route("/{code}")
  @get getByCode(@path code: string, @query lastName?: string): Booking | NotFoundError;

  @route("/{code}/cancel")
  @post cancel(
    @path code: string,
    @body request: CancelBookingRequest
  ): CancelledBooking | NotFoundError;
}

@error
model ValidationError {
  @statusCode statusCode: 400;
  code: "validation_error";
  message: string;
}

@error
model NotFoundError {
  @statusCode statusCode: 404;
  code: "not_found";
  message: string;
}