import { createRouter, createWebHistory} from 'vue-router'

import LayoutView from '@/views/layout/index.vue'
import EmpView from '@/views/emp/index.vue'
import IndexView from '@/views/index/index.vue'
import DeptView from '@/views/dept/index.vue'
import ClassView from '@/views/class/index.vue'
import StudentView from '@/views/student/index.vue'
import EmpReportView from '@/views/report/emp/index.vue'
import StudentReportView from '@/views/report/student/index.vue'
import LogView from '@/views/log/index.vue'

const routes = [
  {
    path: '/',
    name: 'layout',
    component: LayoutView,
    redirect: 'index',
    children: [
      {
        path :'emp',
        name: 'emp',
        component: EmpView
      },
      {
        path :'index',
        name: 'index',
        component: IndexView
      },
      {
        path :'dept',
        name: 'dept',
        component: DeptView
      },
      {
        path :'class',
        name: 'class',
        component: ClassView
      },
      {
        path :'student',
        name: 'student',
        component: StudentView
      },
      {
        path :'report',
        name: 'report',
        children: [
          {
            path: 'emp',
            name: 'empReport',
            component: EmpReportView
          },
          {
            path: 'student',
            name: 'studentReport',
            component: StudentReportView
          }
        ]
      },
      {
        path: 'log',
        name: 'log',
        component: LogView
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router