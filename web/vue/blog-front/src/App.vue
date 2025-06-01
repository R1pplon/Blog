<template>
  <!-- 导航栏 -->
  <NavBar />
<RouterView></RouterView>
</template>

<script lang="ts" setup>
    import {RouterView} from 'vue-router'
    import NavBar from "@/components/NavBar.vue";
</script>

<style>
.app {
    background-color: #fff;
    color: #333;
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
}
</style>