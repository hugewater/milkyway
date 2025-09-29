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
  font-family: "IBM Plex Sans", -apple-system, BlinkMacSystemFont, system-ui, Roboto, "Noto Sans", "Helvetica Neue", Arial, sans-serif;
  -webkit-text-size-adjust: 100%;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  min-height: 100vh;
  min-height: 100dvh;
}

/* Custom breakpoints for more granular control */
.hidden { display: none !important; }
.block { display: block !important; }
.flex { display: flex !important; }
.inline-flex { display: inline-flex !important; }

/* xs: 480px and up (small phones in landscape) */
@media (min-width: 480px) {
  .xs\\:block { display: block !important; }
  .xs\\:hidden { display: none !important; }
  .xs\\:inline { display: inline !important; }
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
  gap: 0.75rem;
}

@media (min-width: 480px) {
  .responsive-grid {
    gap: 1rem;
  }
}

@media (min-width: 640px) {
  .responsive-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .responsive-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 1.5rem;
  }
}

@media (min-width: 1280px) {
  .responsive-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

/* Button utilities */
.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  font-weight: 500;
  transition: all 0.2s ease-in-out;
  cursor: pointer;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

/* Touch targets for mobile */
@media (max-width: 768px) {
  button, .btn-primary, input[type="button"], input[type="submit"] {
    min-height: 44px;
    min-width: 44px;
  }
}

/* Form inputs responsive */
.input-text {
  border: 1px solid #d1d5db;
  border-radius: 6px;
  padding: 0.75rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  width: 100%;
  transition: border-color 0.15s, box-shadow 0.15s;
  background-color: #ffffff;
}

.input-text:focus {
  outline: none;
  border-color: #6366f1;
  box-shadow: 0 0 0 1px #6366f1;
}

@media (max-width: 640px) {
  .input-text {
    font-size: 16px; /* Prevent zoom on iOS */
    padding: 0.875rem 0.75rem;
  }
}
</style>