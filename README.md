# Asteroid Alert

Asteroid Alert is a Spring Boot application that uses Apache Kafka to monitor and alert users about nearby asteroids. It fetches data from NASA's Near Earth Object API and processes it to determine potential threats.

## Features
- Fetches real-time asteroid data from NASA API
- Detects potentially hazardous asteroids
- Sends email alerts to subscribed users if an asteroid is near a certain threshold
- Stores all events in a MySQL database
- Uses Apache Kafka for event-driven processing

## Installation

To set up and run the project locally, follow these steps:

1. **Clone the repository:**
   ```sh
   git clone https://github.com/MeRitik/asteroid-alert.git
   ```
2. **Navigate to the project directory:**
   ```sh
   cd asteroid-alert
   ```
3. **Configure application properties:**
   - Set up database credentials in `application.properties`
   - Configure NASA API key
   - Set up email service credentials

4. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```

## Usage
- The application periodically fetches asteroid data from NASA API.
- If an asteroid is detected within the defined limit, an email alert is sent to users.
- All asteroid event data is stored in MySQL for record-keeping.

## Technologies Used
- **Backend:** Spring Boot
- **Messaging System:** Apache Kafka
- **Database:** MySQL
- **API Integration:** NASA Near Earth Object API
- **Email Service:** JavaMailSender (or any SMTP provider)
