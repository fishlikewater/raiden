### JDK 原生httpclient封装工具

#### 使用方式
- 使用`spring boot` 

  - 引入`raiden-spring-boot-starter`
  - 添加扫描路径

  ```java
  @SpringBootApplication
  @HttpScan("com.github.fishlikewater.test")  // 这里添加扫描路径
  public class RaidenSpringBootTestApplication {
  
      public static void main(String[] args) {
          SpringApplication.run(RaidenSpringBootTestApplication.class, args);
      }
  
  }
  ```

  - 定义接口

  ```java
  @HttpServer(sourceHttpClient = "customer") //customer 为自定义注册的httpclient
  @Interceptor(MyInterceptor.class ) // 这里定义改接口类下所有接口的拦截器
  public interface DemoRemote {
  
      /**
       * 测试访问百度 如不加协议 默认是http
       *
       * @return {@code String}
       */
      @GET("www.baidu.com")
      String baidu();
  
      /**
       * 测试访问百度 https
       *
       * @return {@code String}
       */
      @GET("https://www.baidu.com")
      String baidu2();
  }
  ```

  - 自定义注册的httpclient （系统会默认注册一个名为`default`的`httpclient`）

  ```java
  @Component
  public class CustomerHttpClient implements SourceHttpClientRegister {
      @Override
      public void register(SourceHttpClientRegistry registry) {
          SSLContext ctx;
          try {
              ctx = SSLContext.getInstance("TLS");
              X509TrustManager tm = new DefaultTrustManager();
              ctx.init(null, new TrustManager[]{tm}, null);
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
          HttpClient customer = HttpClient.newBuilder()
                  .sslContext(ctx)
                  .connectTimeout(Duration.ofSeconds(60))
                  .version(HttpClient.Version.HTTP_1_1)
                  .build();
          registry.register("customer", customer);
      }
  }
  ```

  - 定义拦截器

  ```java
  public class MyInterceptor implements HttpClientInterceptor {
  
      @Override
      public HttpRequest requestBefore(HttpRequest httpRequest) {
          System.out.println("自定义拦截器--请求");
  
          return httpRequest;
      }
  
      @Override
      public <T> HttpResponse<T> requestAfter(HttpResponse<T> response) {
          System.out.println("自定义拦截器--响应");
          return response;
      }
  }
  ```



- 单独使用

  - 引入`raiden-core`即可

  - 1.定义接口使用

    - 定义接口

    ```java
    @HttpServer(sourceHttpClient = "third") //customer 为自定义注册的httpclient
    @Interceptor(MyInterceptor.class ) // 这里定义改接口类下所有接口的拦截器
    public interface DemoRemote {
    
        /**
         * 测试访问百度 如不加协议 默认是http
         *
         * @return {@code String}
         */
        @GET("www.baidu.com")
        String baidu();
    
        /**
         * 测试访问百度 https
         *
         * @return {@code String}
         */
        @GET("https://www.baidu.com")
        String baidu2();
    }
    ```

    - 调用接口

    ```java
    public class RemoteTest {
    
        @Before
        public void before() throws ClassNotFoundException {
            HttpBootStrap.setSelfManager(true);
            HttpBootStrap.init("com.github.fishlikewater.raidencore.remote");
            HttpBootStrap.registerHttpClient("third", HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build());
            HttpBootStrap.getLogConfig().setEnableLog(false).setLogLevel(LogConfig.LogLevel.BASIC);
        }
    
        @Test
        public void test() {
            DemoRemote remote = HttpBootStrap.getProxy(DemoRemote.class);
            String s = remote.baidu();
            Assert.assertNotNull(s);
        }
    
    }
    ```

    

  - 2.直接使用

    ```java
    public class OnlyHttpRequestClientTest {
    
        @Before
        public void before() {
            HttpBootStrap.init();
            HttpBootStrap
                    .getLogConfig()
                    .setLogLevel(LogConfig.LogLevel.HEADS)
                    .setEnableLog(true);
        }
    
        @Test
        public void testClient() throws InterruptedException, IOException {
            HttpRequestClient httpRequestClient = HttpBootStrap.getHttpRequestClient();
            String sync = httpRequestClient.getSync("https://www.baidu.com", String.class);
            Assert.assertNotNull(sync);
        }
    }
    ```

    

> 更改使用方式参考 `raiden-spring-boot-test` 下的使用实例