import { createRouter, createWebHashHistory } from 'vue-router'
import Content from '@/components/Content'
import About from "@/components/About";

const routes = [
  {
    path: "/",
        name: "content",
        component: Content,
  },
  {
    path: "/about",
    name: "about",
    component: About,
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
