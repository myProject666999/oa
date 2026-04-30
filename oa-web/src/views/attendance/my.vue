<template>
  <div class="attendance-container">
    <el-card>
      <template #header>
        <span>今日考勤</span>
      </template>
      
      <div class="today-attendance">
        <el-row :gutter="40">
          <el-col :span="6">
            <div class="attendance-item">
              <div class="icon" style="background-color: #409EFF;">
                <el-icon><Calendar /></el-icon>
              </div>
              <div class="info">
                <p class="label">签到时间</p>
                <p class="value">{{ todayRecord?.checkInTime ? formatTime(todayRecord.checkInTime) : '--:--' }}</p>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="attendance-item">
              <div class="icon" style="background-color: #67C23A;">
                <el-icon><Timer /></el-icon>
              </div>
              <div class="info">
                <p class="label">签退时间</p>
                <p class="value">{{ todayRecord?.checkOutTime ? formatTime(todayRecord.checkOutTime) : '--:--' }}</p>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="attendance-item">
              <div class="icon" :style="{ backgroundColor: getStatusColor(todayRecord?.checkInStatus) }">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="info">
                <p class="label">签到状态</p>
                <p class="value" :style="{ color: getStatusColor(todayRecord?.checkInStatus) }">
                  {{ getStatusText(todayRecord?.checkInStatus) }}
                </p>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="attendance-item">
              <div class="icon" :style="{ backgroundColor: getStatusColor(todayRecord?.checkOutStatus) }">
                <el-icon><Checked /></el-icon>
              </div>
              <div class="info">
                <p class="label">签退状态</p>
                <p class="value" :style="{ color: getStatusColor(todayRecord?.checkOutStatus) }">
                  {{ getStatusText(todayRecord?.checkOutStatus) }}
                </p>
              </div>
            </div>
          </el-col>
        </el-row>
        
        <div class="action-buttons">
          <el-button type="primary" size="large" :disabled="todayRecord?.checkInTime" @click="handleCheckIn">
            <el-icon><CircleCheck /></el-icon>
            签到
          </el-button>
          <el-button type="success" size="large" :disabled="!todayRecord?.checkInTime || todayRecord?.checkOutTime" @click="handleCheckOut">
            <el-icon><CircleCheck /></el-icon>
            签退
          </el-button>
        </div>
      </div>
    </el-card>

    <el-card style="margin-top: 20px;">
      <template #header>
        <span>考勤记录</span>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="attendDate" label="日期" width="120" />
        <el-table-column prop="checkInTime" label="签到时间" width="180">
          <template #default="scope">
            {{ scope.row.checkInTime ? formatDateTime(scope.row.checkInTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="checkInStatus" label="签到状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.checkInStatus === '0' ? 'success' : 'warning'">
              {{ scope.row.checkInStatus === '0' ? '正常' : '迟到' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkOutTime" label="签退时间" width="180">
          <template #default="scope">
            {{ scope.row.checkOutTime ? formatDateTime(scope.row.checkOutTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="checkOutStatus" label="签退状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.checkOutStatus === '0' ? 'success' : 'warning'">
              {{ scope.row.checkOutStatus === '0' ? '正常' : '早退' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.current"
        v-model:page-size="queryParams.size"
        :page-sizes="[10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="getList"
        @current-change="getList"
        style="margin-top: 20px;"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const total = ref(0)
const tableData = ref([])
const todayRecord = ref(null)

const queryParams = reactive({
  current: 1,
  size: 10
})

const formatTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toTimeString().slice(0, 5)
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN')
}

const getStatusColor = (status) => {
  if (status === '0') return '#67C23A'
  if (status === '1') return '#E6A23C'
  return '#909399'
}

const getStatusText = (status) => {
  if (status === '0') return '正常'
  if (status === '1') return '迟到/早退'
  return '未打卡'
}

const getTodayRecord = async () => {
  const res = await request({
    url: '/api/attendance/today',
    method: 'get'
  })
  if (res.code === 200) {
    todayRecord.value = res.data
  }
}

const getList = async () => {
  loading.value = true
  try {
    const res = await request({
      url: '/api/attendance/myRecords',
      method: 'get',
      params: queryParams
    })
    if (res.code === 200) {
      tableData.value = res.data.records || []
      total.value = res.data.total || 0
    }
  } finally {
    loading.value = false
  }
}

const handleCheckIn = async () => {
  try {
    const res = await request({
      url: '/api/attendance/checkIn',
      method: 'post'
    })
    if (res.code === 200) {
      ElMessage.success('签到成功')
      getTodayRecord()
    }
  } catch (error) {
    console.error(error)
  }
}

const handleCheckOut = async () => {
  try {
    const res = await request({
      url: '/api/attendance/checkOut',
      method: 'post'
    })
    if (res.code === 200) {
      ElMessage.success('签退成功')
      getTodayRecord()
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  getTodayRecord()
  getList()
})
</script>

<style scoped>
.attendance-container {
  padding: 0;
}

.today-attendance {
  padding: 20px 0;
}

.attendance-item {
  display: flex;
  align-items: center;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.attendance-item .icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 28px;
}

.attendance-item .info {
  margin-left: 20px;
}

.attendance-item .info .label {
  color: #909399;
  font-size: 14px;
  margin: 0 0 5px 0;
}

.attendance-item .info .value {
  font-size: 20px;
  font-weight: bold;
  margin: 0;
  color: #303133;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-top: 30px;
}

.action-buttons .el-button {
  width: 150px;
  height: 50px;
  font-size: 16px;
}
</style>