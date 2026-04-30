import request from '@/utils/request'

export function checkIn() {
  return request({
    url: '/api/attendance/checkIn',
    method: 'post'
  })
}

export function checkOut() {
  return request({
    url: '/api/attendance/checkOut',
    method: 'post'
  })
}

export function getToday() {
  return request({
    url: '/api/attendance/today',
    method: 'get'
  })
}

export function getMyRecords(params) {
  return request({
    url: '/api/attendance/myRecords',
    method: 'get',
    params
  })
}

export function getAttendanceList(params) {
  return request({
    url: '/api/attendance/page',
    method: 'get',
    params
  })
}

export function getStatisticsList(params) {
  return request({
    url: '/api/attendance/statistics/page',
    method: 'get',
    params
  })
}

export function getMyStatistics(params) {
  return request({
    url: '/api/attendance/myStatistics',
    method: 'get',
    params
  })
}