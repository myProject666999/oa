<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="user-info">
            <el-avatar :size="100" icon="UserFilled" />
            <h3>{{ userInfo.realName || userInfo.username }}</h3>
            <p class="role">{{ roles.join(', ') }}</p>
          </div>
          <el-divider />
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ userInfo.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userInfo.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="性别">
              {{ userInfo.gender === '1' ? '男' : userInfo.gender === '2' ? '女' : '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="userInfo.status === '0' ? 'success' : 'danger'">
                {{ userInfo.status === '0' ? '正常' : '停用' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      
      <el-col :span="16">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="info">
            <el-card>
              <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
                <el-form-item label="姓名" prop="realName">
                  <el-input v-model="form.realName" placeholder="请输入姓名" />
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="form.phone" placeholder="请输入手机号" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="form.email" placeholder="请输入邮箱" />
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model="form.gender">
                    <el-radio label="1">男</el-radio>
                    <el-radio label="2">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitForm">保存修改</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="修改密码" name="password">
            <el-card>
              <el-form :model="pwdForm" :rules="pwdRules" ref="pwdFormRef" label-width="120px">
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input v-model="pwdForm.oldPassword" type="password" placeholder="请输入原密码" show-password />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="pwdForm.newPassword" type="password" placeholder="请输入新密码" show-password />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input v-model="pwdForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitPwdForm">确认修改</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import request from '@/utils/request'

const userStore = useUserStore()
const activeTab = ref('info')
const formRef = ref(null)
const pwdFormRef = ref(null)

const userInfo = computed(() => userStore.userInfo)
const roles = computed(() => userStore.roles)

const form = reactive({
  realName: '',
  phone: '',
  email: '',
  gender: ''
})

const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== pwdForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
}

const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const loadUserInfo = async () => {
  const res = await request({
    url: '/api/user/profile',
    method: 'get'
  })
  if (res.code === 200) {
    form.realName = res.data.realName || ''
    form.phone = res.data.phone || ''
    form.email = res.data.email || ''
    form.gender = res.data.gender || ''
  }
}

const submitForm = async () => {
  await formRef.value.validate()
  const res = await request({
    url: '/api/user/profile',
    method: 'put',
    data: form
  })
  if (res.code === 200) {
    ElMessage.success('修改成功')
    await userStore.getUserInfo()
  }
}

const submitPwdForm = async () => {
  await pwdFormRef.value.validate()
  const res = await request({
    url: '/api/user/password',
    method: 'put',
    params: {
      oldPassword: pwdForm.oldPassword,
      newPassword: pwdForm.newPassword
    }
  })
  if (res.code === 200) {
    ElMessage.success('密码修改成功')
    pwdForm.oldPassword = ''
    pwdForm.newPassword = ''
    pwdForm.confirmPassword = ''
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 0;
}

.user-info {
  text-align: center;
  padding: 20px 0;
}

.user-info h3 {
  margin: 15px 0 5px 0;
  font-size: 20px;
}

.user-info .role {
  color: #909399;
  font-size: 14px;
}
</style>