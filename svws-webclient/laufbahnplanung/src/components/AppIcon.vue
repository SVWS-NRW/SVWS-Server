<template>
	<i-ri-folder-open-line v-if="props.routename === 'load'" />
	<i-ri-presentation-line v-else-if="props.routename === 'laufbahnplanung'" />
	<i-ri-file-close-line v-else-if="props.routename === 'close'" />
</template>

<script setup lang="ts">
	const props = defineProps<{ routename: string }>();
</script>
