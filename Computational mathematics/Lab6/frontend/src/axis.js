// Scale before subtracting extrema: finite values may still overflow on subtraction.
export function yAxis(min, max) {
  const scale = Math.max(Math.abs(min), Math.abs(max)) || 1;
  const lo = min / scale;
  const hi = max / scale;
  const padding = (hi - lo || Math.abs(hi) * 0.1 || 1) * 0.1;
  const bound = Number.MAX_VALUE / scale;
  return { scale, min: Math.max(-bound, lo - padding), max: Math.min(bound, hi + padding) };
}
