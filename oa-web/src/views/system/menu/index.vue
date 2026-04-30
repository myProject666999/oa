<template>
  <div class="menu-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增菜单
          </el-button>
        </div>
      </template>
      
      <el-table :data="menuTree" v-loading="loading" row-key="id" default-expand-all>
        <el-table-column prop="menuName" label="菜单名称" width="180">
          <template #default="scope">
            <el-icon v-if="scope.row.icon" style="margin-right: 5px;">
              <component :is="scope.row.icon" />
            </el-icon>
            {{ scope.row.menuName }}
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由地址" width="150" />
        <el-table-column prop="component" label="组件路径" width="200" />
        <el-table-column prop="perms" label="权限标识" width="200" />
        <el-table-column prop="menuType" label="类型" width="80">
          <template #default="scope">
            <el-tag :type="getMenuTypeTag(scope.row.menuType)">
              {{ getMenuTypeText(scope.row.menuType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
              {{ scope.row.status === '0' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" link size="small" @click="handleAddChild(scope.row)">
              新增
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
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="form.parentId"
            :data="menuTree"
            :props="{ label: 'menuName', value: 'id', children: 'children' }"
            placeholder="请选择上级菜单"
            check-strictly
            :default-expand-all="true"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="form.menuType">
            <el-radio label="M">目录</el-radio>
            <el-radio label="C">菜单</el-radio>
            <el-radio label="F">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="路由地址" v-if="form.menuType !== 'F'">
          <el-input v-model="form.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="组件路径" v-if="form.menuType === 'C'">
          <el-input v-model="form.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="权限标识" v-if="form.menuType === 'F'">
          <el-input v-model="form.perms" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="图标" v-if="form.menuType !== 'F'">
          <el-input v-model="form.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
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
const menuTree = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增菜单')
const formRef = ref(null)

const form = reactive({
  id: null,
  parentId: 0,
  menuName: '',
  menuType: 'C',
  path: '',
  component: '',
  perms: '',
  icon: '',
  orderNum: 0,
  status: '0'
})

const rules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  orderNum: [{ required: true, message: '请输入排序', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'blur' }]
}

const getMenuTypeTag = (type) => {
  if (type === 'M') return 'warning'
  if (type === 'C') return 'primary'
  return 'success'
}

const getMenuTypeText = (type) => {
  if (type === 'M') return '目录'
  if (type === 'C') return '菜单'
  return '按钮'
}

const getList = async () => {
  loading.value = true
  try {
    const res = await request({
      url: '/api/menu/tree',
      method: 'get'
    })
    if (res.code === 200) {
      menuTree.value = res.data || []
    }
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  form.id = null
  form.parentId = 0
  form.menuName = ''
  form.menuType = 'C'
  form.path = ''
  form.component = ''
  form.perms = ''
  form.icon = ''
  form.orderNum = 0
  form.status = '0'
}

const handleAdd = () => {
  dialogTitle.value = '新增菜单'
  resetForm()
  dialogVisible.value = true
}

const handleAddChild = (row) => {
  dialogTitle.value = '新增子菜单'
  resetForm()
  form.parentId = row.id
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑菜单'
  form.id = row.id
  form.parentId = row.parentId || 0
  form.menuName = row.menuName
  form.menuType = row.menuType || 'C'
  form.path = row.path || ''
  form.component = row.component || ''
  form.perms = row.perms || ''
  form.icon = row.icon || ''
  form.orderNum = row.orderNum || 0
  form.status = row.status || '0'
  dialogVisible.value = true
}

const submitForm = async () => {
  await formRef.value.validate()
  const url = form.id ? '/api/menu' : '/api/menu'
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
  ElMessageBox.confirm('确定要删除该菜单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await request({
      url: '/api/menu/' + row.id,
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
.menu-container {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>