package com.wayne.gateway.client;

import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

public interface HttpStatusCodeHtmlWriter {
  Mono<Void> write(ServerHttpResponse response);
}
