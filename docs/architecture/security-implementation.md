# Security Implementation - Hexagonal Architecture

## Overview

This document outlines the security implementation for the Pitch Perfect Football App, designed according to hexagonal architecture principles. Security is implemented as a cross-cutting concern that spans all layers of the application while maintaining the separation of concerns.

## Security Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Applications                       │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Infrastructure Layer                        │
│                                                                 │
│  ┌─────────────────┐      ┌─────────────────┐      ┌──────────┐ │
│  │ Authentication  │      │  Authorization  │      │  Crypto  │ │
│  │    Filter       │◄────►│     Filter      │◄────►│ Adapter  │ │
│  └─────────────────┘      └─────────────────┘      └──────────┘ │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Application Layer                           │
│                                                                 │
│  ┌─────────────────┐      ┌─────────────────┐      ┌──────────┐ │
│  │ Authentication  │      │  Authorization  │      │  Crypto  │ │
│  │  Service Port   │◄────►│  Service Port   │◄────►│   Port   │ │
│  └─────────────────┘      └─────────────────┘      └──────────┘ │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                        Domain Layer                              │
│                                                                 │
│  ┌─────────────────┐      ┌─────────────────┐      ┌──────────┐ │
│  │ Security Policy │      │   User Domain   │      │ Security │ │
│  │     Rules       │◄────►│      Model      │◄────►│ Entities │ │
│  └─────────────────┘      └─────────────────┘      └──────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## Security Components

### 1. Authentication

#### Domain Layer
- **User Entity**: Contains core user identity information
- **Authentication Policy**: Defines rules for valid authentication
- **Role Entity**: Represents user roles for authorization

#### Application Layer (Ports)
- **AuthenticationServicePort**: Interface for authentication operations
- **TokenServicePort**: Interface for token generation and validation
- **UserDetailsServicePort**: Interface for loading user details

#### Infrastructure Layer (Adapters)
- **JwtAuthenticationAdapter**: JWT implementation of authentication
- **DatabaseUserDetailsAdapter**: Database implementation of user details service
- **AuthenticationFilter**: HTTP filter for authentication processing

### 2. Authorization

#### Domain Layer
- **Permission Entity**: Represents granular permissions
- **Authorization Policy**: Defines access control rules

#### Application Layer (Ports)
- **AuthorizationServicePort**: Interface for authorization operations
- **PermissionEvaluatorPort**: Interface for evaluating permissions

#### Infrastructure Layer (Adapters)
- **RoleBasedAuthorizationAdapter**: Role-based access control implementation
- **MethodSecurityAdapter**: Method-level security implementation
- **AuthorizationFilter**: HTTP filter for authorization processing

### 3. Cryptography

#### Domain Layer
- **PasswordPolicy**: Defines password strength requirements
- **EncryptionPolicy**: Defines encryption requirements

#### Application Layer (Ports)
- **PasswordEncoderPort**: Interface for password encoding
- **EncryptionServicePort**: Interface for data encryption/decryption

#### Infrastructure Layer (Adapters)
- **BCryptPasswordEncoderAdapter**: BCrypt implementation of password encoder
- **AESEncryptionAdapter**: AES implementation of encryption service

## Authentication Flow

### 1. Registration Flow

```
Client                                                 Server
  │                                                      │
  │  POST /api/auth/register                             │
  │  {username, email, password, ...}                    │
  │ ─────────────────────────────────────────────────────>
  │                                                      │
  │  Validate input                                      │
  │                                                      │
  │  Encode password                                     │
  │                                                      │
  │  Create user in database                             │
  │                                                      │
  │  Generate JWT token                                  │
  │                                                      │
  │  201 Created                                         │
  │  {id, username, email, token, ...}                   │
  │ <─────────────────────────────────────────────────────
  │                                                      │
```

1. Client sends registration request with user details
2. Server validates input against domain rules
3. Password is encoded using password encoder
4. User is created in the database
5. JWT token is generated for immediate authentication
6. Response includes user details and token

### 2. Login Flow

```
Client                                                 Server
  │                                                      │
  │  POST /api/auth/login                                │
  │  {username, password}                                │
  │ ─────────────────────────────────────────────────────>
  │                                                      │
  │  Load user details                                   │
  │                                                      │
  │  Verify password                                     │
  │                                                      │
  │  Generate JWT token                                  │
  │                                                      │
  │  200 OK                                              │
  │  {id, username, token, expiresAt}                    │
  │ <─────────────────────────────────────────────────────
  │                                                      │
```

1. Client sends login request with credentials
2. Server loads user details from database
3. Password is verified against stored hash
4. JWT token is generated with user details and roles
5. Response includes user details and token

### 3. Authentication Verification Flow

```
Client                                                 Server
  │                                                      │
  │  Request with Authorization header                   │
  │  Authorization: Bearer {token}                       │
  │ ─────────────────────────────────────────────────────>
  │                                                      │
  │  Extract token from header                           │
  │                                                      │
  │  Validate token signature                            │
  │                                                      │
  │  Check token expiration                              │
  │                                                      │
  │  Load user details                                   │
  │                                                      │
  │  Set authentication in security context              │
  │                                                      │
  │  Process request                                     │
  │                                                      │
  │  Response                                            │
  │ <─────────────────────────────────────────────────────
  │                                                      │
```

1. Client includes JWT token in Authorization header
2. Authentication filter extracts token from header
3. Token service validates token signature and expiration
4. User details are loaded and authentication is set in security context
5. Request is processed with authenticated user

## Authorization Implementation

### Role-Based Access Control (RBAC)

- **Roles**: USER, ADMIN, MODERATOR
- **Role Hierarchy**:
  - ADMIN > MODERATOR > USER
- **Method Security**:
  - `@PreAuthorize("hasRole('ADMIN')")` for admin-only methods
  - `@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")` for moderation methods
  - `@PreAuthorize("authentication.principal.id == #userId")` for user-specific operations

### Permission-Based Access Control

- **Permissions**: READ, WRITE, DELETE, ADMIN
- **Permission Evaluator**:
  - Custom `PermissionEvaluator` implementation
  - `@PreAuthorize("hasPermission(#id, 'Match', 'WRITE')")` for fine-grained control

### WebSocket Authorization

- **Message Destination Authorization**:
  - Channel interceptors for subscription authorization
  - Topic-specific access control

## Password Security

### Password Storage

- **Algorithm**: BCrypt with strength factor 12
- **Salt**: Unique per-user salt
- **Implementation**: Spring Security's `BCryptPasswordEncoder`

### Password Policy

- Minimum length: 8 characters
- Requires at least one uppercase letter
- Requires at least one lowercase letter
- Requires at least one digit
- Requires at least one special character
- Password history: Last 5 passwords cannot be reused

## Token-Based Authentication

### JWT Structure

- **Header**:
  ```json
  {
    "alg": "HS256",
    "typ": "JWT"
  }
  ```
- **Payload**:
  ```json
  {
    "sub": "user-id",
    "username": "username",
    "roles": ["ROLE_USER"],
    "iat": 1516239022,
    "exp": 1516242622
  }
  ```
- **Signature**: HMAC-SHA256 with secret key

### Token Management

- **Token Validity**: 1 hour
- **Refresh Token Validity**: 7 days
- **Token Storage**: Client-side (localStorage or secure cookie)
- **Token Blacklisting**: Redis-based for revoked tokens

## API Security

### HTTPS Configuration

- TLS 1.3 required
- Strong cipher suites only
- HSTS enabled with long max-age

### CORS Configuration

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://frontend-domain.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### Rate Limiting

- **API Rate Limiting**: 100 requests per minute per user
- **Login Rate Limiting**: 5 attempts per minute per IP
- **Implementation**: Bucket4j with Redis backend

```java
@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    private final RateLimiterService rateLimiterService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) {
        String key = extractKey(request);
        ConsumptionProbe probe = rateLimiterService.tryConsume(key);
        
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", 
                              String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", 
                              String.valueOf(probe.getNanosToWaitForRefill() / 1_000_000_000));
        }
    }
}
```

## Data Protection

### Sensitive Data Encryption

- **PII Encryption**: AES-256 for personally identifiable information
- **Database Column Encryption**: Transparent encryption for sensitive columns
- **Implementation**: Attribute converter for JPA entities

```java
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {
    
    private final EncryptionService encryptionService;
    
    @Autowired
    public EncryptedStringConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }
    
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute != null ? encryptionService.encrypt(attribute) : null;
    }
    
    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData != null ? encryptionService.decrypt(dbData) : null;
    }
}
```

### Data Masking

- Credit card numbers: Only last 4 digits visible
- Email addresses: Partial masking in logs and non-essential displays

## Security Headers

- **Content-Security-Policy**: Strict CSP to prevent XSS
- **X-XSS-Protection**: Enabled with blocking mode
- **X-Content-Type-Options**: nosniff
- **X-Frame-Options**: DENY
- **Referrer-Policy**: strict-origin-when-cross-origin

```java
@Configuration
public class SecurityHeadersConfig {
    @Bean
    public HeaderWriterFilter headerWriterFilter() {
        HeaderWriterFilter filter = new HeaderWriterFilter(Arrays.asList(
            new ContentSecurityPolicyHeaderWriter("default-src 'self'; script-src 'self'"),
            new XContentTypeOptionsHeaderWriter(),
            new XXssProtectionHeaderWriter(),
            new ReferrerPolicyHeaderWriter("strict-origin-when-cross-origin"),
            new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.DENY)
        ));
        return filter;
    }
}
```

## Audit Logging

- **Security Events**: All authentication and authorization events
- **Data Access**: Logging of sensitive data access
- **Admin Actions**: All administrative actions
- **Implementation**: AOP-based audit logging

```java
@Aspect
@Component
public class SecurityAuditAspect {
    
    private final AuditLogService auditLogService;
    
    @Autowired
    public SecurityAuditAspect(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
    
    @AfterReturning("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public void logSecureMethodAccess(JoinPoint joinPoint) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        
        auditLogService.logSecurityEvent(
            username,
            joinPoint.getSignature().toShortString(),
            "METHOD_ACCESS",
            true,
            null
        );
    }
}
```

## Security Testing

- **Unit Tests**: For security components
- **Integration Tests**: For security flows
- **Penetration Testing**: Regular security assessments
- **Dependency Scanning**: Automated vulnerability scanning

## Implementation Details

### Spring Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                         UserDetailsService userDetailsService,
                         PasswordEncoder passwordEncoder) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) 
            throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
```

### JWT Authentication Filter (Adapter)

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    
    @Autowired
    public JwtAuthenticationFilter(TokenService tokenService,
                                 UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);
        
        if (token != null && tokenService.validateToken(token)) {
            String username = tokenService.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                    
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

### Token Service Port (Interface)

```java
public interface TokenService {
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    Date getExpirationDateFromToken(String token);
    boolean isTokenExpired(String token);
    boolean isTokenBlacklisted(String token);
    void blacklistToken(String token);
}
```

### Token Service Implementation (Application Layer)

```java
@Service
public class JwtTokenService implements TokenService {
    
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration}")
    private long jwtExpiration;
    
    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpiration;
    
    private final RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    public JwtTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        claims.put("roles", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
                
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    
    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return !isTokenExpired(token) && !isTokenBlacklisted(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    @Override
    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
    
    @Override
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    @Override
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }
    
    @Override
    public void blacklistToken(String token) {
        Date expiration = getExpirationDateFromToken(token);
        long ttl = (expiration.getTime() - System.currentTimeMillis()) / 1000;
        redisTemplate.opsForValue().set("blacklist:" + token, "true", ttl, TimeUnit.SECONDS);
    }
}
```

## Conclusion

The security implementation for the Pitch Perfect Football App follows hexagonal architecture principles, separating concerns between the domain core, application services, and infrastructure adapters. This design ensures that security is consistently applied across the application while maintaining the flexibility to adapt to changing security requirements and technologies.
