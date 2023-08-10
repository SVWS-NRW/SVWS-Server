<template>
	<div class="svws-ui-zeitraster--entry">
		<slot />
	</div>
</template>

<script setup lang="ts">

</script>

<style lang="postcss">
.svws-ui-zeitraster--entry {
  @apply border-t border-black/25 p-1 hover:bg-svws/5 hover:text-svws select-none cursor-pointer;

  &.svws-alternative {
    &:hover {
      .svws-ui-zeitraster--stunde {
        @apply bg-svws text-white;
      }
    }
  }
}
</style>
