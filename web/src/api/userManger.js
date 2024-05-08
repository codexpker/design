import request from '@/utils/request'

export default{
  getUserList(search){
    return request({
      url: '/user/list',
      method: 'get',
      params: {
        pageNo: search.pageNo,
        pageSize: search.pageSize,
        username: search.username,
        phone: search.phone
      }
    })
  },
  addUser(user){
    return request({
      url: '/user',
      method: 'post',
      data: user
    })
  },
  updateUser(user){
    return request({
      url: '/user',
      method: 'put',
      data: user
    })
  },
  getUserById(id){
    return request({
      url: `/user/${id}`,
      method: 'get',
    })
  },
  saveUser(user){
    //等会验证,好像都可以
    if(user.id == null || user.id == undefined){
      return this.addUser(user);
    }
    return this.updateUser(user);
  },
  deleteUserById(id){
    return request({
      url: `/user/${id}`,
      method: 'delete',
    })
  }
}