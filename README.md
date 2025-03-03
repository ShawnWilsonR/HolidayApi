**Application Overview**

This Spring Boot application connects to the https://date.nager.at/Api API to fetch holiday data and provides the following functionality:

1. Last 3 holidays for a given country.
2. Number of public holidays (excluding weekends) for a list of countries.
3. A deduplicated list of common holidays between two countries.

**Documentation on Running the Application**

1. Clone the repository (or create a new project).
   
2. Run the Spring Boot application:
If you're using an IDE (like IntelliJ IDEA or Eclipse), simply run the **AssignmentApplication class**.
Or use the terminal: **mvn spring-boot:run**

3. Test the endpoints using a tool like Postman or your browser:

**Endpoint 1** - Get the last 3 holidays for a country:

**GET /country/last3holidays?countryCode=?**

**Example**: **GET** http://localhost:8080/country/last3Holidays?countryCode=US

**Endpoint 2** - Get the number of public holidays (not on weekends) for a list of countries:

**GET /country/publicholidays?year=?&countryCodes=?**

**Example**: **GET** http://localhost:8080/country/publicHolidays?year=2025&countryCodes=US,NL,AT

**Endpoint 3** - Get the common holidays between two countries:

**GET /country/commonholidays?year=?&countryCode1=?&countryCode2=?**

**Example**: **GET** http://localhost:8080/countries/commonHolidays?year=2025&countryCode1=US&countryCode2=NL

Also **Swagger UI** can be used to verify endpoints - http://localhost:8080/swagger-holiday-api.html

API related docs can be checked at http://localhost:8080/holiday-api-docs
