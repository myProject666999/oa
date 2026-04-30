<template>
  <div class="meeting-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>会议管理</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增会议
          </el-button>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="meetingName" label="会议名称" min-width="200" />
        <el-table-column prop="roomName" label="会议室" width="120" />
        <el-table-column prop="startTime" label="开始时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="organizer" label="组织者" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" link size="small" @click="handleView(scope.row)">
              详情
            </el-button>
            <el-button type="warning" link size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(scope.row)">
              删除
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

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="会议名称" prop="meetingName">
          <el-input v-model="form.meetingName" placeholder="请输入会议名称" />
        </el-form-item>
        <el-form-item label="会议室">
          <el-select v-model="form.roomId" placeholder="请选择会议室" style="width: 100%">
            <el-option
              v-for="room in roomList"
              :key="room.id"
              :label="room.roomName"
              :value="room.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="参会人员">
          <el-input v-model="form.participants" type="textarea" placeholder="请输入参会人员" :rows="2" />
        </el-form-item>
        <el-form-item label="会议内容">
          <el-input v-model="form.content" type="textarea" placeholder="请输入会议内容" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
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
const roomList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增会议')
const formRef = ref(null)

const queryParams = reactive({
  current: 1,
  size: 10
})

const form = reactive({
  id: null,
  meetingName: '',
  roomId: null,
  startTime: null,
  endTime: null,
  participants: '',
  content: ''
})

const rules = {
  meetingName: [{ required: true, message: '请输入会议名称', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN')
}

const getStatusTag = (status) => {
  if (status === '0') return 'info'
  if (status === '1') return 'success'
  return 'warning'
}

const getStatusText = (status) => {
  if (status === '0') return '未开始'
  if (status === '1') return '进行中'
  return '已结束'
}

const getList = async () => {
  loading.value = true
  try {
    const res = await request({
      url: '/api/meeting/page',
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

const getRoomList = async () => {
  const res = await request({
    url: '/api/meeting-room/list',
    method: 'get'
  })
  if (res.code === 200) {
    roomList.value = res.data || []
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增会议'
  form.id = null
  form.meetingName = ''
  form.roomId = null
  form.startTime = null
  form.endTime = null
  form.participants = ''
  form.content = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑会议'
  form.id = row.id
  form.meetingName = row.meetingName
  form.roomId = row.roomId
  form.startTime = row.startTime
  form.endTime = row.endTime
  form.participants = row.participants
  form.content = row.content
  dialogVisible.value = true
}

const handleView = (row) => {
  ElMessageBox.alert(`
    <p><strong>会议名称：</strong>${row.meetingName}</p>
    <p><strong>开始时间：</strong>${formatDateTime(row.startTime)}</p>
    <p><strong>结束时间：</strong>${formatDateTime(row.endTime)}</p>
    <p><strong>参会人员：</strong>${row.participants || '-'}</p>
    <p><strong>会议内容：</strong>${row.content || '-'}</p>
  `, '会议详情', {
    dangerouslyUseHTMLString: true
  })
}

const submitForm = async () => {
  await formRef.value.validate()
  const url = form.id ? '/api/meeting' : '/api/meeting'
  const method = form.id ? 'put' : 'post'
  const res = await request({
    url,
    method,
    data: form
  })
  if (res.code === 200) {
    ElMessage.success(form.id ? '修改成功' : '新增成功')
    dialogVisible.value = false
    getList()
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该会议吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await request({
      url: '/api/meeting/' + row.id,
      method: 'delete'
    })
    if (res.code === 200) {
      ElMessage.success('删除成功')
      getList()
    }
  }).catch(() => {})
}

onMounted(() => {
  getList()
  getRoomList()
})
</script>

<style scoped>
.meeting-container {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>