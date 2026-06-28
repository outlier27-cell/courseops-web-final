# CourseOps Full Polish Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Turn the current CourseOps demo into a fully wired, polished course-operations product with real auth, stable page layouts, tighter mobile behavior, and a Nanfu-style visual system.

**Architecture:** Keep the existing Vue 3 + Spring Boot split, but tighten the contract between UI and API first: real auth, stable runtime state, and no silent demo fallback masking failures. Then refactor each page as a distinct product surface with consistent spacing, hierarchy, and interaction rules, finishing with a shared visual language that borrows Nanfu's restraint, scroll rhythm, and editorial product presentation.

**Tech Stack:** Vue 3, TypeScript, Vite, Pinia, Vue Router, Arco Design Vue, ECharts, GSAP, Three.js, Spring Boot 3.5, MyBatis-Plus, MySQL, MongoDB, Spring Security, JWT, Playwright

---

### Task 1: Fix the auth and runtime contract first

**Files:**
- Modify: `courseops-backend/src/main/java/com/uibe/courseops/config/SecurityConfig.java`
- Modify: `courseops-backend/src/main/java/com/uibe/courseops/controller/AuthController.java`
- Modify: `courseops-backend/src/main/java/com/uibe/courseops/auth/JwtService.java`
- Modify: `courseops-backend/src/main/java/com/uibe/courseops/common/Result.java`
- Modify: `courseops-web/src/services/courseopsApi.ts`
- Modify: `courseops-web/src/stores/useCourseOpsStore.ts`
- Test: `courseops-backend/src/test/java/com/uibe/courseops/CourseopsBackendApplicationTests.java`

- [ ] **Step 1: Write the failing tests for auth and status payloads**

```java
@SpringBootTest
class CourseopsBackendApplicationTests {
    @Autowired MockMvc mockMvc;

    @Test
    void login_returns_token_and_user_without_password() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"u-student-001\",\"password\":\"123456\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.data.token").isNotEmpty())
            .andExpect(jsonPath("$.data.user.password").value(""));
    }
}
```

- [ ] **Step 2: Run the test and confirm the current security/auth contract is not yet strict**

Run: `cmd /c mvnw.cmd test -q`
Expected: pass only after auth and security are aligned; before that, the test should expose the current weak contract.

- [ ] **Step 3: Implement minimal auth tightening**

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/login", "/api/auth/me", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                    .anyRequest().authenticated())
            .build();
}
```

```ts
export async function login(userId: string, password: string) {
  const result = await request<LoginResponse>('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ userId, password }),
  })
  localStorage.setItem('courseops-token', result.token)
  return result
}
```

- [ ] **Step 4: Run the backend test and front-end typecheck**

Run:
`cmd /c mvnw.cmd test -q`
`cmd /c npm.cmd run build`
Expected: both pass.

- [ ] **Step 5: Commit**

```bash
git add courseops-backend/src/main/java/com/uibe/courseops/config/SecurityConfig.java courseops-backend/src/main/java/com/uibe/courseops/controller/AuthController.java courseops-web/src/services/courseopsApi.ts courseops-web/src/stores/useCourseOpsStore.ts
git commit -m "fix: tighten auth and runtime contract"
```

### Task 2: Add a real login entry and route guard

**Files:**
- Modify: `courseops-web/src/router/index.ts`
- Create: `courseops-web/src/components/landing/LoginPage.vue`
- Modify: `courseops-web/src/components/landing/PublicLanding.vue`
- Modify: `courseops-web/src/components/shell/AppShell.vue`
- Modify: `courseops-web/src/main.ts`
- Modify: `courseops-web/src/stores/useCourseOpsStore.ts`

- [ ] **Step 1: Add a login page that actually posts credentials**

```vue
<template>
  <section class="login-page">
    <form @submit.prevent="submit">
      <input v-model="userId" autocomplete="username" />
      <input v-model="password" type="password" autocomplete="current-password" />
      <button type="submit">登录</button>
    </form>
  </section>
</template>
```

- [ ] **Step 2: Wire `/login` and guard `/app`**

```ts
{
  path: '/login',
  name: 'login',
  component: LoginPage,
},
{
  path: '/app',
  component: AppShell,
  meta: { requiresAuth: true },
  children: [...]
}
```

- [ ] **Step 3: Add a route guard and redirect behavior**

```ts
router.beforeEach((to) => {
  const token = localStorage.getItem('courseops-token')
  if (to.meta.requiresAuth && !token) return { name: 'login' }
  if (to.name === 'login' && token) return { path: '/app' }
})
```

- [ ] **Step 4: Verify login flow in browser**

Run: `npm.cmd run dev` and open `/login`
Expected: successful login redirects to `/app`, invalid login stays on the page and shows error.

- [ ] **Step 5: Commit**

```bash
git add courseops-web/src/router/index.ts courseops-web/src/components/landing/LoginPage.vue courseops-web/src/components/landing/PublicLanding.vue courseops-web/src/components/shell/AppShell.vue courseops-web/src/main.ts courseops-web/src/stores/useCourseOpsStore.ts
git commit -m "feat: add real login entry and guard"
```

### Task 3: Rebuild the landing page into a product-quality entry screen

**Files:**
- Modify: `courseops-web/src/components/landing/PublicLanding.vue`
- Modify: `courseops-web/src/components/landing/ImmersiveCourseStage.vue`
- Modify: `courseops-web/src/components/landing/FloatingVisual.vue`
- Modify: `courseops-web/src/components/common/ProductShowcaseHero.vue`
- Modify: `courseops-web/src/components/visuals/ThreeDHeroScene.vue`
- Modify: `courseops-web/src/styles/base.css`
- Modify: `courseops-web/src/style.css`

- [ ] **Step 1: Rework the first viewport to make the product name the signal**

```vue
<ProductShowcaseHero
  eyebrow="CourseOps"
  title="课程项目运营平台"
  description="把课程项目、材料提交、教师反馈和审计日志串成一条可操作的工作流。"
/>
```

- [ ] **Step 2: Reduce marketing copy and make the scroll story more editorial**

```vue
<section class="landing-scene-corridor">
  <section class="product-proof-band">...</section>
  <section class="capability-cinema">...</section>
  <section class="flow-section cinematic-flow">...</section>
</section>
```

- [ ] **Step 3: Replace decorative clutter with one clear visual system**

```css
.public-landing {
  background: var(--page-bg);
  color: var(--text-strong);
}
.landing-scene-corridor {
  display: grid;
  gap: 24px;
}
```

- [ ] **Step 4: Check desktop and mobile screenshots**

Run: open `/` at `1440x900` and `390x844`
Expected: first viewport reads as a product, not a promo page; no overlap; no clipped labels.

- [ ] **Step 5: Commit**

```bash
git add courseops-web/src/components/landing/PublicLanding.vue courseops-web/src/components/landing/ImmersiveCourseStage.vue courseops-web/src/components/landing/FloatingVisual.vue courseops-web/src/components/common/ProductShowcaseHero.vue courseops-web/src/components/visuals/ThreeDHeroScene.vue courseops-web/src/styles/base.css courseops-web/src/style.css
git commit -m "feat: rebuild landing as product entry"
```

### Task 4: Tighten the shell, navigation, and top bar

**Files:**
- Modify: `courseops-web/src/components/shell/SideNav.vue`
- Modify: `courseops-web/src/components/shell/TopNav.vue`
- Modify: `courseops-web/src/components/shell/AppShell.vue`
- Modify: `courseops-web/src/components/shell/RoleSwitcher.vue`
- Modify: `courseops-web/src/components/shell/SearchCommandPalette.vue`
- Modify: `courseops-web/src/components/shell/NotificationPopover.vue`

- [ ] **Step 1: Add a clearer active state and stronger hierarchy to the shell**

```vue
<button :class="{ active: currentPath.startsWith(item.path) }">
  <component :is="item.icon" :size="18" />
  <span>
    <strong>{{ item.label }}</strong>
    <small>{{ item.hint }}</small>
  </span>
</button>
```

- [ ] **Step 2: Fix keyboard and focus behavior in global search and notifications**

```ts
function onGlobalKeydown(event: KeyboardEvent) {
  if ((event.metaKey || event.ctrlKey) && event.key.toLowerCase() === 'k') {
    event.preventDefault()
    openSearch()
  }
}
```

- [ ] **Step 3: Make mock/runtime state visible but not noisy**

```vue
<section v-if="store.apiStatus.usingMock || store.errorMessage" class="runtime-status-bar">
  ...
</section>
```

- [ ] **Step 4: Verify all shell actions still work**

Run: navigate between `/app`, `/app/projects`, `/app/tasks`, `/app/analytics`, `/app/logs`
Expected: no broken focus trap, no clipped labels, no navigation dead-ends.

- [ ] **Step 5: Commit**

```bash
git add courseops-web/src/components/shell/SideNav.vue courseops-web/src/components/shell/TopNav.vue courseops-web/src/components/shell/AppShell.vue courseops-web/src/components/shell/RoleSwitcher.vue courseops-web/src/components/shell/SearchCommandPalette.vue courseops-web/src/components/shell/NotificationPopover.vue
git commit -m "refactor: tighten shell navigation"
```

### Task 5: Rework the workspace home into a denser operating cockpit

**Files:**
- Modify: `courseops-web/src/components/dashboard/WorkspaceHome.vue`
- Modify: `courseops-web/src/components/common/AnimatedNumber.vue`
- Modify: `courseops-web/src/components/common/ProgressBar.vue`
- Modify: `courseops-web/src/components/common/RiskBadge.vue`
- Modify: `courseops-web/src/components/common/StatusPill.vue`
- Modify: `courseops-web/src/styles/base.css`

- [ ] **Step 1: Rewrite the hero to show decision-making cues first**

```vue
<header class="cockpit-hero">
  <div>
    <span class="eyebrow">Intelligent Cockpit</span>
    <h1>今天要处理的内容</h1>
  </div>
</header>
```

- [ ] **Step 2: Make the priority stack and project pulse read like operational queues**

```vue
<div class="todo-grid priority-stack">
  <button v-for="material in focusMaterials.slice(0, 6)" class="todo-card interactive-lift">
    ...
  </button>
</div>
```

- [ ] **Step 3: Reduce empty space and unify metric behavior**

```css
.role-metrics {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
}
```

- [ ] **Step 4: Verify the page at 1280 and 390 widths**

Run: open `/app`
Expected: no wrapping collision, no stacked buttons becoming too tall, no content hidden under the shell.

- [ ] **Step 5: Commit**

```bash
git add courseops-web/src/components/dashboard/WorkspaceHome.vue courseops-web/src/components/common/AnimatedNumber.vue courseops-web/src/components/common/ProgressBar.vue courseops-web/src/components/common/RiskBadge.vue courseops-web/src/components/common/StatusPill.vue courseops-web/src/styles/base.css
git commit -m "refactor: densify workspace cockpit"
```

### Task 6: Fix projects and project space so they behave like a real workspace

**Files:**
- Modify: `courseops-web/src/components/projects/ProjectCenter.vue`
- Modify: `courseops-web/src/components/projects/ProjectSpace.vue`
- Modify: `courseops-web/src/components/projects/ProjectCenter.vue`
- Modify: `courseops-web/src/components/projects/TeamAvatarStack.vue`
- Modify: `courseops-web/src/components/common/ProgressRing.vue`
- Modify: `courseops-web/src/components/common/SegmentedControl.vue`

- [ ] **Step 1: Turn project center into a dense comparison surface**

```vue
<div class="project-card-grid">
  <article v-for="project in visibleProjects" class="course-project-card project-board-card">
    ...
  </article>
</div>
```

- [ ] **Step 2: Make project space focus on project state, not duplicated content**

```vue
<section class="project-command-strip">
  <article class="project-health-core">...</article>
  <article>...</article>
  <article>...</article>
</section>
```

- [ ] **Step 3: Fix tab hierarchy and spacing**

```css
.project-tabs button.active {
  border-bottom: 2px solid var(--brand-primary);
}
```

- [ ] **Step 4: Verify deep links and refresh behavior**

Run: `/app/projects/p-web-final`, switch tabs, refresh the page
Expected: selected project stays in sync with route; no blank state unless the project is missing.

- [ ] **Step 5: Commit**

```bash
git add courseops-web/src/components/projects/ProjectCenter.vue courseops-web/src/components/projects/ProjectSpace.vue courseops-web/src/components/projects/TeamAvatarStack.vue courseops-web/src/components/common/ProgressRing.vue courseops-web/src/components/common/SegmentedControl.vue
git commit -m "refactor: polish project surfaces"
```

### Task 7: Make the material workflow the strongest page in the product

**Files:**
- Modify: `courseops-web/src/components/tasks/MaterialWorkflowCenter.vue`
- Modify: `courseops-web/src/components/tasks/MaterialDetailDrawer.vue`
- Modify: `courseops-web/src/components/tasks/MaterialTaskCard.vue`
- Modify: `courseops-web/src/components/tasks/MaterialTimeline.vue`
- Modify: `courseops-web/src/components/common/FileFormatBadge.vue`
- Modify: `courseops-web/src/components/common/EmptyState.vue`

- [ ] **Step 1: Redesign the flow rail, board, and drawer as one connected surface**

```vue
<section class="material-flow-rail">
  <div class="rail-copy">...</div>
  <article v-for="column in store.materialColumns" class="flow-station">...</article>
</section>
```

- [ ] **Step 2: Add clearer state-to-action mapping**

```ts
const statusGuidance = {
  not_started: { title: '先把材料骨架搭起来', tip: '...' },
  preparing: { title: '整理后就可以提交', tip: '...' },
  submitted: { title: '等待老师确认', tip: '...' },
  revision_required: { title: '优先处理高风险修改', tip: '...' },
  approved: { title: '材料已可归档', tip: '...' },
}
```

- [ ] **Step 3: Make upload, status update, and feedback states visibly distinct**

```vue
<MaterialDetailDrawer
  :material="store.selectedMaterial"
  :submissions="selectedSubmissions"
  :histories="selectedHistories"
  :feedback="selectedFeedback"
/>
```

- [ ] **Step 4: Verify the page works with upload success and upload error**

Run: select a material, upload a file with the wrong format, then with the correct format
Expected: error is shown inline; success updates timeline, submission list, and status pill.

- [ ] **Step 5: Commit**

```bash
git add courseops-web/src/components/tasks/MaterialWorkflowCenter.vue courseops-web/src/components/tasks/MaterialDetailDrawer.vue courseops-web/src/components/tasks/MaterialTaskCard.vue courseops-web/src/components/tasks/MaterialTimeline.vue courseops-web/src/components/common/FileFormatBadge.vue courseops-web/src/components/common/EmptyState.vue
git commit -m "refactor: strengthen material workflow"
```

### Task 8: Make analytics and logs feel like real operational tools

**Files:**
- Modify: `courseops-web/src/components/analytics/OperationalInsights.vue`
- Modify: `courseops-web/src/components/analytics/InsightChartCard.vue`
- Modify: `courseops-web/src/components/analytics/OperationalInsights.vue`
- Modify: `courseops-web/src/components/logs/ActivityTimeline.vue`
- Modify: `courseops-web/src/components/logs/LogFilterBar.vue`
- Modify: `courseops-web/src/components/logs/TimelineItem.vue`
- Modify: `courseops-web/src/styles/base.css`

- [ ] **Step 1: Separate decision cards from chart panels**

```vue
<section class="mega-proof-board">
  <article class="proof-hero-metric">...</article>
  <article>...</article>
  <article>...</article>
</section>
```

- [ ] **Step 2: Reduce chart styling noise and ensure the chart grid remains legible**

```ts
const grid = { left: 34, right: 18, top: 24, bottom: 36 }
```

- [ ] **Step 3: Tighten timeline grouping and filter semantics**

```vue
<section v-for="group in groupedLogs" class="audit-timeline-shell">
  ...
</section>
```

- [ ] **Step 4: Verify the analysis page at desktop and mobile**

Run: open `/app/analytics` and `/app/logs`
Expected: no chart overlap, no text collision, no group heading ambiguity.

- [ ] **Step 5: Commit**

```bash
git add courseops-web/src/components/analytics/OperationalInsights.vue courseops-web/src/components/analytics/InsightChartCard.vue courseops-web/src/components/logs/ActivityTimeline.vue courseops-web/src/components/logs/LogFilterBar.vue courseops-web/src/components/logs/TimelineItem.vue courseops-web/src/styles/base.css
git commit -m "refactor: make analytics and logs operational"
```

### Task 9: Rebuild the shared design system and responsive rules

**Files:**
- Modify: `courseops-web/src/styles/tokens.css`
- Modify: `courseops-web/src/styles/base.css`
- Modify: `courseops-web/src/styles/motion.css`
- Modify: `courseops-web/src/style.css`
- Modify: `courseops-web/src/components/common/GlassCard.vue`
- Modify: `courseops-web/src/components/common/PageHero.vue`
- Modify: `courseops-web/src/components/visuals/VisualShaderBackground.vue`
- Modify: `courseops-web/src/components/visuals/MotionReveal.vue`

- [ ] **Step 1: Replace the current overly decorative language with one stable token set**

```css
:root {
  --page-bg: #f6f8f7;
  --panel-bg: #ffffff;
  --text-strong: #111827;
  --text-body: #334155;
  --brand-primary: #0f766e;
  --brand-accent: #2563eb;
}
```

- [ ] **Step 2: Remove overlapping card-heavy composition from full-page bands**

```css
.workspace-layout,
.project-space-layout,
.insight-grid {
  display: grid;
  gap: 24px;
}
```

- [ ] **Step 3: Add explicit responsive constraints**

```css
.side-nav {
  width: 232px;
  max-width: 100%;
}
```

- [ ] **Step 4: Verify 1440, 1280, and 390 widths**

Run: screenshot every page at desktop and mobile widths
Expected: no overflow, no stacked controls breaking the shell, no clipped labels.

- [ ] **Step 5: Commit**

```bash
git add courseops-web/src/styles/tokens.css courseops-web/src/styles/base.css courseops-web/src/styles/motion.css courseops-web/src/style.css courseops-web/src/components/common/GlassCard.vue courseops-web/src/components/common/PageHero.vue courseops-web/src/components/visuals/VisualShaderBackground.vue courseops-web/src/components/visuals/MotionReveal.vue
git commit -m "refactor: unify design system and responsiveness"
```

### Task 10: Close the demo gap and harden the verification loop

**Files:**
- Modify: `courseops-web/src/services/courseopsApi.ts`
- Modify: `courseops-web/src/stores/useCourseOpsStore.ts`
- Modify: `courseops-web/src/components/shell/AppShell.vue`
- Modify: `courseops-backend/src/main/resources/application.yml`
- Modify: `courseops-backend/src/main/resources/db/schema.sql`
- Modify: `courseops-backend/src/test/java/com/uibe/courseops/CourseopsBackendApplicationTests.java`
- Create: `docs/courseops-verification-checklist.md`

- [ ] **Step 1: Make mock fallback explicit and measurable**

```ts
export function getApiRuntimeState(): ApiRuntimeState {
  return { ...apiRuntimeState }
}
```

- [ ] **Step 2: Add a verification checklist for release-time smoke tests**

```md
- Login works with valid credentials
- Wrong password shows a visible error
- `/app` redirects to `/login` without token
- Uploading a wrong file type fails cleanly
- Status updates create history and logs
- Analytics and logs render without overlap
```

- [ ] **Step 3: Add backend smoke checks for the live services**

```java
@Test
void health_endpoints_and_core_list_endpoints_work() throws Exception {
    mockMvc.perform(get("/api/projects")).andExpect(status().isOk());
    mockMvc.perform(get("/api/materials")).andExpect(status().isOk());
    mockMvc.perform(get("/api/analytics")).andExpect(status().isOk());
}
```

- [ ] **Step 4: Verify release candidate behavior end-to-end**

Run:
`cmd /c mvnw.cmd test -q`
`cmd /c npm.cmd run build`
`cmd /c npm.cmd run dev -- --host 127.0.0.1 --port 5173`
Expected: backend starts, frontend starts, all main pages render, no console errors in the browser, and no horizontal overflow on mobile.

- [ ] **Step 5: Commit**

```bash
git add courseops-web/src/services/courseopsApi.ts courseops-web/src/stores/useCourseOpsStore.ts courseops-web/src/components/shell/AppShell.vue courseops-backend/src/main/resources/application.yml courseops-backend/src/main/resources/db/schema.sql courseops-backend/src/test/java/com/uibe/courseops/CourseopsBackendApplicationTests.java docs/courseops-verification-checklist.md
git commit -m "chore: harden release verification"
```

## Self-Review

### Spec coverage
- Login and auth: Task 1, Task 2, Task 10
- Landing page / first viewport: Task 3
- Dashboard / workspace: Task 5
- Project center and project space: Task 6
- Material workflow: Task 7
- Analytics: Task 8
- Logs: Task 8
- Shared design system and mobile fit: Task 9
- End-to-end verification: Task 10

### Placeholder scan
- No TBD-style placeholders remain in task steps.
- Each task includes exact files, commands, and expected outcomes.

### Type consistency
- Auth flow uses `token`, `user`, and `courseops-token` consistently.
- Material flow uses existing `MaterialStatus`, `Submission`, and `UploadResponse` shapes.
- Route guard references `requiresAuth` only on `/app`.

