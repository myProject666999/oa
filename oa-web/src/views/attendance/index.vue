<template>
  <div class="attendance-container">
    <el-card>
      <template #header>
        <span>考勤统计</span>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="年份">
          <el-select v-model="queryParams.year" placeholder="请选择年份" clearable>
            <el-option :label="currentYear" :value="currentYear" />
            <el-option :label="currentYear - 1" :value="currentYear - 1" />
          </el-select>
        </el-form-item>
        <el-form-item label="月份">
          <el-select v-model="queryParams.month" placeholder="请选择月份" clearable>
            <el-option v-for="m in 12" :key="m" :label="m + '月'" :value="m" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="userName" label="员工姓名" width="120">
          <template #default="scope">
            {{ scope.row.userName || '--' }}
          </template>
        </el-table-column>
        <el-table-column prop="year" label="年份" width="80" />
        <el-table-column prop="month" label="月份" width="80">
          <template #default="scope">
            {{ scope.row.month }}月
          </template>
        </el-table-column>
        <el-table-column prop="workDays" label="工作日数" width="100" />
        <el-table-column prop="attendDays" label="出勤天数" width="100">
          <template #default="scope">
            <el-tag type="success">{{ scope.row.attendDays }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lateDays" label="迟到天数" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.lateDays > 0 ? 'warning' : 'info'">
              {{ scope.row.lateDays }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="earlyDays" label="早退天数" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.earlyDays > 0 ? 'warning' : 'info'">
              {{ scope.row.earlyDays }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="absenceDays" label="缺勤天数" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.absenceDays > 0 ? 'danger' : 'info'">
              {{ scope.row.absenceDays }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="leaveDays" label="请假天数" width="100" />
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
import { ref, reactive, computed, onMounted } from 'vue'
import request from '@/utils/request'

const loading = ref(false)
const total = ref(0)
const tableData = ref([])
const currentYear = computed(() => new Date().getFullYear())

const queryParams = reactive({
  current: 1,
  size: 10,
  year: new Date().getFullYear(),
  month: null
})

const getList = async () => {
  loading.value = true
  try {
    const res = await request({
      url: '/api/attendance/statistics/page',
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

const handleQuery = () => {
  queryParams.current = 1
  getList()
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.attendance-container {
  padding: 0;
}

.search-form {
  margin-bottom: 20px;
}
</style>