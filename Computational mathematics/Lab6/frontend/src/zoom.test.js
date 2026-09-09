import { test } from 'node:test';
import assert from 'node:assert/strict';
import { plot, zoomToSelection, visibleRows } from './zoom.js';

const view = { left: 0, right: 10, min: -1, max: 1, scale: 1e308 };
const at = (x,y) => ({ x: plot.left+(plot.right-plot.left)*x, y: plot.top+(plot.bottom-plot.top)*y });

test('rectangle zooms X and Y and allows dragging in either direction', () => {
  const a=at(.25,.25), b=at(.75,.75);
  const expected={left:2.5,right:7.5,min:-.5,max:.5,scale:1e308};
  assert.deepEqual(zoomToSelection(view,a,b),expected);
  assert.deepEqual(zoomToSelection(view,b,a),expected);
  assert.deepEqual(zoomToSelection(view,at(.75,.25),at(.25,.75)),expected);
  assert.deepEqual(zoomToSelection(expected,a,b),{left:3.75,right:6.25,min:-.25,max:.25,scale:1e308});
});

test('clicks, thin selections and unrepresentable intervals do not zoom', () => {
  assert.equal(zoomToSelection(view,at(.5,.5),at(.5,.5)),null);
  assert.equal(zoomToSelection(view,at(.5,.1),at(.501,.9)),null);
  assert.equal(zoomToSelection(view,at(.1,.5),at(.9,.501)),null);
  assert.equal(zoomToSelection({...view,left:1e16,right:1e16+2},at(.1,.1),at(.4,.4)),null);
});

test('release outside plot is clamped to its edges', () => {
  assert.deepEqual(zoomToSelection(view,{x:-100,y:-100},{x:1000,y:1000}),view);
});

test('curves retain segments crossing selection with no data nodes inside', () => {
  const rows=[[0,0],[1,1],[2,4],[3,9]];
  assert.deepEqual(visibleRows(rows,1.2,1.8),[[1,1],[2,4]]);
  assert.deepEqual(visibleRows(rows,0,3),rows);
});
