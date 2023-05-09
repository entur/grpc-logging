package org.entur.logging.grpc.slf4jv20;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Interceptor for mapping requests (and/or other input sources) to MDC fields.
 * <p>
 * Do not use this class directly, rather subclass it, so that interceptors may be specified per grpc service.
 */

public abstract class CustomizableMdcContextInterceptor implements ServerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(CustomizableMdcContextInterceptor.class);

	protected final BiFunction<ServerCall, Metadata, Map<String, String>> mdcMapper;

	public CustomizableMdcContextInterceptor(BiFunction<ServerCall, Metadata, Map<String, String>> mdcMapper) {
		this.mdcMapper = mdcMapper;
	}

	@Override
	public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
		GrpcMdcContext grpcMdcContext = GrpcMdcContext.get();
		if (grpcMdcContext != null) {
			Map<String, String> context = mdcMapper.apply(call, headers);
			if (context != null) {
				// could use MDC.put(..) here, but faster to update directly
				grpcMdcContext.putAll(context);

				ServerCall<ReqT, RespT> interceptCall = new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
					public void close(Status status, Metadata trailers) {
						try {
							super.close(status, trailers);
						} finally {
							grpcMdcContext.removeAll(context.keySet());
						}
					}
				};
				return next.startCall(interceptCall, headers);
			}

			// NOOP
			return next.startCall(call, headers);
		} else {
			// ignore
			log.warn("Unable to set trace MDC context, please add " + GrpcMdcContextInterceptor.class.getName() + " before this interceptor");
			return next.startCall(call, headers);
		}
	}

}
