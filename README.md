# web项目公共功能库
# 模块
## 1. common-response-starter
### 简介
封装了接口的统一响应(包括业务码、消息以及真实数据)，使得我们的接口只需要
返回真实数据如用户信息对象，就会由我们的starter自动包裹一层统一响应。
```
// 返回对象转的json
{
    "username": "xxc",
    "age": 24
}
// 统一响应包裹后的json
{
    "code": 200,
    "msg": "success",
    "data": {
        "username": "xxc",
        "age": 24
    }
}
```
其中成功的响应码可通过application.yml配置,如下:
```
common:
    response:
        success-code: 300
```
### 接入
- 引入依赖
```
  <dependency>
    <artifactId>common-response-starter</artifactId>
    <groupId>org.originit</groupId>
    <version>最新版本</version>
  </dependency>
```
### 使用
#### @ResponseResult
标注在类或者HTTP接口方法上，声明响应的结果类型，默认为PlatformResult。 
标注该注解之后才会进行统一响应的包裹。
```
public class PlatformResult<T> extends Result{

    private Integer code;
    private String msg;
    private T data;
     
    public PlatformResult(T data) {
        this.code = SpringUtil.getBean(SuccessCodeAcquirer.class).getSuccessCode();
        this.data = data;
    }
}
```
如果要实现自定义的统一响应。需要继承Result类，同时提供一个Object参数的构造方法，如上构造方法会因为泛型擦除转为Object 
最后再通过ResponseResult的value属性进行声明，框架将会自动使用该响应。

使用案例如下:
```
// 通过@RestController注解所有的接口方法都会将返回值转换成JSON
// 通过@ResponseResult注解，所有接口都会在返回值的基础上包一层com.xxc.response.result.PlatformResult，这样就不用手写了
// 如果有些接口不需要包裹，就在方法上添加@OriginResponse注解即可
// 注意，当接口正常响应会包裹，若出现未捕获的异常，则会转到全局异常处理器
@RestController
@ResponseResult
@RequestMapping("/resource")
public class ResourceController {
    
}
```
#### 基本类型的统一响应
对于基本类型的统一响应，我们需要返回SimpleData对象去包裹一层，如下:
```
@RequestMapping("/hello")
public SimpleData<String> sayHello() {
    return SimpleData.of("hahah");
}
```
这是因为HandlerMethodReturnValueHandler对String的处理会先被其他的Handler拦截，导致我们的统一响应无法生效，所以我们需要手动包裹一层使得返回值为对象类型，才能使得RequestResponseBodyMethodProcessor去处理我们的响应。
#### @OriginResponse
对于标注了ResponseResult注解的类，默认所有接口都会做包裹，我们可以通过声明OriginResponse使得指定方法不使用统一响应
### 注意事项
我们只会对返回RestController或者标注ResponseBody的接口进行包裹统一响应。
## 2. common-exception-starter
### 简介
封装了SpringMvc的异常处理器，帮助我们统一处理异常，使得我们的接口只需要关注业务逻辑，不需要关注异常处理。
对于异常情况只需要直接抛出异常即可转化为异常情况下的JSON给前端。
### 接入
- 引入依赖
```
  <dependency>
    <artifactId>common-exception-starter</artifactId>
    <groupId>org.originit</groupId>
    <version>最新版本</version>
  </dependency>
```
- 配置结果生成器
这是common-web-starter的一个扩展，在其中我们默认提供了一个结果生成器，但是如果你想要自定义结果生成器，可以通过实现ExceptionResultGenerator接口来实现，然后通过@Bean注解将其注入到Spring容器中即可。
主要是为了返回一个对象，然后由SpringMvc自动转换成JSON。
```
public class DefaultResultGenerator implements ExceptionResultGenerator {

    @Override
    public Object generate(String msg, Integer code) {
        return PlatformResult.fail(code, msg);
    }
}
@Bean
public ExceptionResultGenerator exceptionResultGenerator() {
    return new DefaultResultGenerator();
}
```
### 使用
自定义异常类，继承BusinessException，然后在构造方法中传入异常码和异常消息即可。
```
public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String message, Integer code) {
        super(message, code);
    }
}

@RequestMapping("/hello")
public SimpleData<String> sayHello(@RequestParam Boolean error) {
    if (error) {
    throw new UserNotFoundException( "用户找不到了", 10001);
    }
    return SimpleData.of("hahah");
}
// 返回值
{
    "code": 10001,
    "msg": "用户找不到了",
    "data": null
}
```
## 3. common-web-starter
### 简介
集成了common-response-starter和common-exception-starter，使得
我们只需要引入common-web-starter就可以使用统一响应和统一异常处理。不需要
自己提供ExceptionResultGenerator就能直接快速使用。

## 4. common-mybatis-crud(轮子)
### 简介
封装了Mybatis的通用增删改查，使得我们只需要继承对应的Mapper接口，就可以直接增删改查
## 5. common-logger-starter
### 简介
封装了基于AOP的方法注解的日志打印工具，我们只需要在需要打印日志的方法上添加@Log注解即可打印该方法的参数以及当前请求信息。
### 接入
- 引入依赖
```
  <dependency>
    <artifactId>common-logger-starter</artifactId>
    <groupId>org.originit</groupId>
    <version>最新版本</version>
  </dependency>
```
### 使用
对目标方法或者方法所在的类标注@Log注解即可，如下:
```
@Log
@RequestMapping("/hello")
public SimpleData<String> sayHello(@RequestParam Boolean error) {
    if (error) {
        throw new UserNotFoundException( "用户找不到了", 10001);
    }
    return SimpleData.of("hahah");
}
```
对于RestController,我们还可以配置是否打印Get请求的参数,默认为true,如下：
```yaml
common:
  logger:
    log-get: false
```
