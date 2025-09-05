<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <router-view />
  </div>
</template>

<script setup>
// Single Page Application root component
</script>

<style>
/* Global responsive styles */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

#app {
  min-height: 100vh;
  min-height: 100dvh;
}

/* Responsive breakpoints */
@media (max-width: 640px) {
  .sidebar {
    width: 100% !important;
  }
}

/* iOS momentum scrolling */
.ios-scroller {
  -webkit-overflow-scrolling: touch;
}

@media (min-width: 1024px) {
  .sidebar {
    position: relative !important;
    transform: translateX(0) !important;
  }
}

/* Mobile-first responsive utilities */
.responsive-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1rem;
}

@media (min-width: 640px) {
  .responsive-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .responsive-grid {
    grid-template-columns: repeat(4, 1fr);
    gap: 1.5rem;
  }
}

@media (min-width: 1280px) {
  .responsive-grid {
    grid-template-columns: repeat(6, 1fr);
  }
}
</style>