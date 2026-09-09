import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
export default defineConfig({
  base: '/lab6/',
  plugins: [react()],
  server: { proxy: { '/lab6/api': 'http://127.0.0.1:8180' } },
});
