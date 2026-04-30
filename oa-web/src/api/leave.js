import request from '@/utils/request'

export function getMyLeaveList(params) {
  return request({
    url: '/api/leave/myList',
    method: 'get',
    params
  })
}

export function getLeaveList(params) {
  return request({
    url: '/api/leave/page',
    method: 'get',
    params
  })
}

export function getLeaveById(id) {
  return request({
    url: '/api/leave/' + id,
    method: 'get'
  })
}

export function applyLeave(data) {
  return request({
    url: '/api/leave/apply',
    method: 'post',
    data
  })
}

export function approveLeave(data) {
  return request({
    url: '/api/leave/approve',
    method: 'post',
    data
  })
}

export function deleteLeave(id) {
  return request({
    url: '/api/leave/' + id,
    method: 'delete'
  })
}