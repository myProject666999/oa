import request from '@/utils/request'

export function getAnnouncementList() {
  return request({
    url: '/api/announcement/list',
    method: 'get'
  })
}

export function getAnnouncementPage(params) {
  return request({
    url: '/api/announcement/page',
    method: 'get',
    params
  })
}

export function getAnnouncementById(id) {
  return request({
    url: '/api/announcement/' + id,
    method: 'get'
  })
}

export function addAnnouncement(data) {
  return request({
    url: '/api/announcement',
    method: 'post',
    data
  })
}

export function updateAnnouncement(data) {
  return request({
    url: '/api/announcement',
    method: 'put',
    data
  })
}

export function deleteAnnouncement(id) {
  return request({
    url: '/api/announcement/' + id,
    method: 'delete'
  })
}