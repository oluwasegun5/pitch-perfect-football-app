# Testing Strategy - Hexagonal Architecture

## Overview

This document outlines the testing strategy for the Pitch Perfect Football App, designed according to hexagonal architecture principles. The testing approach leverages the architecture's separation of concerns to enable comprehensive, isolated testing of each layer.

## Testing Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                      End-to-End Tests                           │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Integration Tests                           │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│  ┌─────────────────┐      ┌─────────────────┐      ┌──────────┐ │
│  │ Adapter Tests   │      │ Application     │      │  Domain  │ │
│  │ (Infrastructure)│      │ Service Tests   │      │  Tests   │ │
│  └─────────────────┘      └─────────────────┘      └──────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Test Types and Layers

### 1. Domain Layer Tests

#### Unit Tests for Domain Models
- **Focus**: Core business entities and value objects
- **Tools**: JUnit, AssertJ
- **Approach**: Test domain model behavior, invariants, and business rules
- **Mocking**: Minimal to none (pure domain logic)

```java
@Test
void matchShouldNotAllowNegativeScores() {
    Match match = new Match(homeTeam, awayTeam, venue, startTime);
    
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> match.updateScore(-1, 0));
}
```

#### Unit Tests for Domain Services
- **Focus**: Core business logic and rules
- **Tools**: JUnit, AssertJ
- **Approach**: Test business operations and domain rules
- **Mocking**: Only for dependencies on other domain services

```java
@Test
void matchShouldTransitionToCompletedStateAfterFullTime() {
    Match match = new Match(homeTeam, awayTeam, venue, startTime);
    match.start();
    
    matchDomainService.completeMatch(match);
    
    assertThat(match.getStatus()).isEqualTo(MatchStatus.COMPLETED);
}
```

### 2. Application Layer Tests

#### Unit Tests for Application Services
- **Focus**: Use case orchestration and coordination
- **Tools**: JUnit, Mockito, AssertJ
- **Approach**: Test application service logic with mocked ports
- **Mocking**: Mock all outbound ports (repositories, external services)

```java
@Test
void shouldCreateMatchAndNotifySubscribers() {
    // Arrange
    CreateMatchCommand command = new CreateMatchCommand(homeTeamId, awayTeamId, venue, startTime);
    when(teamRepository.findById(homeTeamId)).thenReturn(Optional.of(homeTeam));
    when(teamRepository.findById(awayTeamId)).thenReturn(Optional.of(awayTeam));
    when(matchRepository.save(any(Match.class))).thenReturn(match);
    
    // Act
    MatchDto result = matchService.createMatch(command);
    
    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(match.getId());
    verify(notificationService).notifyMatchCreated(any(Match.class));
}
```

#### Integration Tests for Application Services
- **Focus**: Integration between application services
- **Tools**: JUnit, Spring Test
- **Approach**: Test interaction between multiple application services
- **Mocking**: Mock external dependencies, use real application services

```java
@Test
void shouldCreateMatchAndUpdateStatistics() {
    // Arrange
    CreateMatchCommand command = new CreateMatchCommand(homeTeamId, awayTeamId, venue, startTime);
    
    // Act
    MatchDto result = matchService.createMatch(command);
    
    // Assert
    assertThat(result).isNotNull();
    assertThat(statisticsService.getStatisticsForMatch(result.getId())).isNotNull();
}
```

### 3. Infrastructure Layer Tests

#### Unit Tests for Adapters
- **Focus**: Individual adapter implementations
- **Tools**: JUnit, Mockito, AssertJ
- **Approach**: Test adapter-specific logic
- **Mocking**: Mock external systems (database, external APIs)

```java
@Test
void shouldConvertEntityToDomainModel() {
    // Arrange
    MatchEntity matchEntity = new MatchEntity();
    matchEntity.setId(UUID.randomUUID());
    matchEntity.setHomeTeamId(UUID.randomUUID());
    matchEntity.setAwayTeamId(UUID.randomUUID());
    matchEntity.setVenue("Test Stadium");
    matchEntity.setStartTime(LocalDateTime.now());
    matchEntity.setStatus(MatchStatus.SCHEDULED.name());
    
    when(teamRepository.findById(matchEntity.getHomeTeamId())).thenReturn(Optional.of(homeTeam));
    when(teamRepository.findById(matchEntity.getAwayTeamId())).thenReturn(Optional.of(awayTeam));
    
    // Act
    Match match = matchRepositoryAdapter.toDomainModel(matchEntity);
    
    // Assert
    assertThat(match).isNotNull();
    assertThat(match.getId()).isEqualTo(matchEntity.getId());
    assertThat(match.getHomeTeam()).isEqualTo(homeTeam);
    assertThat(match.getAwayTeam()).isEqualTo(awayTeam);
    assertThat(match.getVenue()).isEqualTo(matchEntity.getVenue());
    assertThat(match.getStartTime()).isEqualTo(matchEntity.getStartTime());
    assertThat(match.getStatus()).isEqualTo(MatchStatus.SCHEDULED);
}
```

#### Integration Tests for Adapters
- **Focus**: Adapter interaction with external systems
- **Tools**: JUnit, Spring Test, Testcontainers
- **Approach**: Test adapter with real external systems
- **Mocking**: Minimal, use test containers for databases, etc.

```java
@Test
void shouldSaveAndRetrieveMatch() {
    // Arrange
    Match match = new Match(homeTeam, awayTeam, "Test Stadium", LocalDateTime.now());
    
    // Act
    UUID savedId = matchRepositoryAdapter.save(match);
    Optional<Match> retrieved = matchRepositoryAdapter.findById(savedId);
    
    // Assert
    assertThat(retrieved).isPresent();
    assertThat(retrieved.get().getId()).isEqualTo(savedId);
    assertThat(retrieved.get().getHomeTeam()).isEqualTo(homeTeam);
    assertThat(retrieved.get().getAwayTeam()).isEqualTo(awayTeam);
}
```

#### Controller Tests
- **Focus**: REST and WebSocket controllers
- **Tools**: Spring MVC Test, WebTestClient
- **Approach**: Test HTTP endpoints and WebSocket handlers
- **Mocking**: Mock application services

```java
@Test
void shouldReturnMatchDetails() throws Exception {
    // Arrange
    UUID matchId = UUID.randomUUID();
    MatchDto matchDto = new MatchDto(matchId, homeTeamDto, awayTeamDto, "Test Stadium", 
                                    LocalDateTime.now(), MatchStatus.SCHEDULED);
    when(matchService.getMatchById(matchId)).thenReturn(matchDto);
    
    // Act & Assert
    mockMvc.perform(get("/api/matches/{id}", matchId))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(matchId.toString()))
           .andExpect(jsonPath("$.venue").value("Test Stadium"))
           .andExpect(jsonPath("$.status").value("SCHEDULED"));
}
```

### 4. Integration Tests

#### API Integration Tests
- **Focus**: End-to-end API flows
- **Tools**: Spring Boot Test, RestAssured, Testcontainers
- **Approach**: Test complete API flows with real database
- **Mocking**: External services only

```java
@Test
void shouldCreateMatchAndRetrieveIt() {
    // Arrange
    CreateMatchRequest request = new CreateMatchRequest(homeTeamId, awayTeamId, 
                                                      "Test Stadium", LocalDateTime.now());
    
    // Act
    UUID matchId = given()
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post("/api/matches")
        .then()
        .statusCode(201)
        .extract().as(MatchResponse.class).getId();
    
    // Assert
    given()
        .when()
        .get("/api/matches/{id}", matchId)
        .then()
        .statusCode(200)
        .body("id", equalTo(matchId.toString()))
        .body("venue", equalTo("Test Stadium"));
}
```

#### WebSocket Integration Tests
- **Focus**: WebSocket communication
- **Tools**: Spring Boot Test, STOMP client
- **Approach**: Test WebSocket subscriptions and messages
- **Mocking**: Minimal

```java
@Test
void shouldReceiveMatchUpdates() throws Exception {
    // Arrange
    StompSession session = connectToWebSocket();
    BlockingQueue<MatchUpdateMessage> updates = new LinkedBlockingQueue<>();
    
    session.subscribe("/topic/matches/" + matchId, new StompFrameHandler() {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return MatchUpdateMessage.class;
        }
        
        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            updates.add((MatchUpdateMessage) payload);
        }
    });
    
    // Act
    matchService.updateScore(matchId, 1, 0);
    
    // Assert
    MatchUpdateMessage update = updates.poll(5, TimeUnit.SECONDS);
    assertThat(update).isNotNull();
    assertThat(update.getType()).isEqualTo("SCORE_UPDATE");
    assertThat(update.getData().get("homeScore")).isEqualTo(1);
    assertThat(update.getData().get("awayScore")).isEqualTo(0);
}
```

### 5. End-to-End Tests

#### UI End-to-End Tests
- **Focus**: Complete user flows
- **Tools**: Selenium, Cypress
- **Approach**: Test user interactions through UI
- **Mocking**: Minimal, mostly real systems

```javascript
describe('Match Details Page', () => {
    it('should display match details and live updates', () => {
        // Arrange
        cy.login('user', 'password');
        
        // Act
        cy.visit(`/matches/${matchId}`);
        
        // Assert
        cy.get('[data-testid="match-venue"]').should('contain', 'Test Stadium');
        cy.get('[data-testid="match-status"]').should('contain', 'SCHEDULED');
        
        // Simulate score update from backend
        cy.window().then((win) => {
            win.dispatchEvent(new CustomEvent('score-update', {
                detail: { homeScore: 1, awayScore: 0 }
            }));
        });
        
        // Assert update is reflected
        cy.get('[data-testid="match-score"]').should('contain', '1 - 0');
    });
});
```

#### Performance Tests
- **Focus**: System performance under load
- **Tools**: JMeter, Gatling
- **Approach**: Simulate high traffic and measure response times
- **Environment**: Staging environment

```scala
val scn = scenario("Match Updates Scenario")
  .exec(http("Get Match Details")
    .get("/api/matches/${matchId}")
    .check(status.is(200)))
  .pause(1)
  .exec(ws("Connect to WebSocket")
    .connect("/ws"))
  .pause(1)
  .exec(ws("Subscribe to Match Updates")
    .sendText("""{"destination":"/topic/matches/${matchId}"}"""))
  .pause(5)
  .exec(ws("Close WebSocket")
    .close())

setUp(
  scn.inject(
    rampUsers(1000).during(60.seconds)
  )
).protocols(httpProtocol)
```

## Test Data Management

### Test Data Strategies

1. **In-Memory Test Data**
   - Used for unit tests
   - Created programmatically for each test
   - Isolated between tests

2. **Test Containers**
   - Used for integration tests
   - Real database instances in containers
   - Fresh instance for each test class or suite

3. **Test Data Builders**
   - Fluent API for creating test data
   - Ensures valid domain objects
   - Simplifies test setup

```java
public class MatchBuilder {
    private Team homeTeam = TeamBuilder.aTeam().build();
    private Team awayTeam = TeamBuilder.aTeam().build();
    private String venue = "Default Stadium";
    private LocalDateTime startTime = LocalDateTime.now().plusDays(1);
    private MatchStatus status = MatchStatus.SCHEDULED;
    
    public static MatchBuilder aMatch() {
        return new MatchBuilder();
    }
    
    public MatchBuilder withHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
        return this;
    }
    
    public MatchBuilder withAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
        return this;
    }
    
    public MatchBuilder withVenue(String venue) {
        this.venue = venue;
        return this;
    }
    
    public MatchBuilder withStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }
    
    public MatchBuilder withStatus(MatchStatus status) {
        this.status = status;
        return this;
    }
    
    public Match build() {
        Match match = new Match(homeTeam, awayTeam, venue, startTime);
        if (status != MatchStatus.SCHEDULED) {
            // Set the status through valid state transitions
            if (status == MatchStatus.LIVE) {
                match.start();
            } else if (status == MatchStatus.COMPLETED) {
                match.start();
                match.complete();
            }
        }
        return match;
    }
}
```

### Database Setup for Tests

```java
@TestConfiguration
public class TestDatabaseConfig {
    
    @Bean
    public DataSource dataSource() {
        PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
        postgres.start();
        
        return DataSourceBuilder.create()
            .url(postgres.getJdbcUrl())
            .username(postgres.getUsername())
            .password(postgres.getPassword())
            .build();
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.localhost.pitchperfect.infrastructure.persistence.entity");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        em.setJpaVendorAdapter(vendorAdapter);
        
        return em;
    }
}
```

## Test Configuration

### Base Test Classes

```java
public abstract class DomainUnitTest {
    // Common setup for domain tests
}

public abstract class ApplicationServiceTest {
    // Common setup for application service tests
    
    @Mock
    protected MatchRepository matchRepository;
    
    @Mock
    protected TeamRepository teamRepository;
    
    @Mock
    protected NotificationService notificationService;
    
    @InjectMocks
    protected MatchServiceImpl matchService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTest {
    // Common setup for integration tests
    
    @Autowired
    protected TestRestTemplate restTemplate;
    
    @Autowired
    protected ObjectMapper objectMapper;
    
    @MockBean
    protected ExternalDataProviderAdapter externalDataProviderAdapter;
}
```

### Test Profiles

```yaml
# application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: password
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
  redis:
    embedded: true
```

## Continuous Integration

### CI Pipeline Stages

1. **Build**
   - Compile code
   - Run static analysis

2. **Unit Tests**
   - Run domain and application layer tests
   - Generate coverage report

3. **Integration Tests**
   - Run adapter and API integration tests
   - Test with containerized dependencies

4. **End-to-End Tests**
   - Run UI and performance tests
   - Test in staging-like environment

5. **Security Tests**
   - Run SAST and dependency scanning
   - Check for vulnerabilities

### CI Configuration

```yaml
# .github/workflows/ci.yml
name: CI Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Build with Maven
        run: mvn -B compile
      
  unit-tests:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run unit tests
        run: mvn -B test
      - name: Upload coverage report
        uses: actions/upload-artifact@v3
        with:
          name: coverage-report
          path: target/site/jacoco
  
  integration-tests:
    needs: unit-tests
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run integration tests
        run: mvn -B verify -DskipUnitTests
```

## Test Coverage Goals

| Layer           | Coverage Target |
|-----------------|----------------|
| Domain          | 95%            |
| Application     | 90%            |
| Infrastructure  | 80%            |
| End-to-End      | Key user flows |

## Mutation Testing

```xml
<!-- pom.xml -->
<plugin>
    <groupId>org.pitest</groupId>
    <artifactId>pitest-maven</artifactId>
    <version>1.9.0</version>
    <dependencies>
        <dependency>
            <groupId>org.pitest</groupId>
            <artifactId>pitest-junit5-plugin</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
    <configuration>
        <targetClasses>
            <param>com.localhost.pitchperfect.domain.*</param>
            <param>com.localhost.pitchperfect.application.*</param>
        </targetClasses>
        <targetTests>
            <param>com.localhost.pitchperfect.domain.*</param>
            <param>com.localhost.pitchperfect.application.*</param>
        </targetTests>
    </configuration>
</plugin>
```

## Test Automation

### Automated Test Execution

- Unit and integration tests run on every PR
- End-to-end tests run nightly
- Performance tests run weekly

### Test Reports

- JUnit XML reports for test results
- JaCoCo for code coverage
- Allure for test reporting and visualization

## Conclusion

The testing strategy for the Pitch Perfect Football App leverages the hexagonal architecture to enable comprehensive testing at all layers. By focusing on the appropriate testing approach for each architectural layer, we ensure that the application is thoroughly tested while maintaining the separation of concerns that makes the hexagonal architecture so powerful.
