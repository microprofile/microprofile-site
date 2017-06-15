package io.microprofile

import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.ejb.Lock
import javax.ejb.LockType
import javax.ejb.Singleton
import javax.ejb.Timeout
import javax.ejb.Timer
import javax.ejb.TimerConfig
import javax.ejb.TimerService
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Singleton
@Lock(LockType.READ)
class CachedHolder {
    private Logger logger = Logger.getLogger(this.class.name)

    private static final long TIMEOUT = TimeUnit.MINUTES.toMillis(1)

    private ConcurrentMap<Object, Map<EntryKey, Object>> caches
    private Map<Long, Object> idInstance
    private Map<Object, Long> instanceId

    private AtomicLong instanceCounter = new AtomicLong(0)

    @Resource
    private TimerService timerService

    @PostConstruct
    void init() {
        this.caches = new ConcurrentHashMap<>()
        this.idInstance = new HashMap<>()
        this.instanceId = new HashMap<>()
    }

    void createCache(Object beanInstance) {
        this.caches.put(beanInstance, Collections.synchronizedMap(new HashMap<>()))
        Long id = instanceCounter.incrementAndGet()
        idInstance.put(id, beanInstance)
        instanceId.put(beanInstance, id)
    }

    void removeCache(Object beanInstance) {
        this.caches.remove(beanInstance)
        def id = instanceId.get(beanInstance)
        instanceId.remove(beanInstance)
        idInstance.remove(id)
    }

    Object get(Object beanInstance, Method method, Object[] arguments) {
        def methodCache = this.caches.get(beanInstance)
        def key = new EntryKey(method: method, arguments: arguments)
        if (methodCache.containsKey(key)) {
            return methodCache.get(key)
        }
        throw new ExceptionCache('cache entry not found')
    }

    void set(Object beanInstance, Method method, Object[] arguments, Object value) {
        def methodCache = this.caches.get(beanInstance)
        def key = new EntryKey(method: method, arguments: arguments)
        if (!methodCache.put(key, value)) {
            logger.info("New cache entry for ${beanInstance}#${method.name}(${arguments?.join(', ')})")
            timerService.createSingleActionTimer(TIMEOUT, new TimerConfig(new TimerPayload(
                    beanId: instanceId.get(beanInstance),
                    entryKey: key
            ), false))
        }
    }

    @Timeout
    void timeout(Timer timer) {
        TimerPayload payload = timer.info as TimerPayload
        def beanInstance = idInstance.get(payload.beanId)
        def methodCache = this.caches.get(beanInstance)
        EntryKey key = payload.entryKey
        try {
            def newValue = key.method.invoke(beanInstance, key.arguments)
            methodCache.put(key, newValue)
            logger.fine("Cache entry updated -> ${beanInstance}#${key})")
            timerService.createSingleActionTimer(TIMEOUT, new TimerConfig(payload, false))
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            methodCache.remove(key)
            logger.info("Cache entry cannot be updated -> ${beanInstance}#${key}) -> ${e.message}")
            logger.info("Removed cache entry for ${beanInstance}#${key})")
        }
    }

    @Lock(LockType.WRITE)
    void clearCache() {
        this.caches.values().each {
            it.clear()
        }
        this.idInstance.clear()
        this.instanceId.clear()
    }

    class TimerPayload implements Serializable {
        public Long beanId
        public EntryKey entryKey
    }

    class EntryKey {
        Method method
        Object[] arguments

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false
            EntryKey entryKey = (EntryKey) o
            String thisKey = "${method.name}(${arguments?.join(', ')})"
            String thatKey = "${entryKey.method.name}(${entryKey.arguments?.join(', ')})"
            return thisKey == thatKey
        }

        int hashCode() {
            int result
            result = method.hashCode()
            result = 31 * result + (arguments != null ? Arrays.hashCode(arguments) : 0)
            return result
        }

        String toString() {
            return "${method.name}(${arguments?.join(', ')})"
        }
    }

}
