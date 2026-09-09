import { useRef, useState } from 'react';
import { equations, methods, short, download } from './data';
import Chart from './Chart';

const initial = { equation: 'linear', ...equations[0].preset, methods: ['euler', 'rk4', 'adams'] };
const fields = [['x0','Начало интервала','x₀'],['xn','Конец интервала','xₙ'],['y0','Начальное значение','y₀'],['h','Начальный шаг','h'],['epsilon','Точность','ε']];

export default function App() {
  const [form, setForm] = useState(initial);
  const [result, setResult] = useState(null);
  const [submitted, setSubmitted] = useState('');
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState('');
  const [errors, setErrors] = useState({});
  const [tab, setTab] = useState('solution');
  const [tableMethod, setTableMethod] = useState('');
  const [page, setPage] = useState(0);
  const [fullPrecision, setFullPrecision] = useState(true);
  const abort = useRef(null);
  const equation = equations.find(e => e.id === form.equation);
  const good = result?.results.filter(r => r.status === 'ok') ?? [];
  const current = good.find(r => r.id === tableMethod) ?? good[0];
  const stale = result && submitted !== JSON.stringify(form);
  function change(key, value) { setForm(f => ({ ...f, [key]: value })); setErrors(e => ({ ...e, [key]: '' })); setError(''); }
  function preset(id) { const e = equations.find(e => e.id === id); setForm(f => ({ ...f, equation: id, ...e.preset })); setErrors({}); setError(''); }
  async function calculate(e) {
    e.preventDefault();
    const invalid = {};
    const parsed = {};
    for (const [id] of fields) {
      const text = form[id].trim().replace(',', '.');
      parsed[id] = Number(text);
      if (!text) invalid[id] = 'Заполните это поле';
      else if (!/^[+-]?(?:\d+(?:\.\d*)?|\.\d+)(?:[eE][+-]?\d+)?$/.test(text)) invalid[id] = 'Введите число: 0,1 или 1e-6';
      else if (!Number.isFinite(parsed[id]) || (parsed[id] === 0 && /[1-9]/.test(text.split(/[eE]/)[0]))) invalid[id] = 'Число вне диапазона вычислений. Измените его масштаб.';
    }
    if (!invalid.x0 && !invalid.xn && parsed.xn <= parsed.x0) invalid.xn = 'xₙ должен быть больше x₀';
    for (const id of ['h','epsilon']) if (!invalid[id] && parsed[id] <= 0) invalid[id] = 'Значение должно быть больше нуля';
    setErrors(invalid); setError('');
    if (Object.keys(invalid).length) { document.getElementById(Object.keys(invalid)[0])?.focus(); return; }
    if (!form.methods.length) { setError('Выберите хотя бы один численный метод.'); return; }
    const snapshot = JSON.stringify(form);
    setBusy(true); abort.current = new AbortController();
    const timeout = setTimeout(() => abort.current?.abort('timeout'), 60000);
    try {
      const response = await fetch(`${import.meta.env.BASE_URL}api/solve`, { method: 'POST', body: new URLSearchParams({ ...form, methods: form.methods.join(',') }), signal: abort.current.signal });
      let data;
      try { data = await response.json(); }
      catch { throw new Error('Сервер вернул некорректный ответ. Проверьте запуск WildFly и повторите расчёт.'); }
      if (!response.ok) throw new Error(data.error || `Ошибка сервера: ${response.status}`);
      setResult(data); setSubmitted(snapshot); setPage(0); setTableMethod(data.results.find(r => r.status === 'ok')?.id ?? '');
    } catch (err) {
      setError(abort.current.signal.aborted ? (abort.current.signal.reason === 'timeout' ? 'Расчёт занял больше минуты. Уменьшите интервал или увеличьте ε.' : 'Расчёт отменён. Можно изменить данные и повторить.') : err instanceof TypeError ? 'Нет связи с Java-сервером. Запустите bash run.sh и повторите расчёт.' : err.message);
    } finally { clearTimeout(timeout); setBusy(false); abort.current = null; }
  }
  function csv() {
    const rows = ['Метод;x;y численное;y точное;Абсолютная ошибка'];
    for (const r of good) for (const row of r.rows) rows.push([r.title, ...row.map(v => String(v).replace('.', ','))].join(';'));
    download('\uFEFF'+rows.join('\r\n'), 'lab6-results.csv', 'text/csv;charset=utf-8');
  }
  return <div className="app">
    <header className="topbar"><a className="brand" href={import.meta.env.BASE_URL} aria-label="Главная"><span className="brand-icon">∂</span><span>Вычислительная математика<small>ЛАБОРАТОРНЫЙ ПРАКТИКУМ</small></span></a><div className="top-meta"><span className="lab-badge">ЛР №06</span><span>Вариант 15 · Задача Коши</span></div></header>
    <main>
      <div className="page-heading"><div><div className="eyebrow">ЧИСЛЕННЫЕ МЕТОДЫ / ДИФФЕРЕНЦИАЛЬНЫЕ УРАВНЕНИЯ</div><h1>От уравнения к решению<span>.</span></h1><p>Решите задачу Коши и сравните точность численных методов.</p></div></div>
      <div className="workspace">
        <aside><form className="panel input-panel" onSubmit={calculate} noValidate><div className="panel-heading"><h2>Параметры задачи</h2><span className="step-label">01 / ВВОД</span></div>
          <fieldset disabled={busy}>
            <label className="field-label" htmlFor="equation">Дифференциальное уравнение</label><select id="equation" value={form.equation} onChange={e => change('equation', e.target.value)}>{equations.map(e => <option key={e.id} value={e.id}>{e.formula}</option>)}</select>
            <div className="equation-preview"><span>{equation.name}</span><strong>{equation.formula}</strong><small>y(x₀) = y₀</small></div>
            <div className="section-label">Начальные условия и сетка</div><div className="field-grid">{fields.map(([id,label,symbol]) => <div className={`field ${id === 'epsilon' ? 'wide' : ''}`} key={id}><label htmlFor={id}>{label} <b>{symbol}</b></label><input id={id} type="text" inputMode="decimal" autoComplete="off" value={form[id]} aria-invalid={!!errors[id]} aria-describedby={errors[id] ? `${id}-error` : undefined} onChange={e => change(id,e.target.value)}/>{errors[id] && <small id={`${id}-error`} className="field-error">{errors[id]}</small>}</div>)}</div>
            <p className="input-hint">Точка и запятая равнозначны. Поддерживается запись 1e-6. Ввод не обрезается; расчёт выполняется в double. Шаг уточняется автоматически.</p>
            <div className="method-heading"><span className="section-label">Методы решения</span><span className="lab-badge">Вариант 15</span></div>
            <div className="method-list">{methods.map(m => <div className="fixed-method" key={m.id}><i style={{ background:m.color }}/><span>{m.name}</span><small>p = {m.order}</small></div>)}</div>
            <p className="input-hint">Все три метода варианта рассчитываются одновременно.</p>
            <button className="primary calculate" type="submit">{busy ? 'Вычисляем…' : 'Рассчитать решение'}<span>{busy ? '◌' : '→'}</span></button>
          </fieldset>
          {busy && <button className="text-button cancel" type="button" onClick={() => abort.current?.abort()}>Отменить расчёт</button>}
          {error && <div className="error-box" role="alert">{error}</div>}
          <div className="preset-row"><span>Готовые примеры</span>{equations.map((e,i) => <button type="button" disabled={busy} key={e.id} onClick={() => preset(e.id)} title={e.name}>{String(i+1).padStart(2,'0')}</button>)}</div>
        </form><div className="side-note"><span>ⓘ</span><p>Одношаговые методы проверяются по правилу Рунге, Адамс — по точному решению.</p></div></aside>
        <section className="results" aria-live="polite">
          {!result ? <div className="panel welcome"><div className="welcome-tag">ЧИСЛЕННЫЙ ЭКСПЕРИМЕНТ</div><svg viewBox="0 0 440 180" aria-hidden="true"><defs><pattern id="grid" width="28" height="28" patternUnits="userSpaceOnUse"><path d="M 28 0 L 0 0 0 28" fill="none" stroke="#e8ebf5"/></pattern></defs><rect width="440" height="180" fill="url(#grid)"/><path d="M20 155 C130 160 125 40 240 65 S340 145 420 20" stroke="#5667db" strokeWidth="3" fill="none"/><path d="M20 155 L75 150 L130 114 L185 69 L240 67 L300 92 L360 97 L420 40" stroke="#e6a03c" strokeWidth="2" fill="none" strokeDasharray="6 5"/><circle cx="20" cy="155" r="5" fill="#5667db"/></svg><h2>Исследуйте поведение решения</h2><p>Выберите уравнение и введите данные слева.<br/>После расчёта здесь появятся график, оценка точности и таблица значений.</p><div className="welcome-features"><span>3 уравнения</span><span>3 метода</span><span>Контроль точности</span></div></div> : <>
            {stale && <div className="notice" role="status">Параметры изменены. Ниже показан предыдущий расчёт — нажмите «Рассчитать решение», чтобы обновить его.</div>}
            <div className="result-heading"><div><span className="step-label">02 / РЕЗУЛЬТАТ</span><h2>{result.equation}</h2><p>x ∈ [{String(result.input.x0)}; {String(result.input.xn)}] · y₀ = {String(result.input.y0)} · ε = {String(result.input.epsilon)}</p></div><span className={`status-badge ${good.length !== result.results.length ? 'warning' : ''}`}>{good.length === result.results.length ? '✓ Расчёт завершён' : `Решено: ${good.length} из ${result.results.length}`}</span></div>
            <div className="metrics">{result.results.map(r => <div className="panel metric" key={r.id} style={{ '--method-color':methods.find(m=>m.id===r.id).color }}><div className="metric-title"><i/>{methods.find(m=>m.id===r.id).name}</div>{r.status === 'ok' ? <><strong title={String(r.estimate)}>{short(r.estimate)}</strong><span>{r.criterion === 'runge' ? 'оценка Рунге' : 'макс. точная ошибка'}</span><div className="metric-bottom"><span title={String(r.h)}>h = {short(r.h)}</span><span>{r.steps} шагов</span></div></> : <p className="method-error">{r.message}</p>}</div>)}</div>
            {!!good.length && <><div className="panel graph-panel"><div className="panel-heading"><div className="tabs"><button className={tab==='solution'?'active':''} onClick={()=>setTab('solution')}>График решения</button><button className={tab==='error'?'active':''} onClick={()=>setTab('error')}>Погрешность</button></div><span className="step-label">y(x)</span></div><Chart key={`${submitted}-${tab}`} data={result} errorMode={tab==='error'}/><div className="exact-formula"><span>ТОЧНОЕ РЕШЕНИЕ</span><code>{result.solution}</code></div></div>
            <div className="panel table-panel"><div className="panel-heading"><h2>Таблица значений</h2><button className="secondary" onClick={csv}>↓ Скачать CSV</button></div><div className="table-controls"><select aria-label="Метод в таблице" value={current.id} onChange={e=>{setTableMethod(e.target.value);setPage(0);}}>{good.map(r=><option key={r.id} value={r.id}>{r.title}</option>)}</select><label><input type="checkbox" checked={fullPrecision} onChange={e=>setFullPrecision(e.target.checked)}/> Полная точность</label><span>{current.rows.length} узлов</span></div><div className="table-scroll"><table><thead><tr><th>i</th><th>xᵢ</th><th>yᵢ численное</th><th>yᵢ точное</th><th>|yᵢ − yᵢ точное|</th></tr></thead><tbody>{current.rows.slice(page*20,(page+1)*20).map((row,i)=><tr key={page*20+i}><td>{page*20+i}</td>{row.map((v,j)=><td key={j} title={String(v)}>{fullPrecision?String(v):short(v)}</td>)}</tr>)}</tbody></table></div><div className="pagination"><span>{page*20+1}–{Math.min((page+1)*20,current.rows.length)} из {current.rows.length}</span><div><button aria-label="Первая страница" disabled={!page} onClick={()=>setPage(0)}>«</button><button aria-label="Предыдущая страница" disabled={!page} onClick={()=>setPage(p=>p-1)}>←</button><span>{page+1} / {Math.ceil(current.rows.length/20)}</span><button aria-label="Следующая страница" disabled={(page+1)*20>=current.rows.length} onClick={()=>setPage(p=>p+1)}>→</button><button aria-label="Последняя страница" disabled={(page+1)*20>=current.rows.length} onClick={()=>setPage(Math.ceil(current.rows.length/20)-1)}>»</button></div></div></div>
</>}
          </>}
        </section>
      </div>

    </main><footer><span>Лабораторная работа №6</span><span>Численное решение обыкновенных дифференциальных уравнений</span></footer>
  </div>;
}
