import request from '@/utils/request'

export function getPostList(params) {
  return request({
    url: '/api/post/list',
    method: 'get',
    params
  })
}

export function getPostPage(params) {
  return request({
    url: '/api/post/page',
    method: 'get',
    params
  })
}

export function getPostById(id) {
  return request({
    url: '/api/post/' + id,
    method: 'get'
  })
}

export function addPost(data) {
  return request({
    url: '/api/post',
    method: 'post',
    data
  })
}

export function updatePost(data) {
  return request({
    url: '/api/post',
    method: 'put',
    data
  })
}

export function deletePost(id) {
  return request({
    url: '/api/post/' + id,
    method: 'delete'
  })
}