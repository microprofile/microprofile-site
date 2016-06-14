package io.microprofile

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.interceptor.AroundInvoke
import javax.interceptor.Interceptor
import javax.interceptor.InvocationContext

@Interceptor
@Cached
class CachedInterceptor {
    @Inject
    private CachedHolder holder

    @PostConstruct
    void initCache(InvocationContext ctx) {
        holder.createCache(ctx.target)
        ctx.proceed()
    }

    @PreDestroy
    void cleanCache(InvocationContext ctx) {
        try {
            holder.removeCache(ctx.target)
        } catch (IllegalStateException ignore) {
            // holder is already undeployed.
        }
    }

    @AroundInvoke
    Object invoke(InvocationContext ctx) throws Exception {
        if (!ctx.method.getAnnotation(Cached)) {
            return ctx.proceed()
        }
        Object result
        try {
            result = holder.get(ctx.target, ctx.method, ctx.parameters)
        } catch (ExceptionCache ignore) {
            result = ctx.proceed()
            holder.set(ctx.target, ctx.method, ctx.parameters, result)
        }
        return result;
    }

}
