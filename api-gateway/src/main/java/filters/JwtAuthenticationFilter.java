package filters;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import security.JwtService;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered{

	private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        // 1. Fully public endpoints — no token needed
        if (isPublic(path)) {
            return chain.filter(exchange);
        }

        // 2. Extract and validate token
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return reject(exchange, HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            return reject(exchange, HttpStatus.UNAUTHORIZED);
        }

        String role = jwtService.extractRole(token);
        if (role == null) {
            return reject(exchange, HttpStatus.UNAUTHORIZED);
        }

        // 3. Role-based access control
        if (!isAuthorized(path, method, role)) {
            return reject(exchange, HttpStatus.FORBIDDEN);
        }

        // 4. Forward role as header so downstream services can use it if needed
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Role", role)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isPublic(String path) {
        return path.equals("/api/auth/login") ||
               path.equals("/api/users/register");
    }

    private boolean isAuthorized(String path, HttpMethod method, String role) {
        // ADMIN can do everything
        if ("ADMIN".equals(role)) {
            return true;
        }

        // --- ADMIN-only endpoints ---
        // Suspend user
        if (path.matches("/api/users/\\d+/suspend") && HttpMethod.PUT.equals(method)) {
            return false;
        }
        // Delete book
        if (path.matches("/api/books/.*") && HttpMethod.DELETE.equals(method)) {
            return false;
        }
        // View ALL fines (not own)
        if (path.equals("/api/fines") && HttpMethod.GET.equals(method)) {
            return "ADMIN".equals(role);
        }
        // Pay fine on behalf of user
        if (path.matches("/api/fines/\\d+/pay") && HttpMethod.PATCH.equals(method)) {
            return "ADMIN".equals(role);
        }
        // View ALL loans
        if (path.equals("/api/loans") && HttpMethod.GET.equals(method)) {
            return "ADMIN".equals(role) || "LIBRARIAN".equals(role);
        }

        // --- LIBRARIAN or ADMIN endpoints ---
        // Add/update books
        if (path.equals("/api/books") && HttpMethod.POST.equals(method)) {
            return "LIBRARIAN".equals(role) || "ADMIN".equals(role);
        }
        if (path.matches("/api/books/.*") && HttpMethod.PUT.equals(method)) {
            return "LIBRARIAN".equals(role) || "ADMIN".equals(role);
        }

        // --- USER, LIBRARIAN, ADMIN (any authenticated) ---
        // Browse books
        if (path.startsWith("/api/books") && HttpMethod.GET.equals(method)) {
            return true;
        }
        // Own loans
        if (path.startsWith("/api/loans") && HttpMethod.GET.equals(method)) {
            return true;
        }
        // Create/return loan
        if (path.equals("/api/loans") && HttpMethod.POST.equals(method)) {
            return true;
        }
        if (path.matches("/api/loans/\\d+/return") && HttpMethod.PATCH.equals(method)) {
            return true;
        }
        // Own fines
        if (path.matches("/api/fines/user/\\d+") && HttpMethod.GET.equals(method)) {
            return true;
        }
        // Own profile
        if (path.matches("/api/users/\\d+") && (HttpMethod.GET.equals(method) || HttpMethod.PUT.equals(method))) {
            return true;
        }
        // Own membership
        if (path.matches("/api/users/\\d+/membership") && HttpMethod.GET.equals(method)) {
            return true;
        }

        // Default: deny anything not explicitly allowed
        return false;
    }

    private Mono<Void> reject(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
