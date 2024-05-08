<template>
  <div>
    <!-- 搜索栏 -->
    <el-card>
      <el-row>
        <el-col :span="20">
          <el-input
            v-model="search.roleName"
            placeholder="请输入角色名称"
          ></el-input>
          <el-button
            type="primary"
            @click="getRoleList"
            round
            icon="el-icon-search"
            >搜索</el-button
          >
        </el-col>
        <el-col :span="4" align="right">
          <el-button
            @click="openEdit(null)"
            type="primary"
            round
            icon="el-icon-plus"
            circle
          ></el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 表格 -->
    <el-card>
      <el-table :data="roleList" stripe style="width: 100%">
        <el-table-column prop="roleId" label="#" width="180">
          <template slot-scope="scope">
            {{ (search.pageNo - 1) * search.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="roleId" label="角色编号" width="180">
        </el-table-column>
        <el-table-column prop="roleName" label="角色名称" width="180">
        </el-table-column>
        <el-table-column prop="roleDesc" label="角色描述"> </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              @click="openEdit(scope.row.roleId)"
              type="primary"
              icon="el-icon-edit"
              circle
              size="medium"
            ></el-button>
            <el-button
              @click="deleteRole(scope.row)"
              type="danger"
              icon="el-icon-delete"
              circle
              size="medium"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="search.pageNo"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="search.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

    <!-- 对话框 -->
    <el-dialog
      @close="clearForm"
      :title="title"
      :visible.sync="dialogFormVisible"
    >
      <el-form :model="roleForm" :rules="rules" ref="roleFormRef">
        <el-form-item
          label="角色名称"
          prop="roleName"
          :label-width="formLabelWidth"
        >
          <el-input v-model="roleForm.roleName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="角色描述"
          prop="roleDesc"
          :label-width="formLabelWidth"
        >
          <el-input v-model="roleForm.roleDesc" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="权限设置"
          prop="menuIdList"
          :label-width="formLabelWidth"
        >
          <el-tree
            :data="menuList"
            node-key="menuId"
            :props="menuProps"
            ref="menuRef"
            show-checkbox
            default-expand-all
          ></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRole">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import roleApi from "@/api/roleManager";
import menuApi from "@/api/menuManager";
export default {
  data() {
    return {
      menuList: [],
      menuProps: {
        children: "children",
        label: "title",
      },
      roleForm: {},
      title: "",
      formLabelWidth: "130px",
      dialogFormVisible: false,
      total: 0,
      roleList: [],
      search: {
        pageNo: 1,
        pageSize: 5,
      },
      rules: {
        roleName: [
          { required: true, message: "请输入角色名称", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "长度在 2 到 20 个字符",
            trigger: "blur",
          },
        ],
        roleDesc: [
          { required: true, message: "请输入角色描述", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "长度在 2 到 20 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    getAllMenu() {
      menuApi.getAllMenu().then((resp) => {
        this.menuList = resp.data;
      });
    },
    deleteRole(role) {
      //确认删除
      this.$confirm(`您确认删除角色${role.roleName}?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          roleApi.deleteRoleById(role.roleId).then((resp) => {
            this.$message({
              type: "success",
              message: resp.message,
            });
            this.getRoleList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    saveRole() {
      //表单验证
      this.$refs.roleFormRef.validate((valid) => {
        if (valid) {
          let checkedKeys = this.$refs.menuRef.getCheckedKeys();
          let halfCheckedKeys = this.$refs.menuRef.getHalfCheckedKeys();
          this.roleForm.menuIdList = checkedKeys.concat(halfCheckedKeys);
          console.log(this.roleForm.menuIdList);
          roleApi.saveRole(this.roleForm).then((resp) => {
            this.$message({
              message: resp.message,
              type: "success",
            });
            //关闭窗口
            this.dialogFormVisible = false;
            //刷新列表
            this.getRoleList();
          });
          //验证成功
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    clearForm() {
      this.roleForm = {};
      this.$refs.roleFormRef.clearValidate();
      this.$refs.menuRef.setCheckedKeys([]);
    },
    handleCurrentChange(pageNo) {
      this.search.pageNo = pageNo;
      this.getRoleList();
    },
    handleSizeChange(pageSize) {
      this.search.pageSize = pageSize;
      this.getRoleList();
    },
    openEdit(roleId) {
      if (roleId == null) {
        this.title = "添加角色";
      } else {
        this.title = "修改角色";
        //获取用户信息
        roleApi.getRoleById(roleId).then((resp) => {
          this.roleForm = resp.data;
          this.$refs.menuRef.setCheckedKeys(resp.data.menuIdList);
        });
      }
      this.dialogFormVisible = true;
    },
    getRoleList() {
      roleApi.getRoleList(this.search).then((resp) => {
        this.roleList = resp.data.rows;
        this.total = resp.data.total;
      });
    },
  },
  created() {
    this.getRoleList();
    this.getAllMenu();
  },
};
</script>

<style>
div {
  overflow: hidden;
}
.el-col .el-input {
  width: 200px;
  margin-right: 20px;
}

.el-dialog .el-input {
  width: 85%;
}
</style>