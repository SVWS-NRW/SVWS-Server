<script setup lang="ts">
import SvwsUiPopover from "./Popover.vue";
import SvwsUiTooltip from "./Tooltip.vue";
</script>

<template>
  <Story title="SVWS UI/Popover">
    <div class="h-screen w-screen flex items-center justify-center">
      <svws-ui-popover>
        <template #trigger>
          <span class="border-b border-dashed cursor-help">Hier versteckt sich ein Tooltip</span>
        </template>
        <template #content>
          <svws-ui-tooltip>
            Das ist ein Tooltip
          </svws-ui-tooltip>
        </template>
      </svws-ui-popover>
    </div>
  </Story>
</template>