import { test } from 'node:test';
import assert from 'node:assert/strict';
import { yAxis } from './axis.js';

test('finite, nonzero axis range for constant and extreme solutions', () => {
  for (const [min, max] of [[0,0],[-1,1],[1.79e308,1.79e308],[-1.79e308,1.79e308],
      [-Number.MAX_VALUE,-Number.MAX_VALUE],[Number.MAX_VALUE,Number.MAX_VALUE],
      [Number.MIN_VALUE,Number.MIN_VALUE],[-1e-300,1e-300]]) {
    const axis = yAxis(min, max);
    assert(Number.isFinite(axis.max - axis.min) && axis.max > axis.min);
    for (const value of [min, max]) {
      const position = (value / axis.scale - axis.min) / (axis.max - axis.min);
      assert(Number.isFinite(position) && position >= 0 && position <= 1);
    }
    for (let i=0;i<=5;i++) {
      const label = (axis.min + (axis.max-axis.min)*i/5) * axis.scale;
      assert(Number.isFinite(label));
    }
  }
});
