import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

type UnitMap = Record<string, number>;

@Component({
  selector: 'app-converter',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './converter.component.html',
  styleUrl: './converter.component.css'
})
export class ConverterComponent {
  activeTab: 'size' | 'weight' | 'currency' | 'temperature' = 'size';

  sizeValue = 1;
  sizeFrom = 'ft';
  sizeTo = 'm';

  weightValue = 1;
  weightFrom = 'oz';
  weightTo = 'g';

  currencyValue = 1;
  currencyFrom = 'USD';
  currencyTo = 'EUR';

  temperatureValue = 32;
  temperatureFrom = 'F';
  temperatureTo = 'C';

  sizeUnits: UnitMap = {
    cm: 0.01,
    m: 1,
    km: 1000,
    in: 0.0254,
    ft: 0.3048,
    yd: 0.9144,
    mi: 1609.344
  };

  weightUnits: UnitMap = {
    g: 1,
    kg: 1000,
    oz: 28.3495,
    lb: 453.592,
  };

  currencyRates: UnitMap = {
    EUR: 1,
    USD: 1.08,
    GBP: 0.85,
    CHF: 0.95
  };

  get temperatureResult(): number {
    return this.convertTemperature(
      this.temperatureValue,
      this.temperatureFrom,
      this.temperatureTo
    );
  }

  convertTemperature(value: number, from: string, to: string): number {

    if (from === to) {
      return value;
    }

    // Fahrenheit -> Celsius
    if (from === 'F' && to === 'C') {
      return (value - 32) * 5 / 9;
    }

    // Celsius -> Fahrenheit
    if (from === 'C' && to === 'F') {
      return (value * 9 / 5) + 32;
    }

    return value;
  }

  get sizeResult(): number {
    return this.convert(this.sizeValue, this.sizeFrom, this.sizeTo, this.sizeUnits);
  }

  get weightResult(): number {
    return this.convert(this.weightValue, this.weightFrom, this.weightTo, this.weightUnits);
  }

  get currencyResult(): number {
    const valueInEur = this.currencyValue / this.currencyRates[this.currencyFrom];
    return valueInEur * this.currencyRates[this.currencyTo];
  }

  convert(value: number, from: string, to: string, units: UnitMap): number {
    const baseValue = value * units[from];
    return baseValue / units[to];
  }

  switchUnits(type: 'size' | 'weight' | 'currency' | 'temperature'): void {
    if (type === 'size') {
      [this.sizeFrom, this.sizeTo] = [this.sizeTo, this.sizeFrom];
    }

    if (type === 'weight') {
      [this.weightFrom, this.weightTo] = [this.weightTo, this.weightFrom];
    }

    if (type === 'currency') {
      [this.currencyFrom, this.currencyTo] = [this.currencyTo, this.currencyFrom];
    }

    if (type === 'temperature') {
      [this.temperatureFrom, this.temperatureTo] =
        [this.temperatureTo, this.temperatureFrom];
    }
  }
}