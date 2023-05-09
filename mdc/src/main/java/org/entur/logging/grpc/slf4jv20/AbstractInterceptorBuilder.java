package org.entur.logging.grpc.slf4jv20;

import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;

/**
 * AbstractInterceptorBuilder builder scaffold. Ensure that the expected slf4j service-provider is in effect.
 *
 * @see <a href=
 * "https://www.sitepoint.com/self-types-with-javas-generics/">https://www.sitepoint.com/self-types-with-javas-generics/</a>
 */

public class AbstractInterceptorBuilder<T extends AbstractInterceptorBuilder> {

    private Class<? extends MDCAdapter> provider;

    public T withMdcAdapter(Class<? extends MDCAdapter> provider) {
        this.provider = provider;
        return (T) this;
    }

    protected void validateProvider() {
        // validate that the expected service-provider exists and is the first
        // see LoggerFactory in 1.8.x
        if (provider == null) {
            provider = GrpcMdcContextAwareMdcAdapter.class;
        }

        MDCAdapter mdcAdapter = MDC.getMDCAdapter();
        if (!provider.isAssignableFrom(mdcAdapter.getClass())) {
            throw new IllegalStateException("Expected " + MDCAdapter.class.getName() + " instance of type " + provider.getName() + ", found " + mdcAdapter.getClass().getName());
        }
    }
}
