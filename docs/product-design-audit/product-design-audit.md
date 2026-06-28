# UIBE CourseOps Product Design Audit

> Phase 1 output for the CourseOps full-stack product upgrade.  
> Audit mode: Product Design plugin workflow, local folder destination.  
> Target: `http://127.0.0.1:5173/`  
> Framework observed: Vue 3 + Vite + TypeScript + Pinia + Vue Router + ECharts.

## 0. Screenshot Evidence

Captured screenshots are stored in:

`D:\Yjw\UIBE课程\课程\Web-Final\docs\product-design-audit\screenshots`

| Area | Screenshot |
|---|---|
| Landing | `01-landing.png` |
| Workspace | `02-workspace.png` |
| Project Center | `03-project-center.png` |
| Project Space | `04-project-space.png` |
| Material Workflow | `05-material-workflow.png` |
| Analytics | `06-analytics.png` |
| Timeline | `07-timeline.png` |

Search Command Palette and Notification Center were audited through `TopNav.vue`, `SearchCommandPalette.vue`, `NotificationPopover.vue`, and the visible top navigation state in screenshots. The browser screenshot command timed out while capturing these transient overlays, so they should be re-shot during Phase 7 visual QA after the redesign.

## 1. Product Design Verdict

The current UI is not "bad"; it already avoids a classic table-heavy school admin system. The main issue is that it still looks like a first-pass visual prototype: large headings, rounded white cards, pale gradient background, and repeated glass surfaces are doing most of the visual work. It has a premium shell, but not enough product-level information architecture.

Current visual score:

| Dimension | Score | Notes |
|---|---:|---|
| Product direction | 8/10 | CourseOps positioning is strong and differentiated. |
| Visual polish | 6.5/10 | Soft, but too repetitive and sometimes empty. |
| Information hierarchy | 6/10 | Huge titles dominate over core actions. |
| SaaS maturity | 6/10 | Search, notification, project space, and workflow need richer product behavior. |
| Demo impact | 7/10 | Landing and workflow can become strong with focused redesign. |

Final target:

**Apple-inspired + Linear + Vercel + Notion**, but with UIBE CourseOps' own calm green identity.

The product should feel like:

> A calm, intelligent course project operating system where students, teachers, and admins can see exactly what needs attention, act quickly, and trust that every material submission leaves a trace.

## 2. Global Problems

### 2.1 Too Much "Big Title", Not Enough Product Density

Many pages use oversized titles as the main visual event. This gives drama, but in app pages it creates an empty feeling. App pages should show:

- Current context
- Most urgent action
- Key data
- Next recommended step

Landing can keep theatrical typography. App pages should be more compact and operational.

### 2.2 Repeated White Cards Flatten the Product

Almost every surface uses the same visual grammar: large rounded white card, soft shadow, pale background. This removes hierarchy. The redesign needs distinct surface types:

- Hero canvas
- Command card
- Workflow card
- Data insight card
- Detail drawer
- Timeline item
- Floating mock panel

### 2.3 Decorative Effects Are Present but Not Meaningful Enough

The current radial glow and glass style are pleasant, but many decorative blocks do not communicate extra information. Decoration must carry business meaning:

- 3D document stack = material workflow
- Floating project cards = active courses and risk
- Progress orb = project health
- Workflow board = status transition
- Timeline rail = audit trail

### 2.4 Functional Components Need Stronger Product Rituals

The upload flow, status switch, notification, search, and audit expansion should feel like mature SaaS interactions. Right now they work, but visually feel temporary.

## 3. Unified Design Language

Design system name:

**Calm Command OS**

Keywords:

- Premium SaaS
- Apple-inspired
- Figma-level polish
- Clean but not empty
- Soft glass
- Calm green
- Subtle 3D
- Floating panels
- Intelligent workflow
- Beautiful but functional

### 3.1 Color Tokens

```css
:root {
  --bg-page: #f5f6f4;
  --bg-page-warm: #fbfbf7;
  --bg-panel: rgba(255, 255, 255, 0.78);
  --bg-panel-strong: rgba(255, 255, 255, 0.92);
  --bg-muted: rgba(17, 24, 39, 0.045);

  --text-primary: #141716;
  --text-secondary: #5d6563;
  --text-tertiary: #8a9490;

  --accent: #087f70;
  --accent-soft: #dff4ef;
  --accent-deep: #045f56;

  --blue: #2f6fed;
  --blue-soft: #e8f0ff;
  --amber: #b7791f;
  --amber-soft: #fff3d7;
  --red: #c2413b;
  --red-soft: #ffe6e3;

  --border-subtle: rgba(20, 23, 22, 0.07);
  --border-strong: rgba(20, 23, 22, 0.12);
}
```

Rules:

- Green is the brand accent, not a large filled background.
- Blue can appear in analytics only after being muted.
- Red and amber must be semantic, never decorative.
- Avoid saturated gradients; use radial glow and soft color fields.

### 3.2 Typography

Use the system stack, but tune scale and letter spacing:

```css
--font-sans: -apple-system, BlinkMacSystemFont, "SF Pro Display", "Inter", "Segoe UI", sans-serif;
```

Recommended scale:

| Use | Size | Weight | Notes |
|---|---:|---:|---|
| Landing hero | 76-92px | 800 | Tight line-height, only on Landing. |
| App hero | 40-52px | 760 | No 64px+ inside app pages. |
| Section title | 24-32px | 720 | Used above functional groups. |
| Card title | 17-22px | 680 | Crisp, not oversized. |
| Body | 14-16px | 400-500 | Use dark gray, not pure black. |
| Metadata | 12-13px | 500 | Uppercase allowed sparingly. |

### 3.3 Spacing

Use a tighter operational rhythm inside app pages:

```css
--space-1: 4px;
--space-2: 8px;
--space-3: 12px;
--space-4: 16px;
--space-5: 20px;
--space-6: 24px;
--space-8: 32px;
--space-10: 40px;
--space-12: 48px;
--space-16: 64px;
```

Rules:

- Landing can use 80-120px vertical rhythm.
- App pages should keep core action visible within first viewport.
- Between related cards: 16-24px.
- Between major sections: 32-48px.

### 3.4 Radius and Shadows

```css
--radius-sm: 12px;
--radius-md: 18px;
--radius-lg: 24px;
--radius-xl: 32px;
--radius-2xl: 40px;

--shadow-soft: 0 18px 50px rgba(20, 23, 22, 0.07);
--shadow-lift: 0 24px 70px rgba(20, 23, 22, 0.11);
--shadow-inner: inset 0 1px 0 rgba(255, 255, 255, 0.72);
```

Rules:

- Do not apply glass to every card.
- Hero and drawers can be glass.
- Small repeated cards should be flatter and more precise.

### 3.5 Motion

```css
--motion-fast: 180ms cubic-bezier(.2, .8, .2, 1);
--motion-base: 240ms cubic-bezier(.2, .8, .2, 1);
--motion-slow: 420ms cubic-bezier(.16, 1, .3, 1);
```

Motion rules:

- Cards hover: `translateY(-3px)` only.
- Buttons: subtle scale `0.98` on active, no bouncing.
- Drawer: slide + fade.
- Search: scale `0.98 -> 1` + blur overlay.
- Kanban status move: card should fade/slide into the new column.
- Animated numbers should count up once, not loop.

## 4. Page-by-Page Audit and Redesign Direction

## 4.1 Landing Page

Screenshot: `01-landing.png`

### Current Problems

- The hero title is visually loud but not refined enough; line breaks create an aggressive block.
- The right product visual is promising, but feels like floating placeholders rather than a clear system preview.
- Feature cards below the hero are too similar and too static.
- The workflow strip is useful but too flat; it should tell the product story.
- The bottom CTA begins, but the page does not yet feel like a complete product narrative.

### Redesign Direction

Landing should become a product marketing page:

1. Keep a sticky pill navigation, but make it thinner and more precise.
2. Replace the hero title with a sharper two-line statement:
   - `把课程项目、材料流转和教师反馈，变成一条可追踪的工作流。`
3. Add a subline focused on roles:
   - `面向学生、教师与管理员的课程项目运营平台，覆盖提交、审核、提醒、洞察和审计。`
4. Right side should become a "live product mock":
   - Floating course cards
   - 3D material document stack
   - Mini workflow board
   - Progress orb
5. Add a scroll narrative:
   - `发布项目`
   - `设置材料要求`
   - `学生上传`
   - `教师反馈`
   - `修改提交`
   - `材料通过`
   - `日志留痕`
6. Add a screenshot-like mock panel instead of generic cards.

### Add Decorations

- Subtle radial glow behind product mock.
- 3D document stack with file type chips.
- Floating project cards with real risk/status data.
- Workflow preview board with 5 status columns.
- Tiny activity feed line in mock panel.

### Function Must Remain First

- Primary CTA to `/app`.
- Secondary CTA to `/app/projects`.
- Product sections must match real app modules, not marketing-only claims.

## 4.2 Workspace

Screenshot: `02-workspace.png`

### Current Problems

- Hero takes too much vertical space for an app page.
- It repeats the "big title" pattern from Landing.
- Today's tasks are visible but not prioritized enough.
- The smart reminder card is too plain and small to feel intelligent.
- Role switching exists, but the layout does not feel deeply role-specific.

### Redesign Direction

Workspace should become a role-aware command center.

Layout:

- Compact header row:
  - Greeting
  - Role-specific mission sentence
  - Primary next action
- Left main column:
  - Today Priority Stack
  - Returned / overdue / pending review cards
- Right action rail:
  - Smart suggestion
  - Current risk project
  - Notification digest
- Metric row should use compact cards, not oversized blocks.

Role-specific content:

- Student:
  - Today to submit
  - Returned materials
  - Upcoming deadlines
  - Latest teacher feedback
- Teacher:
  - Pending review materials
  - Unsubmitted teams
  - Projects requiring reminders
  - Fast feedback actions
- Admin:
  - School-wide health
  - High-risk courses
  - Submission trend
  - Audit anomaly entry

### Add Decorations

- VisualGlowBackground behind only the top command area.
- AnimatedNumber in metrics.
- SmartSuggestionCard with icon, recommended action, and reasoning.
- Small timeline mini-feed, not just text.

### Function Must Remain First

- Role switch must change actual data and CTA routes.
- "Enter Material Workflow" must route to `/app/tasks`.
- Today's task click should open selected material.

## 4.3 Project Center

Screenshot: `03-project-center.png`

### Current Problems

- Cards are clean but generic.
- Missing project description density: risk reason, material checklist, recent feedback.
- Team avatars exist but are too small and purely decorative.
- Progress bar is visually basic.
- Filter segmented control is acceptable, but not premium.

### Redesign Direction

Project Center should feel like a course project gallery.

Each card should include:

- Course name
- Project name
- Short description
- Teacher / assistant
- Team avatar stack with names on hover
- Current phase
- Deadline
- Material completion
- Team progress
- Risk level and risk reason
- Latest feedback summary
- Checklist preview: 3 material requirements
- CTA: `进入项目空间`

Card visual:

- Large premium cards in a 2-column grid at 1440px.
- Top-left course chip, top-right risk badge.
- ProgressOrb or mini ring for health.
- Checklist preview as tiny rows with status dots.
- Hover: lift, border highlight, slight glow.

### Add Decorations

- ProgressOrb per card.
- TeamAvatarStack with subtle overlap.
- Material checklist preview.
- Soft background glow only on active/high-risk cards.

### Function Must Remain First

- Clicking project routes to `/app/projects/:id`.
- Filters must still filter real project data.
- Risk badge must represent actual store state.

## 4.4 Project Space

Screenshot: `04-project-space.png`

### Current Problems

- This page looks empty and unfinished in the first viewport.
- Tabs are present, but the overview tab lacks operational density.
- Project health ring is isolated with too much blank space.
- Team, submissions, feedback, and dynamics are hidden behind tabs, so the page does not immediately feel like a workspace.
- Back button is visually weak and poorly integrated.

### Redesign Direction

Project Space should become the core collaboration page.

Top section:

- Compact project identity header:
  - Course
  - Project name
  - Phase
  - Deadline
  - Risk status
  - Health score
- Inline actions:
  - `进入材料流转`
  - `查看提交`
  - `提醒成员`

Main layout:

- Left: project command panel
  - Health ring
  - Risk reasons
  - Latest teacher feedback
  - Deadline countdown
- Center: tabbed workspace
  - Overview
  - Materials
  - Members
  - Submissions
  - Feedback
  - Activity
- Right: activity rail
  - Recent status changes
  - Recent uploads
  - Open feedback

### Add Decorations

- Project health ProgressOrb integrated into header.
- Risk reason card with semantic color field.
- Floating mini activity rail.
- Tab transition fade/slide.

### Function Must Remain First

- Material rows must route to `/app/tasks?materialId=xxx`.
- Submission records must update after upload.
- Feedback must reflect teacher actions.
- Admin/teacher/student role visibility should differ.

## 4.5 Material Workflow

Screenshot: `05-material-workflow.png`

### Current Problems

- This is the most important page, but it currently feels like a narrow board plus plain details.
- Columns are too compressed at 1440px.
- Material cards use gray fill that makes them look disabled.
- Right detail drawer is readable but not premium.
- Upload, feedback, and status history do not feel like a ritualized workflow.
- The title hero still takes space that should be used by the board.

### Redesign Direction

Material Workflow should be the strongest demo page.

Top:

- Compact command header:
  - `材料流转中心`
  - status counts
  - selected project filter
  - upload shortcut

Board:

- 5 columns:
  - 待准备
  - 待提交
  - 已提交
  - 需修改
  - 已通过
- Columns should feel lightweight:
  - transparent background
  - sticky column header
  - count pill
  - subtle vertical separators
- Cards should show:
  - material name
  - project
  - owner
  - format
  - deadline
  - risk
  - latest file
  - feedback count
  - progress

Detail drawer:

- Glass panel with stronger hierarchy.
- Top status header with file format badge and deadline.
- Upload dropzone with file validation copy.
- Teacher feedback as conversation-like cards.
- Status history as a compact vertical timeline.
- Actions grouped:
  - Student upload
  - Teacher mark revision / approve
  - Admin audit detail

### Add Decorations

- Subtle column glow when selected material belongs to that column.
- Drawer slide-in transition.
- Upload dropzone micro-interaction.
- MaterialTimeline with icons.

### Function Must Remain First

The following must remain real:

- File format validation.
- 50MB size limit.
- Latest file update.
- Submission record append.
- MaterialHistory append.
- OperationLog append.
- Notification append.
- Card moves to new status column.

## 4.6 Analytics

Screenshot: `06-analytics.png`

### Current Problems

- It still looks like a default dashboard.
- The blue bar chart breaks the calm green palette.
- Chart cards are large but not editorial.
- Insight text exists but lacks visual priority.
- Suggested actions look like small secondary buttons rather than decision prompts.

### Redesign Direction

Analytics should be an operational insight product.

Top:

- `本周 3 个课程项目存在材料缺口`
- Smaller subtitle with recommendation.
- Metric strip as compact insight cards.

Main:

- Two large hero insight cards:
  - Submission trend with annotation markers.
  - Course completion with ranked explanation.
- Secondary grid:
  - Risk distribution
  - Material gap distribution
  - Feedback load

Each chart card must include:

- Chart title
- One-sentence insight
- Suggested action button
- Chart
- Optional annotation chip

ECharts theme:

- Calm green primary
- Muted blue secondary
- Amber risk
- Soft grid lines
- Rounded bars
- Glass tooltip
- No saturated default colors

### Add Decorations

- InsightCard side rail.
- Small recommendation chip.
- AnimatedNumber in KPI row.

### Function Must Remain First

- Recommendation buttons should route/filter into relevant pages.
- Charts must dispose and resize correctly.
- Analytics data should later come from backend APIs.

## 4.7 Timeline

Screenshot: `07-timeline.png`

### Current Problems

- It reads as a row list, not a timeline.
- Icons are too small and monochrome.
- No date grouping or vertical rail.
- Student view is acceptable, but admin audit reveal needs stronger interaction.
- Search/filter controls look basic.

### Redesign Direction

Timeline should become a mature activity feed.

Layout:

- Left: filter column or compact filter bar.
- Right: grouped timeline:
  - Today
  - Yesterday
  - Earlier
- Each item:
  - Semantic icon
  - Actor
  - Action
  - Object
  - Time
  - Status transition
  - Expand details

Role labels:

- Student: `动态记录`
- Teacher: `课程动态`
- Admin: `审计日志`

Admin expanded view:

- Audit ID
- Source module
- Before/after status
- Related object ID
- Mongo document ID

### Add Decorations

- Vertical timeline rail.
- Semantic icon bubbles.
- Expand/collapse transition.
- Audit detail panel as small glass drawer inside item.

### Function Must Remain First

- Student should not see technical IDs.
- Admin should see audit ID only after expanding.
- Search and filters must use actual log data.

## 4.8 Search Command Palette

Audited from `TopNav.vue` and `SearchCommandPalette.vue`.

### Current Problems

- Search exists and is functional, but the UI text currently shows mojibake in the source output, which suggests encoding issues in some files.
- Overlay likely behaves like a modal, but needs stronger command-palette conventions.
- Result groups use raw type names; they should be localized and semantically styled.
- Empty state is present but too plain.

### Redesign Direction

Search should feel like a mature global command center.

Requirements:

- Centered glass panel, width around 720px.
- Search field repeated inside overlay for focus.
- Grouped results:
  - 课程
  - 项目
  - 材料
  - 提交记录
  - 日志
- Keyboard support:
  - Escape close
  - Enter open highlighted result
  - Arrow keys optional but recommended
- Empty state:
  - Search icon orb
  - Useful suggestions

### Add Decorations

- Blurred page overlay.
- Subtle scale + fade entrance.
- Type chips on each result.

### Function Must Remain First

- Result click must use `router.push`.
- Search must cover course, project, material, teacher, owner, member, latest file, logs.

## 4.9 Notification Center

Audited from `TopNav.vue` and `NotificationPopover.vue`.

### Current Problems

- Notification button exists and unread count works.
- Popover uses a simple list, not an inbox pattern.
- "Mark read" is text-like and visually weak.
- Notification type is not used enough visually.

### Redesign Direction

Notification Center should become a compact product inbox.

Requirements:

- Right-aligned floating panel under notification button.
- Top:
  - `通知中心`
  - unread count
  - `全部已读`
- Items:
  - type icon
  - title
  - description
  - createdAt
  - unread dot
  - action hint
- Types:
  - deadline
  - feedback
  - submission
  - risk
  - system

### Add Decorations

- Icon bubble per type.
- Unread card background.
- Hover lift only 1-2px.
- Smooth close on outside click.

### Function Must Remain First

- Click notification routes to project/material.
- Mark read must update store/backend.
- Upload/status/feedback actions should generate notifications.

## 5. Component Upgrade Plan

Existing components should be kept but visually upgraded, not thrown away.

### Global Shell

- `AppShell.vue`
  - Reduce sidebar visual weight.
  - Add active route indicator as a soft capsule.
  - Keep app content max width adaptive.
- `TopNav.vue`
  - Fix encoding/mojibake.
  - Make search command style clearer.
  - Notification button becomes an inbox trigger.
- `SideNav.vue`
  - Less empty vertical space.
  - Add route-specific microcopy only if needed.

### Common Components

Upgrade or introduce:

- `HeroPanel`
- `CommandCard`
- `InsightCard`
- `SmartSuggestionCard`
- `WorkflowColumn`
- `WorkflowStatusHeader`
- `AuditTimelineRail`
- `UploadDropzone`
- `ActionCluster`

Avoid:

- One giant reusable card style for every use case.
- Overusing glass background.

## 6. Backend-Aware Product Design Requirements

The visual redesign must anticipate real backend data:

- Use loading skeletons for every API page.
- Use error cards that look intentional, not browser defaults.
- Keep empty states beautiful.
- Never let pending backend calls collapse layout.
- All mutation actions should have optimistic/pending states:
  - Uploading
  - Saving feedback
  - Marking approved
  - Marking read
  - Loading audit detail

## 7. Implementation Priorities for Phase 2

Recommended order:

1. Fix text encoding issues in visible Vue files.
2. Refactor global design tokens in `tokens.css` and `style.css`.
3. Rework AppShell, TopNav, SideNav, Search, Notification.
4. Redesign Landing as product narrative.
5. Redesign Material Workflow and Detail Drawer.
6. Redesign Project Space.
7. Redesign Workspace.
8. Redesign Project Center.
9. Redesign Analytics chart theme.
10. Redesign Timeline.
11. Screenshot QA at 1366, 1440, 1920.

## 8. Non-Negotiable Functional Preservation

Do not break:

- Vue Router real routes.
- Pinia singleton store.
- Role-specific content.
- Global search and result navigation.
- Notification read state.
- Material upload validation.
- Status transitions.
- Operation logs.
- Material history.
- Project space submission updates.
- Admin audit details.

## 9. Phase 2 Acceptance Criteria

Frontend Phase 2 is acceptable only if:

- Landing looks like a real product website, not a school assignment cover.
- App pages do not rely on 64px+ titles for impact.
- Material Workflow is the strongest page.
- Project Space feels like a real collaborative workspace.
- Analytics has unified chart styling and visible recommendations.
- Timeline reads as an audit feed, not a list.
- Search and Notification feel like mature product functions.
- Build passes with `npm.cmd run build`.
- No existing core business flow is removed.

## 10. Notes for Later Full-Stack Phase

The backend should not be bolted on after visual work as an afterthought. The frontend service layer should keep a mock fallback flag, but UI states must be ready for:

- JWT auth
- API loading
- API failure
- Multipart upload progress
- Server-generated IDs
- Mongo audit IDs
- MySQL persisted business entities
- Backend-sourced analytics

This audit intentionally keeps backend implementation separate from visual redesign, because the frontend first needs a stable product language before API integration begins.
