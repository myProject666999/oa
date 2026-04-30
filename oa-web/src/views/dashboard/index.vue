<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="card">
          <div class="card-content">
            <div class="card-info">
              <p class="card-text">今日考勤</p>
              <p class="card-num">{{ statistics.attendanceCount || 0 }}</p>
            </div>
            <div class="card-icon">
              <el-icon class="icon-calendar"><Calendar /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="card">
          <div class="card-content">
            <div class="card-info">
              <p class="card-text">待审批请假</p>
              <p class="card-num">{{ statistics.pendingLeave || 0 }}</p>
            </div>
            <div class="card-icon">
              <el-icon class="icon-document"><Document /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="card">
          <div class="card-content">
            <div class="card-info">
              <p class="card-text">本月会议</p>
              <p class="card-num">{{ statistics.meetingCount || 0 }}</p>
            </div>
            <div class="card-icon">
              <el-icon class="icon-meeting"><OfficeBuilding /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="card">
          <div class="card-content">
            <div class="card-info">
              <p class="card-text">公告数量</p>
              <p class="card-num">{{ statistics.announcementCount || 0 }}</p>
            </div>
            <div class="card-icon">
              <el-icon class="icon-bell"><Bell /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="quick-actions">
            <el-button type="primary" size="large" @click="goToCheckIn">
              <el-icon><Calendar /></el-icon>
              <span>签到/签退</span>
            </el-button>
            <el-button type="success" size="large" @click="goToApplyLeave">
              <el-icon><Document /></el-icon>
              <span>申请请假</span>
            </el-button>
            <el-button type="warning" size="large" @click="goToNote">
              <el-icon><EditPen /></el-icon>
              <span>个人便签</span>
            </el-button>
            <el-button type="info" size="large" @click="goToContact">
              <el-icon><Phone /></el-icon>
              <span>通讯录</span>
            </el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>个人信息</span>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="姓名">{{ userStore.userInfo.realName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ userStore.userInfo.username || '-' }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ userStore.userInfo.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userStore.userInfo.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ userStore.userInfo.gender === '1' ? '男' : userStore.userInfo.gender === '2' ? '女' : '-' }}</el-descriptions-item>
            <el-descriptions-item label="状态">{{ userStore.userInfo.status === '0' ? '启用' : '禁用' }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const statistics = ref({
  attendanceCount: 0,
  pendingLeave: 0,
  meetingCount: 0,
  announcementCount: 0
})

onMounted(() => {
  loadStatistics()
})

const loadStatistics = () => {
  statistics.value = {
    attendanceCount: Math.floor(Math.random() * 30) + 10,
    pendingLeave: Math.floor(Math.random() * 10),
    meetingCount: Math.floor(Math.random() * 20) + 5,
    announcementCount: Math.floor(Math.random() * 10) + 2
  }
}

const goToCheckIn = () => {
  router.push('/my-attendance')
}

const goToApplyLeave = () => {
  router.push('/my-leave')
}

const goToNote = () => {
  router.push('/note')
}

const goToContact = () => {
  router.push('/contact')
}
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.card {
  margin-bottom: 20px;
}

.card-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-info {
  flex: 1;
}

.card-text {
  font-size: 14px;
  color: #909399;
  margin: 0 0 10px 0;
}

.card-num {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  margin: 0;
}

.card-icon {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
}

.icon-calendar {
  font-size: 40px;
  color: #409EFF;
}

.icon-document {
  font-size: 40px;
  color: #67C23A;
}

.icon-meeting {
  font-size: 40px;
  color: #E6A23C;
}

.icon-bell {
  font-size: 40px;
  color: #F56C6C;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.quick-actions .el-button {
  width: 120px;
  height: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.quick-actions .el-button .el-icon {
  font-size: 24px;
  margin-bottom: 5px;
}

.quick-actions .el-button span {
  font-size: 12px;
}
</style>