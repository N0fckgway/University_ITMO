import { useId, useMemo, useRef, useState } from 'react';
import { methods, short, download } from './data';
import { yAxis } from './axis';
import { clampPoint, visibleRows, zoomToSelection } from './zoom';

export default function Chart({ data, errorMode }) {
  const [hidden, setHidden] = useState([]);
  const [hover, setHover] = useState(null);
  const [viewport, setViewport] = useState(null);
  const [selection, setSelection] = useState(null);
  const drag = useRef(null);
  const svgRef = useRef(null);
  const clipId = useId().replace(/[^a-zA-Z0-9_-]/g, '');
  const series = useMemo(() => {
    const good = data.results.filter(r => r.status === 'ok');
    const result = good.map(r => ({ id: r.id, name: methods.find(m => m.id === r.id).name, color: methods.find(m => m.id === r.id).color, rows: r.rows.map(row => [row[0], row[errorMode ? 3 : 1]]) }));
    if (!errorMode && good.length) {
      const dense = good.reduce((a, b) => a.rows.length > b.rows.length ? a : b);
      result.unshift({ id: 'exact', name: 'Точное решение', color: '#28334a', rows: dense.rows.map(row => [row[0], row[2]]) });
    }
    return result;
  }, [data, errorMode]);
  const visible = series.filter(s => !hidden.includes(s.id));
  const { x0, xn } = data.input;
  let ymin = Infinity, ymax = -Infinity;
  for (const s of visible) for (const [, y] of s.rows) { ymin = Math.min(ymin, y); ymax = Math.max(ymax, y); }
  if (!Number.isFinite(ymin)) { ymin = 0; ymax = 1; }
  const view = viewport ?? { left: x0, right: xn, ...yAxis(ymin, ymax) };
  const { left, right } = view;
  const axis = view;
  const W = 900, H = 360, L = 76, R = 24, T = 24, B = 44;
  const px = x => L + ((x-left)/(right-left))*(W-L-R);
  const pyNormalized = y => T + (1 - (y-axis.min)/(axis.max-axis.min))*(H-T-B);
  const py = y => pyNormalized(y/axis.scale);
  function path(rows) {
    const selected = visibleRows(rows, left, right);
    const stride = Math.max(1, Math.ceil(selected.length/1600));
    return selected.filter((_, i) => i%stride === 0 || i === selected.length-1).map(([x, y], i) => `${i ? 'L' : 'M'}${px(x)},${py(y)}`).join(' ');
  }
  function pointerPoint(e) {
    const svg = e.currentTarget;
    const matrix = svg.getScreenCTM();
    if (!matrix) return null;
    const point = svg.createSVGPoint();
    point.x = e.clientX; point.y = e.clientY;
    return point.matrixTransform(matrix.inverse());
  }
  function cancelSelection() {
    const pointerId = drag.current?.pointerId;
    drag.current = null;
    setSelection(null);
    setHover(null);
    if (pointerId !== undefined && svgRef.current?.hasPointerCapture(pointerId))
      svgRef.current.releasePointerCapture(pointerId);
  }
  function resetZoom() { cancelSelection(); setViewport(null); }
  function startSelection(e) {
    if (e.button !== 0 || !e.isPrimary || !visible.length) return;
    const point = pointerPoint(e);
    if (!point || point.x < L || point.x > W-R || point.y < T || point.y > H-B) return;
    e.preventDefault();
    e.currentTarget.focus({ preventScroll: true });
    e.currentTarget.setPointerCapture(e.pointerId);
    drag.current = { start: clampPoint(point), pointerId: e.pointerId, view };
    setSelection({ start: clampPoint(point), end: clampPoint(point) });
    setHover(null);
  }
  function movePointer(e) {
    const point = pointerPoint(e);
    if (!point) return;
    if (drag.current) {
      if (drag.current.pointerId === e.pointerId) setSelection({ start: drag.current.start, end: clampPoint(point) });
    } else {
      setHover(point.x >= L && point.x <= W-R && point.y >= T && point.y <= H-B
        ? left+(point.x-L)/(W-L-R)*(right-left) : null);
    }
  }
  function endSelection(e) {
    if (!drag.current || drag.current.pointerId !== e.pointerId) return;
    const point = pointerPoint(e);
    const next = point && zoomToSelection(drag.current.view, drag.current.start, point);
    cancelSelection();
    if (next) setViewport(next);
  }
  const tooltip = hover === null ? [] : visible.map(s => {
    const index = Math.max(0, Math.min(s.rows.length-1, Math.round((hover-x0)/(xn-x0)*(s.rows.length-1))));
    return { ...s, point: s.rows[index] };
  });
  return <>
    <div className="chart-tools"><div className="legend">{series.map(s => <button key={s.id} aria-pressed={!hidden.includes(s.id)} className={hidden.includes(s.id) ? 'muted' : ''} onClick={() => setHidden(h => h.includes(s.id) ? h.filter(id => id !== s.id) : [...h, s.id])}><i style={{ background: s.color }}/>{s.name}</button>)}</div><button className="text-button" onClick={() => download(svgRef.current.outerHTML, `lab6-${errorMode ? 'errors' : 'solution'}.svg`, 'image/svg+xml')}>↓ SVG</button></div>
    <div className="chart-wrap">
      <svg ref={svgRef} id="solution-chart" className="selectable-chart" xmlns="http://www.w3.org/2000/svg" viewBox={`0 0 ${W} ${H}`} role="img" tabIndex={0} aria-label={`${errorMode ? 'График абсолютных погрешностей' : 'График точного и численных решений'}. Выделите область для приближения; Escape — отмена выделения или возврат.`} onPointerDown={startSelection} onPointerMove={movePointer} onPointerUp={endSelection} onPointerCancel={cancelSelection} onLostPointerCapture={cancelSelection} onPointerLeave={() => setHover(null)} onKeyDown={e => { if (e.key === 'Escape') { e.preventDefault(); if (drag.current) cancelSelection(); else resetZoom(); } }}>
        <rect width={W} height={H} fill="white"/>
        <defs><clipPath id={clipId}><rect x={L} y={T} width={W-L-R} height={H-T-B}/></clipPath></defs>
        {Array.from({ length: 6 }, (_, i) => { const y = axis.min+(axis.max-axis.min)*i/5; const x = left+(right-left)*i/5; return <g key={i} fontFamily="system-ui" fontSize="11" fill="#7a8498"><line x1={L} x2={W-R} y1={pyNormalized(y)} y2={pyNormalized(y)} stroke="#edf0f5"/><text x={L-12} y={pyNormalized(y)+4} textAnchor="end">{short(y*axis.scale)}</text><line x1={px(x)} x2={px(x)} y1={T} y2={H-B} stroke="#f3f4f8"/><text x={px(x)} y={H-18} textAnchor="middle">{short(x)}</text></g>; })}
        <text x="16" y="20" fill="#657087" fontSize="12">{errorMode ? '|Δy|' : 'y'}</text><text x={W-12} y={H-18} fill="#657087" fontSize="12">x</text>
        <g clipPath={`url(#${clipId})`}>{visible.map(s => <path key={s.id} d={path(s.rows)} fill="none" stroke={s.color} strokeWidth={s.id === 'exact' ? 2.6 : 1.8} strokeDasharray={s.id === 'exact' ? '7 5' : undefined} strokeLinejoin="round"/>)}</g>
        {hover !== null && <line x1={px(hover)} x2={px(hover)} y1={T} y2={H-B} stroke="#9ba5bb" strokeDasharray="3 4"/>}
        {selection && <rect x={Math.min(selection.start.x,selection.end.x)} y={Math.min(selection.start.y,selection.end.y)} width={Math.abs(selection.end.x-selection.start.x)} height={Math.abs(selection.end.y-selection.start.y)} fill="#5666d8" fillOpacity="0.12" stroke="#5666d8" strokeWidth="1.2" strokeDasharray="5 3" pointerEvents="none"/>}
      </svg>
      {!visible.length && <div className="chart-empty">Выберите кривую в легенде</div>}
      {tooltip.length > 0 && <div className="chart-tooltip"><strong>Ближайшие узлы сеток</strong>{tooltip.map(s => <div key={s.id} style={{ color: s.color }}>{s.name}: x = {short(s.point[0])}, y = {short(s.point[1])}</div>)}</div>}
    </div>
    <div className="zoom-controls"><span>Зажмите и выделите область для приближения</span><button className="reset-zoom" disabled={!viewport} onClick={resetZoom}>↶ Вернуть полный график</button></div>
  </>;
}
