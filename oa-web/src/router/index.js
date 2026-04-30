import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人信息' }
      },
      {
        path: 'attendance',
        name: 'Attendance',
        component: () => import('@/views/attendance/index.vue'),
        meta: { title: '考勤管理' }
      },
      {
        path: 'my-attendance',
        name: 'MyAttendance',
        component: () => import('@/views/attendance/my.vue'),
        meta: { title: '我的考勤' }
      },
      {
        path: 'leave',
        name: 'Leave',
        component: () => import('@/views/leave/index.vue'),
        meta: { title: '请假管理' }
      },
      {
        path: 'my-leave',
        name: 'MyLeave',
        component: () => import('@/views/leave/my.vue'),
        meta: { title: '我的请假' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理' }
      },
      {
        path: 'dept',
        name: 'Dept',
        component: () => import('@/views/system/dept/index.vue'),
        meta: { title: '部门管理' }
      },
      {
        path: 'post',
        name: 'Post',
        component: () => import('@/views/system/post/index.vue'),
        meta: { title: '岗位管理' }
      },
      {
        path: 'announcement',
        name: 'Announcement',
        component: () => import('@/views/announcement/index.vue'),
        meta: { title: '公告管理' }
      },
      {
        path: 'note',
        name: 'Note',
        component: () => import('@/views/note/index.vue'),
        meta: { title: '个人便签' }
      },
      {
        path: 'contact',
        name: 'Contact',
        component: () => import('@/views/contact/index.vue'),
        meta: { title: '通讯录' }
      },
      {
        path: 'meeting-room',
        name: 'MeetingRoom',
        component: () => import('@/views/meeting/room.vue'),
        meta: { title: '会议室管理' }
      },
      {
        path: 'meeting',
        name: 'Meeting',
        component: () => import('@/views/meeting/index.vue'),
        meta: { title: '会议管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title || 'OA人事管理系统'
  const userStore = useUserStore()
  const token = localStorage.getItem('token')
  
  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      if (!userStore.userInfo.id) {
        userStore.getUserInfo().then(() => {
          next()
        }).catch(() => {
          localStorage.removeItem('token')
          next('/login')
        })
      } else {
        next()
      }
    } else {
      next('/login')
    }
  }
})

export default router