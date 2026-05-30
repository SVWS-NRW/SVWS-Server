<!-- eslint-disable vue/no-v-html -->
<template>
	<div v-html="html" />
</template>

<script setup lang="ts">
	const props = defineProps<{
		html?: string
	}>();
</script>
