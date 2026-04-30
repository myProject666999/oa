<template>
  <div class="leave-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的请假</span>
          <el-button type="primary" size="small" @click="handleApply">
            <el-icon><Plus /></el-icon>
            申请请假
          </el-button>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
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
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="primary" link size="small" @click="handleView(scope.row)">
              详情
            </el-button>
            <el-button
              v-if="scope.row.status === '0'"
              type="danger"
              link
              size="small"
              @click="handleCancel(scope.row)"
            >
              撤销
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

    <el-dialog title="申请请假" v-model="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="请假类型" prop="leaveType">
          <el-select v-model="form.leaveType" placeholder="请选择请假类型" style="width: 100%">
            <el-option label="事假" value="1" />
            <el-option label="病假" value="2" />
            <el-option label="年假" value="3" />
            <el-option label="婚假" value="4" />
            <el-option label="产假" value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
            v-model="form.startDate"
            type="date"
            placeholder="选择开始日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker
            v-model="form.endDate"
            type="date"
            placeholder="选择结束日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="请假天数" prop="leaveDays">
          <el-input-number v-model="form.leaveDays" :min="0.5" :step="0.5" :precision="1" />
        </el-form-item>
        <el-form-item label="请假原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" placeholder="请输入请假原因" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const loading = ref(false)
const total = ref(0)
const tableData = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const queryParams = reactive({
  current: 1,
  size: 10
})

const form = reactive({
  leaveType: '',
  startDate: '',
  endDate: '',
  leaveDays: 1,
  reason: ''
})

const rules = {
  leaveType: [{ required: true, message: '请选择请假类型', trigger: 'change' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
  leaveDays: [{ required: true, message: '请输入请假天数', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入请假原因', trigger: 'blur' }]
}

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
      url: '/api/leave/myList',
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

const handleApply = () => {
  form.leaveType = ''
  form.startDate = ''
  form.endDate = ''
  form.leaveDays = 1
  form.reason = ''
  dialogVisible.value = true
}

const handleView = (row) => {
  ElMessageBox.alert(`
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

const submitForm = async () => {
  await formRef.value.validate()
  const res = await request({
    url: '/api/leave/apply',
    method: 'post',
    data: form
  })
  if (res.code === 200) {
    ElMessage.success('申请成功')
    dialogVisible.value = false
    getList()
  }
}

const handleCancel = (row) => {
  ElMessageBox.confirm('确定要撤销该请假申请吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await request({
      url: '/api/leave/' + row.id,
      method: 'delete'
    })
    if (res.code === 200) {
      ElMessage.success('撤销成功')
      getList()
    }
  }).catch(() => {})
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