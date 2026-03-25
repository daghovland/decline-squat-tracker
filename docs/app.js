const GOAL = 6;
const STORE = 'sets';
const DB_NAME = 'squat-tracker';

// ── IndexedDB setup ──────────────────────────────────────────────────────────

function openDb() {
  return new Promise((resolve, reject) => {
    const req = indexedDB.open(DB_NAME, 1);
    req.onupgradeneeded = e => {
      const db = e.target.result;
      if (!db.objectStoreNames.contains(STORE)) {
        db.createObjectStore(STORE, { keyPath: 'id', autoIncrement: true });
      }
    };
    req.onsuccess = e => resolve(e.target.result);
    req.onerror = () => reject(req.error);
  });
}

function startOfToday() {
  const d = new Date();
  d.setHours(0, 0, 0, 0);
  return d.getTime();
}

async function getTodaySets(db) {
  const since = startOfToday();
  return new Promise((resolve, reject) => {
    const tx = db.transaction(STORE, 'readonly');
    const store = tx.objectStore(STORE);
    const results = [];
    store.openCursor().onsuccess = e => {
      const cursor = e.target.result;
      if (cursor) {
        if (cursor.value.timestamp >= since) results.push(cursor.value);
        cursor.continue();
      } else {
        resolve(results);
      }
    };
    tx.onerror = () => reject(tx.error);
  });
}

async function insertSet(db) {
  return new Promise((resolve, reject) => {
    const tx = db.transaction(STORE, 'readwrite');
    tx.objectStore(STORE).add({ timestamp: Date.now() });
    tx.oncomplete = resolve;
    tx.onerror = () => reject(tx.error);
  });
}

// ── UI ───────────────────────────────────────────────────────────────────────

const counterEl   = document.getElementById('counter');
const doneLabelEl = document.getElementById('done-label');
const logBtn      = document.getElementById('log-btn');
const setsSection = document.getElementById('sets-section');
const setsList    = document.getElementById('sets-list');

function pad(n) { return String(n).padStart(2, '0'); }

function toTimeString(ts) {
  const d = new Date(ts);
  return `${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}

function render(sets) {
  const count = sets.length;
  const done = count >= GOAL;

  counterEl.textContent = `${count} / ${GOAL}`;
  counterEl.classList.toggle('done', done);
  doneLabelEl.textContent = done ? 'Done for today!' : '';
  logBtn.classList.toggle('done', done);

  if (sets.length > 0) {
    setsSection.hidden = false;
    setsList.innerHTML = sets
      .map(s => `<li>${toTimeString(s.timestamp)}</li>`)
      .join('');
  } else {
    setsSection.hidden = true;
  }
}

// ── Init ─────────────────────────────────────────────────────────────────────

let db;

async function init() {
  db = await openDb();
  render(await getTodaySets(db));

  logBtn.addEventListener('click', async () => {
    await insertSet(db);
    render(await getTodaySets(db));
  });
}

init();

// ── Service Worker registration ──────────────────────────────────────────────

if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('sw.js');
}
