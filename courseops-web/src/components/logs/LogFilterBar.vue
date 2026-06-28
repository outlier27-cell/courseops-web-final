<script setup lang="ts">
import type { UserRole } from '../../domain/types'

defineProps<{ searchText: string; actionType: string; roleFilter: string; role: UserRole }>()
defineEmits<{ 'update:searchText': [value: string]; 'update:actionType': [value: string]; 'update:roleFilter': [value: string] }>()
</script>

<template>
  <div class="log-filter-bar">
    <input :value="searchText" placeholder="搜索操作人、对象、课程项目..." @input="$emit('update:searchText', ($event.target as HTMLInputElement).value)" />
    <select :value="actionType" @change="$emit('update:actionType', ($event.target as HTMLSelectElement).value)">
      <option value="all">全部操作</option>
      <option value="上传文件">上传文件</option>
      <option value="更新材料状态">更新材料状态</option>
      <option value="提交反馈">提交反馈</option>
      <option value="标记通知已读">标记通知已读</option>
    </select>
    <select v-if="role === 'admin'" :value="roleFilter" @change="$emit('update:roleFilter', ($event.target as HTMLSelectElement).value)">
      <option value="all">全部角色</option>
      <option value="student">学生</option>
      <option value="teacher">教师</option>
      <option value="admin">管理员</option>
    </select>
  </div>
</template>
