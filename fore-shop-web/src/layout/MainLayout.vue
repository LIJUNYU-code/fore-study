<template>
  <div class="app-wrapper">
    <Sidebar class="sidebar-container" />
    <div class="main-container">
      <Header />
      <div class="main-content">
        <div class="content-wrapper">
          <router-view v-slot="{ Component }">
            <transition name="fade-transform" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import Sidebar from './Sidebar.vue'
import Header from './Header.vue'
</script>

<style scoped>
.app-wrapper {
  display: flex;
  height: 100%;
  width: 100%;
  overflow: hidden;
}

.sidebar-container {
  width: var(--sidebar-width);
  height: 100%;
  flex-shrink: 0;
  background-color: var(--sidebar-bg);
  overflow-y: auto;
  overflow-x: hidden;
  transition: width 0.3s;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-width: 0;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  background-color: var(--main-bg);
  padding: 20px;
}

.content-wrapper {
  min-height: calc(100vh - var(--header-height) - 40px);
}

.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>