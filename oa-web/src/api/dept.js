import request from '@/utils/request'

export function getDeptList(params) {
  return request({
    url: '/api/dept/list',
    method: 'get',
    params
  })
}

export function getDeptTree(params) {
  return request({
    url: '/api/dept/tree',
    method: 'get',
    params
  })
}

export function getDeptById(id) {
  return request({
    url: '/api/dept/' + id,
    method: 'get'
  })
}

export function addDept(data) {
  return request({
    url: '/api/dept',
    method: 'post',
    data
  })
}

export function updateDept(data) {
  return request({
    url: '/api/dept',
    method: 'put',
    data
  })
}

export function deleteDept(id) {
  return request({
    url: '/api/dept/' + id,
    method: 'delete'
  })
}