<template>
  <div class="leave-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>请假审批</span>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="userName" label="申请人" width="120" />
        <el-table-column prop="leaveType" label="请假类型" width="100">
          <template #default="scope">
            <el-tag>{{ getLeaveTypeText(scope.row.leaveType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="leaveDays" label="请假天数" width="100" />
        <el-table-column prop="reason" label="请假原因" min-width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <template v-if="scope.row.status === '0'">
              <el-button type="success" link size="small" @click="handleApprove(scope.row, '1')">
                通过
              </el-button>
              <el-button type="danger" link size="small" @click="handleApprove(scope.row, '2')">
                拒绝
              </el-button>
            </template>
            <el-button type="primary" link size="small" @click="handleView(scope.row)">
              详情
            </el-button>
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

    <el-dialog title="审批意见" v-model="approveVisible" width="500px">
      <el-form :model="approveForm" label-width="80px">
        <el-form-item label="审批意见">
          <el-input v-model="approveForm.comment" type="textarea" placeholder="请输入审批意见" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApprove">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const total = ref(0)
const tableData = ref([])
const approveVisible = ref(false)
const currentApproveId = ref(null)
const approveStatus = ref('')

const queryParams = reactive({
  current: 1,
  size: 10
})

const approveForm = reactive({
  comment: ''
})

const getLeaveTypeText = (type) => {
  const types = {
    '1': '事假',
    '2': '病假',
    '3': '年假',
    '4': '婚假',
    '5': '产假'
  }
  return types[type] || '其他'
}

const getStatusTag = (status) => {
  if (status === '0') return 'warning'
  if (status === '1') return 'success'
  return 'danger'
}

const getStatusText = (status) => {
  if (status === '0') return '待审批'
  if (status === '1') return '已通过'
  return '已拒绝'
}

const getList = async () => {
  loading.value = true
  try {
    const res = await request({
      url: '/api/leave/page',
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

const handleView = (row) => {
  ElMessageBox.alert(`
    <p><strong>申请人：</strong>${row.userName || '-'}</p>
    <p><strong>请假类型：</strong>${getLeaveTypeText(row.leaveType)}</p>
    <p><strong>开始日期：</strong>${row.startDate}</p>
    <p><strong>结束日期：</strong>${row.endDate}</p>
    <p><strong>请假天数：</strong>${row.leaveDays}天</p>
    <p><strong>请假原因：</strong>${row.reason}</p>
    <p><strong>审批意见：</strong>${row.approveComment || '-'}</p>
    <p><strong>状态：</strong>${getStatusText(row.status)}</p>
  `, '请假详情', {
    dangerouslyUseHTMLString: true
  })
}

const handleApprove = (row, status) => {
  currentApproveId.value = row.id
  approveStatus.value = status
  approveForm.comment = ''
  approveVisible.value = true
}

const submitApprove = async () => {
  const res = await request({
    url: '/api/leave/approve',
    method: 'post',
    data: {
      id: currentApproveId.value,
      status: approveStatus.value,
      approveComment: approveForm.comment
    }
  })
  if (res.code === 200) {
    ElMessage.success('审批成功')
    approveVisible.value = false
    getList()
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.leave-container {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>