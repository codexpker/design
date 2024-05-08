<template>
  <div>
    <!-- 搜索栏 -->
    <el-card id="search">
      <el-row>
        <el-col :span="20">
          <el-input
            v-model="search.username"
            placeholder="请输入用户名"
            clearable
          ></el-input>
          <el-input
            v-model="search.phone"
            placeholder="请输入电话号码"
            clearable
          ></el-input>
          <el-button
            @click="getUserList"
            type="primary"
            round
            icon="el-icon-search"
            >搜索</el-button
          >
        </el-col>
        <el-col :span="4" align="right">
          <el-button
            @click="openEdit(null)"
            type="primary"
            icon="el-icon-plus"
            circle
          ></el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 表格 -->
    <el-card>
      <el-table :data="userList" stripe style="width: 100%">
        <el-table-column label="#" width="180">
          <template slot-scope="scope">
            {{ (search.pageNo - 1) * search.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="id" label="用户ID" width="180">
        </el-table-column>
        <el-table-column
          prop="username"
          label="用户名"
          width="180"
        ></el-table-column>
        <el-table-column prop="phone" label="电话号码"> </el-table-column>
        <el-table-column prop="status" label="用户状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status == 1">正常</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱"> </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              @click="openEdit(scope.row.id)"
              type="primary"
              icon="el-icon-edit"
              circle
              size="medium"
            ></el-button>
            <el-button
              @click="deleteUser(scope.row)"
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
      <el-form :model="userForm" :rules="rules" ref="userFormRef">
        <el-form-item
          label="用户名"
          prop="username"
          :label-width="formLabelWidth"
        >
          <el-input v-model="userForm.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          v-if="userForm.id == null || userForm.id == undefined"
          label="登录密码"
          prop="password"
          :label-width="formLabelWidth"
        >
          <el-input
            type="password"
            v-model="userForm.password"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="联系电话" :label-width="formLabelWidth">
          <el-input v-model="userForm.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户状态" :label-width="formLabelWidth">
          <el-switch
            v-model="userForm.status"
            :active-value="1"
            :inactive-value="0"
          >
          </el-switch>
        </el-form-item>
        <el-form-item label="用户角色" :label-width="formLabelWidth">
          <el-checkbox-group
            v-model="userForm.roleIdList"
            :max="2"
            style="width: 85%"
          >
            <el-checkbox
              v-for="role in roleList"
              :label="role.roleId"
              :key="role.roleId"
              >{{ role.roleDesc }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
        <el-form-item
          label="电子邮件"
          prop="email"
          :label-width="formLabelWidth"
        >
          <el-input v-model="userForm.email" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveUser">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import userApi from "@/api/userManger";
import roleApi from "@/api/roleManager";
export default {
  data() {
    var reg =
      /^[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*\.[a-z]{2,}$/;
    var checkEmail = (rule, value, callback) => {
      if (!reg.test(value)) {
        return callback(new Error("邮箱格式错误"));
      } else {
        callback();
      }
    };
    return {
      roleList: [],
      userForm: {
        roleIdList: [],
      },
      formLabelWidth: "130px",
      dialogFormVisible: false,
      title: "",
      total: 0,
      search: {
        pageNo: 1,
        pageSize: 5,
      },
      userList: [],
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 3,
            max: 30,
            message: "长度在 3 到 30 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入用户登录密码", trigger: "blur" },
          {
            min: 6,
            max: 20,
            message: "长度在 6 到 20 个字符",
            trigger: "blur",
          },
        ],
        email: [
          { required: true, message: "请输入电子邮件", trigger: "blur" },
          { validator: checkEmail, trigger: "blur" },
        ],
      },
    };
  },

  methods: {
    deleteUser(user) {
      //确认删除
      this.$confirm(`您确认删除用户${user.username}?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          userApi.deleteUserById(user.id).then((resp) => {
            this.$message({
              type: "success",
              message: resp.message,
            });
            this.getUserList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    saveUser() {
      //表单校验
      this.$refs.userFormRef.validate((valid) => {
        if (valid) {
          //提交给后台
          userApi.saveUser(this.userForm).then((resp) => {
            //成功提示
            this.$message({
              message: resp.message,
              type: "success",
            });
            //关闭窗口
            this.dialogFormVisible = false;
            //刷新表格
            this.getUserList();
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    clearForm() {
      this.userForm = {
        roleIdList: [],
      };
      this.$refs.userFormRef.clearValidate();
    },
    openEdit(id) {
      if (id == null) {
        this.title = "添加用户";
      } else {
        this.title = "修改用户";
        //获取用户信息
        userApi.getUserById(id).then((resp) => {
          this.userForm = resp.data;
        });
      }
      this.dialogFormVisible = true;
    },
    handleSizeChange(pageSize) {
      this.search.pageSize = pageSize;
      this.getUserList();
    },
    handleCurrentChange(pageNo) {
      this.search.pageNo = pageNo;
      this.getUserList();
    },
    getUserList() {
      userApi.getUserList(this.search).then((resp) => {
        this.userList = resp.data.rows;
        this.total = resp.data.total;
      });
    },
    getAllRoleList() {
      roleApi.getAllRoleList().then((resp) => {
        this.roleList = resp.data;
      });
    },
  },
  created() {
    this.getUserList();
    this.getAllRoleList();
  },
};
</script>

<style>
#search .el-input {
  width: 200px;
  margin-right: 20px;
}

.el-dialog .el-input {
  width: 85%;
}
</style>