<script setup>
    
</script>

<template>
<div class="container">
    <el-container>
        <el-header height="80px" class="container-header">
            <h1 class="container-header-title" style="color: white;">艾欧希后台管理系统</h1>
        </el-header>
        <el-container class="container-body">
            <el-aside width="200px" class="container-aside">
                <el-scrollbar height="100%">
                    <el-menu router>
                        <el-menu-item index="/index">
                            <template #title>
                                <el-icon><Promotion /></el-icon>
                                <span>首页</span>
                            </template>
                        </el-menu-item>
                        <el-sub-menu index="1">
                            <template #title>
                                <el-icon><Menu /></el-icon>
                                <span>人员与机构管理</span>
                            </template>
                            <el-menu-item index="/emp">
                                <template #title>
                                    <el-icon><Avatar /></el-icon>
                                    <span>员工管理</span>
                                </template>
                            </el-menu-item>
                            <el-menu-item index="/dept">
                                <template #title>
                                    <el-icon><HomeFilled /></el-icon>
                                    <span>部门管理</span>
                                </template>
                            </el-menu-item>
                            <el-menu-item index="/student">
                                <template #title>
                                    <el-icon><UserFilled /></el-icon>
                                    <span>学生管理</span>
                                </template>
                            </el-menu-item>
                            <el-menu-item index="/class">
                                <template #title>
                                    <el-icon><HelpFilled /></el-icon>
                                    <span>班级管理</span>
                                </template>
                            </el-menu-item>
                        </el-sub-menu>
                        <el-sub-menu index="2">
                            <template #title>
                                <el-icon><Histogram /></el-icon>
                                <span>数据统计信息</span>
                            </template>
                            <el-menu-item index="/report/emp">
                                <template #title>
                                    <el-icon><InfoFilled /></el-icon>
                                    <span>员工信息统计</span>
                                </template>
                            </el-menu-item>
                            <el-menu-item index="/report/student">
                                <template #title>
                                    <el-icon><Share /></el-icon>
                                    <span>学生信息统计</span>
                                </template>
                            </el-menu-item>
                            <el-menu-item index="/log">
                                <template #title>
                                    <el-icon><Comment /></el-icon>
                                    <span>系统日志</span>
                                </template>
                            </el-menu-item>
                        </el-sub-menu>
                    </el-menu>
                </el-scrollbar>
            </el-aside>
            <el-main>
                <router-view />
            </el-main>
        </el-container>
    </el-container>
</div>
</template>

<style scoped>
.container-header {
    background-color: #409EFF;
    align-items: center;
    border-radius: 4px;
    border: 1px solid #ccc;
}
.container-body {
    /* border: 1px solid #ccc; */
    height: 89.5vh;
}
.container-aside {
    border-right: #ccc 1px solid;
}
</style>
