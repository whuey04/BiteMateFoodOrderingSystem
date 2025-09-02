package com.bitemate.context;

/**
 * Use ThreadLocal to store and retrieve USER ID
 * to the current thread during a request lifecycle.
 */
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * Sets the current thread's USER ID
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * Retrieves the current thread's USER ID
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

    /**
     * Removes the USER ID from the current thread's context.
     */
    public static void removeCurrentId(){
        threadLocal.remove();
    }
}
