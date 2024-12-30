package dev.appianway.dashboard.service;

import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.repository.BatteryInfoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BatteryInfoServiceTest {
    @Mock
    BatteryInfoRepository batteryInfoRepository;
    @InjectMocks
    BatteryInfoService batteryInfoService;

    @Test
    void getBatteryInfo() {
        Dashboard dashboard = new Dashboard();
        BatteryInfo expectedBatteryInfo = new BatteryInfo();
        expectedBatteryInfo.setType(BatteryInfoType.REMAINING_CAPACITY);
        Mockito.when(batteryInfoRepository.findByDashboardAndTypeIn(dashboard, List.of(BatteryInfoType.REMAINING_CAPACITY))).thenReturn(List.of(expectedBatteryInfo));
        BatteryInfo batteryInfo = batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.REMAINING_CAPACITY);
        assertEquals(expectedBatteryInfo, batteryInfo);
    }

    @Test
    void getAllBatteryInfos() {
        Dashboard dashboard = new Dashboard();
        List<BatteryInfo> expectedBatteryInfos = List.of(new BatteryInfo(), new BatteryInfo());
        Mockito.when(batteryInfoRepository.findByDashboard(dashboard)).thenReturn(expectedBatteryInfos);
        List<BatteryInfo> batteryInfos = batteryInfoService.getAllBatteryInfos(dashboard);
        assertEquals(expectedBatteryInfos, batteryInfos);
    }

    @Test
    void getBatteryInfosByTypes() {
        Dashboard dashboard = new Dashboard();
        List<BatteryInfoType> types = List.of(BatteryInfoType.REMAINING_CAPACITY, BatteryInfoType.TEMPERATURE);
        List<BatteryInfo> expectedBatteryInfos = List.of(new BatteryInfo(), new BatteryInfo());
        Mockito.when(batteryInfoRepository.findByDashboardAndTypeIn(dashboard, types)).thenReturn(expectedBatteryInfos);
        List<BatteryInfo> batteryInfos = batteryInfoService.getBatteryInfosByTypes(dashboard, types);
        assertEquals(expectedBatteryInfos, batteryInfos);
    }

    @Test
    void updateBatteryInfo() {
        BatteryInfo batteryInfo = new BatteryInfo();
        BatteryInfoType type = BatteryInfoType.REMAINING_CAPACITY;
        Float value = 50.0f;
        String unit = "%";
        BatteryInfo expectedBatteryInfo = new BatteryInfo();
        expectedBatteryInfo.setType(type);
        expectedBatteryInfo.setValue(value);
        expectedBatteryInfo.setUnit(unit);
        Mockito.when(batteryInfoRepository.save(batteryInfo)).thenReturn(expectedBatteryInfo);
        BatteryInfo updatedBatteryInfo = batteryInfoService.updateBatteryInfo(batteryInfo, type, value, unit);
        assertEquals(expectedBatteryInfo, updatedBatteryInfo);
    }
}