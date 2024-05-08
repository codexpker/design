import request from '@/utils/request'

export default{
  getRoleList(search){
    return request({
      url: '/role/list',
      method: 'get',
      params: {
        pageNo: search.pageNo,
        pageSize: search.pageSize,
        roleName: search.roleName
      }
    })
  },
  addRole(role){
    return request({
      url: '/role',
      method: 'post',
      data: role
    })
  },
  updateRole(role){
    return request({
      url: '/role',
      method: 'put',
      data: role
    })
  },
  saveRole(role){
    if(role.roleId == null || role.roleId == undefined){
      return this.addRole(role);
    }
    return this.updateRole(role);
  },
  getRoleById(roleId){
    return request({
      url: `/role/${roleId}`,
      method: 'get'
    })
  },
  deleteRoleById(roleId){
    return request({
      url: `/role/${roleId}`,
      method: 'delete'
    })
  },
  getAllRoleList(){
    return request({
      url: '/role/all',
      method: 'get',
    })
  },
}
