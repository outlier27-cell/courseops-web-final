# CourseOps Final Frontend Audit - 2026-06-28

## Scope

- Public landing: `/`
- Login: `/login`
- Workspace: `/app`
- Projects: `/app/projects`
- Project detail: `/app/projects/p-web-final`
- Materials workflow: `/app/tasks`
- Material detail: `/app/tasks?materialId=m-manual`
- Analytics: `/app/analytics`
- Audit logs: `/app/logs`
- Interactions: login, search palette, notification popover, role switch attempt
- Viewports: desktop default and 390 x 844 mobile

## Evidence

Screenshots are saved in `docs/audits/final-frontend-check-2026-06-28/screenshots/`.

Captured JSON notes:

- `mobile-capture-results.json`
- `desktop-tail-results.json`
- `interaction-results.json`

## Verification Results

- Frontend dev server opened at `http://127.0.0.1:5173/`.
- Login with `u-student-001 / 123456` enters `/app` when backend is unavailable through mock fallback.
- Desktop pages showed no horizontal overflow in sampled checks.
- Mobile pages showed no horizontal overflow in sampled checks.
- Console errors were not observed during captured route checks.
- `npm.cmd run build` passed.
- Production build emitted only large chunk warnings for vendor, Arco, and ECharts bundles.

## Environment Blockers

- Docker daemon was not running, so MySQL and MongoDB could not be started through `docker compose up -d`.
- Backend Maven wrapper failed because `JAVA_HOME` points to `D:\软件\JAVA`, which is not accepted as a valid JDK path by `mvnw.cmd`.
- Because of those local environment blockers, this audit validated the frontend with mock fallback, not the fully connected backend flow.

## Findings

1. Backend-connected end-to-end login and CRUD flows remain unverified.
   - Evidence: Docker API unavailable and Maven wrapper rejected `JAVA_HOME`.
   - Risk: final demo may differ when real backend, database, uploads, and JWT endpoints are active.

2. Mobile app pages can show a visually heavy skeleton-only first screen while mock data is loading.
   - Evidence: early mobile screenshots for app routes show side navigation and skeleton cards before data resolves.
   - Risk: on slower machines the first impression feels unfinished for 1-5 seconds.

3. Search and notification behavior depends on persisted local state.
   - Evidence: notification popover showed 0 unread after prior interactions because local persisted state was retained.
   - Risk: repeated demos may not start from the intended default state unless local storage is cleared or reset controls are provided.

4. Large production chunks remain.
   - Evidence: Vite warned for `vendor`, `arco`, and `echarts` chunks above 500 kB.
   - Risk: first-load performance may be weaker on slow networks.

5. No explicit logout/reset entry is visible in the app shell.
   - Evidence: route guard redirects `/login` to `/app` while token exists; user-facing logout was not found in shell controls.
   - Risk: demo role/account switching and returning to login require manual local storage clearing.

## Recommended Fixes Before Final Handoff

1. Start Docker Desktop and fix `JAVA_HOME`, then rerun backend startup and real API login/upload/status tests.
2. Add a visible logout or reset-demo-state control.
3. Improve mobile loading state with a compact "loading course workspace" message above skeleton cards.
4. Add a one-click demo data reset for local persisted state.
5. Defer ECharts/Arco-heavy chunks further or raise the warning threshold if current bundle size is acceptable for local course demo.

