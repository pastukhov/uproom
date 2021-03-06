package ru.uproom.gate.notifications.zwave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwave4j.Manager;
import org.zwave4j.Notification;
import org.zwave4j.NotificationType;
import ru.uproom.gate.devices.GateDevicesSet;
import ru.uproom.gate.devices.zwave.ZWaveDeviceParametersNames;
import ru.uproom.gate.devices.zwave.ZWaveValueIndexFactory;
import ru.uproom.gate.notifications.NotificationHandler;

/**
 * Created by osipenko on 15.09.14.
 */

@ZwaveNotificationHandlerAnnotation(value = NotificationType.VALUE_CHANGED)
public class ValueChangedNotificationHandler implements NotificationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ValueChangedNotificationHandler.class);

    @Override
    public boolean execute(Notification notification, GateDevicesSet home) {

        int paramIndex = ZWaveValueIndexFactory.createIndex(notification.getValueId());
        ZWaveDeviceParametersNames paramName = ZWaveDeviceParametersNames.byZWaveCode(paramIndex);

        home.addGateDeviceParameter(notification.getNodeId(), paramName, notification.getValueId());

        LOG.debug("z-wave notification : {}; node : {}; label : {}", new Object[]{
                notification.getType(),
                notification.getNodeId(),
                Manager.get().getValueLabel(notification.getValueId()),
        });

        return true;
    }
}
