package ru.uproom.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.uproom.gate.transport.dto.DeviceDTO;
import ru.uproom.gate.transport.dto.DeviceState;
import ru.uproom.gate.transport.dto.DeviceType;
import ru.uproom.gate.transport.dto.parameters.DeviceParametersNames;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hedin on 13.07.2014.
 */
@Entity
@Table(name = "device")
@NamedQueries({
        @NamedQuery(name = "userDevices", query = "select d from Device d where d.user = :user"),
        @NamedQuery(name = "userDeviceByZId", query = "select d from Device d where d.user.id = :userId " +
                " AND d.zid = :zid")
})
@JsonIgnoreProperties(ignoreUnknown = true, value = {""})
public class Device {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "zid")
    private int zid;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "user")
    @JsonIgnore
    private User user;
    @Column(name = "type")
    private DeviceType type;

    @Transient
    private DeviceState state;
    @Transient
    private Map<DeviceParametersNames, Object> parameters = new HashMap<>();

    public Device() {
    }

    public Device(DeviceDTO dto) {
        id = dto.getId();
        zid = dto.getZId();
        parameters = dto.getParameters();
        type = dto.getType();
    }

    public DeviceDTO toDto() {
        return new DeviceDTO(id, zid, type, new HashMap<>(parameters));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if (null == name)
            name = "not named";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }

    public int getZid() {
        return zid;
    }

    public void setZid(int zid) {
        this.zid = zid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public Map<DeviceParametersNames, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<DeviceParametersNames, Object> parameters) {
        this.parameters = parameters;
    }

    public void mergeById(Device device) {
        if (0 != device.getZid())
            zid = device.getZid();
        if (null != device.getState())
            state = device.getState();
        parameters.putAll(device.getParameters());
    }

    public void mergeByZId(Device device) {
        if (0 != device.getId())
            id = device.getZid();
        if (null != device.getState())
            state = device.getState();
        parameters.putAll(device.getParameters());
        if (null != device.getName())
            name = device.getName();
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", zid=" + zid +
                ", name='" + name + '\'' +
                ", user=" + (user==null?"null":user.getId()) +
                ", type=" + type +
                ", state=" + state +
                ", parameters=" + parameters +
                '}';
    }
}
