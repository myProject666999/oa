<template>
  <div class="contact-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>通讯录</span>
          <el-button type="primary" size="small" @click="handleSync">
            <el-icon><Refresh /></el-icon>
            同步通讯录
          </el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="queryParams.realName" placeholder="请输入姓名" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="realName" label="姓名" width="120" />
        <el-table-column prop="phone" label="手机号" width="150" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="deptName" label="部门" width="150" />
        <el-table-column prop="postName" label="岗位" width="150" />
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.current"
        v-model:page-size="queryParams.size"
        :page-sizes="[10, 20, 50, 100]"
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

const queryParams = reactive({
  current: 1,
  size: 10,
  realName: ''
})

const getList = async () => {
  loading.value = true
  try {
    const res = await request({
      url: '/api/contact/page',
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

const resetQuery = () => {
  queryParams.realName = ''
  handleQuery()
}

const handleSync = async () => {
  try {
    const res = await request({
      url: '/api/contact/sync',
      method: 'post'
    })
    if (res.code === 200) {
      ElMessage.success('同步成功')
      getList()
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.contact-container {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>