import request from '@/utils/request'

export function getNoteList() {
  return request({
    url: '/api/note/myList',
    method: 'get'
  })
}

export function getNoteById(id) {
  return request({
    url: '/api/note/' + id,
    method: 'get'
  })
}

export function addNote(data) {
  return request({
    url: '/api/note',
    method: 'post',
    data
  })
}

export function updateNote(data) {
  return request({
    url: '/api/note',
    method: 'put',
    data
  })
}

export function deleteNote(id) {
  return request({
    url: '/api/note/' + id,
    method: 'delete'
  })
}