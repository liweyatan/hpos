import { createApp } from 'vue';
import App from './App.vue';
import './assets/main.css';

const link = document.createElement('link');
link.rel = 'stylesheet';
link.href = 'https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css';
document.head.appendChild(link);

createApp(App).mount('#app');
