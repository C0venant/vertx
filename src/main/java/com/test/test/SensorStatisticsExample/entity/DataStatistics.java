package com.test.test.SensorStatisticsExample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataStatistics {

  private double averageTemp;

  private double maxTemp;

  private double minTemp;

}
