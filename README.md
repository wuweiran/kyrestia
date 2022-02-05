# Kyrestia

**Kyrestia**, named after *Kyrestia the Firstborne*, is a process engine supporting mainstream process definition standards. It is not only lightweight, easy and fast to use, but also has plenty of advanced features.
Kyrestia can be integrated into large-scale web applications and microservices to orchestrate business processes, as well as used by offline systems in traditional scenarios.

## Features
- Consistency: Executions are always in a consistent status when visited by any thread at any time.
- Durability: Data of executions can be persisted and restored for later usage.
- Configurability: Behaviors are customizable by modifying settings.
- *More features to be added...*

## Philosophy

0. KISS: Keep it simple and stupid, and keep the word.
1. Standalone: Literally no third-party JARs.
2. Standardization: Embrace BPMN 2.0.
3. Extensibility: Can be extended by implementing extension points.
4. Performance: Pure memory operations and no redundant runtime objects created.

## User Guide

### Native Interface (Recommended)

#### Maven Dependency
```xml
<dependency>
    <groupId>clan.midnight</groupId>
    <artifactId>kyrestia-native</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Sample Code Snippet
Define BPMN process and execute
```java
KyrestiaEngine engine = new KyrestiaEngine();
InputStream inputStream = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("your-bpmn.xml");
// load process definition to memory
engine.deployBPMN(inputStream);
// create an execution
Execution execution = engine.execute(deployedProcessId, initialParameters);
execution.run();
```
Persist execution
```java
// create the persistence object from an execution
ExecutionPO executionPO = engine.convertExecutionForPersistence(execution);
// restore from an persistence object
execution = engine.restore(executionPO);
execution.proceed();
```

#### Integration with Spring
Config Kyrestia engine
```java
@Configuration
public class YourConfiguration {
    @Autowired
    private ApplicationContext applicationContext;
    
    @Bean
    public KyrestiaEngine kyrestiaEngine() {
        Configuration.implementationAccessor = new ImplementationAccessor() {
            @Override
            public Object access(String identifier) {
                Class<?> clazz = ClassUtils.loadClass(identifier);
                return applicationContext.getBean(clazz);
            }
        }
        KyrestiaEngine engine = new KyrestiaEngine();
        InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("your-bpmn.xml");
        engine.deployBPMN(inputStream);
        return engine;
    }
}
```
Delegate BPMN task
```java
@Component
public class DelegationTest implements Delegation {
    @Override
    public void execute(DelegationContext context) {
        // business logic
    }
}
```
## Acknowledgement

Inspired by Alibaba's SmartEngine
