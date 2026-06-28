# CourseOps Motion System

## Reference Read

Reference: https://www.nanfu.global/

What we borrow:

- A single dominant visual object carries the page instead of isolated cards.
- Scroll advances the story in chapters while the visual field stays continuous.
- Pointer movement subtly shifts light, rings, and depth layers.
- Product proof appears as large cinematic evidence, then operational details follow.

What we adapt for CourseOps:

- The central object is not a product photo. It is a 3D workflow engine made of project, material, feedback, and audit modules.
- Dashboard pages stay useful and dense. The cinematic treatment belongs mostly to the public landing and global background layer.
- Motion must have reduced-motion fallback and avoid blocking core course operations.

## FigureSpec-Style Structure

```json
{
  "canvas": { "width": 1440, "height": 920 },
  "nodes": [
    { "id": "background", "label": "Global depth field\npointer + scroll reactive", "x": 720, "y": 120, "width": 420, "height": 96 },
    { "id": "hero", "label": "Hero stage\nCourseOps value proposition", "x": 320, "y": 330, "width": 300, "height": 120 },
    { "id": "engine", "label": "Immersive workflow engine\nsticky 3D core", "x": 720, "y": 430, "width": 330, "height": 140 },
    { "id": "chapters", "label": "Scroll chapters\nproject -> material -> feedback -> audit", "x": 1120, "y": 430, "width": 330, "height": 140 },
    { "id": "app", "label": "Operational app shell\nquiet, scan-friendly dashboards", "x": 720, "y": 710, "width": 420, "height": 110 }
  ],
  "edges": [
    { "from": "background", "to": "hero", "label": "sets atmosphere" },
    { "from": "hero", "to": "engine", "label": "hands off to story" },
    { "from": "engine", "to": "chapters", "label": "syncs with scroll" },
    { "from": "chapters", "to": "app", "label": "grounds in real workflow" }
  ]
}
```

## Implementation Rules

- Keep one primary cinematic object on landing: `ImmersiveCourseStage`.
- Use CSS custom properties `--pointer-x`, `--pointer-y`, and `--stage-progress` for motion coordination.
- Do not convert every dashboard panel into a theatrical card. Use subtle global depth only inside the app workspace.
- Respect `prefers-reduced-motion`: remove sticky scroll height and stop expensive 3D motion.
- Verify with desktop and mobile screenshots after every major visual pass.
