package com.popcorn.agent.core.adapter.python.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Python能力服务：AI推理、数据处理
 * </pre>
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class PythonAgentServiceGrpc {

  private PythonAgentServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.agent.adapter.python.PythonAgentService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest,
      com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse> getLlmInferMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "LlmInfer",
      requestType = com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest.class,
      responseType = com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest,
      com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse> getLlmInferMethod() {
    io.grpc.MethodDescriptor<com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest, com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse> getLlmInferMethod;
    if ((getLlmInferMethod = PythonAgentServiceGrpc.getLlmInferMethod) == null) {
      synchronized (PythonAgentServiceGrpc.class) {
        if ((getLlmInferMethod = PythonAgentServiceGrpc.getLlmInferMethod) == null) {
          PythonAgentServiceGrpc.getLlmInferMethod = getLlmInferMethod =
              io.grpc.MethodDescriptor.<com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest, com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "LlmInfer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PythonAgentServiceMethodDescriptorSupplier("LlmInfer"))
              .build();
        }
      }
    }
    return getLlmInferMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest,
      com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse> getDataProcessMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DataProcess",
      requestType = com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest.class,
      responseType = com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest,
      com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse> getDataProcessMethod() {
    io.grpc.MethodDescriptor<com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest, com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse> getDataProcessMethod;
    if ((getDataProcessMethod = PythonAgentServiceGrpc.getDataProcessMethod) == null) {
      synchronized (PythonAgentServiceGrpc.class) {
        if ((getDataProcessMethod = PythonAgentServiceGrpc.getDataProcessMethod) == null) {
          PythonAgentServiceGrpc.getDataProcessMethod = getDataProcessMethod =
              io.grpc.MethodDescriptor.<com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest, com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DataProcess"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PythonAgentServiceMethodDescriptorSupplier("DataProcess"))
              .build();
        }
      }
    }
    return getDataProcessMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PythonAgentServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PythonAgentServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PythonAgentServiceStub>() {
        @java.lang.Override
        public PythonAgentServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PythonAgentServiceStub(channel, callOptions);
        }
      };
    return PythonAgentServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static PythonAgentServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PythonAgentServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PythonAgentServiceBlockingV2Stub>() {
        @java.lang.Override
        public PythonAgentServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PythonAgentServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return PythonAgentServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PythonAgentServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PythonAgentServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PythonAgentServiceBlockingStub>() {
        @java.lang.Override
        public PythonAgentServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PythonAgentServiceBlockingStub(channel, callOptions);
        }
      };
    return PythonAgentServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PythonAgentServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PythonAgentServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PythonAgentServiceFutureStub>() {
        @java.lang.Override
        public PythonAgentServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PythonAgentServiceFutureStub(channel, callOptions);
        }
      };
    return PythonAgentServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Python能力服务：AI推理、数据处理
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * AI大模型推理接口
     * </pre>
     */
    default void llmInfer(com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest request,
        io.grpc.stub.StreamObserver<com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLlmInferMethod(), responseObserver);
    }

    /**
     * <pre>
     * 数据处理接口（结构化数据清洗/转换）
     * </pre>
     */
    default void dataProcess(com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest request,
        io.grpc.stub.StreamObserver<com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDataProcessMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service PythonAgentService.
   * <pre>
   * Python能力服务：AI推理、数据处理
   * </pre>
   */
  public static abstract class PythonAgentServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return PythonAgentServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service PythonAgentService.
   * <pre>
   * Python能力服务：AI推理、数据处理
   * </pre>
   */
  public static final class PythonAgentServiceStub
      extends io.grpc.stub.AbstractAsyncStub<PythonAgentServiceStub> {
    private PythonAgentServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PythonAgentServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PythonAgentServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * AI大模型推理接口
     * </pre>
     */
    public void llmInfer(com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest request,
        io.grpc.stub.StreamObserver<com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLlmInferMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 数据处理接口（结构化数据清洗/转换）
     * </pre>
     */
    public void dataProcess(com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest request,
        io.grpc.stub.StreamObserver<com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDataProcessMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service PythonAgentService.
   * <pre>
   * Python能力服务：AI推理、数据处理
   * </pre>
   */
  public static final class PythonAgentServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<PythonAgentServiceBlockingV2Stub> {
    private PythonAgentServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PythonAgentServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PythonAgentServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * AI大模型推理接口
     * </pre>
     */
    public com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse llmInfer(com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getLlmInferMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 数据处理接口（结构化数据清洗/转换）
     * </pre>
     */
    public com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse dataProcess(com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDataProcessMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service PythonAgentService.
   * <pre>
   * Python能力服务：AI推理、数据处理
   * </pre>
   */
  public static final class PythonAgentServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<PythonAgentServiceBlockingStub> {
    private PythonAgentServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PythonAgentServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PythonAgentServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * AI大模型推理接口
     * </pre>
     */
    public com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse llmInfer(com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLlmInferMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 数据处理接口（结构化数据清洗/转换）
     * </pre>
     */
    public com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse dataProcess(com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDataProcessMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service PythonAgentService.
   * <pre>
   * Python能力服务：AI推理、数据处理
   * </pre>
   */
  public static final class PythonAgentServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<PythonAgentServiceFutureStub> {
    private PythonAgentServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PythonAgentServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PythonAgentServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * AI大模型推理接口
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse> llmInfer(
        com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLlmInferMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 数据处理接口（结构化数据清洗/转换）
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse> dataProcess(
        com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDataProcessMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LLM_INFER = 0;
  private static final int METHODID_DATA_PROCESS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LLM_INFER:
          serviceImpl.llmInfer((com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest) request,
              (io.grpc.stub.StreamObserver<com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse>) responseObserver);
          break;
        case METHODID_DATA_PROCESS:
          serviceImpl.dataProcess((com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest) request,
              (io.grpc.stub.StreamObserver<com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getLlmInferMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.popcorn.agent.core.adapter.python.grpc.LlmInferRequest,
              com.popcorn.agent.core.adapter.python.grpc.LlmInferResponse>(
                service, METHODID_LLM_INFER)))
        .addMethod(
          getDataProcessMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.popcorn.agent.core.adapter.python.grpc.DataProcessRequest,
              com.popcorn.agent.core.adapter.python.grpc.DataProcessResponse>(
                service, METHODID_DATA_PROCESS)))
        .build();
  }

  private static abstract class PythonAgentServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PythonAgentServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.popcorn.agent.core.adapter.python.grpc.PythonAgentProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PythonAgentService");
    }
  }

  private static final class PythonAgentServiceFileDescriptorSupplier
      extends PythonAgentServiceBaseDescriptorSupplier {
    PythonAgentServiceFileDescriptorSupplier() {}
  }

  private static final class PythonAgentServiceMethodDescriptorSupplier
      extends PythonAgentServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    PythonAgentServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PythonAgentServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PythonAgentServiceFileDescriptorSupplier())
              .addMethod(getLlmInferMethod())
              .addMethod(getDataProcessMethod())
              .build();
        }
      }
    }
    return result;
  }
}
