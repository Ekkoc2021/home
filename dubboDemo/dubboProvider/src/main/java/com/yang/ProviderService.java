package com.yang;

import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface ProviderService {
    /**
     *
     * @param name
     * @return
     */
    String sayHello(String name);
}
