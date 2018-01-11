package com.github.btnguyen2k.gearmanworker;

import org.gearman.GearmanLostConnectionAction;
import org.gearman.GearmanLostConnectionGrounds;
import org.gearman.GearmanLostConnectionPolicy;
import org.gearman.GearmanServer;

/**
 * Reconnect to server if connection is dropped.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public class ReconnectLostConnectionPolicy implements GearmanLostConnectionPolicy {

    public static final ReconnectLostConnectionPolicy INSTANCE = new ReconnectLostConnectionPolicy();

    private ReconnectLostConnectionPolicy() {
    }

    @Override
    public GearmanLostConnectionAction lostConnection(GearmanServer server,
            GearmanLostConnectionGrounds grounds) {
        return GearmanLostConnectionAction.RECONNECT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownServer(GearmanServer server) {
        // EMPTY
    }
}
