export const methods = [
  { id: 'euler', name: 'Эйлер', order: 1, color: '#e69531' },
  { id: 'rk4', name: 'Рунге–Кутта 4', order: 4, color: '#5368dc' },
  { id: 'adams', name: 'Адамс', order: 4, color: '#189c90' },
];
export const equations = [
  { id: 'linear', name: 'Линейное уравнение', formula: 'y′ = y − x² + 1', exact: 'y = (x + 1)² + (y₀ − (x₀ + 1)²)e^(x − x₀)', preset: { x0: '0', xn: '2', y0: '0.5', h: '0.2', epsilon: '0.001' } },
  { id: 'sine', name: 'Периодическое решение', formula: 'y′ = cos(x)', exact: 'y = y₀ + sin(x) − sin(x₀)', preset: { x0: '0', xn: '6', y0: '0', h: '0.3', epsilon: '0.001' } },
  { id: 'square', name: 'Нелинейное уравнение', formula: 'y′ = y²', exact: 'y = y₀ / (1 − y₀(x − x₀))', preset: { x0: '0', xn: '1', y0: '-1', h: '0.1', epsilon: '0.001' } },
];
export const short = n => n === 0 ? '0' : Math.abs(n) < 0.0001 || Math.abs(n) >= 1e6 ? n.toExponential(3) : Number(n.toPrecision(6)).toString();
export function download(content, name, type) {
  const url = URL.createObjectURL(new Blob([content], { type }));
  const a = document.createElement('a'); a.href = url; a.download = name; a.click();
  setTimeout(() => URL.revokeObjectURL(url), 1000);
}
