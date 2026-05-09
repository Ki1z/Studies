<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';

const deptList = ref([]);

const getDeptList = async () => {
    const result = await axios.get('/api/depts');
    deptList.value = result.data.data;
}

onMounted(() => {
    getDeptList();
});
</script>

<template>
<div id="dept">
    <div class="dept-table">
        <el-button type="success" @click="" class="add-button">新增部门</el-button>
        <el-table 
            :data="deptList" 
            style="width: 100%"
            :row-style = "() => { return { 
                height: '70px'
             } }"
            max-height="70vh"
        >
            <el-table-column type="index" label="序号" width="150" />
            <el-table-column prop="id" label="部门ID" width="150" />
            <el-table-column prop="name" label="部门名称" width="180" />
            <el-table-column prop="createTime" label="创建时间" width="260" />
            <el-table-column prop="updateTime" label="上次修改时间" width="500" />
            <el-table-column fixed="right" label="操作" min-width="120">
            <template #default>
                <el-button link type="primary">编辑</el-button>
                <el-button link type="primary">删除</el-button>
            </template>
            </el-table-column>
        </el-table>
    </div>
</div>
</template>

<style scoped>
#dept {
    margin: 20px;
}
.add-button {
    margin-bottom: 20px;
}
.dept-table {
    width: 90%;
    margin: 0 auto;
    margin-bottom: 20px;
}
</style>