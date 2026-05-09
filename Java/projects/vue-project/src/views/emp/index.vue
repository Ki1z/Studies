<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import { ElMessage } from 'element-plus'
import { StarFilled } from '@element-plus/icons-vue'

// 搜索表单
const name = ref('');
const sex = ref('');
const begin = ref('');
const end = ref('');
const pageSize = ref(10);
const page = ref(1);

// 员工列表
const emp_list = ref([]);
// 部门列表
const dept_list = ref([]);
// 职位列表
const job_list = ref([]);

// 新增员工对话框是否可见
const addEmpDialogVisible = ref(false);

// 新增员工属性
const emp = ref({
    name: null,
    birth: null,
    sex: null,
    avatarPath: null,
    deptName: null,
    jobName: null,
    boardDate: null,
    empExpList: []
});
// 新增员工方法
const addEmp = async () => {
    const result = await axios.post('/api/emps', emp.value);
    if (result.data.code !== 1) {
        ElMessage.error(`添加失败！可能的原因：${result.data.msg}`);
    } else {
        ElMessage.success(`添加成功！`);
    }
    addEmpDialogVisible.value = false;
    getEmpList();
    clearForm();
}

// 打开创建员工对话框方法
const openAddEmpDialog = () => {
    addEmpDialogVisible.value = true;
    clearForm();
    getDeptList();
    getJobList();
}

// 清空表单方法
const clearForm = () => {
    emp.value = { name: null, birth: null, sex: null, avatarPath: null, deptName: null, jobName: null, boardDate: null, empExpList: [] };
}

// 头像上传限制方法
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png' && rawFile.type !== 'image/jpg') {
    ElMessage.error('必须为jpeg、png、jpg格式的图片')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('文件大小不能超过2MiB')
    return false
  }
  return true
}

// 头像上传成功方法
const handleAvatarSuccess = (response) => {
    emp.value.avatarPath = response.data;
    ElMessage.success('上传成功')
}

// 获取员工列表方法
const getEmpList = async () => {
    const result = await axios.get('/api/emps', {
        params: {
            name: name.value,
            sex: sex.value,
            begin: begin.value,
            end: end.value,
            pageSize: pageSize.value,
            page: page.value
        }
    })
    emp_list.value = result.data.data;
}

// 获取部门列表方法
const getDeptList = async () => {
    const result = await axios.get('/api/depts')
    dept_list.value = result.data.data;
}

// 获取职位列表方法
const getJobList = async () => {
    const result = await axios.get('/api/jobs')
    job_list.value = result.data.data;
}

// 删除工作经历方法
const deleteEmpExp = async (index) => {
    emp.value.empExpList.splice(index, 1);
}

// 新增工作经历方法
const addEmpExp = () => {
    emp.value.empExpList.push({
        "startTime": null,
        "endTime": null,
        "company": null,
        "job": null
    });
}

// 打开更新员工对话框方法
const openUpdateEmpDialog = async (id) => {
    const result = await axios.get(`/api/emps/${id}`);
    emp.value = result.data.data;
    
}

// 表格组件
const table = ref();
// 删除员工列表
const deleteEmpList = ref([]);

// 删除员工多选框监听器方法
const handleSelectionChange = () => {
    deleteEmpList.value = table.value.getSelectionRows();
}

// 删除员工对话框是否可见
const deleteEmpDialogVisible = ref(false);

// 删除员工方法
const deleteEmp = async () => {
    deleteEmpDialogVisible.value = false;
    const deleteEmpListId = deleteEmpList.value.map(emp => emp.id);
    const result = await axios.delete('/api/emps', {
        params: {
            ids: deleteEmpListId
        }
    })
    if (result.data.code !== 1) {
        ElMessage.error(`删除失败！可能的原因：${result.data.msg}`);

    } else {
        ElMessage.success(`删除成功！`);
    }
    getEmpList();
    handleSelectionChange();
}

onMounted(() => {
    getEmpList();
});
</script>

<template>
<div id="emp">
    <div class="search">
        <span>
            <label>员工姓名：</label>
            <el-input v-model="name" style="width: 150px" placeholder="请输入姓名" />
        </span>
        <span>
            <label>性别：</label>
            <el-select v-model="sex" placeholder="选择性别" style="width: 80px">
                <el-option label="全部" value="" />
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
            </el-select>
        </span>
        <span>
            <label>入职日期范围：</label>
            <el-date-picker
                v-model="begin"
                type="date"
                placeholder="开始"
                value-format="YYYY-M-d"
                style="width: 150px"
            />
            <label style="margin: 0 10px">到</label>
            <el-date-picker
                v-model="end"
                type="date"
                placeholder="结束"
                value-format="YYYY-M-d"
                style="width: 150px"
            />
        </span>
        <el-button type="primary" @click="getEmpList">搜索</el-button>
        <el-button type="success" @click="openAddEmpDialog">新增员工</el-button>
        <el-button type="danger" @click="deleteEmpDialogVisible = true" v-show="deleteEmpList.length > 0">删除员工</el-button>
    </div>
    <div class="list">
        <el-table 
            :data="emp_list.rows"
            ref="table"
            width="100%"
            @selection-change="handleSelectionChange"
        >
            <el-table-column width="100" type="selection" />
            <el-table-column fixed prop="id" label="工号" width="100" />
            <el-table-column prop="name" label="Name" width="150" />
            <el-table-column prop="birth" label="出生日期" width="150" />
            <el-table-column prop="sex" label="性别" width="120" />
            <el-table-column label="头像" width="100">
                <template #default="scope">
                    <el-avatar :src="scope.row.avatarPath">
                        <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png" />
                    </el-avatar>
                </template>
            </el-table-column>
            <el-table-column prop="deptName" label="部门" width="120" />
            <el-table-column prop="jobName" label="职位" width="120" />
            <el-table-column prop="boardDate" label="入职日期" width="150" />
            <el-table-column prop="updateTime" label="上次修改时间" width="200" />
            <el-table-column fixed="right" label="操作" min-width="80">
            <template #default="scope">
                <el-button link type="primary" @click="openUpdateEmpDialog(scope.row.id)">更新</el-button>
            </template>
            </el-table-column>
        </el-table>
    </div>
    <div class="page">
        <el-pagination
            v-model:current-page="page"
            v-model:page-size="pageSize"
            :page-sizes="[5, 10, 20, 50, 100]"
            layout="total, ->, sizes, prev, pager, next, jumper"
            :total=emp_list.total
            @change="getEmpList" 
        />
    </div>
    <div class="addEmpDialog">
        <el-dialog
            v-model="addEmpDialogVisible"
            width="40%"
            align="left"
            style="margin-top: 100px;"
        >
            <template #header>
                <div class="my-header">
                    <h1 style="font-size: 24px;margin: 1px;">新增员工</h1>
                </div>
            </template>
            <div class="addEmpDialog-body">
                <div class="addEmpDialog-body-info" style="margin-top: -30px; font-size: 16px;">
                    <el-divider content-position="center">员工基本信息</el-divider>
                    <!-- 头像 -->
                    <div class="addEmpDialog-body-info-avatar" align="center">
                        <el-avatar :src="emp.avatarPath" :size="70">
                            <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png" />
                        </el-avatar>
                        <el-upload
                            action="/api/upload"
                            name="file"
                            :show-file-list="false"
                            :before-upload="beforeAvatarUpload"
                            :on-success="handleAvatarSuccess"
                        >
                            <el-button type="success" style="margin-top: 10px;">点击上传</el-button>
                        </el-upload>
                    </div>
                    <!-- 员工信息 -->
                    <div class="addEmpDialog-body-info-empInfo" align="center" style="margin-top: 20px;">
                        <div class="empInfo">
                            <label>员工姓名：</label>
                            <el-input v-model="emp.name" style="width: 220px" placeholder="请输入姓名" />
                            <label style="margin-left: 20px">性别：</label>
                            <el-select v-model="emp.sex" placeholder="选择性别" style="width: 250px">
                                <el-option label="男" value="男" />
                                <el-option label="女" value="女" />
                            </el-select>
                        </div>
                        <div class="empInfo">
                            <label>出生日期：</label>
                            <el-date-picker
                                v-model="emp.birth"
                                type="date"
                                placeholder="选择日期"
                                value-format="YYYY-MM-DD"
                                style="width: 220px"
                            />
                            <label style="margin-left: 20px">部门：</label>
                            <el-select v-model="emp.deptName" placeholder="选择部门" style="width: 250px">
                                <el-option v-for="dept in dept_list" :key="dept.id" :label="dept.name" :value="dept.name" />
                            </el-select>
                        </div>
                        <div class="empInfo">
                            <label>职位：</label>
                            <el-select v-model="emp.jobName" placeholder="选择职位" style="width: 220px">
                                <el-option v-for="job in job_list" :key="job" :label="job" :value="job" />
                            </el-select>
                            <label style="margin-left: 20px">入职日期：</label>
                            <el-date-picker
                                v-model="emp.boardDate"
                                type="date"
                                placeholder="选择日期"
                                value-format="YYYY-MM-DD"
                                style="width: 250px"
                            />
                        </div>
                    </div>
                </div>
                <div class="addEmpDialog-body-empExpList" style="font-size: 16px;" align="center">
                    <el-divider content-position="center">员工工作经历</el-divider>
                    <el-scrollbar max-height="250px">
                        <div class="addEmpDialog-body-empExpList-empExp" v-for="(empExp, index) in emp.empExpList" :key="index">
                            <div class="empExp-info">
                                <label>开始时间：</label>
                                <el-date-picker
                                    v-model="empExp.startTime"
                                    type="date"
                                    placeholder="选择日期"
                                    value-format="YYYY-MM-DD"
                                    style="width: 220px"
                                />
                                <label style="margin-left: 20px">结束时间：</label>
                                <el-date-picker
                                    v-model="empExp.endTime"
                                    type="date"
                                    placeholder="选择日期"
                                    value-format="YYYY-MM-DD"
                                    style="width: 220px"
                                />
                            </div>
                            <div class="empExp-info">
                                <label>担任职位：</label>
                                <el-input v-model="empExp.job" style="width: 220px" placeholder="请输入职位" />
                                <label style="margin-left: 20px">公司名称：</label>
                                <el-input v-model="empExp.company" style="width: 220px" placeholder="请输入公司名称" />
                            </div>
                            <el-button type="danger" @click="deleteEmpExp(empExp.id)">点击删除</el-button>
                            <el-divider>
                                <el-icon><star-filled /></el-icon>
                            </el-divider>
                        </div>
                        <div class="addEmpExp-button">
                            <el-button type="success" @click="addEmpExp" style="margin-bottom: -10px;">点击添加</el-button>
                        </div>
                    </el-scrollbar>
                </div>
            </div>
            <template #footer>
            <div class="addEmpDialog-footer">
                <el-button @click="addEmpDialogVisible = false">关闭</el-button>
                <el-button type="success" @click="addEmp">提交</el-button>
            </div>
            </template>
        </el-dialog>
    </div>
    <div class="deleteEmpDialog">
        <el-dialog
            v-model="deleteEmpDialogVisible"
            width="35%"
            align="left"
            align-center="true"
        >
        <template #header>
            <div class="my-header">
                <h1 style="font-size: 24px;margin: 1px;">批量删除员工</h1>
            </div>
        </template>
        <div class="deleteEmpDialog-body" style="margin-top: -30px;">
            <label style="font-size: 16px;">是否删除以下员工</label>
            <div class="deleteEmpDialog-body-empInfo" align="center">
                <el-table :data="deleteEmpList" style="width: 100%" max-height="300px">
                    <el-table-column prop="id" label="工号" width="100" />
                    <el-table-column prop="name" label="姓名" width="120" />
                    <el-table-column prop="sex" label="性别" width="120" />
                    <el-table-column prop="deptName" label="部门" width="120" />
                    <el-table-column prop="jobName" label="职位" />
                </el-table>
            </div>
        </div>
        <template #footer>
            <div class="deleteEmpDialog-footer">
                <el-button @click="deleteEmpDialogVisible = false">关闭</el-button>
                <el-button type="danger" @click="deleteEmp">确定</el-button>
            </div>
            </template>
        </el-dialog>
    </div>
</div>
</template>

<style scoped>
#emp {
    text-align: center;
    margin-top: 20px;
}
.search {
    margin-bottom: 20px;
}
.search span {
    margin-right: 20px;
}
.list {
    width: 90%;
    margin: 0 auto;
    margin-bottom: 20px;
}
.page {
    width: 80%;
    margin: 0 auto;
}
.empInfo, .empExp-info {
    margin-bottom: 20px;
}
.addEmpExp-button {
    height: 50px;
}
</style>