package bit.clarksj4.labyrinth.Engine;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by StickasaurusRex on 09-Dec-16.
 */

public abstract class UIDObject
{
    static final AtomicLong NEXT_ID = new AtomicLong(0);
    final long uid = NEXT_ID.getAndIncrement();

    public long getUID() { return uid; }
}
