<script setup lang="ts">
import { logEvent } from 'histoire/client';
import SvwsAvatar from "./Avatar.vue";
</script>

<template>
  <Story title="SVWS UI/Layout/Avatar">
    <Avatar @image:captured="logEvent('Image captured', $event)" @image:uploaded="logEvent('Image Uploaded', $event)" />
  </Story>
</template>