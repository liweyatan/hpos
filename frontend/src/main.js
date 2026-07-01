import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index.js'
import './assets/main.css'

const link = document.createElement('link')
link.rel = 'stylesheet'
link.href = '/bootstrap-icons.css'
document.head.appendChild(link)

const app = createApp(App)
app.use(router)
app.mount('#app')
