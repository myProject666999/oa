<template>
  <div class="note-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>个人便签</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增便签
          </el-button>
        </div>
      </template>
      
      <el-row :gutter="20" v-loading="loading">
        <el-col :span="6" v-for="item in noteList" :key="item.id">
          <el-card :style="{ backgroundColor: item.color || '#ffffff' }" class="note-card">
            <div class="note-header">
              <span class="note-title">{{ item.title || '无标题' }}</span>
              <el-dropdown trigger="click">
                <el-icon><MoreFilled /></el-icon>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleEdit(item)">编辑</el-dropdown-item>
                    <el-dropdown-item @click="handleDelete(item)" divided>删除</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <div class="note-content">{{ item.content || '暂无内容' }}</div>
            <div class="note-footer">
              <span>{{ formatDate(item.createTime) }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" placeholder="请输入内容" :rows="6" />
        </el-form-item>
        <el-form-item label="颜色">
          <el-radio-group v-model="form.color">
            <el-radio label="#ffffff">白色</el-radio>
            <el-radio label="#fff9e6">黄色</el-radio>
            <el-radio label="#e6f7ff">蓝色</el-radio>
            <el-radio label="#f6ffed">绿色</el-radio>
            <el-radio label="#fff1f0">红色</el-radio>
          </el-radio-group>
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
const noteList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增便签')
const formRef = ref(null)

const form = reactive({
  id: null,
  title: '',
  content: '',
  color: '#ffffff'
})

const formatDate = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleDateString('zh-CN')
}

const getList = async () => {
  loading.value = true
  try {
    const res = await request({
      url: '/api/note/myList',
      method: 'get'
    })
    if (res.code === 200) {
      noteList.value = res.data || []
    }
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增便签'
  form.id = null
  form.title = ''
  form.content = ''
  form.color = '#ffffff'
  dialogVisible.value = true
}

const handleEdit = (item) => {
  dialogTitle.value = '编辑便签'
  form.id = item.id
  form.title = item.title
  form.content = item.content
  form.color = item.color || '#ffffff'
  dialogVisible.value = true
}

const submitForm = async () => {
  const url = form.id ? '/api/note' : '/api/note'
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

const handleDelete = (item) => {
  ElMessageBox.confirm('确定要删除该便签吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await request({
      url: '/api/note/' + item.id,
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
})
</script>

<style scoped>
.note-container {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.note-card {
  margin-bottom: 20px;
  min-height: 180px;
  cursor: pointer;
  transition: all 0.3s;
}

.note-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.note-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.note-title {
  font-weight: bold;
  font-size: 14px;
  color: #303133;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.note-content {
  font-size: 13px;
  color: #606266;
  line-height: 1.6;
  max-height: 80px;
  overflow: hidden;
}

.note-footer {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}
</style>