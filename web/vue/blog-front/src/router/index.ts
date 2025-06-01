import { createRouter, createWebHashHistory, type RouteRecordRaw } from 'vue-router'

// 定义路由配置类型
const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        component: () => import('@/views/HomeView.vue'),
        meta: {
            title: '首页'
        }
    },
    {
        path: '/blogs',
        name: 'blogs',
        component: () => import('@/views/BlogsView.vue'),
        meta: {
            title: '首页'
        }
    },
    {
        path: '/article/:id',
        name: 'article',
        component: () => import('@/views/Article.vue'),
        props: (route) => ({
            id: Number(route.params.id) // 将参数转换为数字类型
        }),
        beforeEnter: (to) => {
            // 参数验证
            if (isNaN(Number(to.params.id))) {
                return { name: 'blogs' } // 无效ID跳转首页
            }
        },
        meta: {
            title: '文章详情',
            requiresAuth: false // 示例：可扩展权限控制
        }
    }
]

const router = createRouter({
    history: createWebHashHistory(import.meta.env.BASE_URL),
    routes,
    scrollBehavior(to, from, savedPosition) {
        // 滚动行为控制
        return savedPosition || { top: 0 }
    }
})

// 全局前置守卫（示例）
router.beforeEach((to) => {
    // 标题处理
    const baseTitle = '技术博客'
    document.title = to.meta.title ? `${to.meta.title} | ${baseTitle}` : baseTitle
})

export default router
