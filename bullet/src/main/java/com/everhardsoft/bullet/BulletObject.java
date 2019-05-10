package com.everhardsoft.bullet;

/**
 * Created by faisa on 12/2/2016.
 */

public abstract class BulletObject {
    protected long pointer = 0;

    public boolean isCreated() {
        return pointer != 0;
    }
    public long getPointer() {
        return pointer;
    }
    public void create() {
        if(isCreated())
            return;
        pointer = createFunction();
    }
    public void destroy() {
        if(!isCreated())
            return;
        destroyFunction();
        pointer = 0;
    }
    protected abstract long createFunction();
    protected abstract void destroyFunction();
}
