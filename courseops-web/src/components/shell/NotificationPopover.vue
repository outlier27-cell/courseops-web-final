<script setup lang="ts">
import { AlertTriangle, BellRing, CheckCircle2, Clock3, MessageSquareText, UploadCloud } from 'lucide-vue-next'
import { computed, ref, type Component } from 'vue'
import type { NotificationItem } from '../../domain/types'

const props = defineProps<{ open: boolean; notifications: NotificationItem[] }>()
defineEmits<{ close: []; openNotification: [notification: NotificationItem]; markRead: [id: string] }>()

const activeType = ref<'all' | NotificationItem['type']>('all')

const typeMeta: Record<NotificationItem['type'], { label: string; icon: Component }> = {
  deadline: { label: '截止提醒', icon: Clock3 },
  feedback: { label: '教师反馈', icon: MessageSquareText },
  submission: { label: '提交动态', icon: UploadCloud },
  risk: { label: '风险提醒', icon: AlertTriangle },
  system: { label: '系统通知', icon: BellRing },
}

const tabs = computed(() => [
  { label: '全部', value: 'all' as const, count: props.notifications.length },
  ...Object.entries(typeMeta).map(([value, meta]) => ({
    label: meta.label,
    value: value as NotificationItem['type'],
    count: props.notifications.filter((item) => item.type === value).length,
  })),
])

const visibleNotifications = computed(() => (
  activeType.value === 'all'
    ? props.notifications
    : props.notifications.filter((item) => item.type === activeType.value)
))

const unreadCount = computed(() => props.notifications.filter((item) => !item.read).length)
const unreadNotifications = computed(() => props.notifications.filter((item) => !item.read))
</script>

<template>
  <Teleport to="body">
    <div v-if="open" class="notification-layer" @click.self="$emit('close')">
      <section class="notification-popover" role="dialog" aria-label="通知中心">
        <header>
          <span>
            <strong>通知中心</strong>
            <small>{{ unreadCount }} 条未读 · 聚焦材料、反馈和截止风险</small>
          </span>
          <button class="ghost-action" @click="unreadNotifications.forEach((item) => $emit('markRead', item.id))">
            <CheckCircle2 :size="15" />
            全部已读
          </button>
        </header>

        <nav class="notification-tabs" aria-label="通知分类">
          <button
            v-for="tab in tabs"
            :key="tab.value"
            :class="{ active: activeType === tab.value }"
            @click="activeType = tab.value"
          >
            {{ tab.label }}
            <b>{{ tab.count }}</b>
          </button>
        </nav>

        <div class="notification-list">
          <button
            v-for="item in visibleNotifications"
            :key="item.id"
            :class="{ unread: !item.read }"
            @click="$emit('openNotification', item)"
          >
            <i>
              <component :is="typeMeta[item.type].icon" :size="16" />
            </i>
            <span>
              <small>{{ typeMeta[item.type].label }} · {{ item.createdAt }}</small>
              <strong>{{ item.title }}</strong>
              <em>{{ item.description }}</em>
            </span>
            <b v-if="!item.read" @click.stop="$emit('markRead', item.id)">已读</b>
          </button>
        </div>

        <div v-if="!visibleNotifications.length" class="empty-state compact">
          <div class="empty-orb"></div>
          <h3>暂无通知</h3>
          <p>材料提交、教师反馈和风险提醒会出现在这里。</p>
        </div>
      </section>
    </div>
  </Teleport>
</template>
