export const plot = { width: 900, height: 360, left: 76, right: 876, top: 24, bottom: 316 };

export function clampPoint(point) {
  return {
    x: Math.max(plot.left, Math.min(plot.right, point.x)),
    y: Math.max(plot.top, Math.min(plot.bottom, point.y)),
  };
}

/** Select both axes. Y bounds stay normalized to avoid overflow at large values. */
export function zoomToSelection(view, start, end) {
  const a = clampPoint(start), b = clampPoint(end);
  if (Math.abs(a.x-b.x) < 8 || Math.abs(a.y-b.y) < 8) return null;
  const xAt = x => view.left + (x-plot.left)/(plot.right-plot.left)*(view.right-view.left);
  const yAt = y => view.max - (y-plot.top)/(plot.bottom-plot.top)*(view.max-view.min);
  const next = {
    left: xAt(Math.min(a.x,b.x)), right: xAt(Math.max(a.x,b.x)),
    min: yAt(Math.max(a.y,b.y)), max: yAt(Math.min(a.y,b.y)), scale: view.scale,
  };
  if (!(next.left < next.right && next.min < next.max) || !Object.values(next).every(Number.isFinite)) return null;
  return next;
}

/** Include neighbors so even a selection between two nodes shows the crossing line. */
export function visibleRows(rows, left, right) {
  const lowerBound = value => {
    let lo = 0, hi = rows.length;
    while (lo < hi) {
      const mid = Math.floor((lo+hi)/2);
      if (rows[mid][0] < value) lo = mid+1;
      else hi = mid;
    }
    return lo;
  };
  return rows.slice(Math.max(0, lowerBound(left)-1), Math.min(rows.length, lowerBound(right)+1));
}
