package org.entur.logging.grpc.slf4jv20;

import io.grpc.*;

/**
 * Interceptor for adding MDC for a gRPC {@linkplain Context}.
 */

public class GrpcMdcContextInterceptor implements ServerInterceptor {

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder extends AbstractInterceptorBuilder<Builder> {
		public GrpcMdcContextInterceptor build() {
			validateProvider();

			return new GrpcMdcContextInterceptor();
		}
	}

	protected GrpcMdcContextInterceptor() {
		// prefer to use builder
	}

	@Override
	public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
		GrpcMdcContext grpcMdcContext = new GrpcMdcContext();
		Context context = Context.current().withValue(GrpcMdcContext.KEY_CONTEXT, grpcMdcContext);
		return Contexts.interceptCall(context, call, headers, next);
	}

}
