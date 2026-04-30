import request from '@/utils/request'

export function getContactList(params) {
  return request({
    url: '/api/contact/list',
    method: 'get',
    params
  })
}

export function getContactPage(params) {
  return request({
    url: '/api/contact/page',
    method: 'get',
    params
  })
}

export function syncContact() {
  return request({
    url: '/api/contact/sync',
    method: 'post'
  })
}