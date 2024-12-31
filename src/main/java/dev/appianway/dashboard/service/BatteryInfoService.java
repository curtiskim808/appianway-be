package dev.appianway.dashboard.service;

import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.repository.BatteryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
// BatteryInfoService for to manage battery info.
@Service
public class BatteryInfoService {

    @Autowired
    private BatteryInfoRepository batteryInfoRepository;

    public BatteryInfo getBatteryInfo(Dashboard dashboard, BatteryInfoType type) {
        List<BatteryInfo> batteryInfos = batteryInfoRepository.findByDashboardAndTypeIn(dashboard, List.of(type));
        if (batteryInfos.isEmpty()) {
            return null;
        }
        return batteryInfos.get(0);
    }

    public List<BatteryInfo> getAllBatteryInfos(Dashboard dashboard) {
        return batteryInfoRepository.findByDashboard(dashboard);
    }

    public List<BatteryInfo> getBatteryInfosByTypes(Dashboard dashboard, List<BatteryInfoType> types) {
        return batteryInfoRepository.findByDashboardAndTypeIn(dashboard, types);
    }

    public BatteryInfo updateBatteryInfo(BatteryInfo batteryInfo, BatteryInfoType type, Float value, String unit) {
        batteryInfo.setType(type);
        batteryInfo.setValue(value);
        batteryInfo.setUnit(unit);
        return batteryInfoRepository.save(batteryInfo);
    }

    public BatteryInfo saveBatteryInfo(BatteryInfo batteryInfo) {
        return batteryInfoRepository.save(batteryInfo);
    }
}
